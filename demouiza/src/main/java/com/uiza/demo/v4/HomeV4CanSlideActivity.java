package com.uiza.demo.v4;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.uiza.demo.R;
import com.uiza.demo.v4.categories.FrmCategories;
import com.uiza.demo.v4.entities.FrmEntities;
import com.uiza.demo.v4.home.FrmHome;
import com.uiza.demo.v4.live.FrmLive;
import com.uiza.demo.v4.live.broadcast.MenuActivity;
import com.uiza.demo.v4.login.FrmLogin;
import com.uiza.demo.v4.search.FrmSearch;
import io.uiza.core.api.response.linkplay.LinkPlay;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.util.LLog;
import io.uiza.core.util.UzCommonUtil;
import io.uiza.core.util.UzDisplayUtil;
import io.uiza.core.view.LToast;
import io.uiza.core.view.draggablepanel.DraggableListener;
import io.uiza.core.view.draggablepanel.DraggablePanel;
import io.uiza.player.UzPlayerConfig;
import io.uiza.player.interfaces.IOnBackPressed;

public class HomeV4CanSlideActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private Activity activity;
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
        UzPlayerConfig.setCasty(this);
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v4_home_canslide_activity);
        llActionBar = findViewById(R.id.ll_action_bar);
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, R.string.open,
                R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.nv);

        navigationView.findViewById(R.id.ll_home).setOnClickListener(v -> {
            replaceFragment(new FrmHome());
            drawerLayout.closeDrawer(Gravity.START, true);
        });
        navigationView.findViewById(R.id.ll_browser).setOnClickListener(v -> {
            UzCommonUtil.openUrlInBrowser(activity, "https://uiza.io/");
            drawerLayout.closeDrawer(Gravity.START, true);
        });

        TextView tvCopyright = navigationView.findViewById(R.id.tv_copyright);
        tvCopyright.setText(
                "© 2018 Uiza. All rights reserved.\nVersion " + UzCommonUtil.getAppVersionCode()
                        + "\nContact: Loitp@uiza.io");
        TextView tvLogin = navigationView.findViewById(R.id.tv_login);
        tvLogin.setOnClickListener(v -> {
            drawerLayout.closeDrawers();
            setVisibilityBtSearch(View.INVISIBLE);
            replaceFragment(new FrmLogin());
        });
        tvEntities = navigationView.findViewById(R.id.tv_entities);
        tvEntities.setOnClickListener(v -> {
            replaceFragment(new FrmEntities());
            drawerLayout.closeDrawer(Gravity.START, true);
        });
        tvCategories = navigationView.findViewById(R.id.tv_categories);
        tvCategories.setOnClickListener(v -> {
            replaceFragment(new FrmCategories());
            drawerLayout.closeDrawer(Gravity.START, true);
        });
        tvLivestream = navigationView.findViewById(R.id.tv_livestream);
        tvLivestream.setOnClickListener(v -> {
            replaceFragment(new FrmLive());
            drawerLayout.closeDrawer(Gravity.START, true);
        });

        TextView tvBroadcast = navigationView.findViewById(R.id.tv_broadcast);
        tvBroadcast.setOnClickListener(v -> {
            drawerLayout.closeDrawer(Gravity.START, true);
            Intent intent = new Intent(HomeV4CanSlideActivity.this, MenuActivity.class);
            startActivity(intent);
        });

        tvTitle = findViewById(R.id.tv_title);
        btMenu = findViewById(R.id.bt_menu);
        btMenu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerVisible(Gravity.START)) {
                drawerLayout.closeDrawer(Gravity.START, true);
            } else {
                drawerLayout.openDrawer(Gravity.START, true);
            }
        });
        btSearch = findViewById(R.id.bt_search);
        btSearch.setOnClickListener(v -> {
            llActionBar.setVisibility(View.GONE);
            replaceFragment(new FrmSearch());
        });

        draggablePanel = findViewById(R.id.draggable_panel);
        draggablePanel.setDraggableListener(new DraggableListener() {
            @Override
            public void onMaximized() {
            }

            @Override
            public void onMinimized() {
                frmVideoTop.getUZVideo().hideController();
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
        initializeDraggablePanel();
        replaceFragment(new FrmEntities());

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                if (draggablePanel != null) {
                    if (draggablePanel.getVisibility() == View.VISIBLE) {
                        if (draggablePanel.isMaximized()) {
                            draggablePanel.minimize();
                        }
                    }
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }

    private boolean doubleBackToExitPressedOnce = false;

    public void replaceFragment(Fragment baseFragment) {
        if (baseFragment instanceof FrmEntities) {
            replaceFragment((AppCompatActivity) activity, R.id.fl_container, baseFragment, false);
        } else {
            replaceFragment((AppCompatActivity) activity, R.id.fl_container, baseFragment, true);
        }
        tvTitle.setText(baseFragment.getClass().getSimpleName());
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
                if (!frmVideoTop.getUZVideo().isCasting()) {
                    draggablePanel.setEnableSlide(true);
                }
            }
        }
    }

    private void setSizeFrmTop() {
        if (isLandscape) {
            draggablePanel.setTopViewHeightApllyNow(UzDisplayUtil.getScreenHeight());
        } else {
            draggablePanel.setTopViewHeightApllyNow(UzDisplayUtil.getScreenWidth() * 9 / 16);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.START)) {
            drawerLayout.closeDrawer(Gravity.START, true);
            return;
        }
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fl_container);
        //LLog.d(TAG, "onBackPressed before " + fragment.getClass().getSimpleName());
        if (fragment instanceof FrmEntities) {
            if (draggablePanel.getVisibility() == View.VISIBLE) {
                if (draggablePanel.isMaximized()) {
                    if (!frmVideoTop.getUZVideo().isCasting()) {
                        if (frmVideoTop.getUZVideo().isLandscape()) {
                            frmVideoTop.getUZVideo().toggleFullscreen();
                        } else {
                            draggablePanel.minimize();
                        }
                    }
                    return;
                }
            }
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            LToast.show(activity, "Please click BACK again to exit");
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            return;
        }
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            boolean isLandscapeScreen = UzDisplayUtil.isFullScreen(activity);
            if (isLandscapeScreen) {
                UzDisplayUtil.toggleScreenOrientation(activity);
            } else {
                if (draggablePanel.getVisibility() == View.VISIBLE) {
                    if (draggablePanel.isMaximized()) {
                        if (!frmVideoTop.getUZVideo().isCasting()) {
                            if (frmVideoTop.getUZVideo().isLandscape()) {
                                frmVideoTop.getUZVideo().toggleFullscreen();
                            } else {
                                draggablePanel.minimize();
                            }
                        }
                        return;
                    }
                }
                super.onBackPressed();
                Fragment currentFragment = getSupportFragmentManager()
                        .findFragmentById(R.id.fl_container);
                LLog.d(TAG, "onBackPressed after " + currentFragment.getClass().getSimpleName());
                tvTitle.setText(currentFragment.getClass().getSimpleName());
            }
        }
    }

    public void playEntityId(final String entityId) {
        if (draggablePanel.getVisibility() != View.VISIBLE) {
            draggablePanel.setVisibility(View.VISIBLE);
        }
        if (draggablePanel.isClosedAtLeft() || draggablePanel.isClosedAtRight()) {
            draggablePanel.minimize();
            UzCommonUtil.actionWithDelayed(500, mls -> draggablePanel.maximize());
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

    public void playLiveEntityId(final String entityId) {
        if (draggablePanel.getVisibility() != View.VISIBLE) {
            draggablePanel.setVisibility(View.VISIBLE);
        }
        if (draggablePanel.isClosedAtLeft() || draggablePanel.isClosedAtRight()) {
            draggablePanel.minimize();
            UzCommonUtil.actionWithDelayed(500, mls -> draggablePanel.maximize());
        } else {
            draggablePanel.maximize();
        }
        if (frmVideoTop != null) {
            frmVideoTop.initLiveEntity(entityId);
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
            UzCommonUtil.actionWithDelayed(500, mls -> draggablePanel.maximize());
        } else {
            draggablePanel.maximize();
        }
        if (frmVideoTop != null) {
            frmVideoTop.initPlaylistFolder(metadataId);
        }
    }

    //this method will be called when entity is ready to play
    public void isInitResult(boolean isGetDataSuccess, LinkPlay linkPlay, VideoData data) {
        LLog.d(TAG, "onDataInitialized: this method will be called when entity is ready to play");
        if (frmVideoBottom != null && isGetDataSuccess) {
            frmVideoBottom.updateUI(linkPlay, data);
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

    @Override
    protected void onPause() {
        super.onPause();
        if (draggablePanel != null && draggablePanel.getVisibility() == View.VISIBLE
                && draggablePanel.isMinimized()) {
            draggablePanel.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (draggablePanel != null && draggablePanel.getVisibility() == View.INVISIBLE
                && draggablePanel.isMinimized()) {
            draggablePanel.setVisibility(View.VISIBLE);
        }
    }
}
