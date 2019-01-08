package testlibuiza.sample.v3.demoui;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import testlibuiza.R;
import uizacoresdk.interfaces.IOnBackPressed;
import uizacoresdk.util.UZUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.views.draggablepanel.DraggableListener;
import vn.uiza.views.draggablepanel.DraggablePanel;

public class HomeCanSlideActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private DraggablePanel draggablePanel;

    public DraggablePanel getDraggablePanel() {
        return draggablePanel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        //UZUtil.setCurrentPlayerId(R.layout.uiza_controller_skin_custom_main);
        //UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_0);
        UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_1);
        //UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_1);
        //UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_2);
        //UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_3);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v4_home_canslide_activity);
        draggablePanel = (DraggablePanel) findViewById(R.id.draggable_panel);
        draggablePanel.setDraggableListener(new DraggableListener() {
            @Override
            public void onMaximized() {
                //LLog.d(TAG, "onMaximized");
            }

            @Override
            public void onMinimized() {
                //LLog.d(TAG, "onMinimized");
                if (frmVideoTop != null && frmVideoTop.getUZVideo() != null) {
                    frmVideoTop.getUZVideo().hideController();
                }
            }

            @Override
            public void onClosedToLeft() {
                //LLog.d(TAG, "onClosedToLeft");
                if (frmVideoTop != null && frmVideoTop.getUZVideo() != null) {
                    frmVideoTop.getUZVideo().onDestroy();
                }
            }

            @Override
            public void onClosedToRight() {
                //LLog.d(TAG, "onClosedToRight");
                if (frmVideoTop != null && frmVideoTop.getUZVideo() != null) {
                    frmVideoTop.getUZVideo().onDestroy();
                }
            }

            @Override
            public void onDrag(int left, int top, int dx, int dy) {
                //LLog.d(TAG, "onDrag " + left + " - " + top + " - " + dx + " - " + dy);
            }
        });
        initializeDraggablePanel();
        replaceFragment(new FrmHome());
    }

    public void replaceFragment(Fragment baseFragment) {
        if (baseFragment instanceof FrmHome) {
            LScreenUtil.replaceFragment((AppCompatActivity) activity, R.id.fl_container, baseFragment, false);
        } else {
            LScreenUtil.replaceFragment((AppCompatActivity) activity, R.id.fl_container, baseFragment, true);
        }
    }

    private FrmVideoTop frmVideoTop;
    private FrmVideoBottom frmVideoBottom;

    private void initializeDraggablePanel() {
        if (frmVideoTop != null || frmVideoBottom != null) {
            LLog.d(TAG, "initializeDraggablePanel exist");
            draggablePanel.minimize();
            frmVideoTop.onResume();
            return;
        }
        frmVideoTop = new FrmVideoTop();
        frmVideoBottom = new FrmVideoBottom();

        draggablePanel.setFragmentManager(getSupportFragmentManager());
        draggablePanel.setTopFragment(frmVideoTop);
        draggablePanel.setBottomFragment(frmVideoBottom);

        //draggablePanel.setXScaleFactor(xScaleFactor);
        //draggablePanel.setYScaleFactor(yScaleFactor);
        //draggablePanel.setTopViewHeight(800);
        //draggablePanel.setTopFragmentMarginRight(topViewMarginRight);
        //draggablePanel.setTopFragmentMarginBottom(topViewMargnBottom);
        draggablePanel.setClickToMaximizeEnabled(true);
        draggablePanel.setClickToMinimizeEnabled(false);
        draggablePanel.setEnableHorizontalAlphaEffect(false);
        setSizeFrmTop();
        draggablePanel.initializeView();
        draggablePanel.setVisibility(View.GONE);
    }

    private boolean isLandscape;

    public boolean isLandscapeScreen() {
        return isLandscape;
    }

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

    private int topFragmentHeight;

    public void setTopViewHeightApllyNow(int topFragmentHeight) {
        if (draggablePanel != null) {
            this.topFragmentHeight = topFragmentHeight;
            draggablePanel.setTopViewHeightApllyNow(topFragmentHeight);
        }
    }

    private void setSizeFrmTop() {
        if (isLandscape) {
            draggablePanel.setTopViewHeightApllyNow(LScreenUtil.getScreenHeight());
        } else {
            draggablePanel.setTopViewHeightApllyNow(topFragmentHeight == 0 ? LScreenUtil.getScreenWidth() * 9 / 16 : topFragmentHeight);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_container);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            /*if (draggablePanel != null) {
                draggablePanel.setVisibility(View.INVISIBLE);
            }*/
            super.onBackPressed();
        }
    }

    public void playEntityId(final String entityId) {
        LLog.d(TAG, "playEntityId " + entityId);
        if (draggablePanel.getVisibility() != View.VISIBLE) {
            draggablePanel.setVisibility(View.VISIBLE);
        }
        if (draggablePanel.isClosedAtLeft() || draggablePanel.isClosedAtRight()) {
            draggablePanel.minimize();
            LUIUtil.setDelay(500, new LUIUtil.DelayCallback() {
                @Override
                public void doAfter(int mls) {
                    draggablePanel.maximize();
                }
            });
        } else {
            draggablePanel.maximize();
        }
        if (frmVideoTop != null) {
            frmVideoTop.initEntity(entityId);
        }
    }

    public void playPlaylistFolder(final String metadataId) {
        LLog.d(TAG, "playPlaylistFolder " + metadataId);
        if (draggablePanel.getVisibility() != View.VISIBLE) {
            draggablePanel.setVisibility(View.VISIBLE);
        }
        if (draggablePanel.isClosedAtLeft() || draggablePanel.isClosedAtRight()) {
            draggablePanel.minimize();
            LUIUtil.setDelay(500, new LUIUtil.DelayCallback() {
                @Override
                public void doAfter(int mls) {
                    draggablePanel.maximize();
                }
            });
        } else {
            draggablePanel.maximize();
        }
        if (frmVideoTop != null) {
            frmVideoTop.initPlaylistFolder(metadataId);
        }
    }

    //this method will be called when entity is ready to play
    public void isInitResult(boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
        //LLog.d(TAG, "isInitResult: this method will be called when entity is ready to play");
        if (frmVideoBottom != null && isGetDataSuccess) {
            frmVideoBottom.updateUI(resultGetLinkPlay, data);
        }
    }
}
