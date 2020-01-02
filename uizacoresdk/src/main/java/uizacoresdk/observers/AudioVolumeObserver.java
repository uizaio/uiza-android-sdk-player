package uizacoresdk.observers;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;

import androidx.annotation.NonNull;

public class AudioVolumeObserver {

    private final Handler mHandler;
    private final Context mContext;
    private final AudioManager mAudioManager;
    private int audioStreamType = AudioManager.STREAM_MUSIC;
    private AudioVolumeContentObserver mAudioVolumeContentObserver;

    public AudioVolumeObserver(@NonNull Context context, @NonNull Handler handler) {
        mContext = context;
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mHandler = handler;
    }

    public void register(int audioStreamType,
                         @NonNull OnAudioVolumeChangedListener listener) {
        audioStreamType = audioStreamType;
        // with this handler AudioVolumeContentObserver#onChange()
        //   will be executed in the main thread
        // To execute in another thread you can use a Looper
        // +info: https://stackoverflow.com/a/35261443/904907

        mAudioVolumeContentObserver = new AudioVolumeContentObserver(
                mHandler,
                mAudioManager,
                audioStreamType,
                listener);

        mContext.getContentResolver().registerContentObserver(
                android.provider.Settings.System.CONTENT_URI,
                true,
                mAudioVolumeContentObserver);
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void unregister() {
        if (mAudioVolumeContentObserver != null) {
            mContext.getContentResolver().unregisterContentObserver(mAudioVolumeContentObserver);
            mAudioVolumeContentObserver = null;
        }
    }

    public int getCurrentVolume() {
        return mAudioManager.getStreamVolume(audioStreamType);
    }

    public int getMaxVolume() {
        return mAudioManager.getStreamMaxVolume(audioStreamType);
    }
}