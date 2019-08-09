package uizacoresdk.view.floatview;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import com.google.android.exoplayer2.Player;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import uizacoresdk.R;
import uizacoresdk.util.TmpParamData;
import uizacoresdk.util.UZData;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.ComunicateMng;
import vn.uiza.core.common.Constants;
import vn.uiza.core.exception.UZException;
import vn.uiza.core.utilities.LAnimationUtil;
import vn.uiza.core.utilities.LConnectivityUtil;
import vn.uiza.core.utilities.LDeviceUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.utils.util.SentryUtils;
import vn.uiza.utils.util.ViewUtils;

public class FUZVideoService extends Service implements FUZVideo.Callback {
    private static final String TAG = FUZVideoService.class.getSimpleName();
    private WindowManager windowManager;
    private View floatingView;
    private View viewDestroy;
    private RelativeLayout rlControl;
    private RelativeLayout moveView;
    private ImageView btExit;
    private ImageView btFullScreen;
    private ImageView btPlayPause;
    private TextView tvMsg;
    private FUZVideo fuzVideo;
    private ViewStub controlStub;
    private WindowManager.LayoutParams params;
    private String linkPlay;
    private boolean isLivestream;
    private boolean isInitCustomLinkplay;
    private String cdnHost;
    private String uuid;
    private long contentPosition;
    private int screenWidth;
    private int screenHeight;
    private int pipTopPosition;
    private int videoW = 16;
    private int videoH = 9;
    private boolean isEzDestroy;
    private boolean isEnableVibration;
    private boolean isEnableSmoothSwitch;
    private boolean isAutoSize;
    private int videoWidthFromSettingConfig;
    private int videoHeightFromSettingConfig;
    private int marginL;
    private int marginT;
    private int marginR;
    private int marginB;
    private int progressBarColor;
    private ResultGetLinkPlay resultGetLinkPlay;
    private int positionBeforeDisappearX = Constants.UNKNOW;
    private int positionBeforeDisappearY = Constants.UNKNOW;
    private CountDownTimer countDownTimer;
    private GestureDetector gestureDetector;

    private enum MiniPosition { TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER_LEFT, CENTER_RIGHT, CENTER_TOP, CENTER_BOTTOM, LEFT, RIGHT, TOP, BOTTOM, CENTER }
    private MiniPosition pos;

    public FUZVideoService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isInitCustomLinkplay = intent.getBooleanExtra(Constants.FLOAT_USER_USE_CUSTOM_LINK_PLAY, false);
        contentPosition = intent.getLongExtra(Constants.FLOAT_CONTENT_POSITION, 0);
        progressBarColor = intent.getIntExtra(Constants.FLOAT_PROGRESS_BAR_COLOR, Color.WHITE);
        int pipControlSkin = intent.getIntExtra(Constants.FLOAT_CONTROL_SKIN_ID, 0);
        if (controlStub != null && rlControl == null) {
            // this means control is not inflated yet
            if (pipControlSkin != 0) {
                controlStub.setLayoutResource(pipControlSkin);
            }
            inflateControls();
        }

        try {
            cdnHost = resultGetLinkPlay.getData().getCdn().get(0).getHost();
        } catch (NullPointerException e) {
            LLog.e(TAG, "Error cannot find cdnHost " + e.toString());
            SentryUtils.captureException(e);
        }
        uuid = intent.getStringExtra(Constants.FLOAT_UUID);
        if (!isInitCustomLinkplay) {
            if (UZData.getInstance().getData() == null) {
                return START_NOT_STICKY;
            }
        }
        if (intent.getExtras() != null) {
            linkPlay = intent.getStringExtra(Constants.FLOAT_LINK_PLAY);
            isLivestream = intent.getBooleanExtra(Constants.FLOAT_IS_LIVESTREAM, false);
            LLog.d(TAG, "onStartCommand isInitCustomLinkplay " + isInitCustomLinkplay + ", contentPosition: " + contentPosition + ", cdnHost: " + cdnHost + ", linkPlay: " + linkPlay);
            setupVideo();
        }
        return START_NOT_STICKY;
    }

    private void findViews() {
        moveView = floatingView.findViewById(R.id.move_view);
        fuzVideo = floatingView.findViewById(R.id.uiza_video);
        controlStub = floatingView.findViewById(R.id.control_stub);

        tvMsg = floatingView.findViewById(R.id.tv_msg);
        LUIUtil.setTextShadow(tvMsg);
        tvMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LAnimationUtil.play(v, Techniques.Pulse, new LAnimationUtil.Callback() {
                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onEnd() {
                        setupVideo();
                    }

                    @Override
                    public void onRepeat() {
                    }

                    @Override
                    public void onStart() {
                    }
                });
            }
        });

        viewDestroy = floatingView.findViewById(R.id.view_destroy);
        int colorViewDestroy = UZUtil.getMiniPlayerColorViewDestroy(getBaseContext());
        viewDestroy.setBackgroundColor(colorViewDestroy);
        isEzDestroy = UZUtil.getMiniPlayerEzDestroy(getBaseContext());
        isEnableVibration = UZUtil.getMiniPlayerEnableVibration(getBaseContext());
        isEnableSmoothSwitch = UZUtil.getMiniPlayerEnableSmoothSwitch(getBaseContext());

        isAutoSize = UZUtil.getMiniPlayerAutoSize(getBaseContext());
        if (!isAutoSize) {
            videoWidthFromSettingConfig = UZUtil.getMiniPlayerSizeWidth(getBaseContext());
            videoHeightFromSettingConfig = UZUtil.getMiniPlayerSizeHeight(getBaseContext());
        }
        //Drag and move floating view using user's touch action.
        dragAndMove();
    }

    private void inflateControls() {
        controlStub.inflate();
        // inflate controls from skin
        rlControl = floatingView.findViewById(R.id.controls_root);
        btExit = floatingView.findViewById(R.id.uiza_mini_exit);
        btFullScreen = floatingView.findViewById(R.id.uiza_mini_full_screen);
        btPlayPause = floatingView.findViewById(R.id.uiza_mini_pause_resume);
        setControlsClickListener();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        resultGetLinkPlay = UZData.getInstance().getResultGetLinkPlay();
        if (resultGetLinkPlay == null) {
            stopSelf();
        }
        videoW = UZUtil.getVideoWidth(getBaseContext());
        videoH = UZUtil.getVideoHeight(getBaseContext());
        screenWidth = LScreenUtil.getScreenWidth();
        screenHeight = LScreenUtil.getScreenHeight();
        pipTopPosition = UZUtil.getStablePipTopPosition(getBaseContext());
        marginL = UZUtil.getMiniPlayerMarginL(getBaseContext());
        marginT = UZUtil.getMiniPlayerMarginT(getBaseContext());
        marginR = UZUtil.getMiniPlayerMarginR(getBaseContext());
        marginB = UZUtil.getMiniPlayerMarginB(getBaseContext());
        floatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_uiza_video, null, false);
        findViews();
        //Add the view to the window.
        int layoutFlag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutFlag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutFlag = WindowManager.LayoutParams.TYPE_PHONE;
        }
        //OPTION 1: floatview se neo vao 1 goc cua device
        /*params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutFlag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);*/
        //OPTION 2: floatview se ko neo vao 1 goc cua device
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutFlag, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                PixelFormat.TRANSLUCENT);

        setSizeMoveView(true, false);

        //Specify the view position
        //OPTION 1
        //Initially view will be added to top-left corner
        //params.gravity = Gravity.TOP | Gravity.LEFT;
        //params.x = 0;
        //params.y = 0;
        //OPTION 2
        //right-bottom corner
        /*params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = screenWidth - getMoveViewWidth();
        params.y = screenHeight - getMoveViewHeight();*/
        //OPTION 3
        //init lan dau tien se neo vao canh BOTTOM_RIGHT cua man hinh
        /*params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = screenWidth - getMoveViewWidth();
        //params.y = screenHeight - getMoveViewHeight();
        params.y = screenHeight - getMoveViewHeight() - pipTopPosition;
        //LLog.d(TAG, "first position: " + params.x + "-" + params.y);*/
        //OPTION 4
        //float view o ben ngoai screen cua device
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = screenWidth - 1;
        params.y = screenHeight - 1;

        //Add the view to the window
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(floatingView, params);

    }

    private void setControlsClickListener() {
        if (btExit != null) {
            btExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopSelf();
                }
            });
        }
        if (btFullScreen != null) {
            btFullScreen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openApp();
                }
            });
        }
        if (btPlayPause != null) {
            btPlayPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleResumePause();
                }
            });
        }

    }

    //==================================================================================================START CONFIG
    private void toggleResumePause() {
        if (fuzVideo == null || btPlayPause == null) {
            return;
        }
        boolean isToggleResume = fuzVideo.togglePauseResume();
        if (isToggleResume) {
            btPlayPause.setImageResource(R.drawable.baseline_pause_circle_outline_white_48);
        } else {
            btPlayPause.setImageResource(R.drawable.baseline_play_circle_outline_white_48);
        }
    }

    private void pauseVideo() {
        if (fuzVideo == null || btPlayPause == null) {
            return;
        }
        fuzVideo.pauseVideo();
        btPlayPause.setImageResource(R.drawable.baseline_play_circle_outline_white_48);
    }

    private void resumeVideo() {
        if (fuzVideo == null || btPlayPause == null) {
            return;
        }
        fuzVideo.resumeVideo();
        btPlayPause.setImageResource(R.drawable.baseline_pause_circle_outline_white_48);
    }

    private void disappear() {
        if (fuzVideo == null) {
            return;
        }
        positionBeforeDisappearX = params.x;
        positionBeforeDisappearY = params.y;
        updateUiSlide(screenWidth, screenHeight);
    }

    private void appear() {
        if (positionBeforeDisappearX == Constants.UNKNOW || positionBeforeDisappearY == Constants.UNKNOW) {
            return;
        }
        updateUiSlide(positionBeforeDisappearX, positionBeforeDisappearY);
        positionBeforeDisappearX = Constants.UNKNOW;
        positionBeforeDisappearY = Constants.UNKNOW;
    }

    private void openApp() {
        if (fuzVideo == null || fuzVideo.getPlayer() == null) {
            return;
        }
        //stop video
        if (!isEnableSmoothSwitch) {
            fuzVideo.getPlayer().setPlayWhenReady(false);
        }
        //moveView.setOnTouchListener(null);//disabled move view
        UZUtil.setClickedPip(getApplicationContext(), true);
        if (UZData.getInstance().getData() == null) {
            return;
        }
        LLog.d(TAG, "miniplayer STEP 5 START OPEN APP, miniplayer content position " + fuzVideo.getCurrentPosition());
        ComunicateMng.MsgFromServiceOpenApp msgFromServiceOpenApp = new ComunicateMng.MsgFromServiceOpenApp(null);
        msgFromServiceOpenApp.setPositionMiniPlayer(fuzVideo.getCurrentPosition());
        ComunicateMng.postFromService(msgFromServiceOpenApp);
    }

    private boolean isControllerShowing() {
        if (rlControl == null) {
            return false;
        }
        return rlControl.getVisibility() == View.VISIBLE;
    }

    private void showController() {
        if (rlControl == null) {
            return;
        }
        if (!isControllerShowing()) {
            rlControl.setVisibility(View.VISIBLE);
            setSizeMoveView(false, true);
        }
    }

    private void hideController() {
        if (rlControl == null) {
            return;
        }
        if (isControllerShowing()) {
            rlControl.setVisibility(View.GONE);
            setSizeMoveView(false, false);
        }
    }

    private void toggleController() {
        if (isControllerShowing()) {
            hideController();
        } else {
            showController();
        }
    }

    private int getMoveViewWidth() {
        if (moveView == null) {
            return 0;
        }
        return moveView.getLayoutParams().width;
    }

    private int getMoveViewHeight() {
        if (moveView == null) {
            return 0;
        }
        return moveView.getLayoutParams().height;
    }

    private int getVideoW() {
        if (fuzVideo == null) {
            return 0;
        }
        return fuzVideo.getVideoW();
    }

    private int getVideoH() {
        if (fuzVideo == null) {
            return 0;
        }
        return fuzVideo.getVideoH();
    }

    //==================================================================================================END CONFIG
    //==================================================================================================START UI
    private void updateUiVideoSizeOneTime(int videoW, int videoH) {
        int vW;
        int vH;
        if (isAutoSize) {
            vW = screenWidth / 2;
            vH = vW * videoH / (videoW == 0 ? 1 : videoW);
        } else {
            vW = videoWidthFromSettingConfig;
            vH = videoHeightFromSettingConfig;
        }
        int firstPositionX = UZUtil.getMiniPlayerFirstPositionX(getBaseContext());
        int firstPositionY = UZUtil.getMiniPlayerFirstPositionY(getBaseContext());
        if (firstPositionX == Constants.NOT_FOUND || firstPositionY == Constants.NOT_FOUND) {
            firstPositionX = screenWidth - vW;
            firstPositionY = screenHeight - vH - pipTopPosition;
        }
        slideToPosition(firstPositionX, firstPositionY);
    }

    private void slideToPosition(int goToPosX, int goToPosY) {
        final int currentPosX = params.x;
        final int currentPosY = params.y;
        final int gotoPosX;
        final int gotoPosY;
        int videoW = getVideoW();
        int videoH = getVideoH();
        if (goToPosX <= 0) {
            gotoPosX = marginL;
        } else if (goToPosX >= screenWidth - videoW) {
            gotoPosX = goToPosX - marginR;
        } else {
            gotoPosX = goToPosX;
        }
        if (goToPosY <= 0) {
            gotoPosY = marginT;
        } else if (goToPosY >= screenHeight - videoH) {
            gotoPosY = goToPosY - marginB;
        } else {
            gotoPosY = goToPosY;
        }
        final int a = Math.abs(gotoPosX - currentPosX);
        final int b = Math.abs(gotoPosY - currentPosY);
        countDownTimer = new CountDownTimer(300, 3) {
            public void onTick(long t) {
                float step = (300.f - t) / 3;
                int tmpX;
                int tmpY;
                if (currentPosX > gotoPosX) {
                    if (currentPosY > gotoPosY) {
                        tmpX = currentPosX - (int) (a * step / 100);
                        tmpY = currentPosY - (int) (b * step / 100);
                    } else {
                        tmpX = currentPosX - (int) (a * step / 100);
                        tmpY = currentPosY + (int) (b * step / 100);
                    }
                } else {
                    if (currentPosY > gotoPosY) {
                        tmpX = currentPosX + (int) (a * step / 100);
                        tmpY = currentPosY - (int) (b * step / 100);
                    } else {
                        tmpX = currentPosX + (int) (a * step / 100);
                        tmpY = currentPosY + (int) (b * step / 100);
                    }
                }
                updateUiSlide(tmpX, tmpY);
            }

            public void onFinish() {
                updateUiSlide(gotoPosX, gotoPosY);
            }
        }.start();
    }

    private void updateUiSlide(int x, int y) {
        params.x = x;
        params.y = y;
        if (isFloatingViewValid()) {
            windowManager.updateViewLayout(floatingView, params);
        }
    }

    private boolean isFloatingViewValid() {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            return floatingView != null && floatingView.isAttachedToWindow();
        }
        return floatingView != null;
    }

    //==================================================================================================END UI

    private class GestureTap extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            boolean isTapToFullPlayer = UZUtil.getMiniPlayerTapToFullPlayer(getBaseContext());
            if (isTapToFullPlayer) {
                setSizeMoveView(false, true);//remove this line make animation switch from mini-player to full-player incorrectly
                openApp();
            } else {
                toggleController();
            }
            return true;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void dragAndMove() {
        gestureDetector = new GestureDetector(getBaseContext(), new GestureTap());
        moveView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        onMoveUp();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        int tmpX = initialX + (int) (event.getRawX() - initialTouchX);
                        int tmpY = initialY + (int) (event.getRawY() - initialTouchY);

                        updateUiSlide(tmpX, tmpY);
                        getLocationOnScreen(floatingView);
                        return true;
                }
                return false;
            }
        });
    }


    private void notiPos(MiniPosition tmpPos) {
        if (pos == tmpPos) {
            return;
        }
        pos = tmpPos;
        if (isEzDestroy) {
            switch (pos) {
                case TOP_LEFT:
                case TOP_RIGHT:
                case BOTTOM_LEFT:
                case BOTTOM_RIGHT:
                case CENTER_LEFT:
                case CENTER_RIGHT:
                case CENTER_TOP:
                case CENTER_BOTTOM:
                    if (isEnableVibration) {
                        LDeviceUtil.vibrate(getBaseContext());
                    }
                    ViewUtils.visibleViews(viewDestroy);
                    break;
                default:
                    ViewUtils.goneViews(viewDestroy);
                    break;
            }
        } else {
            switch (pos) {
                case TOP_LEFT:
                case TOP_RIGHT:
                case BOTTOM_LEFT:
                case BOTTOM_RIGHT:
                    if (isEnableVibration) {
                        LDeviceUtil.vibrate(getBaseContext());
                    }
                    ViewUtils.visibleViews(viewDestroy);
                    break;
                default:
                    ViewUtils.goneViews(viewDestroy);
                    break;
            }
        }
    }

    private void getLocationOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int posLeft = location[0];
        int posTop = location[1];
        int posRight = posLeft + view.getWidth();
        int posBottom = posTop + view.getHeight();
        int centerX = (posLeft + posRight) / 2;
        int centerY = (posTop + posBottom) / 2;
        if (centerX < 0) {
            if (centerY < 0) {
                notiPos(MiniPosition.TOP_LEFT);
            } else if (centerY > screenHeight) {
                notiPos(MiniPosition.BOTTOM_LEFT);
            } else {
                notiPos(MiniPosition.CENTER_LEFT);
            }
        } else if (centerX > screenWidth) {
            if (centerY < 0) {
                notiPos(MiniPosition.TOP_RIGHT);
            } else if (centerY > screenHeight) {
                notiPos(MiniPosition.BOTTOM_RIGHT);
            } else {
                notiPos(MiniPosition.CENTER_RIGHT);
            }
        } else {
            if (centerY < 0) {
                notiPos(MiniPosition.CENTER_TOP);
            } else if (centerY > screenHeight) {
                notiPos(MiniPosition.CENTER_BOTTOM);
            } else {
                if (posLeft < 0) {
                    notiPos(MiniPosition.LEFT);
                } else if (posRight > screenWidth) {
                    notiPos(MiniPosition.RIGHT);
                } else {
                    if (posTop < 0) {
                        notiPos(MiniPosition.TOP);
                    } else if (posBottom > screenHeight) {
                        notiPos(MiniPosition.BOTTOM);
                    } else {
                        notiPos(MiniPosition.CENTER);
                    }
                }
            }
        }

    }

    private void onMoveUp() {
        if (pos == null) {
            return;
        }
        int posX;
        int posY;
        int centerPosX;
        int centerPosY;
        if (isEzDestroy) {
            switch (pos) {
                case TOP_LEFT:
                case TOP_RIGHT:
                case BOTTOM_LEFT:
                case BOTTOM_RIGHT:
                case CENTER_LEFT:
                case CENTER_RIGHT:
                case CENTER_TOP:
                case CENTER_BOTTOM:
                    stopSelf();
                    break;
                case TOP:
                    slideToTop();
                    break;
                case BOTTOM:
                    slideToBottom();
                    break;
                case LEFT:
                    slideToLeft();
                    break;
                case RIGHT:
                    slideToRight();
                    break;
                case CENTER:
                    posX = params.x;
                    posY = params.y;
                    centerPosX = posX + getMoveViewWidth() / 2;
                    centerPosY = posY + getMoveViewHeight() / 2;
                    if (centerPosX < screenWidth / 2) {
                        if (centerPosY < screenHeight / 2) {
                            //LLog.d(TAG, "top left part");
                            slideToPosition(0, 0);
                        } else {
                            //LLog.d(TAG, "bottom left part");
                            slideToPosition(0, screenHeight - getMoveViewHeight() - pipTopPosition);
                        }
                    } else {
                        if (centerPosY < screenHeight / 2) {
                            //LLog.d(TAG, "top right part");
                            slideToPosition(screenWidth - getMoveViewWidth(), 0);
                        } else {
                            //LLog.d(TAG, "bottom right part");
                            slideToPosition(screenWidth - getMoveViewWidth(), screenHeight - getMoveViewHeight() - pipTopPosition);
                        }
                    }
                    break;
            }
        } else {
            switch (pos) {
                case TOP_LEFT:
                case TOP_RIGHT:
                case BOTTOM_LEFT:
                case BOTTOM_RIGHT:
                    stopSelf();
                    break;
                case TOP:
                case CENTER_TOP:
                    slideToTop();
                    break;
                case BOTTOM:
                case CENTER_BOTTOM:
                    slideToBottom();
                    break;
                case LEFT:
                case CENTER_LEFT:
                    slideToLeft();
                    break;
                case RIGHT:
                case CENTER_RIGHT:
                    slideToRight();
                    break;
                case CENTER:
                    posX = params.x;
                    posY = params.y;
                    centerPosX = posX + getMoveViewWidth() / 2;
                    centerPosY = posY + getMoveViewHeight() / 2;
                    if (centerPosX < screenWidth / 2) {
                        if (centerPosY < screenHeight / 2) {
                            slideToPosition(0, 0);
                        } else {
                            slideToPosition(0, screenHeight - getMoveViewHeight() - pipTopPosition);
                        }
                    } else {
                        if (centerPosY < screenHeight / 2) {
                            slideToPosition(screenWidth - getMoveViewWidth(), 0);
                        } else {
                            slideToPosition(screenWidth - getMoveViewWidth(), screenHeight - getMoveViewHeight() - pipTopPosition);
                        }
                    }
                    break;
            }
        }
    }

    private void slideToTop() {
        int posX = params.x;
        int centerPosX = posX + getMoveViewWidth() / 2;
        if (centerPosX < screenWidth / 2) {
            slideToPosition(0, 0);
        } else {
            slideToPosition(screenWidth - getMoveViewWidth(), 0);
        }
    }

    private void slideToBottom() {
        int posX = params.x;
        int centerPosX = posX + getMoveViewWidth() / 2;
        if (centerPosX < screenWidth / 2) {
            slideToPosition(0, screenHeight - getMoveViewHeight() - pipTopPosition);
        } else {
            slideToPosition(screenWidth - getMoveViewWidth(), screenHeight - getMoveViewHeight() - pipTopPosition);
        }
    }

    private void slideToLeft() {
        int posY = params.y;
        int centerPosY = posY + getMoveViewHeight() / 2;
        if (centerPosY < screenHeight / 2) {
            slideToPosition(0, 0);
        } else {
            slideToPosition(0, screenHeight - getMoveViewHeight() - pipTopPosition);
        }
    }

    private void slideToRight() {
        int posY = params.y;
        int centerPosY = posY + getMoveViewHeight() / 2;
        if (centerPosY < screenHeight / 2) {
            slideToPosition(screenWidth - getMoveViewWidth(), 0);
        } else {
            slideToPosition(screenWidth - getMoveViewWidth(), screenHeight - getMoveViewHeight() - pipTopPosition);
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (floatingView != null) {
            windowManager.removeView(floatingView);
        }
        if (fuzVideo != null) {
            fuzVideo.onDestroy();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        UZUtil.setClickedPip(getApplicationContext(), false);
        super.onDestroy();
    }

    @Override
    public void isInitResult(boolean isInitSuccess) {
        if (!isInitSuccess || fuzVideo == null || floatingView == null) {
            return;
        }
        LLog.d(TAG, "miniplayer STEP 2 isInitResult true");
        editSizeOfMoveView();
        //sau khi da play thanh cong thi chuyen mini player ben ngoai screen vao trong screen
        updateUiVideoSizeOneTime(fuzVideo.getVideoW(), fuzVideo.getVideoH());
        if (!isSendMsgToActivity) {
            //LLog.d(TAG, "state finish loading PIP -> send msg to UZVideo");
            ComunicateMng.MsgFromServiceIsInitSuccess msgFromServiceIsInitSuccess = new ComunicateMng.MsgFromServiceIsInitSuccess(null);
            msgFromServiceIsInitSuccess.setInitSuccess(true);
            ComunicateMng.postFromService(msgFromServiceIsInitSuccess);
            isSendMsgToActivity = true;
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case Player.STATE_BUFFERING:
            case Player.STATE_IDLE:
            case Player.STATE_READY:
                break;
            case Player.STATE_ENDED:
                onStateEnded();
                break;
        }
    }

    private void onStateEnded() {
        if (UZData.getInstance().isPlayWithPlaylistFolder()) {
            if (fuzVideo == null) {
                return;
            }
            fuzVideo.getLinkPlayOfNextItem(new FUZVideo.CallbackGetLinkPlay() {
                @Override
                public void onSuccess(String lp) {
                    linkPlay = lp;
                    if (linkPlay == null) {
                        stopSelf();
                    }
                    contentPosition = 0;
                    setupVideo();
                }
            });
        } else {
            stopSelf();
        }
    }

    @Override
    public void onVideoSizeChanged(int width, int height) {
    }

    @Override
    public void onPlayerError(UZException error) {
        LLog.e(TAG, "onPlayerError " + error.getMessage());
        stopSelf();
    }

    private boolean isSendMsgToActivity;

    private void setupVideo() {
        if (linkPlay == null || linkPlay.isEmpty()) {
            LLog.e(TAG, "setupVideo linkPlay == null || linkPlay.isEmpty() -> stopSelf");
            stopSelf();
            return;
        }
        LLog.d(TAG, "setupVideo linkPlay " + linkPlay + ", isLivestream: " + isLivestream);
        if (LConnectivityUtil.isConnected(this)) {
            fuzVideo.init(linkPlay, cdnHost, uuid, isLivestream, contentPosition, isInitCustomLinkplay, progressBarColor, this);
            tvMsg.setVisibility(View.GONE);
        } else {
            tvMsg.setVisibility(View.VISIBLE);
        }
    }

    //click vo se larger, click lan nua de smaller
    private void setSizeMoveView(boolean isFirstSizeInit, boolean isLarger) {
        if (moveView == null) {
            return;
        }
        int w = 0;
        int h = 0;
        if (isFirstSizeInit) {
            w = screenWidth / 2;
            if (videoW == 0) {
                //fixed java.lang.ArithmeticException: divide by zero
                h = w * 9 / 16;
            } else {
                h = w * videoH / videoW;
            }
        } else {
            //works fine
            //OPTION 1: isLarger->mini player se to hon 1 chut
            //!isLarger->ve trang thai ban dau
            /*if (isLarger) {
                w = getMoveViewWidth() * 120 / 100;
                h = getMoveViewHeight() * 120 / 100;
            } else {
                int videoW = fuzVideo.getVideoW();
                int videoH = fuzVideo.getVideoH();
                w = screenWidth / 2;
                h = w * videoH / videoW;
            }*/
        }
        if (w != 0 && h != 0) {
            moveView.getLayoutParams().width = w;
            moveView.getLayoutParams().height = h;
            moveView.requestLayout();
        }
    }

    private void editSizeOfMoveView() {
        if (fuzVideo == null || moveView == null || videoW == 0) {
            return;
        }
        int videoW = fuzVideo.getVideoW();
        int videoH = fuzVideo.getVideoH();
        int moveW;
        int moveH;
        if (isAutoSize) {
            moveW = getMoveViewWidth();
            moveH = moveW * videoH / (videoW == 0 ? 1 : videoW);
        } else {
            moveW = videoWidthFromSettingConfig;
            moveH = videoHeightFromSettingConfig;
        }
        TmpParamData.getInstance().setPlayerWidth(moveW);
        TmpParamData.getInstance().setPlayerHeight(moveH);
        moveView.getLayoutParams().width = moveW;
        moveView.getLayoutParams().height = moveH;
        moveView.requestLayout();
    }

    //listen msg from UZVideo
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ComunicateMng.MsgFromActivity msgFromActivity) {
        if (msgFromActivity == null || fuzVideo == null) {
            return;
        }
        if (msgFromActivity instanceof ComunicateMng.MsgFromActivityPosition) {
            //LLog.d(TAG, "Nhan duoc content position moi cua UZVideo va tien hanh seek toi day");
            if (isEnableSmoothSwitch) {
                //smooth but content position is delay
                LLog.d(TAG, "miniplayer STEP 4 MsgFromActivityPosition -> isEnableSmoothSwitch true -> do nothing");
            } else {
                long contentPosition = ((ComunicateMng.MsgFromActivityPosition) msgFromActivity).getPosition();
                long contentBufferedPosition = fuzVideo.getContentBufferedPosition();
                LLog.d(TAG, "miniplayer STEP 4 MsgFromActivityPosition -> isEnableSmoothSwitch false -> contentBufferedPosition " + contentBufferedPosition + ", position: " + contentPosition);
                if (contentPosition >= contentBufferedPosition) {
                    fuzVideo.seekTo(contentBufferedPosition);
                } else {
                    fuzVideo.seekTo(contentPosition);
                }
            }
        } else if (msgFromActivity instanceof ComunicateMng.MsgFromActivityIsInitSuccess) {
            LLog.d(TAG, "miniplayer STEP 7 MsgFromActivityIsInitSuccess isInitSuccess: " + ((ComunicateMng.MsgFromActivityIsInitSuccess) msgFromActivity).isInitSuccess());
            stopSelf();
        }
        if (msgFromActivity.getMsg() == null) {
            return;
        }
        handleMsgFromActivity(msgFromActivity);
    }

    private void handleMsgFromActivity(ComunicateMng.MsgFromActivity msgFromActivity) {
        String msg = msgFromActivity.getMsg();
        switch (msg) {
            case ComunicateMng.SHOW_MINI_PLAYER_CONTROLLER:
                showController();
                break;
            case ComunicateMng.HIDE_MINI_PLAYER_CONTROLLER:
                hideController();
                break;
            case ComunicateMng.TOGGLE_MINI_PLAYER_CONTROLLER:
                toggleController();
                break;
            case ComunicateMng.PAUSE_MINI_PLAYER:
                pauseVideo();
                break;
            case ComunicateMng.RESUME_MINI_PLAYER:
                resumeVideo();
                break;
            case ComunicateMng.TOGGLE_RESUME_PAUSE_MINI_PLAYER:
                toggleResumePause();
                break;
            case ComunicateMng.OPEN_APP_FROM_MINI_PLAYER:
                openApp();
                break;
            case ComunicateMng.DISAPPEAR:
                disappear();
                break;
            case ComunicateMng.APPEAR:
                appear();
                break;
        }
    }
}