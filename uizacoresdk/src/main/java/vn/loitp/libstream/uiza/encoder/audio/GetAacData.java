package vn.loitp.libstream.uiza.encoder.audio;

import android.media.MediaCodec;
import android.media.MediaFormat;

import java.nio.ByteBuffer;

/**
 * Created by loitp on 19/01/17.
 */

public interface GetAacData {

    void getAacData(ByteBuffer aacBuffer, MediaCodec.BufferInfo info);

    void onAudioFormat(MediaFormat mediaFormat);
}
