package uiza.v4.search;

/**
 * Created by www.muathu@gmail.com on 12/24/2017.
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uiza.R;
import uiza.v4.HomeV4CanSlideActivity;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.base.BaseFragment;
import vn.loitp.core.utilities.LActivityUtil;
import vn.loitp.core.utilities.LLog;
import vn.loitp.core.utilities.LScreenUtil;
import vn.loitp.core.utilities.LUIUtil;
import vn.loitp.restapi.restclient.RestClientV3;
import vn.loitp.restapi.uiza.UizaServiceV3;
import vn.loitp.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.loitp.restapi.uiza.model.v3.videoondeman.listallentity.ResultListEntity;
import vn.loitp.rxandroid.ApiSubscriber;
import vn.loitp.uizavideo.view.IOnBackPressed;
import vn.loitp.utils.util.KeyboardUtils;

public class FrmSearch extends BaseFragment implements View.OnClickListener, IOnBackPressed {
    private final String TAG = getClass().getSimpleName();
    private ImageView ivBack;
    private ImageView ivClearText;
    private EditText etSearch;
    private TextView tv;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivBack = (ImageView) view.findViewById(R.id.iv_back);
        ivClearText = (ImageView) view.findViewById(R.id.iv_clear_text);
        etSearch = (EditText) view.findViewById(R.id.et_search);
        etSearch.requestFocus();
        tv = (TextView) view.findViewById(R.id.tv);
        ivBack.setOnClickListener(this);
        ivClearText.setOnClickListener(this);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    ivClearText.setVisibility(View.GONE);
                    tv.setVisibility(View.GONE);
                } else {
                    ivClearText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        LUIUtil.setImeiActionSearch(etSearch, new LUIUtil.CallbackSearch() {
            @Override
            public void onSearch() {
                search(etSearch.getText().toString(), false);
            }
        });
        KeyboardUtils.showSoftInput(etSearch);
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.v4_frm_search;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                getActivity().onBackPressed();
                break;
            case R.id.iv_clear_text:
                etSearch.setText("");
                break;
        }
    }

    private void search(String keyword, boolean isCallFromLoadMore) {
        tv.setVisibility(View.GONE);

        UizaServiceV3 service = RestClientV3.createService(UizaServiceV3.class);
        subscribe(service.searchEntity(keyword), new ApiSubscriber<ResultListEntity>() {
            @Override
            public void onSuccess(ResultListEntity result) {
                if (result == null || result.getData().isEmpty()) {
                    tv.setText(getString(R.string.empty_list));
                    tv.setVisibility(View.VISIBLE);
                    return;
                }
                setupUIList(result.getData());
            }

            @Override
            public void onFail(Throwable e) {
                LLog.e(TAG, "search onFail " + e.toString());
                tv.setText("Error search " + e.getMessage());
                tv.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupUIList(List<Data> dataList) {

    }

    @Override
    public boolean onBackPressed() {
        boolean isLandscapeScreen = LScreenUtil.isFullScreen(getActivity());
        if (isLandscapeScreen) {
            LActivityUtil.toggleScreenOritation((BaseActivity) getContext());
        } else {
            if (((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().getVisibility() == View.VISIBLE) {
                if (((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().isMaximized()) {
                    ((HomeV4CanSlideActivity) getActivity()).getDraggablePanel().minimize();
                    return true;
                } else {
                }
            } else {
            }
        }
        return false;
    }

    @Override
    public void onDestroyView() {
        ((HomeV4CanSlideActivity) getActivity()).llActionBar.setVisibility(View.VISIBLE);
        KeyboardUtils.hideSoftInput(getActivity());
        super.onDestroyView();
    }
}
