package io.uiza.samplelive;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import vn.uiza.models.live.LiveIngest;

public class InputActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatEditText etUrl, etKey;

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.dialog_create_ignest);
        etUrl = findViewById(R.id.et_stream_url);
        etKey = findViewById(R.id.et_stream_key);
        findViewById(R.id.bt_cancel).setOnClickListener(this);
        findViewById(R.id.bt_ok).setOnClickListener(this);
        etUrl.setText("rtmp://679b139b89-in.streamwiz.dev/transcode");
        etKey.setText("live_ljNx4GLp3F");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_cancel:
                finish();
                break;
            case R.id.bt_ok:
                String url = etUrl.getText().toString();
                if (TextUtils.isEmpty(url)) {
                    etUrl.setError("Required");
                    return;
                }
                String key = etKey.getText().toString();
                if (TextUtils.isEmpty(key)) {
                    etKey.setError("Required");
                    return;
                }
                LiveIngest ingest = new LiveIngest(url, key);
                Intent intent = new Intent(InputActivity.this, UizaLiveActivity.class);
                intent.putExtra(SampleLiveApplication.EXTRA_STREAM_ENDPOINT, ingest.getStreamLink());
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
