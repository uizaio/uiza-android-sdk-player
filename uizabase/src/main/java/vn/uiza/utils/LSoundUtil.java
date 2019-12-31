package vn.uiza.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by www.muathu@gmail.com on 6/1/2017.
 */

public class LSoundUtil {

    public static void startMusicFromAsset(Context context, String fileName) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            AssetFileDescriptor assetFileDescriptor = context.getAssets().openFd(fileName);
            mediaPlayer.setDataSource(
                    assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength()
            );
            assetFileDescriptor.close();
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mediaPlayer1 -> {
                mediaPlayer1.stop();
                mediaPlayer1.reset();
                mediaPlayer1.release();
            });
        } catch (IOException e) {
            SentryUtils.captureException(e);
        }
    }
}
