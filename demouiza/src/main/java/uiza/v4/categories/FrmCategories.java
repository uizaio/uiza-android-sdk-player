package uiza.v4.categories;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import uiza.R;
import uiza.app.LSApplication;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientV3;
import vn.loitp.restapi.uiza.UizaServiceV3;
import vn.loitp.restapi.uiza.model.v3.metadata.getlistmetadata.ResultGetListMetadata;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uizavideo.view.IOnBackPressed;

public class FrmCategories extends BaseFragment implements IOnBackPressed {
    private final String TAG = getClass().getSimpleName();
    private RecyclerView recyclerView;
    private TextView tvMsg;
    private ProgressBar pb;

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v4_frm_categories;
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        tvMsg = (TextView) view.findViewById(R.id.tv_msg);
        pb = (ProgressBar) view.findViewById(R.id.pb);
        LUIUtil.setColorProgressBar(pb, Color.WHITE);

        getListMetadata();
    }

    private void getListMetadata() {
        tvMsg.setVisibility(View.GONE);
        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        subscribe(service.getListMetadata(), new ApiSubscriber<ResultGetListMetadata>() {
            @Override
            public void onSuccess(ResultGetListMetadata resultGetListMetadata) {
                if (resultGetListMetadata == null) {
                    tvMsg.setText("Error ResultGetListMetadata is null");
                    return;
                }
                LLog.d(TAG, "onSuccess " + LSApplication.getInstance().getGson().toJson(resultGetListMetadata));
                resultGetListMetadata.getData();
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "getListAllMetadata onFail " + e.getMessage());
                tvMsg.setText("Error ResultGetListMetadata: " + e.getMessage());
            }
        });
    }
}
