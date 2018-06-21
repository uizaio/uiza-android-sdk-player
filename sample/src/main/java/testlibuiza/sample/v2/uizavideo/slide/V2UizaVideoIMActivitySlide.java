package testlibuiza.sample.v2.uizavideo.slide;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.google.android.exoplayer2.ui.PlayerControlView;

import testlibuiza.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LPref;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.uizavideo.view.ComunicateMng;
import vn.loitp.uizavideo.view.IOnBackPressed;
import vn.loitp.views.draggablepanel.DraggableListener;
import vn.loitp.views.draggablepanel.DraggablePanel;

public class V2UizaVideoIMActivitySlide extends BaseActivity {
    private DraggablePanel draggablePanel;
    //private long positionFromPipService;

    public DraggablePanel getDraggablePanel() {
        return draggablePanel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //positionFromPipService = getIntent().getLongExtra(Constants.FLOAT_CURRENT_POSITION, 0l);
        //LLog.d(TAG, "positionFromPipService " + positionFromPipService);

        draggablePanel = (DraggablePanel) findViewById(R.id.draggable_panel);
        draggablePanel.setDraggableListener(new DraggableListener() {
            @Override
            public void onMaximized() {
                //LLog.d(TAG, "onMaximized");
            }

            @Override
            public void onMinimized() {
                //LLog.d(TAG, "onMinimized");
                frmTop.getUizaIMAVideo().getPlayerView().hideController();
            }

            @Override
            public void onClosedToLeft() {
                //LLog.d(TAG, "onClosedToLeft");
                frmTop.getUizaIMAVideo().onDestroy();
            }

            @Override
            public void onClosedToRight() {
                //LLog.d(TAG, "onClosedToRight");
                frmTop.getUizaIMAVideo().onDestroy();
            }

            @Override
            public void onDrag(int left, int top, int dx, int dy) {
                //LLog.d(TAG, "onDrag " + left + " - " + top + " - " + dx + " - " + dy);
                frmTop.getUizaIMAVideo().getPlayerView().hideController();
            }
        });
        replaceFragment(new FrmHome());

        if (LPref.getClickedPip(activity)) {
            //called from PiP Service
            String entityId = getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_ID);
            String entityTitle = getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_TITLE);
            String videoCoverUrl = getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_COVER);
            LLog.d(TAG, "onCreate pip entityId: " + entityId);
            LLog.d(TAG, "onCreate pip entityTitle: " + entityTitle);
            LLog.d(TAG, "onCreate pip videoCoverUrl: " + videoCoverUrl);
            if (entityId == null || entityId.isEmpty()) {
                //LToast.show(activity, "Error\nCannot play this video from PiP because entityId is null or empty!");
                LLog.e(TAG, "onCreate pip entityId == null || entityId.isEmpty()");
                return;
            }
            play();
        }
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
        return R.layout.test_uiza_ima_video_activity_rl_slide;
    }

    private FrmTop frmTop;
    private FrmBottom frmBottom;

    private void initFrmTop() {
        frmTop.setFragmentCallback(new BaseFragment.FragmentCallback() {
            @Override
            public void onViewCreated() {
                String entityId = null;
                String entityTitle = null;
                String videoCoverUrl = null;

                if (getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_ID) == null) {
                    entityId = "9213e9b8-a926-4282-b081-12b69555cb10";
                } else {
                    entityId = getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_ID);
                }

                if (getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_TITLE) == null) {
                    entityTitle = "Dummy title";
                } else {
                    entityTitle = getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_TITLE);
                }

                if (getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_COVER) == null) {
                    videoCoverUrl = "//motosaigon.vn/wp-content/uploads/2016/07/yamaha-r3-do-banh-to-190-motosaigon-5.jpg";
                } else {
                    videoCoverUrl = getIntent().getStringExtra(Constants.FLOAT_LINK_ENTITY_COVER);
                }
                //String urlIMAAd = activity.getString(loitp.core.R.string.ad_tag_url);
                String urlIMAAd = null;

                //String urlThumnailsPreviewSeekbar = activity.getString(loitp.core.R.string.url_thumbnails);
                String urlThumnailsPreviewSeekbar = null;
                frmTop.setupVideo(entityId, entityTitle, videoCoverUrl, urlIMAAd, urlThumnailsPreviewSeekbar);
            }
        });
    }

    private void initializeDraggablePanel() {
        if (frmTop != null || frmBottom != null) {
            LLog.d(TAG, "initializeDraggablePanel exist");
            draggablePanel.minimize();
            frmTop.onResume();
            return;
        }
        frmTop = new FrmTop();
        initFrmTop();
        frmBottom = new FrmBottom();

        draggablePanel.setFragmentManager(getSupportFragmentManager());
        draggablePanel.setTopFragment(frmTop);
        draggablePanel.setBottomFragment(frmBottom);

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

        frmTop.setFrmTopCallback(new FrmTop.FrmTopCallback() {
            @Override
            public void initDone() {
                LLog.d(TAG, "initDone");
                if (LPref.getClickedPip(activity)) {
                    ComunicateMng.MsgFromActivityIsInitSuccess msgFromActivityIsInitSuccess = new ComunicateMng.MsgFromActivityIsInitSuccess(null);
                    msgFromActivityIsInitSuccess.setInitSuccess(true);
                    ComunicateMng.postFromActivity(msgFromActivityIsInitSuccess);
                }
                frmTop.getUizaIMAVideo().getPlayerView().setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
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
        });
    }

    public void play() {
        initializeDraggablePanel();
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
}
