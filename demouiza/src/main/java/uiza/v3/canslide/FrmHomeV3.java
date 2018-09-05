package uiza.v3.canslide;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import uiza.R;
import uiza.v2.home.view.UizaActionBar;
import uiza.v2.home.view.UizaDrawerBottom;
import uiza.v2.home.view.UizaDrawerHeader;
import uiza.v3.data.HomeDataV3;
import uiza.v3.view.EntityItemV3;
import uiza.v3.view.UizaDrawerMenuItemV3;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LDialogUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientV3;
import vn.loitp.restapi.uiza.UizaServiceV3;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.loitp.restapi.uiza.model.v3.metadata.getlistmetadata.ResultGetListMetadata;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uizavideo.view.IOnBackPressed;
import vn.loitp.uizavideov3.util.UizaUtil;
import vn.loitp.views.LToast;
import vn.loitp.views.placeholderview.lib.placeholderview.PlaceHolderView;

public class FrmHomeV3 extends BaseFragment implements IOnBackPressed {
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawerLayout;
    private ProgressBar progressBar;
    private List<Data> dataList = new ArrayList<>();

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView) view.findViewById(R.id.drawerView);
        progressBar = (ProgressBar) view.findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(getActivity(), R.color.White));
        LUIUtil.setPullLikeIOSVertical(mDrawerView);

        setupDrawer();
        setupActionBar();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v3_frm_home;
    }

    private void setupDrawer() {
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
                //delay to get more beautiful animation
                LUIUtil.setDelay(300, new LUIUtil.DelayCallback() {
                    @Override
                    public void doAfter(int mls) {
                        ((HomeV3CanSlideActivity) getActivity()).addFragment(new FrmLoginV3(), true);
                    }
                });
            }
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
                ((HomeV3CanSlideActivity) getActivity()).addFragment(new FrmSearchV3(), true);
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
        Data dataMenu = new Data();
        dataMenu.setName(Constants.MENU_HOME_V3);
        dataMenu.setType("folder");
        dataList.add(dataMenu);
        //end add home menu

        //add live menu
        Data dataLivestream = new Data();
        dataLivestream.setName(Constants.MENU_LIVESTREAM);
        dataList.add(dataLivestream);
        //end add live menu
    }

    private void getListAllMetadata() {
        genHomeMenu();

        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        subscribe(service.getListMetadata(), new ApiSubscriber<ResultGetListMetadata>() {
            @Override
            public void onSuccess(ResultGetListMetadata resultGetListMetadata) {
                if (resultGetListMetadata == null) {
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
                genListDrawerLayout(resultGetListMetadata);
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

    private void genListDrawerLayout(ResultGetListMetadata resultGetListMetadata) {
        if (resultGetListMetadata != null) {
            dataList.addAll(resultGetListMetadata.getData());
        }

        for (int i = 0; i < this.dataList.size(); i++) {
            mDrawerView.addView(new UizaDrawerMenuItemV3(getActivity(), dataList, i, new UizaDrawerMenuItemV3.Callback() {
                @Override
                public void onMenuItemClick(int pos) {
                    HomeDataV3.getInstance().setCurrentPosition(pos);
                    HomeDataV3.getInstance().setData(dataList.get(pos));
                    mDrawerLayout.closeDrawers();

                    attachFrm();
                }
            }));
        }

        mDrawerView.addView(new UizaDrawerBottom());

        //init data first
        HomeDataV3.getInstance().setData(dataList.get(0));
        FrmHomeChannelV3 frmHomeChannelV3 = new FrmHomeChannelV3();
        frmHomeChannelV3.setCallback(new EntityItemV3.Callback() {
            @Override
            public void onClick(Data data, int position) {
                onClickVideo(data, position);
            }

            @Override
            public void onPosition(int position) {
                //do nothing
            }
        });
        LScreenUtil.replaceFragment(this, R.id.fragment_container, frmHomeChannelV3, false);

        LUIUtil.hideProgressBar(progressBar);
    }

    public void attachFrm() {
        FrmHomeChannelV3 frmHomeChannelV3 = new FrmHomeChannelV3();
        frmHomeChannelV3.setCallback(new EntityItemV3.Callback() {
            @Override
            public void onClick(Data data, int position) {
                onClickVideo(data, position);
            }

            @Override
            public void onPosition(int position) {
            }
        });
        LScreenUtil.replaceFragment(FrmHomeV3.this, R.id.fragment_container, frmHomeChannelV3, false);
    }

    private void onClickVideo(Data data, int position) {
        UizaUtil.setClickedPip(getActivity(), false);
        ((HomeV3CanSlideActivity) getActivity()).play(data);
    }

    private long backPressed;

    @Override
    public boolean onBackPressed() {
        if (backPressed + 2000 > System.currentTimeMillis()) {
            return false;
        } else {
            boolean isLandscapeScreen = LScreenUtil.isFullScreen(getActivity());
            if (isLandscapeScreen) {
                LActivityUtil.toggleScreenOritation((BaseActivity) getContext());
            } else {
                if (((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().getVisibility() == View.VISIBLE) {
                    if (((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().isMaximized()) {
                        ((HomeV3CanSlideActivity) getActivity()).getDraggablePanel().minimize();
                        return true;
                    } else {
                    }
                } else {
                }
            }
            LToast.show(getActivity(), getString(R.string.press_again_to_exit));
        }
        backPressed = System.currentTimeMillis();
        return true;
    }
}
