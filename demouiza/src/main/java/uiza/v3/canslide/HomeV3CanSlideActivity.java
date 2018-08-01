package uiza.v3.canslide;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.google.android.exoplayer2.ui.PlayerControlView;

import uiza.R;
import uiza.app.LSApplication;
import uiza.v3.data.HomeDataV3;
import vn.loitp.chromecast.Casty;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.utilities.LConnectivityUtil;
import vn.loitp.core.utilities.LDialogUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LPref;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.restapi.uiza.model.v2.listallentity.Item;
import vn.loitp.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.loitp.restapi.uiza.model.v3.videoondeman.retrieveanentity.ResultRetrieveAnEntity;
import vn.loitp.uizavideo.view.ComunicateMng;
import vn.loitp.uizavideo.view.IOnBackPressed;
import vn.loitp.uizavideo.view.rl.videoinfo.ItemAdapterV2;
import vn.loitp.uizavideo.view.util.UizaUtil;
import vn.loitp.uizavideov3.view.util.UizaDataV3;
import vn.loitp.views.draggablepanel.DraggableListener;
import vn.loitp.views.draggablepanel.DraggablePanel;

public class HomeV3CanSlideActivity extends BaseActivity {
    private DraggablePanel draggablePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UizaDataV3.getInstance().setCasty(Casty.create(this));
        super.onCreate(savedInstanceState);
        LPref.setAcitivityCanSlideIsRunning(activity, true);

        draggablePanel = (DraggablePanel) findViewById(R.id.draggable_panel);
        draggablePanel.setDraggableListener(new DraggableListener() {
            @Override
            public void onMaximized() {
            }

            @Override
            public void onMinimized() {
                if (!frmVideoTop.getUizaIMAVideoV3().isCastingChromecast()) {
                    frmVideoTop.getUizaIMAVideoV3().hideController();
                }
            }

            @Override
            public void onClosedToLeft() {
                frmVideoTop.getUizaIMAVideoV3().onDestroy();
            }

            @Override
            public void onClosedToRight() {
                frmVideoTop.getUizaIMAVideoV3().onDestroy();
            }

            @Override
            public void onDrag(int left, int top, int dx, int dy) {
            }
        });
        replaceFragment(new FrmHomeV3());
        if (LPref.getClickedPip(activity)) {
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

    private void initializeDraggablePanel(final Data data) {
        if (data == null) {
            return;
        } else {
            LPref.setData(activity, data, LSApplication.getInstance().getGson());
        }
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
            initFrmTop(data, false);
            draggablePanel.maximize();
            return;
        }
        frmVideoTop = new FrmVideoTopV3();
        frmVideoTop.setFragmentCallback(new BaseFragment.FragmentCallback() {
            @Override
            public void onViewCreated() {
                initFrmTop(data, false);
            }
        });
        frmVideoBottom = new FrmVideoBottomV3();
        frmVideoBottom.setFragmentCallback(new BaseFragment.FragmentCallback() {
            @Override
            public void onViewCreated() {
                frmVideoBottom.init(new ItemAdapterV2.Callback() {
                    @Override
                    public void onClickItemBottom(Item item, int position) {
                        LPref.setClickedPip(activity, false);
                        clearUIFrmBottom();
                        initFrmTop(data, true);
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
                if (LPref.getClickedPip(activity)) {
                    ComunicateMng.MsgFromActivityIsInitSuccess msgFromActivityIsInitSuccess = new ComunicateMng.MsgFromActivityIsInitSuccess(null);
                    msgFromActivityIsInitSuccess.setInitSuccess(true);
                    ComunicateMng.postFromActivity(msgFromActivityIsInitSuccess);
                }
                frmVideoTop.getUizaIMAVideoV3().getPlayerView().setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
                    @Override
                    public void onVisibilityChange(int visibility) {
                        if (draggablePanel != null && !isLandscape) {
                            if (draggablePanel.isMaximized()) {
                                if (visibility == View.VISIBLE) {
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
                LPref.setClickedPip(activity, false);
                clearUIFrmBottom();
                initFrmTop(data, true);
            }
        });
    }

    private void initFrmTop(Data data, boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed) {
        if (!LPref.getClickedPip(activity)) {
            UizaUtil.stopServicePiPIfRunningV3(activity);
        }
        //String urlIMAAd = activity.getString(loitp.core.R.string.ad_tag_url);
        String urlIMAAd = null;
        //String urlThumnailsPreviewSeekbar = activity.getString(loitp.core.R.string.url_thumbnails);
        String urlThumnailsPreviewSeekbar = null;

        frmVideoTop.setupVideo(data, urlIMAAd, urlThumnailsPreviewSeekbar, isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed);
    }

    private void intFrmBottom(Data data) {
        frmVideoBottom.setup(data);
    }

    private void clearUIFrmBottom() {
        frmVideoBottom.clearAllViews();
    }

    public void play(Data data) {
        if (data == null) {
            data = LPref.getData(activity, LSApplication.getInstance().getGson());
            if (data == null) {
                LLog.e(TAG, "play error data null");
                return;
            }
        }
        initializeDraggablePanel(data);
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
        LPref.setAcitivityCanSlideIsRunning(activity, false);
        HomeDataV3.getInstance().clearAll();
        super.onDestroy();
    }
}
