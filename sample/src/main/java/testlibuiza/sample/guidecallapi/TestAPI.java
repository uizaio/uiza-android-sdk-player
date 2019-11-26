package testlibuiza.sample.guidecallapi;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import testlibuiza.R;
import testlibuiza.app.LSApplication;
import vn.uiza.core.utilities.LDialogUtil;
import vn.uiza.core.utilities.LLog;
import vn.uiza.core.utilities.LUIUtil;
import vn.uiza.restapi.UZAPIMaster;
import vn.uiza.restapi.restclient.UZRestClient;

public class TestAPI extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private String domainAPI = "https://loctbprod01.uiza.co";
    private String token = "uap-9816792bb84642f09d843af4f93fb748-b94fcbd1";
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_api);
        UZRestClient.init(domainAPI, token);

        tv = (TextView) findViewById(R.id.tv);

        findViewById(R.id.bt_get_list_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getListAllUser();
            }
        });
    }

    private void showTv(Object o) {
        LUIUtil.printBeautyJson(o, tv);
    }

    private void getListAllUser() {
        Service service = UZRestClient.createService(Service.class);
        UZAPIMaster.getInstance().subscribe(service.listAllUser(), o -> {
            LLog.d(TAG, "createAnUser onSuccess: " + LSApplication.getInstance().getGson().toJson(o));
            showTv(o);
        }, throwable -> {
            LLog.e(TAG, "createAnUser onFail " + throwable.toString());
            showTv(throwable.getMessage());
        });

    }

    @Override
    protected void onDestroy() {
        UZAPIMaster.getInstance().destroy();
        LDialogUtil.clearAll();
        super.onDestroy();
    }
}
