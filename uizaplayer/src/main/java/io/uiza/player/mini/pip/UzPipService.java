package io.uiza.player.mini.pip;

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
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.LLog;
import io.uiza.core.util.SentryUtil;
import io.uiza.core.util.UzAnimationUtil;
import io.uiza.core.util.UzCommonUtil;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.util.connection.UzConnectivityUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.player.R;
import io.uiza.player.analytic.TmpParamData;
import io.uiza.player.util.ComunicateMsg;
import io.uiza.player.util.UzPlayerData;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class UzPipService extends Service implements UzPipPlayer.Callback {

    private static final String TAG = UzPipService.class.getSimpleName();
    private WindowManager windowManager;
    private View floatingView;
    private View viewDestroy;
    private RelativeLayout rlControl;
    private RelativeLayout moveView;
    private ImageView btExit;
    private ImageView btFullScreen;
    private ImageView btPlayPause;
    private TextView tvMsg;
    private UzPipPlayer uzPipPlayer;
    private ViewStub controlStub;
    private WindowManager.LayoutParams params;
    private String linkPlay;
    private boolean isLivestream;
    private boolean isInitCustomLinkPlay;
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
    private LinkPlay liveLinkPlay;
    private int positionBeforeDisappearX = Constants.UNKNOWN;
    private int positionBeforeDisappearY = Constants.UNKNOWN;
    private CountDownTimer countDownTimer;
    private GestureDetector gestureDetector;
    private MiniPos miniPos;

    private enum MiniPos {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER_LEFT, CENTER_RIGHT, CENTER_TOP,
        CENTER_BOTTOM, LEFT, RIGHT, TOP, BOTTOM, CENTER
    }

    public UzPipService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isInitCustomLinkPlay = intent
                .getBooleanExtra(Constants.FLOAT_USER_USE_CUSTOM_LINK_PLAY, false);
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
            cdnHost = liveLinkPlay.getCdn().get(0).getHost();
        } catch (NullPointerException e) {
            LLog.e(TAG, "Error cannot find cdnHost " + e.toString());
            SentryUtil.captureException(e);
        }
        uuid = intent.getStringExtra(Constants.FLOAT_UUID);
        if (!isInitCustomLinkPlay) {
            if (UzPlayerData.getInstance().getVideoData() == null) {
                return START_NOT_STICKY;
            }
        }
        if (intent.getExtras() != null) {
            linkPlay = intent.getStringExtra(Constants.FLOAT_LINK_PLAY);
            isLivestream = intent.getBooleanExtra(Constants.FLOAT_IS_LIVESTREAM, false);
            LLog.d(TAG, "onStartCommand isInitCustomLinkPlay " + isInitCustomLinkPlay
                    + ", contentPosition: " + contentPosition + ", cdnHost: " + cdnHost
                    + ", linkPlay: " + linkPlay);
            setupVideo();
        }
        return START_NOT_STICKY;
    }

    private void findViews() {
        moveView = floatingView.findViewById(R.id.move_view);
        uzPipPlayer = floatingView.findViewById(R.id.uiza_video);
        controlStub = floatingView.findViewById(R.id.control_stub);

        tvMsg = floatingView.findViewById(R.id.tv_msg);
        UzDisplayUtil.setTextShadow(tvMsg);
        tvMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UzAnimationUtil.play(v, Techniques.Pulse, new UzAnimationUtil.Callback() {
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
        int colorViewDestroy = PipHelper.getMiniPlayerColorViewDestroy(getBaseContext());
        viewDestroy.setBackgroundColor(colorViewDestroy);
        isEzDestroy = PipHelper.getMiniPlayerEzDestroy(getBaseContext());
        isEnableVibration = PipHelper.getMiniPlayerEnableVibration(getBaseContext());
        isEnableSmoothSwitch = PipHelper.getMiniPlayerEnableSmoothSwitch(getBaseContext());

        isAutoSize = PipHelper.getMiniPlayerAutoSize(getBaseContext());
        if (!isAutoSize) {
            videoWidthFromSettingConfig = PipHelper.getMiniPlayerSizeWidth(getBaseContext());
            videoHeightFromSettingConfig = PipHelper.getMiniPlayerSizeHeight(getBaseContext());
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
        liveLinkPlay = UzPlayerData.getInstance().getLinkPlay();
        if (liveLinkPlay == null) {
            stopSelf();
        }
        videoW = PipHelper.getVideoWidth(getBaseContext());
        videoH = PipHelper.getVideoHeight(getBaseContext());
        screenWidth = UzDisplayUtil.getScreenWidth();
        screenHeight = UzDisplayUtil.getScreenHeight();
        pipTopPosition = PipHelper.getStablePipTopPosition(getBaseContext());
        marginL = PipHelper.getMiniPlayerMarginL(getBaseContext());
        marginT = PipHelper.getMiniPlayerMarginT(getBaseContext());
        marginR = PipHelper.getMiniPlayerMarginR(getBaseContext());
        marginB = PipHelper.getMiniPlayerMarginB(getBaseContext());
        floatingView = LayoutInflater.from(this)
                .inflate(R.layout.layout_floating_uiza_video, null, false);
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
        if (uzPipPlayer == null || btPlayPause == null) {
            return;
        }
        boolean isToggleResume = uzPipPlayer.togglePauseResume();
        if (isToggleResume) {
            btPlayPause.setImageResource(R.drawable.baseline_pause_circle_outline_white_48);
        } else {
            btPlayPause.setImageResource(R.drawable.baseline_play_circle_outline_white_48);
        }
    }

    private void pauseVideo() {
        if (uzPipPlayer == null || btPlayPause == null) {
            return;
        }
        uzPipPlayer.pauseVideo();
        btPlayPause.setImageResource(R.drawable.baseline_play_circle_outline_white_48);
    }

    private void resumeVideo() {
        if (uzPipPlayer == null || btPlayPause == null) {
            return;
        }
        uzPipPlayer.resumeVideo();
        btPlayPause.setImageResource(R.drawable.baseline_pause_circle_outline_white_48);
    }

    private void disappear() {
        if (uzPipPlayer == null) {
            return;
        }
        positionBeforeDisappearX = params.x;
        positionBeforeDisappearY = params.y;
        updateUiSlide(screenWidth, screenHeight);
    }

    private void appear() {
        if (positionBeforeDisappearX == Constants.UNKNOWN
                || positionBeforeDisappearY == Constants.UNKNOWN) {
            return;
        }
        updateUiSlide(positionBeforeDisappearX, positionBeforeDisappearY);
        positionBeforeDisappearX = Constants.UNKNOWN;
        positionBeforeDisappearY = Constants.UNKNOWN;
    }

    private void openApp() {
        if (uzPipPlayer == null || uzPipPlayer.getPlayer() == null) {
            return;
        }
        //stop video
        if (!isEnableSmoothSwitch) {
            uzPipPlayer.getPlayer().setPlayWhenReady(false);
        }
        //moveView.setOnTouchListener(null);//disabled move view
        PipHelper.setClickedPip(getApplicationContext(), true);
        if (UzPlayerData.getInstance().getVideoData() == null) {
            return;
        }
        LLog.d(TAG, "miniplayer STEP 5 START OPEN APP, miniplayer content position " + uzPipPlayer
                .getCurrentPosition());
        ComunicateMsg.MsgFromServiceOpenApp msgFromServiceOpenApp = new ComunicateMsg.MsgFromServiceOpenApp(
                null);
        msgFromServiceOpenApp.setPositionMiniPlayer(uzPipPlayer.getCurrentPosition());
        ComunicateMsg.postFromService(msgFromServiceOpenApp);
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
        if (uzPipPlayer == null) {
            return 0;
        }
        return uzPipPlayer.getVideoW();
    }

    private int getVideoH() {
        if (uzPipPlayer == null) {
            return 0;
        }
        return uzPipPlayer.getVideoH();
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
        int firstPositionX = PipHelper.getMiniPlayerFirstPositionX(getBaseContext());
        int firstPositionY = PipHelper.getMiniPlayerFirstPositionY(getBaseContext());
        if (firstPositionX == Constants.NOT_FOUND || firstPositionY == Constants.NOT_FOUND) {
            firstPositionX = screenWidth - vW;
            firstPositionY = screenHeight - vH - pipTopPosition;
        }
        slideToPosition(firstPositionX, firstPositionY);
    }

    private void slideToPosition(int goToPosX, int goToPosY) {
        final int currentPosX = params.x;
        final int currentPosY = params.y;
        final int mGoToPosX;
        final int mGoToPosY;
        int videoW = getVideoW();
        int videoH = getVideoH();
        if (goToPosX <= 0) {
            mGoToPosX = marginL;
        } else if (goToPosX >= screenWidth - videoW) {
            mGoToPosX = goToPosX - marginR;
        } else {
            mGoToPosX = goToPosX;
        }
        if (goToPosY <= 0) {
            mGoToPosY = marginT;
        } else if (goToPosY >= screenHeight - videoH) {
            mGoToPosY = goToPosY - marginB;
        } else {
            mGoToPosY = goToPosY;
        }
        final int a = Math.abs(mGoToPosX - currentPosX);
        final int b = Math.abs(mGoToPosY - currentPosY);
        countDownTimer = new CountDownTimer(300, 3) {
            public void onTick(long t) {
                float step = (300.f - t) / 3;
                int tmpX;
                int tmpY;
                if (currentPosX > mGoToPosX) {
                    if (currentPosY > mGoToPosY) {
                        tmpX = currentPosX - (int) (a * step / 100);
                        tmpY = currentPosY - (int) (b * step / 100);
                    } else {
                        tmpX = currentPosX - (int) (a * step / 100);
                        tmpY = currentPosY + (int) (b * step / 100);
                    }
                } else {
                    if (currentPosY > mGoToPosY) {
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
                updateUiSlide(mGoToPosX, mGoToPosY);
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
            boolean isTapToFullPlayer = PipHelper.getMiniPlayerTapToFullPlayer(getBaseContext());
            if (isTapToFullPlayer) {
                setSizeMoveView(false,
                        true);//remove this line make animation switch from mini-player to full-player incorrectly
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

                        //Update the layout with new X & Y coordinate
                        updateUiSlide(tmpX, tmpY);
                        getLocationOnScreen(floatingView);
                        return true;
                }
                return false;
            }
        });
    }


    private void notiPos(MiniPos tmpMiniPos) {
        if (miniPos == tmpMiniPos) {
            return;
        }
        miniPos = tmpMiniPos;
        if (isEzDestroy) {
            switch (miniPos) {
                case TOP_LEFT:
                case TOP_RIGHT:
                case BOTTOM_LEFT:
                case BOTTOM_RIGHT:
                case CENTER_LEFT:
                case CENTER_RIGHT:
                case CENTER_TOP:
                case CENTER_BOTTOM:
                    if (isEnableVibration) {
                        UzCommonUtil.vibrate(getBaseContext());
                    }
                    viewDestroy.setVisibility(View.VISIBLE);
                    break;
                default:
                    if (viewDestroy.getVisibility() != View.GONE) {
                        viewDestroy.setVisibility(View.GONE);
                    }
                    break;
            }
        } else {
            switch (miniPos) {
                case TOP_LEFT:
                case TOP_RIGHT:
                case BOTTOM_LEFT:
                case BOTTOM_RIGHT:
                    if (isEnableVibration) {
                        UzCommonUtil.vibrate(getBaseContext());
                    }
                    viewDestroy.setVisibility(View.VISIBLE);
                    break;
                default:
                    if (viewDestroy.getVisibility() != View.GONE) {
                        viewDestroy.setVisibility(View.GONE);
                    }
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
                notiPos(MiniPos.TOP_LEFT);
            } else if (centerY > screenHeight) {
                notiPos(MiniPos.BOTTOM_LEFT);
            } else {
                notiPos(MiniPos.CENTER_LEFT);
            }
        } else if (centerX > screenWidth) {
            if (centerY < 0) {
                notiPos(MiniPos.TOP_RIGHT);
            } else if (centerY > screenHeight) {
                notiPos(MiniPos.BOTTOM_RIGHT);
            } else {
                notiPos(MiniPos.CENTER_RIGHT);
            }
        } else {
            if (centerY < 0) {
                notiPos(MiniPos.CENTER_TOP);
            } else if (centerY > screenHeight) {
                notiPos(MiniPos.CENTER_BOTTOM);
            } else {
                if (posLeft < 0) {
                    notiPos(MiniPos.LEFT);
                } else if (posRight > screenWidth) {
                    notiPos(MiniPos.RIGHT);
                } else {
                    if (posTop < 0) {
                        notiPos(MiniPos.TOP);
                    } else if (posBottom > screenHeight) {
                        notiPos(MiniPos.BOTTOM);
                    } else {
                        notiPos(MiniPos.CENTER);
                    }
                }
            }
        }

    }

    private void onMoveUp() {
        if (miniPos == null) {
            return;
        }
        int posX;
        int posY;
        int centerPosX;
        int centerPosY;
        if (isEzDestroy) {
            switch (miniPos) {
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
                            slideToPosition(screenWidth - getMoveViewWidth(),
                                    screenHeight - getMoveViewHeight() - pipTopPosition);
                        }
                    }
                    break;
            }
        } else {
            switch (miniPos) {
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
                            slideToPosition(screenWidth - getMoveViewWidth(),
                                    screenHeight - getMoveViewHeight() - pipTopPosition);
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
            slideToPosition(screenWidth - getMoveViewWidth(),
                    screenHeight - getMoveViewHeight() - pipTopPosition);
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
            slideToPosition(screenWidth - getMoveViewWidth(),
                    screenHeight - getMoveViewHeight() - pipTopPosition);
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (floatingView != null) {
            windowManager.removeView(floatingView);
        }
        if (uzPipPlayer != null) {
            uzPipPlayer.onDestroy();
        }
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        PipHelper.setClickedPip(getApplicationContext(), false);
        super.onDestroy();
    }

    @Override
    public void onDataInitialized(boolean isInitSuccess) {
        if (!isInitSuccess || uzPipPlayer == null || floatingView == null) {
            return;
        }
        LLog.d(TAG, "miniplayer STEP 2 onDataInitialized true");
        editSizeOfMoveView();
        //sau khi da play thanh cong thi chuyen mini player ben ngoai screen vao trong screen
        updateUiVideoSizeOneTime(uzPipPlayer.getVideoW(), uzPipPlayer.getVideoH());
        if (!isSendMsgToActivity) {
            ComunicateMsg.MsgFromServiceIsInitSuccess msgFromServiceIsInitSuccess = new ComunicateMsg.MsgFromServiceIsInitSuccess(
                    null);
            msgFromServiceIsInitSuccess.setInitSuccess(true);
            ComunicateMsg.postFromService(msgFromServiceIsInitSuccess);
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
        if (UzPlayerData.getInstance().isPlayWithPlaylistFolder()) {
            if (uzPipPlayer == null) {
                return;
            }
            uzPipPlayer.getLinkPlayOfNextItem(new UzPipPlayer.CallbackGetLinkPlay() {
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
    public void onPlayerError(UzException error) {
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
        if (UzConnectivityUtil.isConnected(this)) {
            uzPipPlayer.init(linkPlay, cdnHost, uuid, isLivestream, contentPosition,
                    isInitCustomLinkPlay, progressBarColor, this);
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
                h = w * 9 / 16;
            } else {
                h = w * videoH / videoW;
            }
        }
        if (w != 0 && h != 0) {
            moveView.getLayoutParams().width = w;
            moveView.getLayoutParams().height = h;
            moveView.requestLayout();
        }
    }

    private void editSizeOfMoveView() {
        if (uzPipPlayer == null || moveView == null || videoW == 0) {
            return;
        }
        int videoW = uzPipPlayer.getVideoW();
        int videoH = uzPipPlayer.getVideoH();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ComunicateMsg.MsgFromActivity msgFromActivity) {
        if (msgFromActivity == null || uzPipPlayer == null) {
            return;
        }
        if (msgFromActivity instanceof ComunicateMsg.MsgFromActivityPosition) {
            if (isEnableSmoothSwitch) {
                //smooth but content position is delay
                LLog.d(TAG,
                        "miniplayer STEP 4 MsgFromActivityPosition -> isEnableSmoothSwitch true -> do nothing");
            } else {
                long contentPosition = ((ComunicateMsg.MsgFromActivityPosition) msgFromActivity)
                        .getPosition();
                long contentBufferedPosition = uzPipPlayer.getContentBufferedPosition();
                LLog.d(TAG,
                        "miniplayer STEP 4 MsgFromActivityPosition -> isEnableSmoothSwitch false -> contentBufferedPosition "
                                + contentBufferedPosition + ", position: " + contentPosition);
                if (contentPosition >= contentBufferedPosition) {
                    uzPipPlayer.seekTo(contentBufferedPosition);
                } else {
                    uzPipPlayer.seekTo(contentPosition);
                }
            }
        } else if (msgFromActivity instanceof ComunicateMsg.MsgFromActivityIsInitSuccess) {
            LLog.d(TAG, "miniplayer STEP 7 MsgFromActivityIsInitSuccess isInitSuccess: "
                    + ((ComunicateMsg.MsgFromActivityIsInitSuccess) msgFromActivity)
                    .isInitSuccess());
            stopSelf();
        }
        if (msgFromActivity.getMsg() == null) {
            return;
        }
        handleMsgFromActivity(msgFromActivity);
    }

    private void handleMsgFromActivity(ComunicateMsg.MsgFromActivity msgFromActivity) {
        String msg = msgFromActivity.getMsg();
        switch (msg) {
            case ComunicateMsg.SHOW_MINI_PLAYER_CONTROLLER:
                showController();
                break;
            case ComunicateMsg.HIDE_MINI_PLAYER_CONTROLLER:
                hideController();
                break;
            case ComunicateMsg.TOGGLE_MINI_PLAYER_CONTROLLER:
                toggleController();
                break;
            case ComunicateMsg.PAUSE_MINI_PLAYER:
                pauseVideo();
                break;
            case ComunicateMsg.RESUME_MINI_PLAYER:
                resumeVideo();
                break;
            case ComunicateMsg.TOGGLE_RESUME_PAUSE_MINI_PLAYER:
                toggleResumePause();
                break;
            case ComunicateMsg.OPEN_APP_FROM_MINI_PLAYER:
                openApp();
                break;
            case ComunicateMsg.DISAPPEAR:
                disappear();
                break;
            case ComunicateMsg.APPEAR:
                appear();
                break;
        }
    }
}