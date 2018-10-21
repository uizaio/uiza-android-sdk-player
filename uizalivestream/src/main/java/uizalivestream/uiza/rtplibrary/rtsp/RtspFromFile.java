package uizalivestream.uiza.rtplibrary.rtsp;

import android.media.MediaCodec;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.nio.ByteBuffer;

import uizalivestream.uiza.encoder.input.decoder.AudioDecoderInterface;
import uizalivestream.uiza.encoder.input.decoder.VideoDecoderInterface;
import uizalivestream.uiza.rtplibrary.base.FromFileBase;
import uizalivestream.uiza.rtsp.rtsp.Protocol;
import uizalivestream.uiza.rtsp.rtsp.RtspClient;
import uizalivestream.uiza.rtsp.utils.ConnectCheckerRtsp;

/**
 * More documentation see:
 * {@link FromFileBase}
 * <p>
 * Created by pedro on 4/06/17.
 */
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class RtspFromFile extends FromFileBase {

    private RtspClient rtspClient;

    public RtspFromFile(ConnectCheckerRtsp connectCheckerRtsp,
                        VideoDecoderInterface videoDecoderInterface, AudioDecoderInterface audioDecoderInterface) {
        super(videoDecoderInterface, audioDecoderInterface);
        rtspClient = new RtspClient(connectCheckerRtsp);
    }

    /**
     * Internet protocol used.
     *
     * @param protocol Could be Protocol.TCP or Protocol.UDP.
     */
    public void setProtocol(Protocol protocol) {
        rtspClient.setProtocol(protocol);
    }

    @Override
    public void setAuthorization(String user, String password) {
        rtspClient.setAuthorization(user, password);
    }

    @Override
    protected void startStreamRtp(String url) {
        rtspClient.setUrl(url);
    }

    @Override
    protected void stopStreamRtp() {
        rtspClient.disconnect();
    }

    @Override
    protected void onSPSandPPSRtp(ByteBuffer sps, ByteBuffer pps) {
        ByteBuffer newSps = sps.duplicate();
        ByteBuffer newPps = pps.duplicate();
        rtspClient.setSPSandPPS(newSps, newPps);
        rtspClient.connect();
    }

    @Override
    protected void getH264DataRtp(ByteBuffer h264Buffer, MediaCodec.BufferInfo info) {
        rtspClient.sendVideo(h264Buffer, info);
    }

    @Override
    protected void getAacDataRtp(ByteBuffer aacBuffer, MediaCodec.BufferInfo info) {
        rtspClient.sendAudio(aacBuffer, info);
    }
}

