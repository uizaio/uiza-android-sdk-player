package testlibuiza.sample.v3.utube;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.view.draggablepanel.DraggableListener;
import io.uiza.core.view.draggablepanel.DraggablePanel;
import testlibuiza.R;
import uizacoresdk.interfaces.IOnBackPressed;
import uizacoresdk.util.UZUtil;

/**
 * Created by loitp on 9/1/2019.
 */

public class CustomSkinCodeUZTimebarUTubeWithSlideActivity extends AppCompatActivity {
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
            replaceFragment((AppCompatActivity) activity, R.id.fl_container, baseFragment, false);
        } else {
            replaceFragment((AppCompatActivity) activity, R.id.fl_container, baseFragment, true);
        }
    }

    private void replaceFragment(AppCompatActivity activity, int containerFrameLayoutIdRes, Fragment fragment,
            boolean isAddToBackStack) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(containerFrameLayoutIdRes, fragment);
        if (isAddToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
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
            draggablePanel.setTopViewHeightApllyNow(UzDisplayUtil.getScreenHeight());
        } else {
            draggablePanel.setTopViewHeightApllyNow(topFragmentHeight == 0 ? UzDisplayUtil.getScreenWidth() * 9 / 16 : topFragmentHeight);
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
            UzDisplayUtil.setDelay(500, new UzDisplayUtil.DelayCallback() {
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
            UzDisplayUtil.setDelay(500, new UzDisplayUtil.DelayCallback() {
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

    public void isInitResult(boolean isGetDataSuccess, LinkPlay linkPlay, VideoData data) {
        if (frmUTVideoBottom != null && isGetDataSuccess) {
            frmUTVideoBottom.updateUI(linkPlay, data);
        }
    }
}
