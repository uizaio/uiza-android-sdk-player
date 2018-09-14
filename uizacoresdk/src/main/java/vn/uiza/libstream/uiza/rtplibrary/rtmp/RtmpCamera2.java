package vn.uiza.libstream.uiza.rtplibrary.rtmp;

import android.content.Context;
import android.media.MediaCodec;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.SurfaceView;
import android.view.TextureView;

import java.nio.ByteBuffer;

import vn.uiza.libstream.uiza.ossrs.rtmp.ConnectCheckerRtmp;
import vn.uiza.libstream.uiza.ossrs.rtmp.SrsFlvMuxer;
import vn.uiza.libstream.uiza.rtplibrary.base.Camera2Base;
import vn.uiza.libstream.uiza.rtplibrary.view.LightOpenGlView;
import vn.uiza.libstream.uiza.rtplibrary.view.OpenGlView;

/**
 * More documentation see:
 * {@link Camera2Base}
 * <p>
 * Created by pedro on 6/07/17.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class RtmpCamera2 extends Camera2Base {

    private SrsFlvMuxer srsFlvMuxer;

    public RtmpCamera2(SurfaceView surfaceView, ConnectCheckerRtmp connectChecker) {
        super(surfaceView);
        srsFlvMuxer = new SrsFlvMuxer(connectChecker);
    }

    public RtmpCamera2(TextureView textureView, ConnectCheckerRtmp connectChecker) {
        super(textureView);
        srsFlvMuxer = new SrsFlvMuxer(connectChecker);
    }

    public RtmpCamera2(OpenGlView openGlView, ConnectCheckerRtmp connectChecker) {
        super(openGlView);
        srsFlvMuxer = new SrsFlvMuxer(connectChecker);
    }

    public RtmpCamera2(LightOpenGlView lightOpenGlView, ConnectCheckerRtmp connectChecker) {
        super(lightOpenGlView);
        srsFlvMuxer = new SrsFlvMuxer(connectChecker);
    }

    public RtmpCamera2(Context context, boolean useOpengl, ConnectCheckerRtmp connectChecker) {
        super(context, useOpengl);
        srsFlvMuxer = new SrsFlvMuxer(connectChecker);
    }

    /**
     * H264 profile.
     *
     * @param profileIop Could be ProfileIop.BASELINE or ProfileIop.CONSTRAINED
     */
    public void setProfileIop(byte profileIop) {
        srsFlvMuxer.setProfileIop(profileIop);
    }

    @Override
    public void setAuthorization(String user, String password) {
        srsFlvMuxer.setAuthorization(user, password);
    }

    @Override
    protected void prepareAudioRtp(boolean isStereo, int sampleRate) {
        srsFlvMuxer.setIsStereo(isStereo);
        srsFlvMuxer.setSampleRate(sampleRate);
    }

    @Override
    protected void startStreamRtp(String url) {
        if (videoEncoder.getRotation() == 90 || videoEncoder.getRotation() == 270) {
            srsFlvMuxer.setVideoResolution(videoEncoder.getHeight(), videoEncoder.getWidth());
        } else {
            srsFlvMuxer.setVideoResolution(videoEncoder.getWidth(), videoEncoder.getHeight());
        }
        srsFlvMuxer.start(url);
    }

    @Override
    protected void stopStreamRtp() {
        srsFlvMuxer.stop();
    }

    @Override
    protected void getAacDataRtp(ByteBuffer aacBuffer, MediaCodec.BufferInfo info) {
        srsFlvMuxer.sendAudio(aacBuffer, info);
    }

    @Override
    protected void onSPSandPPSRtp(ByteBuffer sps, ByteBuffer pps) {
        srsFlvMuxer.setSpsPPs(sps, pps);
    }

    @Override
    protected void getH264DataRtp(ByteBuffer h264Buffer, MediaCodec.BufferInfo info) {
        srsFlvMuxer.sendVideo(h264Buffer, info);
    }
}

