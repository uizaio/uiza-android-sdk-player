package com.uiza.sample.screen.demoui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.uiza.sample.R;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.util.UzCommonUtil;
import io.uiza.core.util.UzCommonUtil.DelayCallback;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.view.draggablepanel.DraggableListener;
import io.uiza.core.view.draggablepanel.DraggablePanel;
import io.uiza.player.UzPlayerConfig;
import io.uiza.player.interfaces.IOnBackPressed;

public class HomeCanSlideActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private DraggablePanel draggablePanel;
    private FrmVideoTop frmVideoTop;
    private FrmVideoBottom frmVideoBottom;
    private int topFragmentHeight;

    public DraggablePanel getDraggablePanel() {
        return draggablePanel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        UzPlayerConfig.setCurrentSkinRes(R.layout.uz_player_skin_1);
        UzPlayerConfig.setCasty(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v4_home_canslide_activity);
        draggablePanel = findViewById(R.id.draggable_panel);
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
            replaceFragment((AppCompatActivity) activity, R.id.fl_container, baseFragment, false);
        } else {
            replaceFragment((AppCompatActivity) activity, R.id.fl_container, baseFragment, true);
        }
    }

    private void replaceFragment(AppCompatActivity activity, int containerFrameLayoutIdRes,
            Fragment fragment,
            boolean isAddToBackStack) {
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(containerFrameLayoutIdRes, fragment);
        if (isAddToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

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

    public void setTopViewHeightApllyNow(int topFragmentHeight) {
        if (draggablePanel != null) {
            this.topFragmentHeight = topFragmentHeight;
            draggablePanel.setTopViewHeightApllyNow(topFragmentHeight);
        }
    }

    private void setSizeFrmTop() {
        if (frmVideoTop.isLandscape) {
            draggablePanel.setTopViewHeightApllyNow(UzDisplayUtil.getScreenHeight());
        } else {
            draggablePanel.setTopViewHeightApllyNow(
                    topFragmentHeight == 0 ? UzDisplayUtil.getScreenWidth() * 9 / 16
                            : topFragmentHeight);
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
            UzCommonUtil.actionWithDelayed(500, new DelayCallback() {
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
        if (draggablePanel.getVisibility() != View.VISIBLE) {
            draggablePanel.setVisibility(View.VISIBLE);
        }
        if (draggablePanel.isClosedAtLeft() || draggablePanel.isClosedAtRight()) {
            draggablePanel.minimize();
            UzCommonUtil.actionWithDelayed(500, new DelayCallback() {
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

    public void isInitResult(boolean isGetDataSuccess, LinkPlay linkPlay, VideoData data) {
        if (frmVideoBottom != null && isGetDataSuccess) {
            frmVideoBottom.updateUI(linkPlay, data);
        }
    }
}
