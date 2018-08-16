package testlibuiza.sample.livestream;

import android.content.res.Configuration;
import android.media.MediaCodecInfo;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.net.URI;
import java.util.List;

import testlibuiza.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.common.Constants;
import vn.loitp.core.utilities.LLog;
import vn.loitp.libstream.Streamer;
import vn.loitp.libstream.StreamerGL;
import vn.loitp.libstream.StreamerGLBuilder;
import vn.loitp.libstream.VideoEncoder;
import vn.loitp.uizavideov3.util.UizaUtil;

public class LiveVideoBroadcasterActivity extends BaseActivity implements Streamer.Listener {
    private static final Streamer.Size VIDEO_RES = new Streamer.Size(1280, 720);
    private StreamerGL mStreamerGL = null;
    private int mConnectionId = -1;
    private boolean mIsStreaming;
    private boolean mCamera2 = false;
    private String mFrontCameraId;
    private String mBackCameraId;
    private Streamer.Size videoSizeFront;
    private Streamer.Size videoSizeBack;
    private boolean mFilterOn;
    private SurfaceView mPreview;
    private boolean mIsFrontCamera = true;

    @Override
    protected boolean setFullScreen() {
        return true;
    }

    @Override
    protected String setTag() {
        return getClass().getSimpleName();
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_live_video_broadcaster;
    }

    SurfaceHolder.Callback mPreviewCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(final SurfaceHolder holder) {
            LLog.d(TAG, "surfaceCreated");
            new AsyncTask<Integer, Integer, StreamerGLBuilder>() {
                protected StreamerGLBuilder doInBackground(Integer... urls) {
                    return initEncoders();
                }

                protected void onProgressUpdate(Integer... progress) {

                }

                protected void onPostExecute(StreamerGLBuilder builder) {
                    if (builder != null) {
                        startRecord(builder, holder);
                    }
                }
            }.execute();
            if (mFilterOn) {
                mFilterOn = false;
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            LLog.d(TAG, "surfaceChanged " + width + "x" + height);
            /*if (null != mStreamerGL) {
                //LLog.d(TAG, "MainActivity: surfaceChanged() set " + width + "x" + height);
                //mStreamerGL.setSurfaceSize(new Streamer.Size(width, height));
            }*/
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            LLog.d(TAG, "surfaceDestroyed");
            if (null != mStreamerGL) {
                mStreamerGL.stopVideoCapture();
                mStreamerGL.stopAudioCapture();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
    }

    private void endLiveStream(boolean isSave, String title) {
        if (mStreamerGL != null) {
            mStreamerGL.stopVideoCapture();
            mStreamerGL.stopAudioCapture();
            mStreamerGL.release();
            mStreamerGL = null;
            //releaseConnection();
        }
    }

    private void startLivestream() {
        if (mStreamerGL != null) {
            createConnection("rtmp://stag-ap-southeast-1-u-01.uiza.io:1935/push2transcode/test-live-loitp?token=6cb67bc8a7f0ecb8988376c44a8093dc");
        }
    }

    private void createConnection(String url) {
        if (url == null || url.isEmpty()) {
            LLog.e(TAG, "Error: url cannot be null or empty");
            return;
        }
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (Exception e) {
            LLog.e(TAG, "createConnection -> Exception " + e.toString());
            return;
        }

        LLog.d(TAG, "createConnection -> " + url);

        Streamer.MODE mode = Streamer.MODE.AUDIO_VIDEO;
        mConnectionId = mStreamerGL.createConnection(uri.toString(), mode, this);
        LLog.d(TAG, "createConnection -> mConnectionId " + mConnectionId);
        mIsStreaming = !(mConnectionId == -1);
        if (mConnectionId == -1) {
            LLog.e(TAG, "createConnection -> Network failure, could not connect to server ");
            mIsStreaming = false;
        }
    }

    @Override
    public Handler getHandler() {
        return null;
    }

    @Override
    public void OnConnectionStateChanged(int id, Streamer.CONNECTION_STATE state, Streamer.STATUS status) {
        if (null == mStreamerGL) {
            LLog.e(TAG, "OnConnectionStateChanged mStreamerGL==null -> return");
            return;
        }
        switch (state) {
            case INITIALIZED:
                LLog.d(TAG, "INITIALIZED");
                break;
            case CONNECTED:
                LLog.d(TAG, "CONNECTED");
                break;
            case SETUP:
                LLog.d(TAG, "SETUP");
                break;
            case RECORD:
                LLog.d(TAG, "RECORD");
                break;
            case DISCONNECTED:
                LLog.d(TAG, "DISCONNECTED");
                String msg = "";
                if (mIsStreaming) {
                    if (status == Streamer.STATUS.CONN_FAIL) {
                        msg = "Network failure, could not connect to server.";
                    } else if (status == Streamer.STATUS.AUTH_FAIL) {
                        msg = "Authentication failure, please check stream credentials.";
                    } else {
                        msg = "Unknown connection failure.";
                    }
                    LLog.e(TAG, msg);
                }
                mIsStreaming = false;
                if (mStreamerGL != null) {
                    mStreamerGL.stopVideoCapture();
                    mStreamerGL.stopAudioCapture();
                    mStreamerGL.release();
                    mStreamerGL = null;
                }
                break;
        }
    }

    protected void releaseConnection() {
        if (mConnectionId == -1) {
            return;
        }
        if (mStreamerGL != null) {
            mStreamerGL.releaseConnection(mConnectionId);
        }
        mConnectionId = -1;
        mIsStreaming = false;
    }

    @Override
    public void OnVideoCaptureStateChanged(Streamer.CAPTURE_STATE capture_state) {
    }

    @Override
    public void OnAudioCaptureStateChanged(Streamer.CAPTURE_STATE capture_state) {
    }

    private StreamerGLBuilder initEncoders() {
        Streamer.FpsRange fpsRange = new Streamer.FpsRange(24000, 30000);
        //Streamer.FpsRange fpsRange = new Streamer.FpsRange(15000, 15000);

        StreamerGLBuilder builder = new StreamerGLBuilder(this);
        builder.setListener(this);
        builder.setUserAgent(Constants.USER_AGENT);
        builder.setKeyFrameInterval(2);

        //Use Camera2 API
        builder.setCamera2(mCamera2);

        //identify cameras
        List<Streamer.CameraInfo> cameraList = builder.getCameraList(this, mCamera2);
        for (Streamer.CameraInfo cameraInfo : cameraList) {
            if (!cameraInfo.lensFacingBack) {
                mFrontCameraId = cameraInfo.cameraId;
                videoSizeFront = findClosestRes(VIDEO_RES, cameraInfo.recordSizes);
                fpsRange = findMatchingFPS(fpsRange, cameraInfo.fpsRanges);
            } else {
                mBackCameraId = cameraInfo.cameraId;
                videoSizeBack = findClosestRes(VIDEO_RES, cameraInfo.recordSizes);
            }
        }

        String storedCameraId = UizaUtil.getStoredCameraId(this);
        LLog.d(TAG, "storedCameraId " + storedCameraId);
        mIsFrontCamera = (storedCameraId.isEmpty() || storedCameraId.compareToIgnoreCase(mFrontCameraId) == 0);
        builder.setCameraId(mIsFrontCamera ? mFrontCameraId : mBackCameraId);

        if (videoSizeFront.height == 480) {
            builder.setVideoSize(new Streamer.Size(480, 640));
        } else {
            builder.setVideoSize(new Streamer.Size(720, 1280));
        }
        builder.setFrameRate(fpsRange);

        //int bitRate = LConnectivityUtil.isConnectedFast(this) ? 1750000 : 1000000;
        //int bitRate = LConnectivityUtil.isConnectedFast(this) ? 1500 * 1024 : 1000 * 1024;
        //int bitRate = 800000;
        int bitRate = VideoEncoder.calcBitRate(videoSizeFront, 24, MediaCodecInfo.CodecProfileLevel.AVCProfileBaseline);

        LLog.d(TAG, "Output video stream bitrate: " + bitRate + ", fps max " + (int) fpsRange.fps_max / 1000);
        builder.setBitRate(bitRate);

        //Audio
        builder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        builder.setSampleRate(44100);
        builder.setChannelCount(1);

        //GLES
       /* runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.setSurface(holder.getSurface());
                builder.setSurfaceSize(calcPreviewSize(videoSizeFront));
                LLog.d(TAG,"calcPreviewSize"+ calcPreviewSize(videoSizeFront).width +" "+ calcPreviewSize(videoSizeFront).height);
            }
        });*/

        try {
            builder.addCamera(mFrontCameraId, videoSizeFront);
            LLog.d(TAG, "Add camera front " + videoSizeFront.height + videoSizeFront.width);
            LLog.d(TAG, "Add camera front VIDEO_RES " + VIDEO_RES.height + VIDEO_RES.width);
        } catch (NullPointerException e) {
            builder.addCamera(mFrontCameraId, VIDEO_RES);
        }
        try {
            builder.addCamera(mBackCameraId, videoSizeBack);
            LLog.d(TAG, "Add camera back " + videoSizeBack.height + videoSizeBack.width);
            LLog.d(TAG, "Add camera back VIDEO_RES " + VIDEO_RES.height + VIDEO_RES.width);
        } catch (NullPointerException e) {
            builder.addCamera(mFrontCameraId, VIDEO_RES);
        }
        return builder;
    }

    private void startRecord(StreamerGLBuilder builder, SurfaceHolder holder) {
        builder.setSurface(holder.getSurface());
        builder.setSurfaceSize(calcPreviewSize(videoSizeFront));

        // Rotate preview
        // This will be later set to device orientation when user press "Broadcast" button
        //        builder.setVideoRotation(0);
        //        builder.setDisplayRotation(0);

        //add front camera

        //Starting cupturing from Audio and Video source
        LLog.d(TAG, "w: " + videoSizeFront.width + " h: " + videoSizeFront.height);
        VideoEncoder videoEncoder = VideoEncoder.createVideoEncoder(videoSizeFront);
        mStreamerGL = builder.build();
        mStreamerGL.setVideoOrientation(1);
        mStreamerGL.startVideoCapture();
        mStreamerGL.startVideoCapture(this, mFrontCameraId, holder, videoEncoder, this, 1);
        mStreamerGL.startAudioCapture();

        //mStreamerGL.setDisplayRotation((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? 1 : 0);
        //mStreamerGL.setDisplayRotation((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? 1 : 0);

        startLivestream();
    }

    private Streamer.Size findClosestRes(Streamer.Size requested, Streamer.Size[] recordSizes) {
        Streamer.Size size = null;
        for (Streamer.Size s : recordSizes) {
            if ((requested.width == s.width) && (requested.height == s.height)) {
                size = s;
                LLog.d(TAG, "findClosestRes w: " + size.width + " h: " + size.height);
                break;
            }
        }
        if (size == null) {
            Streamer.Size temp = new Streamer.Size(640, 480);
            size = temp;
        }
        return size;
    }

    private Streamer.Size calcPreviewSize(Streamer.Size video_size) {
        int height = mPreview.getHeight();
        ViewGroup.LayoutParams params = mPreview.getLayoutParams();
        params.height = height;
        //LLog.i(TAG, "calcPreviewSize navi: " + getBottomBarHeight());
        //LLog.i(TAG, "calcPreviewSize status: " + getStatusBarHeight());
        //LLog.i(TAG, "calcPreviewSize height: " + r_h);
        //LLog.i(TAG, "calcPreviewSize ratio: " + video_size.getRatio());
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            params.width = (int) (height / video_size.getRatio());
            //LLog.i(TAG, "calcPreviewSize -> PORT: " + params.width);
        } else {
            params.width = (int) (height * video_size.getRatio());
            //LLog.i(TAG, "calcPreviewSize -> LANS: " + params.width);
        }
        mPreview.setLayoutParams(params);

        //LLog.i(TAG, "calcPreviewSize w: " + params.width + " h: " + params.height);
        return new Streamer.Size(params.width, params.height);
    }

    private Streamer.FpsRange findMatchingFPS(Streamer.FpsRange fpsRange, Streamer.FpsRange[] fpsRanges) {
        if (fpsRanges.length == 1) {
            return fpsRanges[0];
        }
        int minmaxId = 0;
        for (int i = 1; i < fpsRanges.length; i++) {
            float prev = Math.max(Math.abs(fpsRange.fps_min - fpsRanges[minmaxId].fps_min), Math.abs(fpsRange.fps_max - fpsRanges[minmaxId].fps_max));
            float cur = Math.max(Math.abs(fpsRange.fps_min - fpsRanges[i].fps_min), Math.abs(fpsRange.fps_max - fpsRanges[i].fps_max));
            if (cur < prev) {
                minmaxId = i;
            }
        }
        return fpsRanges[minmaxId];
    }

    @Override
    protected void onDestroy() {
        releaseConnection();
        if (mStreamerGL != null) {
            mStreamerGL.stopVideoCapture();
            mStreamerGL.stopAudioCapture();
            mStreamerGL.release();
            mStreamerGL = null;

        }
        mPreview = null;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        UizaUtil.storeFilterState(this, mFilterOn);
        UizaUtil.storeCameraId(this, mIsFrontCamera ? mFrontCameraId : mBackCameraId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFilterOn = UizaUtil.getStoredFilterState(this);
        if (mPreview == null) {
            mPreview = (SurfaceView) findViewById(R.id.surface);
            SurfaceHolder surfaceHolder = mPreview.getHolder();
            surfaceHolder.addCallback(mPreviewCallback);
        }
    }
}
