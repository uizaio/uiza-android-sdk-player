package uiza.v4;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import uiza.R;
import uiza.v4.categories.FrmCategories;
import uiza.v4.entities.FrmEntities;
import uiza.v4.login.FrmLogin;
import uiza.v4.search.FrmSearch;
import vn.loitp.chromecast.Casty;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.loitp.uizavideo.view.IOnBackPressed;
import vn.loitp.uizavideov3.util.UizaDataV3;
import vn.loitp.views.LToast;
import vn.loitp.views.draggablepanel.DraggableListener;
import vn.loitp.views.draggablepanel.DraggablePanel;

public class HomeV4CanSlideActivity extends BaseActivity {
    private DraggablePanel draggablePanel;
    private FrmVideoTop frmVideoTop;
    private FrmVideoBottom frmVideoBottom;
    private boolean isLandscape;

    public DraggablePanel getDraggablePanel() {
        return draggablePanel;
    }

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private FloatingActionButton btMenu;
    public RelativeLayout llActionBar;
    private TextView tvTitle;
    private TextView tvEntities;
    private TextView tvCategories;
    private TextView tvLivestream;
    private FloatingActionButton btSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UizaDataV3.getInstance().setCasty(Casty.create(this));
        super.onCreate(savedInstanceState);

        llActionBar = (RelativeLayout) findViewById(R.id.ll_action_bar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.nv);
        NestedScrollView nestedScrollView = (NestedScrollView) navigationView.findViewById(R.id.scroll_view);
        LUIUtil.setPullLikeIOSVertical(nestedScrollView);

        navigationView.findViewById(R.id.ll_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                LLog.d(TAG, "onClick ll_home");
            }
        });

        TextView tvLogin = (TextView) navigationView.findViewById(R.id.tv_login);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                setVisibilityBtSearch(View.INVISIBLE);
                replaceFragment(new FrmLogin());
            }
        });
        tvEntities = (TextView) navigationView.findViewById(R.id.tv_entities);
        tvEntities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new FrmEntities());
                drawerLayout.closeDrawer(Gravity.START, true);
            }
        });
        tvCategories = (TextView) navigationView.findViewById(R.id.tv_categories);
        tvCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new FrmCategories());
                drawerLayout.closeDrawer(Gravity.START, true);
            }
        });
        tvLivestream = (TextView) navigationView.findViewById(R.id.tv_livestream);
        tvLivestream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                LLog.d(TAG, "onClick tv_livestream");
                drawerLayout.closeDrawer(Gravity.START, true);
            }
        });

        tvTitle = (TextView) findViewById(R.id.tv_title);
        btMenu = (FloatingActionButton) findViewById(R.id.bt_menu);
        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerVisible(Gravity.START)) {
                    drawerLayout.closeDrawer(Gravity.START, true);
                } else {
                    drawerLayout.openDrawer(Gravity.START, true);
                }
            }
        });
        btSearch = (FloatingActionButton) findViewById(R.id.bt_search);
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llActionBar.setVisibility(View.GONE);
                replaceFragment(new FrmSearch());
            }
        });

        draggablePanel = (DraggablePanel) findViewById(R.id.draggable_panel);
        draggablePanel.setDraggableListener(new DraggableListener() {
            @Override
            public void onMaximized() {
                //LLog.d(TAG, "onMaximized");
            }

            @Override
            public void onMinimized() {
                //LLog.d(TAG, "onMinimized");
                frmVideoTop.getUizaIMAVideoV3().hideController();
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
        replaceFragment(new FrmEntities());
    }

    private boolean doubleBackToExitPressedOnce = false;

    public void replaceFragment(BaseFragment baseFragment) {
        if (baseFragment instanceof FrmEntities) {
            LScreenUtil.replaceFragment(activity, R.id.fl_container, baseFragment, false);
        } else {
            LScreenUtil.replaceFragment(activity, R.id.fl_container, baseFragment, true);
        }
        tvTitle.setText(baseFragment.TAG);
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
    }

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
                if (!frmVideoTop.getUizaIMAVideoV3().isCastingChromecast()) {
                    draggablePanel.setEnableSlide(true);
                }
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

    public void addFragment(BaseFragment baseFragment) {
        if (baseFragment instanceof FrmEntities) {
            LScreenUtil.addFragment(activity, R.id.fl_container, baseFragment, false);
        } else {
            LScreenUtil.addFragment(activity, R.id.fl_container, baseFragment, true);
        }
        tvTitle.setText(baseFragment.TAG);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_container);
        LLog.d(TAG, "loitp before " + fragment.getClass().getSimpleName());
        if (fragment instanceof FrmEntities) {
            if (draggablePanel.getVisibility() == View.VISIBLE) {
                if (draggablePanel.isMaximized()) {
                    if (frmVideoTop.getUizaIMAVideoV3() != null && frmVideoTop.getUizaIMAVideoV3().isCastingChromecast()) {
                    } else {
                        draggablePanel.minimize();
                    }
                    //draggablePanel.minimize();
                    return;
                }
            }
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            LToast.show(activity, "Please click BACK again to exit");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
            return;
        }
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            boolean isLandscapeScreen = LScreenUtil.isFullScreen(activity);
            if (isLandscapeScreen) {
                LActivityUtil.toggleScreenOritation(activity);
            } else {
                if (draggablePanel.getVisibility() == View.VISIBLE) {
                    if (draggablePanel.isMaximized()) {
                        if (frmVideoTop.getUizaIMAVideoV3() != null && frmVideoTop.getUizaIMAVideoV3().isCastingChromecast()) {
                        } else {
                            draggablePanel.minimize();
                        }
                        //draggablePanel.minimize();
                        return;
                    }
                }
                super.onBackPressed();
                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fl_container);
                LLog.d(TAG, "loitp after " + currentFragment.getClass().getSimpleName());
                if (currentFragment instanceof BaseFragment) {
                    tvTitle.setText(((BaseFragment) currentFragment).TAG);
                }
            }
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
        if (frmVideoTop != null) {
            frmVideoTop.initEntity(entityId);
        }
        if (frmVideoBottom != null) {
            frmVideoBottom.clearUI();
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
        if (frmVideoTop != null) {
            frmVideoTop.initPlaylistFolder(metadataId);
        }
    }

    //this method will be called when entity is ready to play
    public void isInitResult(ResultGetLinkPlay resultGetLinkPlay, Data data) {
        LLog.d(TAG, "isInitResult: this method will be called when entity is ready to play");
        if (frmVideoBottom != null) {
            frmVideoBottom.updateUI(resultGetLinkPlay, data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setVisibilityBtSearch(int visibilityBtSearch) {
        btSearch.setVisibility(visibilityBtSearch);
    }
}
