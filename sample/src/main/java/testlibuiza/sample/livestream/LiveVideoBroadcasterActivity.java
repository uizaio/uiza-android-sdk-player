package testlibuiza.sample.livestream;

import android.content.res.Configuration;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import testlibuiza.R;
import vn.loitp.core.base.BaseActivity;
import vn.loitp.core.utilities.LLog;
import vn.loitp.libstream.Streamer;
import vn.loitp.libstream.StreamerGL;
import vn.loitp.libstream.StreamerGLBuilder;
import vn.loitp.libstream.VideoEncoder;
import vn.loitp.uizavideov3.util.UizaUtil;

public class LiveVideoBroadcasterActivity extends BaseActivity implements Streamer.Listener {
    private static final int MAX_RECONNECT_ATTEMPTS = 1;
    private static final Streamer.Size VIDEO_RES = new Streamer.Size(1280, 720);
    private final int REQUEST_CODE_DIALOG_ASK = 6969;
    protected StreamerGL mStreamerGL = null;
    long timeInMilliseconds = 0;
    private boolean isFullScreenView;
    private int mConnectionId = -1;
    private boolean mIsStreaming;
    private Streamer.CONNECTION_STATE mConnectionState;
    private boolean mCamera2 = false;
    private String mFrontCameraId;
    private String mBackCameraId;
    private Streamer.Size video_size_front;
    private Streamer.Size video_size_back;
    private boolean mFilterOn;
    private SurfaceView mPreview;
    //private static final Streamer.Size VIDEO_RES = new Streamer.Size(480, 360);
    private boolean mIsFrontCamera = true;
    private int mRetryCount;
    private int countConnect = 0;
    private boolean isExist = false;
    private long startRecordTime = 0;
    private long updatedTime = 0;
    private long timeSwapBuff = 0;
    private boolean goOnPause = false;
    private boolean isPauseInRoom = false;
    private boolean isStarted = false;
    private boolean isStartedLive = false;
    private int currentUserInRoom = 0;
    private Handler mHandler = new Handler();
    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startRecordTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            //LLog.d(TAG, "update time: " + secs);
            int mins = secs / 60;
            secs = secs % 60;
            mHandler.postDelayed(this, 0);
        }

    };

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
            //LLog.d(TAG, "MainActivity: surfaceChanged() " + width + "x" + height);
            //LLog.d(TAG, "surfaceChanged");
            if (null != mStreamerGL) {
                //LLog.d(TAG, "MainActivity: surfaceChanged() set " + width + "x" + height);
                //mStreamerGL.setSurfaceSize(new Streamer.Size(width, height));
            }
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            //LLog.d(TAG, "surfaceDestroyed");
            if (null != mStreamerGL) {
                //LLog.d(TAG, "surfaceDestroyed -> null != mStreamerGL");
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
        /*timestamp = new Timestamp(System.currentTimeMillis());
                startRecordTime = timestamp.getTime() / 1000;*/
        startRecordTime = SystemClock.uptimeMillis();
        mHandler.postDelayed(updateTimerThread, 0);
        //LLog.d(TAG, "startLivestream: " + startRecordTime);
        if (mStreamerGL != null) {
            //createConnection(startLive.getBroadcastUrl());
            createConnection("rtmp://stag-ap-southeast-1-u-01.uiza.io:1935/push2transcode/test-live-loitp?token=6cb67bc8a7f0ecb8988376c44a8093dc");
        }
        //createConnection(startLive.getBroadcastUrl());
        //connectAndStartRoom();
    }

    private void createConnection(String url) {
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            LLog.e(TAG, "createConnection -> URISyntaxException " + e.toString());
            //Toast.makeText(this, R.string.an_error_has_occurred, Toast.LENGTH_SHORT);
            return;
        } catch (Exception e) {
            LLog.e(TAG, "createConnection -> Exception " + e.toString());
        }

        LLog.d(TAG, "Remote stream: " + url);

        Streamer.MODE mode = Streamer.MODE.AUDIO_VIDEO;
        LLog.d(TAG, "Remote stream: " + uri.toString());
        mConnectionId = mStreamerGL.createConnection(uri.toString(), mode, this);
        LLog.d(TAG, "createConnection -> mConnectionId " + mConnectionId);
        mIsStreaming = !(mConnectionId == -1);

        if (mConnectionId == -1) {
            LLog.d(TAG, "createConnection -> Network failure, could not connect to server ");
            mIsStreaming = false;
        }
    }

    @Override
    public Handler getHandler() {
        return mHandler;
    }

    @Override
    public void OnConnectionStateChanged(int id, Streamer.CONNECTION_STATE state, Streamer.STATUS status) {
        if (null == mStreamerGL) {
            return;
        }

        if (id == mConnectionId) {
            mConnectionState = state;
        }

        switch (state) {
            case INITIALIZED:
                break;
            case CONNECTED:
                LLog.d(TAG, "CONNECTED");
//                connTime_ = prevTime_ = System.currentTimeMillis();
//                prevBytes_ = mStreamer.getBytesSent(mConnectionId);
                break;
            case SETUP:
                break;
            case RECORD:
                LLog.d(TAG, "RECORD");
                //lCountDownView.setTextLive();
                break;
            case DISCONNECTED:
                LLog.d(TAG, "OnConnectionStateChanged -> id " + id);
                if (id != mConnectionId) {
                    LLog.e(TAG, "unregistered connection");
                    break;
                }

                int delay = 3000;
                String _msg = "";
                if (mIsStreaming) {
                    if (status == Streamer.STATUS.CONN_FAIL) {
                        _msg = "Network failure, could not connect to server.";
                    } else if (status == Streamer.STATUS.AUTH_FAIL) {
                        _msg = "Authentication failure, please check stream credentials.";
                    } else {
                        _msg = "Unknown connection failure.";
                    }
                    LLog.d(TAG, _msg);
                    //releaseConnection();
                    mRetryCount++;
                }

                if (mRetryCount >= MAX_RECONNECT_ATTEMPTS) {
                    LLog.d(TAG, "mRetryCount=10");
                    mIsStreaming = false;
                    mRetryCount = 0;
                    /*   if (mStreamerGL != null) {
                        releaseConnection();
                    }*/
                    if (mStreamerGL != null) {
                        mHandler.removeCallbacks(updateTimerThread);
                        mStreamerGL.stopVideoCapture();
                        mStreamerGL.stopAudioCapture();
                        mStreamerGL.release();
                        mStreamerGL = null;
                    }
                }

                if (mIsStreaming) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mIsStreaming) {
                                createConnection("");
                            }
                        }
                    }, delay);
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
        switch (capture_state) {
            case STARTED:
                break;
        }
    }

    @Override
    public void OnAudioCaptureStateChanged(Streamer.CAPTURE_STATE capture_state) {
    }

    /*private void initEncoders(final SurfaceHolder holder) {
        //Create Streamer
        Streamer.FpsRange _fpsRange = new Streamer.FpsRange(24000, 30000);

        final StreamerGLBuilder builder = new StreamerGLBuilder(this);
        builder.setListener(this);
        //builder.setUserAgent("LiveStar Broadcaster App" + "/" + BuildConfig.VERSION_NAME + "-" + BuildConfig.VERSION_CODE);
        builder.setUserAgent("Yup" + "/" + "1" + "-" + 1);
        builder.setKeyFrameInterval(2);
        //Use Camera2 API
        builder.setCamera2(mCamera2);

        //identify cameras
        List<Streamer.CameraInfo> cameraList = builder.getCameraList(this, mCamera2);
        for (Streamer.CameraInfo _info : cameraList) {
            if (!_info.lensFacingBack) {
                mFrontCameraId = _info.cameraId;
                //Streamer.Size VIDEO_RES = new Streamer.Size(1280, 720);
                video_size_front = findClosestRes(VIDEO_RES, _info.recordSizes);
                //LLog.i(TAG, "initEncoders->video_size_front: " + video_size_front.width + " " + video_size_front.height);
                _fpsRange = findMatchingFPS(_fpsRange, _info.fpsRanges);
                //LLog.d(TAG, "video_size_front " + LSApplication.getInstance().getGson().toJson(video_size_front));
                //LLog.d(TAG, "_info.fpsRanges " + LSApplication.getInstance().getGson().toJson(_info.fpsRanges));
                //LLog.d(TAG, "_info.recordSizes " + LSApplication.getInstance().getGson().toJson(_info.recordSizes));
                //LLog.d(TAG, "_fpsRange" + LSApplication.getInstance().getGson().toJson(_fpsRange));
            } else {
                mBackCameraId = _info.cameraId;
                video_size_back = findClosestRes(VIDEO_RES, _info.recordSizes);
            }
        }

        String _id = LPref.getStoredCameraId(this);
        mIsFrontCamera = (_id.isEmpty() || _id.compareToIgnoreCase(mFrontCameraId) == 0);
        builder.setCameraId(mIsFrontCamera ? mFrontCameraId : mBackCameraId);

        //builder.setVideoSize(video_size_back);
        if (video_size_front.height == 480) {
            builder.setVideoSize(new Streamer.Size(480, 640));
        } else {
            builder.setVideoSize(new Streamer.Size(720, 1280));
        }
        //builder.setVideoSize(new Streamer.Size(480, 640));
        builder.setFrameRate(_fpsRange);
        int bitRate = LConnectivityUtil.isConnectedFast(this) ? 1750000 : 1000000;
        //int bitRate = VideoEncoder.calcBitRate(video_size_front,24 , MediaCodecInfo.CodecProfileLevel.AVCProfileBaseline);
        LLog.d(TAG, "Output video stream bitrate: " + bitRate + " fps max " + (int) _fpsRange.fps_max / 1000);
        builder.setBitRate(bitRate);
        //Audio
        builder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        builder.setSampleRate(44100);
        builder.setChannelCount(1);
        //GLES
       *//* runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.setSurface(holder.getSurface());
                builder.setSurfaceSize(calcPreviewSize(video_size_front));
                LLog.d(TAG, "calcPreviewSize" + calcPreviewSize(video_size_front).width + " " + calcPreviewSize(video_size_front).height);
            }
        });*//*
        new Thread() {
            @Override
            public void run() {
                //super.run();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        builder.setSurface(holder.getSurface());
                        builder.setSurfaceSize(calcPreviewSize(video_size_front));
                    }
                });
            }
        }.start();
        //builder.setSurface(holder.getSurface());
        //builder.setSurfaceSize(calcPreviewSize(video_size_front));

        // Rotate preview
        int _r = this.getWindowManager().getDefaultDisplay().getRotation();
        // This will be later set to device orientation when user press "Broadcast" button
        //        builder.setVideoRotation(0);
        //        builder.setDisplayRotation(0);

        //add front camera
        try {
            builder.addCamera(mFrontCameraId, video_size_front);
            //LLog.d(TAG,"Add camera front "+video_size_front.height + video_size_front.width);
            //LLog.d(TAG,"Add camera front VIDEO_RES "+VIDEO_RES.height + VIDEO_RES.width);
        } catch (NullPointerException e) {
            builder.addCamera(mFrontCameraId, VIDEO_RES);
        }
        //add back camera
        try {
            builder.addCamera(mBackCameraId, video_size_back);
            //LLog.d(TAG,"Add camera back "+video_size_back.height + video_size_back.width);
            //LLog.d(TAG,"Add camera back VIDEO_RES "+VIDEO_RES.height + VIDEO_RES.width);
        } catch (NullPointerException e) {
            builder.addCamera(mFrontCameraId, VIDEO_RES);
        }

        //Starting cupturing from Audio and Video source
        //LLog.d(TAG, "w: " + video_size_front.width + " h: " + video_size_front.height);
        VideoEncoder videoEncoder = VideoEncoder.createVideoEncoder(video_size_front);
        mStreamerGL = builder.build();
        mStreamerGL.setVideoOrientation(1);
        mStreamerGL.startVideoCapture();
        mStreamerGL.startVideoCapture(this, mFrontCameraId, holder, videoEncoder, this, 1);
        mStreamerGL.startAudioCapture();

        //mStreamerGL.setDisplayRotation((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? 1 : 0);
        //mStreamerGL.setDisplayRotation((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? 1 : 0);
        //startLivestream();

        startLivestream();
    }*/

    private StreamerGLBuilder initEncoders() {
        //Streamer.FpsRange _fpsRange = new Streamer.FpsRange(24000, 30000);
        Streamer.FpsRange _fpsRange = new Streamer.FpsRange(15000, 15000);

        StreamerGLBuilder builder = new StreamerGLBuilder(this);
        builder.setListener(this);
        //builder.setUserAgent("LiveStar Broadcaster App" + "/" + BuildConfig.VERSION_NAME + "-" + BuildConfig.VERSION_CODE);
        builder.setUserAgent("Yup" + "/" + "1" + "-" + 1);
        builder.setKeyFrameInterval(2);
        //Use Camera2 API
        builder.setCamera2(mCamera2);

        //identify cameras
        List<Streamer.CameraInfo> cameraList = builder.getCameraList(this, mCamera2);
        for (Streamer.CameraInfo _info : cameraList) {
            if (!_info.lensFacingBack) {
                mFrontCameraId = _info.cameraId;
                //Streamer.Size VIDEO_RES = new Streamer.Size(1280, 720);
                video_size_front = findClosestRes(VIDEO_RES, _info.recordSizes);
                //LLog.i(TAG, "initEncoders->video_size_front: " + video_size_front.width + " " + video_size_front.height);
                _fpsRange = findMatchingFPS(_fpsRange, _info.fpsRanges);
                //LLog.d(TAG, "video_size_front " + LSApplication.getInstance().getGson().toJson(video_size_front));
                //LLog.d(TAG, "_info.fpsRanges " + LSApplication.getInstance().getGson().toJson(_info.fpsRanges));
                //LLog.d(TAG, "_info.recordSizes " + LSApplication.getInstance().getGson().toJson(_info.recordSizes));
                //LLog.d(TAG, "_fpsRange" + LSApplication.getInstance().getGson().toJson(_fpsRange));
            } else {
                mBackCameraId = _info.cameraId;
                video_size_back = findClosestRes(VIDEO_RES, _info.recordSizes);
            }
        }

        String _id = UizaUtil.getStoredCameraId(this);
        mIsFrontCamera = (_id.isEmpty() || _id.compareToIgnoreCase(mFrontCameraId) == 0);
        builder.setCameraId(mIsFrontCamera ? mFrontCameraId : mBackCameraId);

        //builder.setVideoSize(video_size_back);
        if (video_size_front.height == 480) {
            builder.setVideoSize(new Streamer.Size(480, 640));
        } else {
            builder.setVideoSize(new Streamer.Size(720, 1280));
        }
        //builder.setVideoSize(new Streamer.Size(480, 640));
        builder.setFrameRate(_fpsRange);
        //int bitRate = LConnectivityUtil.isConnectedFast(this) ? 1750000 : 1000000;
        //int bitRate = LConnectivityUtil.isConnectedFast(this) ? 1500 * 1024 : 1000 * 1024;
        int bitRate = 800000;
        //int bitRate = VideoEncoder.calcBitRate(video_size_front, 24, MediaCodecInfo.CodecProfileLevel.AVCProfileBaseline);
        LLog.d(TAG, "Output video stream bitrate: " + bitRate + " fps max " + (int) _fpsRange.fps_max / 1000);
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
                builder.setSurfaceSize(calcPreviewSize(video_size_front));
                LLog.d(TAG,"calcPreviewSize"+ calcPreviewSize(video_size_front).width +" "+ calcPreviewSize(video_size_front).height);
            }
        });*/
        try {
            builder.addCamera(mFrontCameraId, video_size_front);
            LLog.d(TAG, "Add camera front " + video_size_front.height + video_size_front.width);
            LLog.d(TAG, "Add camera front VIDEO_RES " + VIDEO_RES.height + VIDEO_RES.width);
        } catch (NullPointerException e) {
            builder.addCamera(mFrontCameraId, VIDEO_RES);
        }
        //add back camera
        try {
            builder.addCamera(mBackCameraId, video_size_back);
            LLog.d(TAG, "Add camera back " + video_size_back.height + video_size_back.width);
            LLog.d(TAG, "Add camera back VIDEO_RES " + VIDEO_RES.height + VIDEO_RES.width);
        } catch (NullPointerException e) {
            builder.addCamera(mFrontCameraId, VIDEO_RES);
        }
        return builder;
    }

    private void startRecord(StreamerGLBuilder builder, SurfaceHolder holder) {
        //Create Streamer
        builder.setSurface(holder.getSurface());
        builder.setSurfaceSize(calcPreviewSize(video_size_front));

        // Rotate preview
        // This will be later set to device orientation when user press "Broadcast" button
        //        builder.setVideoRotation(0);
        //        builder.setDisplayRotation(0);

        //add front camera

        //Starting cupturing from Audio and Video source
        LLog.d(TAG, "w: " + video_size_front.width + " h: " + video_size_front.height);
        VideoEncoder videoEncoder = VideoEncoder.createVideoEncoder(video_size_front);
        mStreamerGL = builder.build();
        mStreamerGL.setVideoOrientation(1);
        mStreamerGL.startVideoCapture();
        mStreamerGL.startVideoCapture(this, mFrontCameraId, holder, videoEncoder, this, 1);
        mStreamerGL.startAudioCapture();

        //mStreamerGL.setDisplayRotation((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? 1 : 0);
        //mStreamerGL.setDisplayRotation((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? 1 : 0);
        //startLivestream();
        startLivestream();
        //connectAndStartRoom();
    }

    private Streamer.Size findClosestRes(Streamer.Size requested, Streamer.Size[] recordSizes) {
        Streamer.Size _res = null;
        //LLog.d(TAG, "requested " + LSApplication.getInstance().getGson().toJson(requested));
        for (Streamer.Size _s : recordSizes) {
            if ((requested.width == _s.width) && (requested.height == _s.height)) {
                _res = _s;
                LLog.d(TAG, "findClosestRes w: " + _res.width + " h: " + _res.height);
                break;
            }
        }
        if (_res == null) {
            Streamer.Size temp = new Streamer.Size(640, 480);
            _res = temp;
        }
        //LLog.d(TAG, "_res " + LSApplication.getInstance().getGson().toJson(_res));
        return _res;
    }

    private Streamer.Size calcPreviewSize(Streamer.Size video_size) {
        //int r_h = mPreview.getHeight() + getBottomBarHeight() + getStatusBarHeight();
        //int r_h = mPreview.getHeight() + LScreenUtils.getBottomBarHeight(activity) + LScreenUtils.getStatusBarHeight(activity);
        int r_h = mPreview.getHeight();
        ViewGroup.LayoutParams params = mPreview.getLayoutParams();
        params.height = r_h;
        //LLog.i(TAG, "calcPreviewSize navi: " + getBottomBarHeight());
        //LLog.i(TAG, "calcPreviewSize status: " + getStatusBarHeight());
        //LLog.i(TAG, "calcPreviewSize height: " + r_h);
        //LLog.i(TAG, "calcPreviewSize ratio: " + video_size.getRatio());
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            params.width = (int) (r_h / video_size.getRatio());
            //LLog.i(TAG, "calcPreviewSize -> PORT: " + params.width);
        } else {
            params.width = (int) (r_h * video_size.getRatio());
            //LLog.i(TAG, "calcPreviewSize -> LANS: " + params.width);
        }
        mPreview.setLayoutParams(params);

        //LLog.i(TAG, "calcPreviewSize w: " + params.width + " h: " + params.height);
        return new Streamer.Size(params.width, params.height);
        //return new Streamer.Size(params.height, params.width);
    }

    private Streamer.FpsRange findMatchingFPS(Streamer.FpsRange fpsRange, Streamer.FpsRange[] fpsRanges) {
        //LLog.i(TAG, String.format("Requested FPS = %.2f, %.2f", fpsRange.fps_min, fpsRange.fps_max));
        if (fpsRanges.length == 1)
            return fpsRanges[0];

        int _minmaxId = 0;

        for (int i = 1; i < fpsRanges.length; i++) {
            float _prev = Math.max(Math.abs(fpsRange.fps_min - fpsRanges[_minmaxId].fps_min),
                    Math.abs(fpsRange.fps_max - fpsRanges[_minmaxId].fps_max));
            float _cur = Math.max(Math.abs(fpsRange.fps_min - fpsRanges[i].fps_min),
                    Math.abs(fpsRange.fps_max - fpsRanges[i].fps_max));
            if (_cur < _prev)
                _minmaxId = i;
        }
        return fpsRanges[_minmaxId];
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacksAndMessages(null);
        mHandler.removeCallbacks(updateTimerThread);
        if (mIsStreaming) {
            //LLog.d(TAG, "onPause -> mIsStreaming -> releaseConnection()");
            releaseConnection();
        }
        if (mStreamerGL != null) {
            /*forceEndLive();*/
            //LLog.d(TAG, "OnDestroy -> startService");
            mStreamerGL.stopVideoCapture();
            mStreamerGL.stopAudioCapture();
            mStreamerGL.release();
            mStreamerGL = null;
            releaseConnection();
        }
        mPreview = null;
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //LLog.d(TAG, "onPause");
        UizaUtil.storeFilterState(this, mFilterOn);
        UizaUtil.storeCameraId(this, mIsFrontCamera ? mFrontCameraId : mBackCameraId);
        goOnPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFilterOn = UizaUtil.getStoredFilterState(this);
        //LLog.d(TAG, "onResume");
        if (mPreview == null) {
            mPreview = (SurfaceView) findViewById(R.id.surface);
            SurfaceHolder surfaceHolder = mPreview.getHolder();
            surfaceHolder.addCallback(mPreviewCallback);
        }
        if (isPauseInRoom) {
            isPauseInRoom = false;
        }
    }
}
