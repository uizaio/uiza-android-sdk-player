package uiza.v3.canslide;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import uiza.R;
import uiza.v3.data.HomeDataV3;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.base.BaseFragment;
import vn.uiza.core.utilities.LConnectivityUtil;
import vn.uiza.core.utilities.LDialogUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.restapi.uiza.model.v2.listallentity.Item;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.uzv1.view.IOnBackPressed;
import vn.uiza.uzv1.view.rl.videoinfo.ItemAdapterV1;
import vn.uiza.uzv3.util.UZData;
import vn.uiza.uzv3.util.UZUtil;
import vn.uiza.uzv3.view.UZPlayerView;
import vn.uiza.views.draggablepanel.DraggableListener;
import vn.uiza.views.draggablepanel.DraggablePanel;

public class HomeV3CanSlideActivity extends BaseActivity {
    private DraggablePanel draggablePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //UZData.getInstance().setCasty(Casty.create(this));
        UZUtil.setCasty(this);
        super.onCreate(savedInstanceState);
        UZUtil.setAcitivityCanSlideIsRunning(activity, true);

        draggablePanel = (DraggablePanel) findViewById(R.id.draggable_panel);
        draggablePanel.setDraggableListener(new DraggableListener() {
            @Override
            public void onMaximized() {
            }

            @Override
            public void onMinimized() {
                if (!frmVideoTop.getUZVideo().isCastingChromecast()) {
                    frmVideoTop.getUZVideo().hideController();
                }
            }

            @Override
            public void onClosedToLeft() {
                frmVideoTop.getUZVideo().onDestroy();
            }

            @Override
            public void onClosedToRight() {
                frmVideoTop.getUZVideo().onDestroy();
            }

            @Override
            public void onDrag(int left, int top, int dx, int dy) {
            }
        });
        replaceFragment(new FrmHomeV3());
        if (UZUtil.getClickedPip(activity)) {
            //TODO
            play(null);
        }
    }

    public void replaceFragment(BaseFragment baseFragment) {
        if (baseFragment instanceof FrmHomeV3) {
            LScreenUtil.replaceFragment(activity, R.id.fl_container, baseFragment, false);
        } else {
            LScreenUtil.replaceFragment(activity, R.id.fl_container, baseFragment, true);
        }
    }

    public void addFragment(BaseFragment baseFragment, boolean isAddToBackstack) {
        LScreenUtil.addFragment(activity, R.id.fl_container, baseFragment, isAddToBackstack);
    }

    @Override
    protected boolean setFullScreen() {
        return false;
    }

    @Override
    protected String setTag() {
        return "TAG" + getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v3_uiza_ima_video_activity_rl_slide;
    }

    private FrmVideoTopV3 frmVideoTop;
    private FrmVideoBottomV3 frmVideoBottom;

    private void initFrmTop(String entityId, boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed) {
        if (!UZUtil.getClickedPip(activity)) {
            UZUtil.stopServicePiPIfRunningV3(activity);
        }
        frmVideoTop.setupVideo(entityId, isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed);
    }

    private void intFrmBottom(Data data) {
        frmVideoBottom.setup(data);
    }

    private void clearUIFrmBottom() {
        frmVideoBottom.clearAllViews();
    }

    public void play(Data data) {
        if (data == null) {
            //data = UZUtil.getData(activity, LSApplication.getInstance().getGson());
            data = UZData.getInstance().getData();
            if (data == null) {
                LLog.e(TAG, "play error data null");
                return;
            }
        }
        initializeDraggablePanelEntity(data);
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

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_container);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

    public DraggablePanel getDraggablePanel() {
        return draggablePanel;
    }

    @Override
    protected void onDestroy() {
        UZUtil.setAcitivityCanSlideIsRunning(activity, false);
        HomeDataV3.getInstance().clearAll();
        super.onDestroy();
    }

    protected void onClickPlaylistFolder(String metadataId) {
        LLog.d(TAG, "onClickPlaylistFolder metadataId " + metadataId);
        initializeDraggablePanelPlaylistFolder(metadataId);
    }

    private void initializeDraggablePanelEntity(final Data data) {
        if (!LConnectivityUtil.isConnected(activity)) {
            LDialogUtil.showDialog1(activity, getString(R.string.err_no_internet), new LDialogUtil.Callback1() {
                @Override
                public void onClick1() {
                }

                @Override
                public void onCancel() {
                }
            });
            return;
        }

        if (draggablePanel.getVisibility() != View.VISIBLE) {
            draggablePanel.setVisibility(View.VISIBLE);
        }

        if (frmVideoTop != null || frmVideoBottom != null) {
            clearUIFrmBottom();
            initFrmTop(data.getId(), false);
            draggablePanel.maximize();
            return;
        }
        frmVideoTop = new FrmVideoTopV3();
        frmVideoTop.setFragmentCallback(new BaseFragment.FragmentCallback() {
            @Override
            public void onViewCreated() {
                initFrmTop(data.getId(), false);
            }
        });
        frmVideoBottom = new FrmVideoBottomV3();
        frmVideoBottom.setFragmentCallback(new BaseFragment.FragmentCallback() {
            @Override
            public void onViewCreated() {
                frmVideoBottom.init(new ItemAdapterV1.Callback() {
                    @Override
                    public void onClickItemBottom(Item item, int position) {
                        UZUtil.setClickedPip(activity, false);
                        clearUIFrmBottom();
                        initFrmTop(data.getId(), true);
                    }

                    @Override
                    public void onLoadMore() {
                    }
                });
            }
        });

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

        frmVideoTop.setFrmTopCallback(new FrmVideoTopV3.FrmTopCallback() {
            @Override
            public void initDone(boolean isInitSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
                frmVideoTop.getUZVideo().setEventBusMsgFromActivityIsInitSuccess();
                frmVideoTop.getUZVideo().setControllerStateCallback(new UZPlayerView.ControllerStateCallback() {
                    @Override
                    public void onVisibilityChange(boolean isShow) {
                        if (draggablePanel != null && !isLandscape) {
                            if (draggablePanel.isMaximized()) {
                                if (isShow) {
                                    draggablePanel.setEnableSlide(false);
                                } else {
                                    draggablePanel.setEnableSlide(true);
                                }
                            } else {
                                draggablePanel.setEnableSlide(true);
                            }
                        }
                    }
                });
                intFrmBottom(data);
            }

            @Override
            public void onClickListEntityRelation(Item item, int position) {
                UZUtil.setClickedPip(activity, false);
                clearUIFrmBottom();
                initFrmTop(data.getId(), true);
            }
        });
    }

    private void initializeDraggablePanelPlaylistFolder(final String metadataId) {
        if (!LConnectivityUtil.isConnected(activity)) {
            LDialogUtil.showDialog1(activity, getString(R.string.err_no_internet), new LDialogUtil.Callback1() {
                @Override
                public void onClick1() {
                }

                @Override
                public void onCancel() {
                }
            });
            return;
        }

        if (draggablePanel.getVisibility() != View.VISIBLE) {
            draggablePanel.setVisibility(View.VISIBLE);
        }

        if (frmVideoTop != null || frmVideoBottom != null) {
            clearUIFrmBottom();
            frmVideoTop.setupPlaylistFolder(metadataId);
            draggablePanel.maximize();
            return;
        }
        frmVideoTop = new FrmVideoTopV3();
        frmVideoTop.setFragmentCallback(new BaseFragment.FragmentCallback() {
            @Override
            public void onViewCreated() {
                frmVideoTop.setupPlaylistFolder(metadataId);
            }
        });
        frmVideoBottom = new FrmVideoBottomV3();

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

        frmVideoTop.setFrmTopCallback(new FrmVideoTopV3.FrmTopCallback() {
            @Override
            public void initDone(boolean isInitSuccess, ResultGetLinkPlay resultGetLinkPlay, Data data) {
                LLog.d(TAG, "initializeDraggablePanelPlaylistFolder initDone " + isInitSuccess);
                frmVideoTop.getUZVideo().setEventBusMsgFromActivityIsInitSuccess();
                frmVideoTop.getUZVideo().setControllerStateCallback(new UZPlayerView.ControllerStateCallback() {
                    @Override
                    public void onVisibilityChange(boolean isShow) {
                        if (draggablePanel != null && !isLandscape) {
                            if (draggablePanel.isMaximized()) {
                                if (isShow) {
                                    draggablePanel.setEnableSlide(false);
                                } else {
                                    draggablePanel.setEnableSlide(true);
                                }
                            } else {
                                draggablePanel.setEnableSlide(true);
                            }
                        }
                    }
                });
                intFrmBottom(data);
            }

            @Override
            public void onClickListEntityRelation(Item item, int position) {
                LLog.d(TAG, "initializeDraggablePanelPlaylistFolder onClickListEntityRelation " + position);
                /*UizaPref.setClickedPip(activity, false);
                clearUIFrmBottom();
                initFrmTop(data.getId(), true);*/
            }
        });
    }
}
