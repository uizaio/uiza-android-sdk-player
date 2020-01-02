package testlibuiza.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import testlibuiza.R;
import testlibuiza.sample.utils.ChatAdapter;
import testlibuiza.sample.utils.ChatData;
import testlibuiza.sample.utils.SampleUtils;
import timber.log.Timber;
import uizacoresdk.UizaCoreSDK;
import uizacoresdk.interfaces.UZCallback;
import uizacoresdk.view.UizaVideoView;
import vn.uiza.core.exception.UizaException;
import vn.uiza.restapi.model.v5.PlaybackInfo;
import vn.uiza.restapi.model.v5.live.LiveEntity;
import vn.uiza.utils.LActivityUtil;
import vn.uiza.utils.ScreenUtil;

public class LivePlaybackActivity extends AppCompatActivity implements UZCallback {

    private UizaVideoView uzVideo;
    /**
     * Database instance
     **/
    private DatabaseReference mReference;
    Handler handler = new Handler();
    private AppCompatEditText mChatInput;
    private RelativeLayout llInput;
    private ChatAdapter mAdapter;
    LiveEntity entity;
    RecyclerView chatRCV;
    /**
     * Class variables
     **/
    private String mUsername;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        UizaCoreSDK.setCasty(this);
        UizaCoreSDK.setCurrentPlayerId(R.layout.fullscreen_skin);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_playback);
        uzVideo = findViewById(R.id.uiza_video);
        chatRCV = findViewById(R.id.rv_chat);
        llInput = findViewById(R.id.ll_input);
        mChatInput = findViewById(R.id.et_compose);
        SampleUtils.setVertical(chatRCV);
        mAdapter = new ChatAdapter();
        chatRCV.setAdapter(mAdapter);
        uzVideo.addUZCallback(this);
        LActivityUtil.toggleFullScreen(this);
        uzVideo.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT); // optional
        setupContent();
        mUsername = SampleUtils.getLocalUsername(this);
        mUserId = SampleUtils.getLocalUserId(this);
        if (!TextUtils.isEmpty(mUserId) && !TextUtils.isEmpty(mUsername)) {
            setupConnection();
            chatRCV.setVisibility(View.VISIBLE);
            llInput.setVisibility(View.VISIBLE);
            mChatInput.setOnEditorActionListener((textView, i, keyEvent) -> {
                Editable message = mChatInput.getText();
                if (message != null) {
                    ChatData data = new ChatData(mUserId, mUsername, message.toString());
                    mReference.child(String.valueOf(new Date().getTime())).setValue(data);
                    closeAndClean();
                }
                return true;
            });
        } else {
            chatRCV.setVisibility(View.GONE);
            llInput.setVisibility(View.GONE);
        }
        uzVideo.toggleStatsForNerds();
    }

    private void closeAndClean() {
        SampleUtils.closeKeyboard(this, mChatInput);
        mChatInput.setText("");
    }

    private void setupContent() {
        entity = getIntent().getParcelableExtra("extra_live_entity");
        if (entity == null) {
            Toast.makeText(this, "Live entity = null", Toast.LENGTH_LONG).show();
            (new Handler()).postDelayed(this::finish, 1000);
            return;
        }
        PlaybackInfo playback = entity.getPlaybackInfo();
        if (playback != null && entity.isOnline()) {
            uzVideo.play(playback);
            uzVideo.setFreeSize(true); // must be set this line
        } else {
            Toast.makeText(this, "No playback or Offline", Toast.LENGTH_LONG).show();
            (new Handler()).postDelayed(this::finish, 2000);
        }

    }

    @Override
    public void isInitResult(boolean isInitSuccess, boolean isGetDataSuccess, PlaybackInfo data) {
    }

    @Override
    public void onStateMiniPlayer(boolean isInitMiniPlayerSuccess) {
        if (isInitMiniPlayerSuccess) {
            onBackPressed();
        }
    }

    @Override
    public void onSkinChange() {

    }

    @Override
    public void onScreenRotate(boolean isLandscape) {
        if (!isLandscape) {
            int w = ScreenUtil.getScreenWidth();
            int h = w * 9 / 16;
            uzVideo.setFreeSize(false);
            uzVideo.setSize(w, h);
        }
    }

    @Override
    public void onError(UizaException e) {

    }

    @Override
    public void onBackPressed() {
        if (uzVideo.isLandscape()) {
            uzVideo.getIbBackScreenIcon().performClick();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        uzVideo.onDestroyView();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        uzVideo.onResumeView();
        super.onResume();
    }

    @Override
    public void onPause() {
        uzVideo.onPauseView();
        super.onPause();
    }

    private void setupConnection() {
        if (entity != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            mReference = database.getReference(entity.getId());

            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Timber.d("SUCCESS!");
                    handleReturn(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Timber.e("ERROR: %s", databaseError.getDetails());
                    handler.post(() -> Toast.makeText(LivePlaybackActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show());
                }
            });
        }
    }

    private void handleReturn(DataSnapshot dataSnapshot) {
        mAdapter.clearData();

        for (DataSnapshot item : dataSnapshot.getChildren()) {
            ChatData data = item.getValue(ChatData.class);
            mAdapter.addData(data);
        }
        mAdapter.notifyDataSetChanged();
        if (mAdapter.getItemCount() > 1)
            chatRCV.post(() -> chatRCV.smoothScrollToPosition(mAdapter.getItemCount() - 1));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        uzVideo.onActivityResult(resultCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
