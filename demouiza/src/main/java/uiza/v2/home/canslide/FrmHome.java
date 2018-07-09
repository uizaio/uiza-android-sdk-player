package uiza.v2.home.canslide;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import uiza.R;
import uiza.app.LSApplication;
import uiza.v2.data.HomeDataV2;
import uiza.v2.home.view.EntityItemV2;
import uiza.v2.home.view.UizaActionBar;
import uiza.v2.home.view.UizaDrawerBottom;
import uiza.v2.home.view.UizaDrawerHeader;
import uiza.v2.home.view.UizaDrawerMenuItemV2;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LDialogUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LPref;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientTracking;
import vn.loitp.restapi.restclient.RestClientV2;
import vn.loitp.restapi.uiza.UizaServiceV2;
import vn.loitp.restapi.uiza.model.v2.listallentity.Item;
import vn.loitp.restapi.uiza.model.v2.listallmetadata.Datum;
import vn.loitp.restapi.uiza.model.v2.listallmetadata.JsonBodyMetadataList;
import vn.loitp.restapi.uiza.model.v2.listallmetadata.ListAllMetadata;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uizavideo.view.IOnBackPressed;
import vn.loitp.views.LToast;
import vn.loitp.views.placeholderview.lib.placeholderview.PlaceHolderView;

public class FrmHome extends BaseFragment implements IOnBackPressed {
    private final String TAG = getClass().getSimpleName();
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawerLayout;
    private List<Datum> datumList = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView) view.findViewById(R.id.drawerView);

        LUIUtil.setPullLikeIOSVertical(mDrawerView);

        setupDrawer();
        setupActionBar();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.frm_home;
    }

    private void setupDrawer() {
        //LLog.d(TAG, "setupDrawer");
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
        UizaActionBar uizaActionBar = (UizaActionBar) frmRootView.findViewById(R.id.uiza_action_bar);
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
                /*Intent intent = new Intent(getActivity(), SearchV2Activity.class);
                startActivity(intent);
                LActivityUtil.tranIn(getActivity());*/
                ((HomeV2CanSlideActivity) getActivity()).addFragment(new FrmSearch(), true);
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
        //LLog.d(TAG, "getListAllMetadata");
        genHomeMenu();

        if (RestClientV2.getRetrofit() == null) {
            LLog.d(TAG, "RestClientV2.getRetrofit() == null");
            String apiEndPoint = LPref.getApiEndPoint(getActivity());
            String currentApiTrackingEndPoint = LPref.getApiTrackEndPoint(getActivity());
            String token = LPref.getToken(getActivity());

            LLog.d(TAG, "getRetrofit apiEndPoint " + apiEndPoint);
            LLog.d(TAG, "getRetrofit currentApiTrackingEndPoint " + currentApiTrackingEndPoint);
            LLog.d(TAG, "getRetrofit token " + token);

            RestClientV2.init(apiEndPoint);
            RestClientV2.addAuthorization(token);
            RestClientTracking.init(currentApiTrackingEndPoint);
        }

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
                    LDialogUtil.showDialog1(getActivity(), getString(R.string.err_unknow), new LDialogUtil.Callback1() {
                        @Override
                        public void onClick1() {
                            if (getActivity() != null) {
                                getActivity().onBackPressed();
                            }
                        }

                        @Override
                        public void onCancel() {
                            if (getActivity() != null) {
                                getActivity().onBackPressed();
                            }
                        }
                    });
                    return;
                }
                genListDrawerLayout(listAllMetadata);
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getListAllMetadata onFail " + e.getMessage());
                LDialogUtil.showDialog1(getActivity(), "getListAllMetadata onFail " + e.getMessage(), new LDialogUtil.Callback1() {
                    @Override
                    public void onClick1() {
                        if (getActivity() != null) {
                            getActivity().onBackPressed();
                        }
                    }

                    @Override
                    public void onCancel() {
                        if (getActivity() != null) {
                            getActivity().onBackPressed();
                        }
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
            mDrawerView.addView(new UizaDrawerMenuItemV2(getActivity(), datumList, i, new UizaDrawerMenuItemV2.Callback() {
                @Override
                public void onMenuItemClick(int pos) {
                    HomeDataV2.getInstance().setCurrentPosition(pos);
                    HomeDataV2.getInstance().setDatum(datumList.get(pos));
                    mDrawerLayout.closeDrawers();
                    FrmHomeChannel frmHomeChannel = new FrmHomeChannel();
                    frmHomeChannel.setCallback(new EntityItemV2.Callback() {
                        @Override
                        public void onClick(Item item, int position) {
                            onClickVideo(item, position);
                        }

                        @Override
                        public void onPosition(int position) {
                            //do nothing
                        }
                    });
                    LScreenUtil.replaceFragment(getActivity(), R.id.fragment_container, frmHomeChannel, false);
                }
            }));
        }

        mDrawerView.addView(new UizaDrawerBottom());

        //init data first
        HomeDataV2.getInstance().setDatum(datumList.get(0));
        FrmHomeChannel frmHomeChannel = new FrmHomeChannel();
        frmHomeChannel.setCallback(new EntityItemV2.Callback() {
            @Override
            public void onClick(Item item, int position) {
                onClickVideo(item, position);
            }

            @Override
            public void onPosition(int position) {
                //do nothing
            }
        });
        LScreenUtil.replaceFragment(getActivity(), R.id.fragment_container, frmHomeChannel, false);
    }

    private void onClickVideo(Item item, int position) {
        //LLog.d(TAG, "onClickVideo at " + position + ": " + LSApplication.getInstance().getGson().toJson(item));
        LPref.setClickedPip(getActivity(), false);
        ((HomeV2CanSlideActivity) getActivity()).play(item.getId(), item.getName(), item.getThumbnail());
    }

    private long backPressed;

    @Override
    public boolean onBackPressed() {
        //LLog.d(TAG, "onBackPressed");
        if (backPressed + 2000 > System.currentTimeMillis()) {
            return false;
        } else {
            boolean isLandscapeScreen = LScreenUtil.isFullScreen(getActivity());
            if (isLandscapeScreen) {
                LActivityUtil.toggleScreenOritation((BaseActivity) getContext());
            } else {
                //LLog.d(TAG, "onBackPressed !isLandscapeScreen");
                if (((HomeV2CanSlideActivity) getActivity()).getDraggablePanel().getVisibility() == View.VISIBLE) {
                    if (((HomeV2CanSlideActivity) getActivity()).getDraggablePanel().isMaximized()) {
                        //LLog.d(TAG, "onBackPressed !isLandscapeScreen VISIBLE if");
                        ((HomeV2CanSlideActivity) getActivity()).getDraggablePanel().minimize();
                        return true;
                    } else {
                        //LLog.d(TAG, "onBackPressed !isLandscapeScreen VISIBLE if");
                    }
                } else {
                    //LLog.d(TAG, "onBackPressed !isLandscapeScreen !VISIBLE");
                }
            }
            LToast.show(getActivity(), getString(R.string.press_again_to_exit));
        }
        backPressed = System.currentTimeMillis();
        return true;
    }
}
