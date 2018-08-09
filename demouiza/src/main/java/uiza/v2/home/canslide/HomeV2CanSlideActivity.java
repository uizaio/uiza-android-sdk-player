package uiza.v2.home.canslide;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.google.android.exoplayer2.ui.PlayerControlView;

import uiza.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.restapi.uiza.model.v2.getdetailentity.GetDetailEntity;
import vn.loitp.restapi.uiza.model.v2.getlinkplay.GetLinkPlay;
import vn.loitp.restapi.uiza.model.v2.listallentity.Item;
import vn.loitp.uizavideo.view.ComunicateMng;
import vn.loitp.uizavideo.view.IOnBackPressed;
import vn.loitp.uizavideo.view.rl.videoinfo.ItemAdapterV2;
import vn.loitp.uizavideov3.UizaUtil;
import vn.loitp.views.draggablepanel.DraggableListener;
import vn.loitp.views.draggablepanel.DraggablePanel;

public class HomeV2CanSlideActivity extends BaseActivity {
    private DraggablePanel draggablePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UizaUtil.setAcitivityCanSlideIsRunning(activity, true);

        draggablePanel = (DraggablePanel) findViewById(R.id.draggable_panel);
        draggablePanel.setDraggableListener(new DraggableListener() {
            @Override
            public void onMaximized() {
            }

            @Override
            public void onMinimized() {
                frmVideoTop.getUizaIMAVideo().getPlayerView().hideController();
            }

            @Override
            public void onClosedToLeft() {
                frmVideoTop.getUizaIMAVideo().onDestroy();
            }

            @Override
            public void onClosedToRight() {
                frmVideoTop.getUizaIMAVideo().onDestroy();
            }

            @Override
            public void onDrag(int left, int top, int dx, int dy) {
            }
        });
        UizaUtil.setupRestClientV2(activity);
        replaceFragment(new FrmHome());
        if (UizaUtil.getClickedPip(activity)) {
            //called from PiP Service
            String entityId = getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_ID);
            String entityTitle = getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_TITLE);
            String videoCoverUrl = getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_COVER);
            if (entityId == null || entityId.isEmpty()) {
                LLog.e(TAG, "onCreate pip entityId == null || entityId.isEmpty()");
                return;
            }
            play(entityId, entityTitle, videoCoverUrl);
        }
    }

    public void replaceFragment(BaseFragment baseFragment) {
        if (baseFragment instanceof FrmHome) {
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
        return R.layout.uiza_ima_video_activity_rl_slide;
    }

    private FrmVideoTop frmVideoTop;
    private FrmVideoBottom frmVideoBottom;

    private void initializeDraggablePanel(final String entityId, final String entityTitle, final String entityCover) {
        if (draggablePanel.getVisibility() != View.VISIBLE) {
            draggablePanel.setVisibility(View.VISIBLE);
        }

        if (frmVideoTop != null || frmVideoBottom != null) {
            clearUIFrmBottom();
            initFrmTop(entityId, entityTitle, entityCover, false);
            draggablePanel.maximize();
            return;
        }
        frmVideoTop = new FrmVideoTop();
        frmVideoTop.setFragmentCallback(new BaseFragment.FragmentCallback() {
            @Override
            public void onViewCreated() {
                initFrmTop(entityId, entityTitle, entityCover, false);
            }
        });
        frmVideoBottom = new FrmVideoBottom();
        frmVideoBottom.setFragmentCallback(new BaseFragment.FragmentCallback() {
            @Override
            public void onViewCreated() {
                frmVideoBottom.init(new ItemAdapterV2.Callback() {
                    @Override
                    public void onClickItemBottom(Item item, int position) {
                        UizaUtil.setClickedPip(activity, false);
                        clearUIFrmBottom();
                        initFrmTop(item.getId(), item.getName(), item.getThumbnail(), true);
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

        frmVideoTop.setFrmTopCallback(new FrmVideoTop.FrmTopCallback() {
            @Override
            public void initDone(boolean isInitSuccess, GetLinkPlay getLinkPlay, GetDetailEntity getDetailEntity) {
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
                intFrmBottom(getDetailEntity);
            }

            @Override
            public void onClickListEntityRelation(Item item, int position) {
                UizaUtil.setClickedPip(activity, false);
                clearUIFrmBottom();
                initFrmTop(item.getId(), item.getName(), item.getThumbnail(), true);
            }
        });
    }

    private void initFrmTop(String entityId, String entityTitle, String videoCoverUrl, boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed) {
        if (!UizaUtil.getClickedPip(activity)) {
            UizaUtil.stopServicePiPIfRunning(activity);
        }

        //String urlIMAAd = activity.getString(loitp.core.R.string.ad_tag_url);
        String urlIMAAd = null;

        //String urlThumnailsPreviewSeekbar = activity.getString(loitp.core.R.string.url_thumbnails);
        String urlThumnailsPreviewSeekbar = null;

        frmVideoTop.getUizaIMAVideo().setExoPictureInPictureVisibility(View.VISIBLE);
        frmVideoTop.setupVideo(entityId, entityTitle, videoCoverUrl, urlIMAAd, urlThumnailsPreviewSeekbar, isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed);
    }

    private void intFrmBottom(GetDetailEntity getDetailEntity) {
        frmVideoBottom.setup(getDetailEntity);
    }

    private void clearUIFrmBottom() {
        frmVideoBottom.clearAllViews();
    }

    public void play(String entityId, String entityTitle, String entityCover) {
        initializeDraggablePanel(entityId, entityTitle, entityCover);
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
        UizaUtil.setAcitivityCanSlideIsRunning(activity, false);
        super.onDestroy();
    }
}
