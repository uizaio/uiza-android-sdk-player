package testlibuiza.sample.uizavideo.slide;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import testlibuiza.R;
import vn.loitp.core.base.BaseActivity;
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

        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });

        draggablePanel.setDraggableListener(new DraggableListener() {
            @Override
            public void onMaximized() {
                LLog.d(TAG, "onMaximized");
            }

            @Override
            public void onMinimized() {
                LLog.d(TAG, "onMinimized");
                frmTop.getUizaIMAVideo().getPlayerView().hideController();
            }

            @Override
            public void onClosedToLeft() {
                LLog.d(TAG, "onClosedToLeft");
                frmTop.getUizaIMAVideo().onDestroy();
            }

            @Override
            public void onClosedToRight() {
                LLog.d(TAG, "onClosedToRight");
                frmTop.getUizaIMAVideo().onDestroy();
            }
        });
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
        setSizeFrmTop(false);
        draggablePanel.initializeView();
    }

    private void play() {
        initializeDraggablePanel();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (activity != null) {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                setSizeFrmTop(true);
            } else {
                setSizeFrmTop(false);
            }
        }
    }

    private void setSizeFrmTop(boolean isLandscape) {
        if (isLandscape) {
            draggablePanel.setTopViewHeight(LScreenUtil.getScreenHeight());
        } else {
            draggablePanel.setTopViewHeight(LScreenUtil.getScreenWidth() * 9 / 16);
        }
    }
}
