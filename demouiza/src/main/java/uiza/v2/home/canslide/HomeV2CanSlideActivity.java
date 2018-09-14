package uiza.v2.home.canslide;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.google.android.exoplayer2.ui.PlayerControlView;

import uiza.R;
import vn.uiza.core.base.BaseActivity;
import vn.uiza.core.base.BaseFragment;
import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LScreenUtil;
import vn.uiza.restapi.uiza.model.v2.getdetailentity.GetDetailEntity;
import vn.uiza.restapi.uiza.model.v2.getlinkplay.GetLinkPlay;
import vn.uiza.restapi.uiza.model.v2.listallentity.Item;
import vn.uiza.uzv1.view.ComunicateMng;
import vn.uiza.uzv1.view.IOnBackPressed;
import vn.uiza.uzv1.view.rl.videoinfo.ItemAdapterV1;
import vn.uiza.uzv3.util.UZUtil;
import vn.uiza.views.draggablepanel.DraggableListener;
import vn.uiza.views.draggablepanel.DraggablePanel;

public class HomeV2CanSlideActivity extends BaseActivity {
    private DraggablePanel draggablePanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UZUtil.setAcitivityCanSlideIsRunning(activity, true);

        draggablePanel = (DraggablePanel) findViewById(R.id.draggable_panel);
        draggablePanel.setDraggableListener(new DraggableListener() {
            @Override
            public void onMaximized() {
            }

            @Override
            public void onMinimized() {
                frmVideoTop.getUZVideoV1().getPlayerView().hideController();
            }

            @Override
            public void onClosedToLeft() {
                frmVideoTop.getUZVideoV1().onDestroy();
            }

            @Override
            public void onClosedToRight() {
                frmVideoTop.getUZVideoV1().onDestroy();
            }

            @Override
            public void onDrag(int left, int top, int dx, int dy) {
            }
        });
        UZUtil.setupRestClientV2(activity);
        replaceFragment(new FrmHome());
        if (UZUtil.getClickedPip(activity)) {
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
                frmVideoBottom.init(new ItemAdapterV1.Callback() {
                    @Override
                    public void onClickItemBottom(Item item, int position) {
                        UZUtil.setClickedPip(activity, false);
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
                if (UZUtil.getClickedPip(activity)) {
                    ComunicateMng.MsgFromActivityIsInitSuccess msgFromActivityIsInitSuccess = new ComunicateMng.MsgFromActivityIsInitSuccess(null);
                    msgFromActivityIsInitSuccess.setInitSuccess(true);
                    ComunicateMng.postFromActivity(msgFromActivityIsInitSuccess);
                }
                frmVideoTop.getUZVideoV1().getPlayerView().setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
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
                UZUtil.setClickedPip(activity, false);
                clearUIFrmBottom();
                initFrmTop(item.getId(), item.getName(), item.getThumbnail(), true);
            }
        });
    }

    private void initFrmTop(String entityId, String entityTitle, String videoCoverUrl, boolean isTryToPlayPreviousUizaInputIfPlayCurrentUizaInputFailed) {
        if (!UZUtil.getClickedPip(activity)) {
            UZUtil.stopServicePiPIfRunning(activity);
        }

        //String urlIMAAd = activity.getString(loitp.core.R.string.ad_tag_url);
        String urlIMAAd = null;

        //String urlThumnailsPreviewSeekbar = activity.getString(loitp.core.R.string.url_thumbnails);
        String urlThumnailsPreviewSeekbar = null;

        frmVideoTop.getUZVideoV1().setExoPictureInPictureVisibility(View.VISIBLE);
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
        UZUtil.setAcitivityCanSlideIsRunning(activity, false);
        super.onDestroy();
    }
}
