package uiza.v4;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.google.android.exoplayer2.ui.PlayerControlView;

import uiza.R;
import vn.loitp.chromecast.Casty;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.uizavideo.view.IOnBackPressed;
import vn.loitp.uizavideov3.util.UizaDataV3;
import vn.loitp.views.draggablepanel.DraggableListener;
import vn.loitp.views.draggablepanel.DraggablePanel;

public class HomeV4CanSlideActivity extends BaseActivity {
    private DraggablePanel draggablePanel;

    public DraggablePanel getDraggablePanel() {
        return draggablePanel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UizaDataV3.getInstance().setCasty(Casty.create(this));
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
            }

            @Override
            public void onClosedToLeft() {
                //LLog.d(TAG, "onClosedToLeft");
                frmVideoTop.getUizaIMAVideoV3().pauseVideo();
            }

            @Override
            public void onClosedToRight() {
                //LLog.d(TAG, "onClosedToRight");
                frmVideoTop.getUizaIMAVideoV3().pauseVideo();
            }

            @Override
            public void onDrag(int left, int top, int dx, int dy) {
                //LLog.d(TAG, "onDrag " + left + " - " + top + " - " + dx + " - " + dy);
            }
        });
        initializeDraggablePanel();
        replaceFragment(new FrmHome());
    }

    public void replaceFragment(BaseFragment baseFragment) {
        if (baseFragment instanceof FrmHome) {
            LScreenUtil.replaceFragment(activity, R.id.fl_container, baseFragment, false);
        } else {
            LScreenUtil.replaceFragment(activity, R.id.fl_container, baseFragment, true);
        }
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
        return R.layout.v4_home_canslide_activity;
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

        /*frmVideoTop.setFragmentCallback(new BaseFragment.FragmentCallback() {
            @Override
            public void onViewCreated() {
                frmVideoTop.getUizaIMAVideoV3().getPlayerView().setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
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
        });*/

        /*frmVideoTop.setFrmTopCallback(new FrmVideoTop.FrmTopCallback() {
            @Override
            public void initDone() {
                LLog.d(TAG, "initDone");
                if (UizaUtil.getClickedPip(activity)) {
                    ComunicateMng.MsgFromActivityIsInitSuccess msgFromActivityIsInitSuccess = new ComunicateMng.MsgFromActivityIsInitSuccess(null);
                    msgFromActivityIsInitSuccess.setInitSuccess(true);
                    ComunicateMng.postFromActivity(msgFromActivityIsInitSuccess);
                }
                frmVideoTop.getUizaIMAVideo().getPlayerView().setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
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
        });*/
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

    private void setSizeFrmTop() {
        if (isLandscape) {
            draggablePanel.setTopViewHeightApllyNow(LScreenUtil.getScreenHeight());
        } else {
            draggablePanel.setTopViewHeightApllyNow(LScreenUtil.getScreenWidth() * 9 / 16);
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
        draggablePanel.maximize();
        if (frmVideoTop != null) {
            frmVideoTop.initEntity(entityId);
        }
    }

    public void playPlaylistFolder(final String metadataId) {
        if (draggablePanel.getVisibility() != View.VISIBLE) {
            draggablePanel.setVisibility(View.VISIBLE);
        }
        draggablePanel.maximize();
        if (frmVideoTop != null) {
            frmVideoTop.initPlaylistFolder(metadataId);
        }
    }
}
