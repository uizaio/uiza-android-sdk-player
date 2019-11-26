package testlibuiza.sample.v3.utube;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import testlibuiza.R;
import uizacoresdk.interfaces.IOnBackPressed;
import uizacoresdk.util.UZUtil;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.views.draggablepanel.DraggableListener;
import vn.uiza.views.draggablepanel.DraggablePanel;

/**
 * Created by loitp on 6/3/2019.
 */

public class CustomSkinCodeUZTimebarUTubeWithSlideActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private DraggablePanel draggablePanel;
    private Activity activity;

    public DraggablePanel getDraggablePanel() {
        return draggablePanel;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        activity = this;
        UZUtil.setCurrentPlayerId(R.layout.framgia_controller_skin_custom_main_1);
        UZUtil.setCasty(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uiza_custom_skin_code_uz_timebar_with_slide);
        draggablePanel = (DraggablePanel) findViewById(R.id.draggable_panel);
        draggablePanel.setDraggableListener(new DraggableListener() {
            @Override
            public void onMaximized() {
            }

            @Override
            public void onMinimized() {
            }

            @Override
            public void onClosedToLeft() {
                if (frmUTVideoTop != null && frmUTVideoTop.getUZVideo() != null) {
                    frmUTVideoTop.getUZVideo().onDestroy();
                }
            }

            @Override
            public void onClosedToRight() {
                if (frmUTVideoTop != null && frmUTVideoTop.getUZVideo() != null) {
                    frmUTVideoTop.getUZVideo().onDestroy();
                }
            }

            @Override
            public void onDrag(int left, int top, int dx, int dy) {
            }
        });
        initializeDraggablePanel();
        replaceFragment(new FrmUTHome());
    }

    public void replaceFragment(Fragment baseFragment) {
        if (baseFragment instanceof FrmUTHome) {
            LScreenUtil.replaceFragment((AppCompatActivity) activity, R.id.fl_container, baseFragment, false);
        } else {
            LScreenUtil.replaceFragment((AppCompatActivity) activity, R.id.fl_container, baseFragment, true);
        }
    }

    private FrmUTVideoTop frmUTVideoTop;
    private FrmUTVideoBottom frmUTVideoBottom;

    private void initializeDraggablePanel() {
        if (frmUTVideoTop != null || frmUTVideoBottom != null) {
            draggablePanel.minimize();
            frmUTVideoTop.onResume();
            return;
        }
        frmUTVideoTop = new FrmUTVideoTop();
        frmUTVideoBottom = new FrmUTVideoBottom();
        draggablePanel.setFragmentManager(getSupportFragmentManager());
        draggablePanel.setTopFragment(frmUTVideoTop);
        draggablePanel.setBottomFragment(frmUTVideoBottom);
        //draggablePanel.setXScaleFactor(xScaleFactor);
        //draggablePanel.setYScaleFactor(yScaleFactor);
        //draggablePanel.setTopViewHeight(800);
        //draggablePanel.setTopFragmentMarginRight(topViewMarginRight);
        //draggablePanel.setTopFragmentMarginBottom(topViewMargnBottom);
        draggablePanel.setClickToMaximizeEnabled(true);
        draggablePanel.setClickToMinimizeEnabled(false);
        draggablePanel.setEnableHorizontalAlphaEffect(false);
        draggablePanel.initializeView();
        draggablePanel.setVisibility(View.GONE);
    }

    public void setTopViewHeightApllyNow(int topFragmentHeight) {
        if (draggablePanel != null) {
            draggablePanel.setTopViewHeightApllyNow(topFragmentHeight);
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
            LUIUtil.setDelay(500, new LUIUtil.DelayCallback() {
                @Override
                public void doAfter(int mls) {
                    draggablePanel.maximize();
                }
            });
        } else {
            draggablePanel.maximize();
        }
        if (frmUTVideoTop != null) {
            frmUTVideoTop.initEntity(entityId);
        }
    }

    public void playPlaylistFolder(final String metadataId) {
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
        if (frmUTVideoTop != null) {
            frmUTVideoTop.initPlaylistFolder(metadataId);
        }
    }

    public void isInitResult(boolean isGetDataSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
        if (frmUTVideoBottom != null && isGetDataSuccess) {
            frmUTVideoBottom.updateUI(resultGetLinkPlay, data);
        }
    }

    @Override
    protected void onDestroy() {
        if (frmUTVideoTop != null) {
            frmUTVideoTop.getUZVideo().pauseVideo();
        }
        super.onDestroy();
    }
}
