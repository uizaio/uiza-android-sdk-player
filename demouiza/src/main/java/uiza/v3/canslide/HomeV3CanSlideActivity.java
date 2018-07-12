package uiza.v3.canslide;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.google.android.exoplayer2.ui.PlayerControlView;

import uiza.R;
import uiza.app.LSApplication;
import uiza.v3.data.HomeDataV3;
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
import vn.loitp.views.draggablepanel.DraggableListener;
import vn.loitp.views.draggablepanel.DraggablePanel;

public class HomeV3CanSlideActivity extends BaseActivity {
    private DraggablePanel draggablePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LPref.setAcitivityCanSlideIsRunning(activity, true);

        draggablePanel = (DraggablePanel) findViewById(R.id.draggable_panel);
        draggablePanel.setDraggableListener(new DraggableListener() {
            @Override
            public void onMaximized() {
                //LLog.d(TAG, "onMaximized");
            }

            @Override
            public void onMinimized() {
                //LLog.d(TAG, "onMinimized");
                frmVideoTop.getUizaIMAVideoV3().getPlayerView().hideController();
            }

            @Override
            public void onClosedToLeft() {
                //LLog.d(TAG, "onClosedToLeft");
                frmVideoTop.getUizaIMAVideoV3().onDestroy();
            }

            @Override
            public void onClosedToRight() {
                //LLog.d(TAG, "onClosedToRight");
                frmVideoTop.getUizaIMAVideoV3().onDestroy();
            }

            @Override
            public void onDrag(int left, int top, int dx, int dy) {
                //LLog.d(TAG, "onDrag " + left + " - " + top + " - " + dx + " - " + dy);
            }
        });
        replaceFragment(new FrmHomeV3());
        if (LPref.getClickedPip(activity)) {
            //called from PiP Service
            /*String entityId = getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_ID);
            String entityTitle = getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_TITLE);
            String videoCoverUrl = getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_COVER);
            LLog.d(TAG, "onCreate pip entityId: " + entityId);
            LLog.d(TAG, "onCreate pip entityTitle: " + entityTitle);
            LLog.d(TAG, "onCreate pip videoCoverUrl: " + videoCoverUrl);
            if (entityId == null || entityId.isEmpty()) {
                //LToast.show(activity, "Error\nCannot play this video from PiP because entityId is null or empty!");
                LLog.e(TAG, "onCreate pip entityId == null || entityId.isEmpty()");
                return;
            }*/
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
        LLog.d(TAG, "initializeDraggablePanel " + LSApplication.getInstance().getGson().toJson(data));
        if (data == null) {
            return;
        } else {
            LPref.setData(activity, data, LSApplication.getInstance().getGson());
        }
        if (!LConnectivityUtil.isConnected(activity)) {
            LDialogUtil.showDialog1(activity, getString(R.string.err_no_internet), new LDialogUtil.Callback1() {
                @Override
                public void onClick1() {
                    //do nothing
                }

                @Override
                public void onCancel() {
                    //do nothing
                }
            });
            return;
        }

        if (draggablePanel.getVisibility() != View.VISIBLE) {
            draggablePanel.setVisibility(View.VISIBLE);
        }

        if (frmVideoTop != null || frmVideoBottom != null) {
            //LLog.d(TAG, "initializeDraggablePanel exist");
            //LLog.d(TAG, "onClickItem FrmChannel " + entityTitle);
            clearUIFrmBottom();
            initFrmTop(data, false);
            draggablePanel.maximize();
            return;
        }
        frmVideoTop = new FrmVideoTopV3();
        frmVideoTop.setFragmentCallback(new BaseFragment.FragmentCallback() {
            @Override
            public void onViewCreated() {
                //LLog.d(TAG, "setFragmentCallback onViewCreated -> initFrmTop");
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
                        //LLog.d(TAG, "onClickItem frmVideoBottom " + item.getName());
                        LPref.setClickedPip(activity, false);
                        clearUIFrmBottom();
                        initFrmTop(data, true);
                    }

                    @Override
                    public void onLoadMore() {
                        //do nothing
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
            public void initDone(boolean isInitSuccess, ResultGetLinkPlay resultGetLinkPlay, ResultRetrieveAnEntity resultRetrieveAnEntity) {
                //LLog.d(TAG, "setFrmTopCallback initDone");
                if (LPref.getClickedPip(activity)) {
                    ComunicateMng.MsgFromActivityIsInitSuccess msgFromActivityIsInitSuccess = new ComunicateMng.MsgFromActivityIsInitSuccess(null);
                    msgFromActivityIsInitSuccess.setInitSuccess(true);
                    ComunicateMng.postFromActivity(msgFromActivityIsInitSuccess);
                }
                frmVideoTop.getUizaIMAVideoV3().getPlayerView().setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
                    @Override
                    public void onVisibilityChange(int visibility) {
                        //LLog.d(TAG, "onVisibilityChange " + visibility);
                        if (draggablePanel != null && !isLandscape) {
                            if (draggablePanel.isMaximized()) {
                                if (visibility == View.VISIBLE) {
                                    //LLog.d(TAG, TAG + " onVisibilityChange visibility == View.VISIBLE");
                                    draggablePanel.setEnableSlide(false);
                                } else {
                                    //LLog.d(TAG, TAG + " onVisibilityChange visibility != View.VISIBLE");
                                    draggablePanel.setEnableSlide(true);
                                }
                            } else {
                                draggablePanel.setEnableSlide(true);
                            }
                        }
                    }
                });
                intFrmBottom(resultRetrieveAnEntity);
            }

            @Override
            public void onClickListEntityRelation(Item item, int position) {
                //LLog.d(TAG, "onClickItemListEntityRelation " + item.getName());
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
        String entityId = data.getId();
        String entityTitle = data.getName();
        String videoCoverUrl = data.getThumbnail();
        //String urlIMAAd = activity.getString(loitp.core.R.string.ad_tag_url);
        String urlIMAAd = null;
        //String urlThumnailsPreviewSeekbar = activity.getString(loitp.core.R.string.url_thumbnails);
        String urlThumnailsPreviewSeekbar = null;

        frmVideoTop.setupVideo(data, urlIMAAd, urlThumnailsPreviewSeekbar, isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed);
    }

    private void intFrmBottom(ResultRetrieveAnEntity resultRetrieveAnEntity) {
        frmVideoBottom.setup(resultRetrieveAnEntity);
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
        //LLog.d(TAG, "onBackPressed");
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
