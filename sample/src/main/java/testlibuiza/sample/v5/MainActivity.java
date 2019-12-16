package testlibuiza.sample.v5;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import testlibuiza.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.bt_guide).setOnClickListener(view ->
                startActivity(new Intent(MainActivity.this, UizaTestAPIActivity.class))
        );
    }
}
