package uiza.activity.home.v2.canslide;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import uiza.R;
import uiza.activity.data.HomeDataV2;
import uiza.activity.home.v2.cannotslide.FrmChannelV2;
import uiza.activity.home.v2.cannotslide.SearchV2Activity;
import uiza.activity.home.view.UizaActionBar;
import uiza.activity.home.view.UizaDrawerHeader;
import uiza.activity.home.view.UizaDrawerMenuItemV2;
import uiza.app.LSApplication;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientV2;
import vn.loitp.restapi.uiza.UizaService;
import vn.loitp.restapi.uiza.model.v2.listallmetadata.Datum;
import vn.loitp.restapi.uiza.model.v2.listallmetadata.JsonBodyMetadataList;
import vn.loitp.restapi.uiza.model.v2.listallmetadata.ListAllMetadata;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uizavideo.view.IOnBackPressed;
import vn.loitp.views.LToast;
import vn.loitp.views.placeholderview.lib.placeholderview.PlaceHolderView;

public class FrmHome extends BaseFragment implements IOnBackPressed {
    private final String TAG = getClass().getSimpleName();
    private View view;
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawerLayout;
    private List<Datum> datumList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frm_home, container, false);//LLog.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView) view.findViewById(R.id.drawerView);

        LUIUtil.setPullLikeIOSVertical(mDrawerView);

        setupDrawer();
        setupActionBar();
        return view;
    }

    private void setupDrawer() {
        LLog.d(TAG, "setupDrawer");
        UizaDrawerHeader uizaDrawerHeader = new UizaDrawerHeader();
        uizaDrawerHeader.setCallback(new UizaDrawerHeader.Callback() {
            @Override
            public void onClickLogOut() {
                LToast.show(getActivity(), "Click");
            }

            @Override
            public void onClickLogin() {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawers();
                }
                ((HomeV2CanSlideActivity) getActivity()).addFragment(new FrmLogin(), true);
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
                //do nothing
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
                //do nothing
            }
        });

        getListAllMetadata();
    }

    private void setupActionBar() {
        UizaActionBar uizaActionBar = (UizaActionBar) view.findViewById(R.id.uiza_action_bar);
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
                Intent intent = new Intent(getActivity(), SearchV2Activity.class);
                startActivity(intent);
                LActivityUtil.tranIn(getActivity());
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
        UizaService service = RestClientV2.createService(UizaService.class);
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
                    showDialogError(getString(R.string.err_unknow));
                    return;
                }
                genListDrawerLayout(listAllMetadata);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getListAllMetadata onFail " + e.getMessage());
                handleException(e);
                genListDrawerLayout(null);
            }
        });
    }

    private void genListDrawerLayout(ListAllMetadata listAllMetadata) {
        if (listAllMetadata != null) {
            datumList.addAll(listAllMetadata.getData());
        }

        for (int i = 0; i < this.datumList.size(); i++) {
            mDrawerView.addView(new UizaDrawerMenuItemV2(getActivity(), datumList, i, new UizaDrawerMenuItemV2.Callback() {
                @Override
                public void onMenuItemClick(int pos) {
                    HomeDataV2.getInstance().setCurrentPosition(pos);
                    HomeDataV2.getInstance().setDatum(datumList.get(pos));
                    mDrawerLayout.closeDrawers();
                    LScreenUtil.replaceFragment(getActivity(), R.id.fragment_container, new FrmChannelV2(), false);
                }
            }));
        }

        //init data first
        HomeDataV2.getInstance().setDatum(datumList.get(0));
        LScreenUtil.replaceFragment(getActivity(), R.id.fragment_container, new FrmChannelV2(), false);
    }

    private long backPressed;

    @Override
    public boolean onBackPressed() {
        LLog.d(TAG, "onBackPressed");
        if (backPressed + 2000 > System.currentTimeMillis()) {
            return false;
        } else {
            LToast.show(getActivity(), getString(R.string.press_again_to_exit));
        }
        backPressed = System.currentTimeMillis();
        return true;
    }
}
