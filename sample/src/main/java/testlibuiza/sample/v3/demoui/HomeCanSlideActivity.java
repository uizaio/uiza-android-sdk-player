package testlibuiza.sample.v3.demoui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import testlibuiza.R;
import uizacoresdk.interfaces.IOnBackPressed;
import uizacoresdk.util.UZUtil;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.views.draggablepanel.DraggableListener;
import vn.uiza.views.draggablepanel.DraggablePanel;

public class HomeCanSlideActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private DraggablePanel draggablePanel;
    private Handler handler = new Handler();

    public DraggablePanel getDraggablePanel() {
        return draggablePanel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        UZUtil.setCurrentPlayerId(R.layout.uz_player_skin_1);
        UZUtil.setCasty(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v4_home_canslide_activity);
        draggablePanel = (DraggablePanel) findViewById(R.id.draggable_panel);
        draggablePanel.setDraggableListener(new DraggableListener() {
            @Override
            public void onMaximized() {
            }

            @Override
            public void onMinimized() {
                if (frmVideoTop != null && frmVideoTop.getUZVideo() != null) {
                    frmVideoTop.getUZVideo().hideController();
                }
            }

            @Override
            public void onClosedToLeft() {
                if (frmVideoTop != null && frmVideoTop.getUZVideo() != null) {
                    frmVideoTop.getUZVideo().onDestroy();
                }
            }

            @Override
            public void onClosedToRight() {
                if (frmVideoTop != null && frmVideoTop.getUZVideo() != null) {
                    frmVideoTop.getUZVideo().onDestroy();
                }
            }

            @Override
            public void onDrag(int left, int top, int dx, int dy) {
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

    private int topFragmentHeight;

    public void setTopViewHeightApllyNow(int topFragmentHeight) {
        if (draggablePanel != null) {
            this.topFragmentHeight = topFragmentHeight;
            draggablePanel.setTopViewHeightApllyNow(topFragmentHeight);
        }
    }

    private void setSizeFrmTop() {
        if (frmVideoTop.isLandscape) {
            draggablePanel.setTopViewHeightApllyNow(LScreenUtil.getScreenHeight());
        } else {
            draggablePanel.setTopViewHeightApllyNow(topFragmentHeight == 0 ? LScreenUtil.getScreenWidth() * 9 / 16 : topFragmentHeight);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_container);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void playEntityId(final String entityId) {
        if (draggablePanel.getVisibility() != View.VISIBLE) {
            draggablePanel.setVisibility(View.VISIBLE);
        }
        if (draggablePanel.isClosedAtLeft() || draggablePanel.isClosedAtRight()) {
            draggablePanel.minimize();
            handler.postDelayed(() -> draggablePanel.maximize(), 500);
        } else {
            draggablePanel.maximize();
        }
        if (frmVideoTop != null) {
            frmVideoTop.initEntity(entityId);
        }
    }

    public void playPlaylistFolder(final String metadataId) {
        if (draggablePanel.getVisibility() != View.VISIBLE) {
            draggablePanel.setVisibility(View.VISIBLE);
        }
        if (draggablePanel.isClosedAtLeft() || draggablePanel.isClosedAtRight()) {
            draggablePanel.minimize();
            handler.postDelayed(() -> draggablePanel.maximize(), 500);
        } else {
            draggablePanel.maximize();
        }
        if (frmVideoTop != null) {
            frmVideoTop.initPlaylistFolder(metadataId);
        }
    }

    public void isInitResult(boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
        if (frmVideoBottom != null && isGetDataSuccess) {
            frmVideoBottom.updateUI(resultGetLinkPlay, data);
        }
    }
}
