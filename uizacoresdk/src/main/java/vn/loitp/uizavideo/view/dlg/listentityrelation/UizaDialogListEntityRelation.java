package vn.loitp.uizavideo.view.dlg.listentityrelation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import loitp.core.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientV2;
import vn.loitp.restapi.uiza.UizaService;
import vn.loitp.restapi.uiza.model.v2.listallentity.Item;
import vn.loitp.restapi.uiza.model.v2.listallentityrelation.JsonBodyListAllEntityRelation;
import vn.loitp.restapi.uiza.model.v2.listallentityrelation.ListAllEntityRelation;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uizavideo.view.util.UizaData;

/**
 * Created by LENOVO on 5/2/2018.
 */

public class UizaDialogListEntityRelation extends Dialog {
    private final String TAG = getClass().getSimpleName();
    private Activity activity;
    private AlertDialog dialog;
    private boolean isLandscape;
    private Gson gson = new Gson();//TODO remove gson

    private ProgressBar progressBar;
    private TextView tvMsg;
    private List<Item> itemList;
    private RecyclerView recyclerView;
    private PlayListAdapter playListAdapter;

    private PlayListCallback playListCallback;

    public UizaDialogListEntityRelation(Activity activity, boolean isLandscape, PlayListCallback playListCallback) {
        super(activity);
        this.activity = activity;
        this.isLandscape = isLandscape;
        this.playListCallback = playListCallback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_list_entity_relation);

        progressBar = (ProgressBar) findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(progressBar, ContextCompat.getColor(activity, R.color.White));

        tvMsg = (TextView) findViewById(R.id.tv_msg);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        getListAllEntityRelation();
    }

    private void getListAllEntityRelation() {
        LUIUtil.showProgressBar(progressBar);
        UizaService service = RestClientV2.createService(UizaService.class);
        LLog.d(TAG, "entityId: " + UizaData.getInstance().getEntityId());

        JsonBodyListAllEntityRelation jsonBodyListAllEntityRelation = new JsonBodyListAllEntityRelation();
        jsonBodyListAllEntityRelation.setId(UizaData.getInstance().getEntityId());

        ((BaseActivity) activity).subscribe(service.getListAllEntityRalationV2(jsonBodyListAllEntityRelation), new ApiSubscriber<ListAllEntityRelation>() {
            @Override
            public void onSuccess(final ListAllEntityRelation listAllEntityRelation) {
                LLog.d(TAG, "getListAllEntityRalationV1 onSuccess " + gson.toJson(listAllEntityRelation));
                LUIUtil.setDelay(1000, new LUIUtil.DelayCallback() {
                    @Override
                    public void doAfter(int mls) {
                        setupUI(listAllEntityRelation);
                    }
                });
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getListAllEntityRelation onFail " + e.toString());
                LUIUtil.setDelay(1000, new LUIUtil.DelayCallback() {
                    @Override
                    public void doAfter(int mls) {
                        LUIUtil.hideProgressBar(progressBar);
                        tvMsg.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    private void setupUI(ListAllEntityRelation listAllEntityRelation) {
        if (listAllEntityRelation == null || listAllEntityRelation.getItemList() == null || listAllEntityRelation.getItemList().isEmpty()) {
            tvMsg.setVisibility(View.VISIBLE);
        } else {
            itemList = listAllEntityRelation.getItemList();
            //LLog.d(TAG, "listAllEntityRelation: " + gson.toJson(listAllEntityRelation));
            if (itemList == null || itemList.isEmpty()) {
                LLog.d(TAG, "itemList == null || itemList.isEmpty() -> return");
                tvMsg.setVisibility(View.VISIBLE);
                return;
            } else {
                tvMsg.setVisibility(View.GONE);
            }
            LLog.d(TAG, "itemList size: " + itemList.size());

            /*if (isLandscape) {
                LLog.d(TAG, "isLandscape");
                recyclerView.setLayoutManager(new GridLayoutManager(activity, 4));
            } else {
                LLog.d(TAG, "!isLandscape");
                recyclerView.setLayoutManager(new GridLayoutManager(activity, 3));
            }*/

            recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

            //recyclerView.setItemAnimator(new DefaultItemAnimator());
            //LLog.d(TAG, "--------> " + widthRecyclerView + " x " + heightRecyclerView);
            playListAdapter = new PlayListAdapter(activity, itemList, new PlayListCallback() {
                @Override
                public void onClickItem(Item item, int position) {
                    dismiss();
                    if (playListCallback != null) {
                        playListCallback.onClickItem(item, position);
                    }
                }

                @Override
                public void onDismiss() {
                    if (playListCallback != null) {
                        playListCallback.onDismiss();
                    }
                }
            });
            recyclerView.setAdapter(playListAdapter);
            LUIUtil.setPullLikeIOSHorizontal(recyclerView);
        }
        LUIUtil.hideProgressBar(progressBar);
    }
}