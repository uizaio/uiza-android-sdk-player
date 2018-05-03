package testlibuiza.sample.uizavideo.slide;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import com.google.android.exoplayer2.ui.PlayerControlView;

import testlibuiza.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.views.draggablepanel.DraggableListener;
import vn.loitp.views.draggablepanel.DraggablePanel;

public class TestUizaVideoIMActivityRlSlide extends BaseActivity {
    private DraggablePanel draggablePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        draggablePanel = (DraggablePanel) findViewById(R.id.draggable_panel);

        draggablePanel.setDraggableListener(new DraggableListener() {
            @Override
            public void onMaximized() {
                //LLog.d(TAG, "onMaximized");
            }

            @Override
            public void onMinimized() {
                //LLog.d(TAG, "onMinimized");
                frmTop.getUizaIMAVideo().getPlayerView().hideController();
            }

            @Override
            public void onClosedToLeft() {
                //LLog.d(TAG, "onClosedToLeft");
                frmTop.getUizaIMAVideo().onDestroy();
            }

            @Override
            public void onClosedToRight() {
                //LLog.d(TAG, "onClosedToRight");
                frmTop.getUizaIMAVideo().onDestroy();
            }

            @Override
            public void onDrag(int left, int top, int dx, int dy) {
                //LLog.d(TAG, "onDrag " + left + " - " + top + " - " + dx + " - " + dy);
                frmTop.getUizaIMAVideo().getPlayerView().hideController();
            }
        });
        replaceFragment(new FrmHome());
    }

    public void replaceFragment(BaseFragment baseFragment) {
        LScreenUtil.replaceFragment(activity, R.id.fl_container, baseFragment, true);
    }

    @Override
    protected boolean setFullScreen() {
        return false;
    }

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.test_uiza_ima_video_activity_rl_slide;
    }

    private FrmTop frmTop;
    private FrmBottom frmBottom;

    private void initializeDraggablePanel() {
        if (frmTop != null || frmBottom != null) {
            LLog.d(TAG, "initializeDraggablePanel exist");
            draggablePanel.minimize();
            frmTop.onResume();
            return;
        }
        frmTop = new FrmTop();
        frmBottom = new FrmBottom();

        draggablePanel.setFragmentManager(getSupportFragmentManager());
        draggablePanel.setTopFragment(frmTop);
        draggablePanel.setBottomFragment(frmBottom);

        //draggablePanel.setXScaleFactor(xScaleFactor);
        //draggablePanel.setYScaleFactor(yScaleFactor);
        //draggablePanel.setTopViewHeight(800);
        //draggablePanel.setTopFragmentMarginRight(topViewMarginRight);
        //draggablePanel.setTopFragmentMarginBottom(topViewMargnBottom);
        draggablePanel.setClickToMaximizeEnabled(false);
        draggablePanel.setClickToMinimizeEnabled(false);
        draggablePanel.setEnableHorizontalAlphaEffect(false);
        setSizeFrmTop();
        draggablePanel.initializeView();

        frmTop.setFrmTopCallback(new FrmTop.FrmTopCallback() {
            @Override
            public void initDone() {
                LLog.d(TAG, "initDone");
                frmTop.getUizaIMAVideo().getPlayerView().setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
                    @Override
                    public void onVisibilityChange(int visibility) {
                        if (draggablePanel != null && !isLandscape) {
                            if (draggablePanel.isMaximized()) {
                                if (visibility == View.VISIBLE) {
                                    LLog.d(TAG, TAG + " onVisibilityChange visibility == View.VISIBLE");
                                    draggablePanel.setEnableSlide(false);
                                } else {
                                    LLog.d(TAG, TAG + " onVisibilityChange visibility != View.VISIBLE");
                                    draggablePanel.setEnableSlide(true);
                                }
                            } else {
                                draggablePanel.setEnableSlide(true);
                            }
                        }
                    }
                });
            }
        });
    }

    public void play() {
        initializeDraggablePanel();
    }

    private boolean isLandscape;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (activity != null) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                isLandscape = true;
                setSizeFrmTop();
                draggablePanel.setEnableSlide(false);
            } else {
                isLandscape = false;
                setSizeFrmTop();
                draggablePanel.setEnableSlide(true);
            }
        }
    }

    private void setSizeFrmTop() {
        if (isLandscape) {
            draggablePanel.setTopViewHeightApllyNow(LScreenUtil.getScreenHeight());
        } else {
            draggablePanel.setTopViewHeightApllyNow(LScreenUtil.getScreenWidth() * 9 / 16);
        }
    }
}
