package uizacoresdk.view.floatview;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Player;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import uizacoresdk.R;
import uizacoresdk.util.UZData;
import uizacoresdk.util.UZUtil;
import uizacoresdk.view.ComunicateMng;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LAnimationUtil;
import vn.uiza.core.utilities.LConnectivityUtil;
import vn.uiza.core.utilities.LDeviceUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LUIUtil;

/**
 * Created by loitp on 1/4/2019.
 */

public class FUZVideoService extends Service implements FUZVideo.Callback {
    private final String TAG = "TAG" + getClass().getSimpleName();
    private WindowManager mWindowManager;
    private View mFloatingView;
    private View viewDestroy;
    private RelativeLayout rlControl;
    private RelativeLayout moveView;
    private ImageButton btExit;
    private ImageButton btFullScreen;
    private ImageButton btPlayPause;
    private TextView tvMsg;
    private FUZVideo fuzVideo;
    private WindowManager.LayoutParams params;
    private String linkPlay;
    //private Gson gson = new Gson();
    private boolean isLivestream;
    private boolean isInitCustomLinkplay;
    private long contentPosition;
    private int screenWidth;
    private int screenHeight;
    private int statusBarHeight;
    private int videoW = 16;
    private int videoH = 9;
    private boolean isEZDestroy;
    private boolean isEnableVibration;
    private boolean isEnableSmoothSwitch;
    private int marginL;
    private int marginT;
    private int marginR;
    private int marginB;

    public FUZVideoService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isInitCustomLinkplay = intent.getBooleanExtra(Constants.FLOAT_USER_USE_CUSTOM_LINK_PLAY, false);
        contentPosition = intent.getLongExtra(Constants.FLOAT_CONTENT_POSITION, 0);
        if (isInitCustomLinkplay) {
        } else {
            if (UZData.getInstance().getData() == null) {
                return super.onStartCommand(intent, flags, startId);
            }
        }
        if (intent.getExtras() != null) {
            linkPlay = intent.getStringExtra(Constants.FLOAT_LINK_PLAY);
            isLivestream = intent.getBooleanExtra(Constants.FLOAT_IS_LIVESTREAM, false);
            //LLog.d(TAG, "onStartCommand isInitCustomLinkplay " + isInitCustomLinkplay + ", contentPosition: " + contentPosition);
            setupVideo();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void findViews() {
        viewDestroy = (View) mFloatingView.findViewById(R.id.view_destroy);
        int colorViewDestroy = UZUtil.getMiniPlayerColorViewDestroy(getBaseContext());
        viewDestroy.setBackgroundColor(colorViewDestroy);
        isEZDestroy = UZUtil.getMiniPlayerEzDestroy(getBaseContext());
        isEnableVibration = UZUtil.getMiniPlayerEnableVibration(getBaseContext());
        isEnableSmoothSwitch = UZUtil.getMiniPlayerEnableSmoothSwitch(getBaseContext());
        //LLog.d(TAG, "isEZDestroy " + isEZDestroy + ", isEnableVibration " + isEnableVibration + ", isEnableSmoothSwitch " + isEnableSmoothSwitch);
        rlControl = (RelativeLayout) mFloatingView.findViewById(R.id.rl_control);
        moveView = (RelativeLayout) mFloatingView.findViewById(R.id.move_view);
        btExit = (ImageButton) mFloatingView.findViewById(R.id.bt_exit);
        btFullScreen = (ImageButton) mFloatingView.findViewById(R.id.bt_full_screen);
        btPlayPause = (ImageButton) mFloatingView.findViewById(R.id.bt_play_pause);
        tvMsg = (TextView) mFloatingView.findViewById(R.id.tv_msg);
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
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        videoW = UZUtil.getVideoWidth(getBaseContext());
        videoH = UZUtil.getVideoHeight(getBaseContext());
        //LLog.d(TAG, "onCreate videoWxvideoH " + videoW + "x" + videoH);
        screenWidth = LScreenUtil.getScreenWidth();
        screenHeight = LScreenUtil.getScreenHeight();
        statusBarHeight = LScreenUtil.getStatusBarHeight(getApplicationContext());
        marginL = UZUtil.getMiniPlayerMarginL(getBaseContext());
        marginT = UZUtil.getMiniPlayerMarginT(getBaseContext());
        marginR = UZUtil.getMiniPlayerMarginR(getBaseContext());
        marginB = UZUtil.getMiniPlayerMarginB(getBaseContext());
        //LLog.d(TAG, "onCreate " + marginL + " - " + marginT + " - " + marginR + " - " + marginB);
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_uiza_video, null);
        findViews();
        //Add the view to the window.
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        //OPTION 1: floatview se neo vao 1 goc cua device
        /*params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);*/

        //OPTION 2: floatview se ko neo vao 1 goc cua device
        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
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
        params.y = screenHeight - getMoveViewHeight() - statusBarHeight;
        //LLog.d(TAG, "first position: " + params.x + "-" + params.y);*/

        //OPTION 3
        //float view o ben ngoai screen cua device
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = screenWidth - 1;
        params.y = screenHeight - 1;
        //LLog.d(TAG, "first position: " + params.x + "-" + params.y);

        fuzVideo = (FUZVideo) mFloatingView.findViewById(R.id.uiza_video);
        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);
        //Drag and move floating view using user's touch action.
        dragAndMove();

        btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });
        btFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openApp();
            }
        });
        btPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleResumePause();
            }
        });
    }

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

    private void updateUIVideoSizeOneTime(int videoW, int videoH) {
        //LLog.d(TAG, "updateUIVideoSizeOneTime " + videoW + "x" + videoH);
        int vW = screenWidth / 2;
        int vH = vW * videoH / videoW;
        int firstPositionX = UZUtil.getMiniPlayerFirstPositionX(getBaseContext());
        int firstPositionY = UZUtil.getMiniPlayerFirstPositionX(getBaseContext());
        if (firstPositionX == Constants.NOT_FOUND || firstPositionY == Constants.NOT_FOUND) {
            firstPositionX = vW;
            firstPositionY = screenHeight - vH - statusBarHeight;
        }
        //LLog.d(TAG, "updateUIVideoSizeOneTime " + firstPositionX + "x" + firstPositionY);
        slideToPosition(firstPositionX, firstPositionY);
    }

    private void openApp() {
        if (fuzVideo == null || fuzVideo.getPlayer() == null) {
            return;
        }
        //set UI
        rlControl.setVisibility(View.GONE);
        int screenHeight = screenWidth * videoH / videoW;
        moveView.getLayoutParams().width = screenWidth;
        moveView.getLayoutParams().height = screenHeight;
        moveView.requestLayout();
        updateUISlide(0, 0);
        //stop video
        if (isEnableSmoothSwitch) {
            //do nothing
        } else {
            fuzVideo.getPlayer().setPlayWhenReady(false);
            LUIUtil.showProgressBar(fuzVideo.getProgressBar());
        }
        moveView.setOnTouchListener(null);//disabled move view
        UZUtil.setClickedPip(getApplicationContext(), true);
        //event bus
        String classNameOfPlayer = UZUtil.getClassNameOfPlayer(getBaseContext());
        if (UZUtil.getClickedPip(getBaseContext())) {
        } else {
            if (UZData.getInstance().getData() == null || classNameOfPlayer == null) {
                return;
            }
        }
        LLog.d(TAG, "miniplayer STEP 5 START OPEN APP -> classNameOfPlayer " + classNameOfPlayer + ", miniplayer content position " + fuzVideo.getCurrentPosition());
        try {
            Class classNamePfPlayer = Class.forName(classNameOfPlayer);
            Intent intent = new Intent(getBaseContext(), classNamePfPlayer);
            UZUtil.setClassNameOfPlayer(getBaseContext(), null);//clear class name of player
            UZUtil.setMiniPlayerContentPositionWhenSwitchToFullPlayer(getBaseContext(), fuzVideo.getCurrentPosition());//save content position for full player seek to
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getBaseContext().startActivity(intent);
        } catch (ClassNotFoundException e) {
            LLog.e(TAG, "Error FloatClickFullScreenReceiver ClassNotFoundException " + e.toString());
        }
    }

    private CountDownTimer countDownTimer;

    private void slideToPosition(int goToPosX, int goToPosY) {
        final int currentPosX = params.x;
        final int currentPosY = params.y;
        //LLog.d(TAG, "slideToPosition current Point: " + currentPosX + " x " + currentPosY);

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

        final int a = (int) Math.abs(mGoToPosX - currentPosX);
        final int b = (int) Math.abs(mGoToPosY - currentPosY);
        //LLog.d(TAG, "slideToPosition " + goToPosX + " x " + goToPosY + " -> a x b: " + a + " x " + b + " -> mGoToPosX x mGoToPosY: " + mGoToPosX + "x" + mGoToPosY);
        countDownTimer = new CountDownTimer(300, 3) {
            public void onTick(long t) {
                float step = (300 - t) / 3;
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
                //LLog.d(TAG, "slideToLeft onTick step: " + step + ", " + tmpX + " x " + tmpY);
                updateUISlide(tmpX, tmpY);
            }

            public void onFinish() {
                //LLog.d(TAG, "slideToLeft onFinish " + goToPosX + " x " + goToPosY);
                updateUISlide(mGoToPosX, mGoToPosY);
            }
        }.start();
    }

    private void updateUISlide(int x, int y) {
        params.x = x;
        params.y = y;
        mWindowManager.updateViewLayout(mFloatingView, params);
    }

    private GestureDetector mTapDetector;

    private class GestureTap extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            //LLog.d(TAG, "onSingleTapConfirmed");
            toggleController();
            return true;
        }
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

    private void dragAndMove() {
        mTapDetector = new GestureDetector(getBaseContext(), new GestureTap());
        moveView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mTapDetector.onTouchEvent(event);
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
                        //LLog.d(TAG, "ACTION_MOVE " + (int) event.getRawX() + " - " + (int) event.getRawY() + "___" + (initialX + (int) (event.getRawX() - initialTouchX) + " - " + (initialY + (int) (event.getRawY() - initialTouchY))));
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        getLocationOnScreen(mFloatingView);
                        return true;
                }
                return false;
            }
        });
    }

    private enum POS {TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER_LEFT, CENTER_RIGHT, CENTER_TOP, CENTER_BOTTOM, LEFT, RIGHT, TOP, BOTTOM, CENTER}

    private POS pos;

    private void notiPos(POS tmpPos) {
        if (pos != tmpPos) {
            pos = tmpPos;
            //LLog.d(TAG, "notiPos: " + pos);
            if (isEZDestroy) {
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
                        viewDestroy.setVisibility(View.VISIBLE);
                        break;
                    default:
                        if (viewDestroy.getVisibility() != View.GONE) {
                            viewDestroy.setVisibility(View.GONE);
                        }
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
        //LLog.d(TAG, "getLocationOnScreen " + posLeft + " - " + posTop + " - " + posRight + " - " + posBottom);
        if (centerX < 0) {
            if (centerY < 0) {
                //LLog.d(TAG, "TOP_LEFT");
                notiPos(POS.TOP_LEFT);
            } else if (centerY > screenHeight) {
                //LLog.d(TAG, "BOTTOM_LEFT");
                notiPos(POS.BOTTOM_LEFT);
            } else {
                //LLog.d(TAG, "CENTER_LEFT");
                notiPos(POS.CENTER_LEFT);
            }
        } else if (centerX > screenWidth) {
            if (centerY < 0) {
                //LLog.d(TAG, "TOP_RIGHT");
                notiPos(POS.TOP_RIGHT);
            } else if (centerY > screenHeight) {
                //LLog.d(TAG, "BOTTOM_RIGHT");
                notiPos(POS.BOTTOM_RIGHT);
            } else {
                //LLog.d(TAG, "CENTER_RIGHT");
                notiPos(POS.CENTER_RIGHT);
            }
        } else {
            if (centerY < 0) {
                //LLog.d(TAG, "CENTER_TOP");
                notiPos(POS.CENTER_TOP);
            } else if (centerY > screenHeight) {
                //LLog.d(TAG, "CENTER_BOTTOM");
                notiPos(POS.CENTER_BOTTOM);
            } else {
                if (posLeft < 0) {
                    //LLog.d(TAG, "LEFT");
                    notiPos(POS.LEFT);
                } else if (posRight > screenWidth) {
                    //LLog.d(TAG, "RIGHT");
                    notiPos(POS.RIGHT);
                } else {
                    if (posTop < 0) {
                        //LLog.d(TAG, "TOP");
                        notiPos(POS.TOP);
                    } else if (posBottom > screenHeight) {
                        //LLog.d(TAG, "BOTTOM");
                        notiPos(POS.BOTTOM);
                    } else {
                        //LLog.d(TAG, "CENTER");
                        notiPos(POS.CENTER);
                    }
                }
            }
        }

    }

    private void onMoveUp() {
        //LLog.d(TAG, "onMoveUp " + pos.name());
        if (pos == null) {
            return;
        }
        int posX;
        int posY;
        int centerPosX;
        int centerPosY;
        if (isEZDestroy) {
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
                    //LLog.d(TAG, "CENTER " + centerPosX + "x" + centerPosY);
                    if (centerPosX < screenWidth / 2) {
                        if (centerPosY < screenHeight / 2) {
                            //LLog.d(TAG, "top left part");
                            slideToPosition(0, 0);
                        } else {
                            //LLog.d(TAG, "bottom left part");
                            slideToPosition(0, screenHeight - getMoveViewHeight() - statusBarHeight);
                        }
                    } else {
                        if (centerPosY < screenHeight / 2) {
                            //LLog.d(TAG, "top right part");
                            slideToPosition(screenWidth - getMoveViewWidth(), 0);
                        } else {
                            //LLog.d(TAG, "bottom right part");
                            slideToPosition(screenWidth - getMoveViewWidth(), screenHeight - getMoveViewHeight() - statusBarHeight);
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
                    //LLog.d(TAG, "CENTER " + centerPosX + "x" + centerPosY);
                    if (centerPosX < screenWidth / 2) {
                        if (centerPosY < screenHeight / 2) {
                            //LLog.d(TAG, "top left part");
                            slideToPosition(0, 0);
                        } else {
                            //LLog.d(TAG, "bottom left part");
                            slideToPosition(0, screenHeight - getMoveViewHeight() - statusBarHeight);
                        }
                    } else {
                        if (centerPosY < screenHeight / 2) {
                            //LLog.d(TAG, "top right part");
                            slideToPosition(screenWidth - getMoveViewWidth(), 0);
                        } else {
                            //LLog.d(TAG, "bottom right part");
                            slideToPosition(screenWidth - getMoveViewWidth(), screenHeight - getMoveViewHeight() - statusBarHeight);
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
            slideToPosition(0, screenHeight - getMoveViewHeight() - statusBarHeight);
        } else {
            slideToPosition(screenWidth - getMoveViewWidth(), screenHeight - getMoveViewHeight() - statusBarHeight);
        }
    }

    private void slideToLeft() {
        int posY = params.y;
        int centerPosY = posY + getMoveViewHeight() / 2;
        if (centerPosY < screenHeight / 2) {
            slideToPosition(0, 0);
        } else {
            slideToPosition(0, screenHeight - getMoveViewHeight() - statusBarHeight);
        }
    }

    private void slideToRight() {
        int posY = params.y;
        int centerPosY = posY + getMoveViewHeight() / 2;
        if (centerPosY < screenHeight / 2) {
            slideToPosition(screenWidth - getMoveViewWidth(), 0);
        } else {
            slideToPosition(screenWidth - getMoveViewWidth(), screenHeight - getMoveViewHeight() - statusBarHeight);
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        if (mFloatingView != null) {
            mWindowManager.removeView(mFloatingView);
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void isInitResult(boolean isInitSuccess) {
        if (isInitSuccess && fuzVideo != null) {
            if (mFloatingView == null) {
                return;
            }
            LLog.d(TAG, "miniplayer STEP 2 isInitResult true");
            editSizeOfMoveView();
            //sau khi da play thanh cong thi chuyen mini player ben ngoai screen vao trong screen
            updateUIVideoSizeOneTime(fuzVideo.getVideoW(), fuzVideo.getVideoH());
            if (!isSendMsgToActivity) {
                //LLog.d(TAG, "state finish loading PIP -> send msg to UZVideo");
                ComunicateMng.MsgFromServiceIsInitSuccess msgFromServiceIsInitSuccess = new ComunicateMng.MsgFromServiceIsInitSuccess(null);
                msgFromServiceIsInitSuccess.setInitSuccess(isInitSuccess);
                ComunicateMng.postFromService(msgFromServiceIsInitSuccess);
                isSendMsgToActivity = true;
            }
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case Player.STATE_BUFFERING:
                //LLog.d(TAG, "STATE_BUFFERING");
                break;
            case Player.STATE_ENDED:
                onStateEnded();
                break;
            case Player.STATE_IDLE:
                //LLog.d(TAG, "STATE_IDLE");
                break;
            case Player.STATE_READY:
                //LLog.d(TAG, "STATE_READY");
                break;
        }
    }

    private void onStateEnded() {
        if (UZData.getInstance().isPlayWithPlaylistFolder()) {
            //LLog.d(TAG, "Dang play o che do playlist/folder -> play next item");
            fuzVideo.getLinkPlayOfNextItem(new FUZVideo.CallbackGetLinkPlay() {
                @Override
                public void onSuccess(String lp) {
                    linkPlay = lp;
                    if (linkPlay == null) {
                        //LLog.d(TAG, "isPlaySuccess false -> stopSelf()");
                        stopSelf();
                    }
                    //LLog.d(TAG, "next linkPlay " + linkPlay);
                    setupVideo();
                }
            });
        } else {
            //LLog.d(TAG, "Dang play o che do entity -> stopSelf()");
            stopSelf();
        }
    }

    @Override
    public void onVideoSizeChanged(int width, int height) {
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        LLog.e(TAG, "onPlayerError " + error.getMessage());
        stopSelf();
    }

    private boolean isSendMsgToActivity;

    private void setupVideo() {
        if (linkPlay == null || linkPlay.isEmpty()) {
            //LLog.d(TAG, "setupVideo linkPlay == null || linkPlay.isEmpty()");
            stopSelf();
            return;
        }
        //LLog.d(TAG, "setupVideo linkPlay " + linkPlay + ", isLivestream: " + isLivestream);
        if (LConnectivityUtil.isConnected(this)) {
            fuzVideo.init(linkPlay, isLivestream, contentPosition, this);
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
            h = w * videoH / videoW;
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
            //LLog.d(TAG, "setSizeMoveView isFirstSizeInit:" + isFirstSizeInit + ",isLarger: " + isLarger + ", " + w + "x" + h);
            moveView.requestLayout();
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

    private void editSizeOfMoveView() {
        if (fuzVideo == null || moveView == null || videoW == 0) {
            return;
        }
        int videoW = fuzVideo.getVideoW();
        int videoH = fuzVideo.getVideoH();
        int moveW = getMoveViewWidth();
        int moveH = moveW * videoH / videoW;
        //LLog.d(TAG, "editSizeOfMoveView " + videoW + "x" + videoH + " -> " + moveW + "x" + moveH);
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
                LLog.d(TAG, "miniplayer STEP 4 MsgFromActivityPosition -> isEnableSmoothSwitch true");
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
            //LLog.d(TAG, "lang nghe UZVideo da init success hay chua");
            LLog.d(TAG, "miniplayer STEP 8 MsgFromActivityIsInitSuccess isInitSuccess: " + ((ComunicateMng.MsgFromActivityIsInitSuccess) msgFromActivity).isInitSuccess());
            if (isEnableSmoothSwitch) {
                //get current content position and pass it to UZVideo
                /*ComunicateMng.MsgFromServicePosition msgFromServicePosition = new ComunicateMng.MsgFromServicePosition(null);
                msgFromServicePosition.setPosition(fuzVideo.getCurrentPosition());
                ComunicateMng.postFromService(msgFromServicePosition);
                */
            } else {
                //do nothing
            }
            stopSelf();
        }
        String msg = msgFromActivity.getMsg();
        //LLog.d(TAG, "msg " + msg);
        if (msg.equals(ComunicateMng.SHOW_MINI_PLAYER_CONTROLLER)) {
            showController();
        } else if (msg.equals(ComunicateMng.HIDE_MINI_PLAYER_CONTROLLER)) {
            hideController();
        } else if (msg.equals(ComunicateMng.TOGGLE_MINI_PLAYER_CONTROLLER)) {
            toggleController();
        } else if (msg.equals(ComunicateMng.PAUSE_MINI_PLAYER)) {
            pauseVideo();
        } else if (msg.equals(ComunicateMng.RESUME_MINI_PLAYER)) {
            resumeVideo();
        } else if (msg.equals(ComunicateMng.TOGGLE_RESUME_PAUSE_MINI_PLAYER)) {
            toggleResumePause();
        } else if (msg.equals(ComunicateMng.OPEN_APP_FROM_MINI_PLAYER)) {
            openApp();
        }
    }
}