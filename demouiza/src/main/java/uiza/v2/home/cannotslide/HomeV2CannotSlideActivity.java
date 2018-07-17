package uiza.v2.home.cannotslide;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import uiza.R;
import uiza.app.LSApplication;
import uiza.v2.data.HomeDataV2;
import uiza.v2.home.view.UizaActionBar;
import uiza.v2.home.view.UizaDrawerBottom;
import uiza.v2.home.view.UizaDrawerHeader;
import uiza.v2.home.view.UizaDrawerMenuItemV2;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LDialogUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientV2;
import vn.loitp.restapi.uiza.UizaServiceV2;
import vn.loitp.restapi.uiza.model.v2.listallmetadata.Datum;
import vn.loitp.restapi.uiza.model.v2.listallmetadata.JsonBodyMetadataList;
import vn.loitp.restapi.uiza.model.v2.listallmetadata.ListAllMetadata;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.views.LToast;
import vn.loitp.views.placeholderview.lib.placeholderview.PlaceHolderView;

public class HomeV2CannotSlideActivity extends BaseActivity {
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawerLayout;
    private List<Datum> datumList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView) findViewById(R.id.drawerView);

        LUIUtil.setPullLikeIOSVertical(mDrawerView);

        setupDrawer();
        setupActionBar();
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
        return R.layout.uiza_home_activity;
    }

    private void setupDrawer() {
        UizaDrawerHeader uizaDrawerHeader = new UizaDrawerHeader();
        uizaDrawerHeader.setCallback(new UizaDrawerHeader.Callback() {
            @Override
            public void onClickLogOut() {
                LToast.show(activity, "Click");
            }

            @Override
            public void onClickLogin() {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawers();
                }
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }

            /*@Override
            public void onClickSetting() {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawers();
                }
                Intent intent = new Intent(activity, SettingActivity.class);
                startActivity(intent);
                LUIUtil.transActivityFadeIn(activity);
            }*/
        });
        mDrawerView.addView(uizaDrawerHeader);

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                updateUIDrawer();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                updateUIDrawer();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        getListAllMetadata();
    }

    private void setupActionBar() {
        UizaActionBar uizaActionBar = (UizaActionBar) findViewById(R.id.uiza_action_bar);
        uizaActionBar.hideTvTitle();
        uizaActionBar.setOnClickBack(new UizaActionBar.Callback() {
            @Override
            public void onClickLeft() {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    updateUIDrawer();
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            }

            @Override
            public void onClickRight() {
                Intent intent = new Intent(activity, SearchV2Activity.class);
                startActivity(intent);
                LActivityUtil.tranIn(activity);
            }
        });
        uizaActionBar.showMenuIcon();
        uizaActionBar.setImageRightIcon(R.drawable.ic_search_black_48dp);
        uizaActionBar.setImageLeftIcon(R.drawable.ic_menu_black_48dp);
        uizaActionBar.setTvTitle("Logo");
    }

    private void updateUIDrawer() {
        if (mDrawerView != null) {
            mDrawerView.refresh();
        }
    }

    private void genHomeMenu() {
        //add home menu
        Datum item = new Datum();
        item.setName("Home");
        item.setId(String.valueOf(Constants.NOT_FOUND));
        item.setType("folder");
        datumList.add(0, item);
        //emd add home menu
    }

    private void getListAllMetadata() {
        LLog.d(TAG, "getListAllMetadata");
        genHomeMenu();
        UizaServiceV2 service = RestClientV2.createService(UizaServiceV2.class);
        int limit = 999;
        String orderBy = "name";
        String orderType = "ASC";

        JsonBodyMetadataList jsonBodyMetadataList = new JsonBodyMetadataList();
        jsonBodyMetadataList.setLimit(limit);
        jsonBodyMetadataList.setOrderBy(orderBy);
        jsonBodyMetadataList.setOrderType(orderType);

        subscribe(service.listAllMetadataV2(jsonBodyMetadataList), new ApiSubscriber<ListAllMetadata>() {
            @Override
            public void onSuccess(ListAllMetadata listAllMetadata) {
                LLog.d(TAG, "getListAllMetadata onSuccess " + LSApplication.getInstance().getGson().toJson(listAllMetadata));
                if (listAllMetadata == null) {
                    LDialogUtil.showDialog1(activity, getString(R.string.err_unknow), new LDialogUtil.Callback1() {
                        @Override
                        public void onClick1() {
                        }

                        @Override
                        public void onCancel() {
                        }
                    });
                    return;
                }
                genListDrawerLayout(listAllMetadata);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getListAllMetadata onFail " + e.getMessage());
                LDialogUtil.showDialog1(activity, "Lỗi lấy danh sách metadata", new LDialogUtil.Callback1() {
                    @Override
                    public void onClick1() {
                    }

                    @Override
                    public void onCancel() {
                    }
                });
                genListDrawerLayout(null);
            }
        });
    }

    private void genListDrawerLayout(ListAllMetadata listAllMetadata) {
        if (listAllMetadata != null) {
            datumList.addAll(listAllMetadata.getData());
        }

        for (int i = 0; i < this.datumList.size(); i++) {
            mDrawerView.addView(new UizaDrawerMenuItemV2(this.getApplicationContext(), datumList, i, new UizaDrawerMenuItemV2.Callback() {
                @Override
                public void onMenuItemClick(int pos) {
                    HomeDataV2.getInstance().setCurrentPosition(pos);
                    HomeDataV2.getInstance().setDatum(datumList.get(pos));
                    mDrawerLayout.closeDrawers();
                    LScreenUtil.replaceFragment(activity, R.id.fragment_container, new FrmChannelV2(), false);
                }
            }));
        }

        mDrawerView.addView(new UizaDrawerBottom());

        //init data first
        HomeDataV2.getInstance().setDatum(datumList.get(0));
        LScreenUtil.replaceFragment(activity, R.id.fragment_container, new FrmChannelV2(), false);
    }

    private long backPressed;

    @Override
    public void onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            LToast.show(activity, getString(R.string.press_again_to_exit));
        }
        backPressed = System.currentTimeMillis();
    }
}
