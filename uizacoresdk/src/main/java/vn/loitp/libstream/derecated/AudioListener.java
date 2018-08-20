//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package vn.loitp.libstream.derecated;

import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;

import java.nio.ByteBuffer;

import vn.loitp.core.utilities.LLog;

class AudioListener extends Thread {
    private static final String TAG = "AudioListener";
    Streamer.Listener listener_;
    Streamer.CAPTURE_STATE state_;
    private AudioRecord mAudioRecord;
    private BufferInfo bufferInfo = new BufferInfo();
    private StreamBuffer streamBuffer_;
    private int audioSource_;
    private AudioEncoder audioEncoder_;
    private long frameId;

    public AudioListener(StreamBuffer streamBuffer, int audioSource, AudioEncoder audioEncoder, Streamer.Listener listener) {
        this.state_ = Streamer.CAPTURE_STATE.STOPPED;
        this.frameId = 0L;
        this.streamBuffer_ = streamBuffer;
        this.audioSource_ = audioSource;
        this.audioEncoder_ = audioEncoder;
        this.listener_ = listener;
    }

    private void setAudioCaptureState(final Streamer.CAPTURE_STATE audioCaptureState) {
        if (audioCaptureState != this.state_) {
            this.state_ = audioCaptureState;
            if (null != this.listener_) {
                Handler handler = this.listener_.getHandler();
                if (null != handler) {
                    handler.post(new Runnable() {
                        public void run() {
                            AudioListener.this.listener_.OnAudioCaptureStateChanged(audioCaptureState);
                        }
                    });
                }
            }
        }
    }

    private boolean openEncoder(int bufferSize) {
        try {
            this.audioEncoder_.getFormat().setInteger("max-input-size", bufferSize);
            this.audioEncoder_.getEncoder().configure(this.audioEncoder_.getFormat(), (Surface) null, (MediaCrypto) null, 1);
            this.audioEncoder_.getEncoder().start();
            return true;
        } catch (Exception var3) {
            LLog.e("AudioListener", Log.getStackTraceString(var3));
            return false;
        }
    }

    public void run() {
        try {
            int e = this.audioEncoder_.getChannelCount() == 1 ? 16 : 12;
            int bufferSize = AudioRecord.getMinBufferSize(this.audioEncoder_.getSampleRate(), e, 2);
            if (bufferSize <= 0) {
                this.setAudioCaptureState(Streamer.CAPTURE_STATE.FAILED);
                throw new Exception();
            }

            if (!this.openEncoder(bufferSize)) {
                this.setAudioCaptureState(Streamer.CAPTURE_STATE.ENCODER_FAIL);
                throw new Exception();
            }

            this.mAudioRecord = new AudioRecord(this.audioSource_, this.audioEncoder_.getSampleRate(), e, 2, bufferSize);
            byte[] audioBuffer = new byte[bufferSize];
            this.mAudioRecord.startRecording();
            this.setAudioCaptureState(Streamer.CAPTURE_STATE.STARTED);
            long sampleSize = (long) (2 * this.audioEncoder_.getChannelCount());
            long expected_ts = SystemClock.uptimeMillis() * 1000L;

            if (AcousticEchoCanceler.isAvailable()) {
                AcousticEchoCanceler _aec = AcousticEchoCanceler.create(mAudioRecord.getAudioSessionId());
                LLog.d("AACStream", "AcousticEchoCanceler is available. Enabling...");
                if (!_aec.getEnabled())
                    _aec.setEnabled(true);
            }

            if (NoiseSuppressor.isAvailable()) {
                NoiseSuppressor _ns = NoiseSuppressor.create(mAudioRecord.getAudioSessionId());
                LLog.d("AACStream", "NoiseSuppressor is available. Enabling...");
                if (!_ns.getEnabled())
                    _ns.setEnabled(true);
            }

            if (AutomaticGainControl.isAvailable()) {
                AutomaticGainControl _agc = AutomaticGainControl.create(mAudioRecord.getAudioSessionId());
                LLog.d("AACStream", "AutomaticGainControl is available. Enabling...");
                if (!_agc.getEnabled())
                    _agc.setEnabled(true);
            }

            while (!this.isInterrupted()) {
                int bytesRead = this.mAudioRecord.read(audioBuffer, 0, audioBuffer.length);
                long cur_ts = SystemClock.uptimeMillis() * 1000L;
                if (bytesRead > 0) {
                    if (cur_ts < expected_ts) {
                        cur_ts = expected_ts;
                    }

                    int inputBufferIndex = this.audioEncoder_.getEncoder().dequeueInputBuffer(-1L);
                    if (inputBufferIndex < 0) {
                        this.setAudioCaptureState(Streamer.CAPTURE_STATE.FAILED);
                        break;
                    }

                    ByteBuffer inputBuffer = this.audioEncoder_.getEncoder().getInputBuffers()[inputBufferIndex];
                    inputBuffer.clear();
                    inputBuffer.put(audioBuffer, 0, bytesRead);
                    this.audioEncoder_.getEncoder().queueInputBuffer(inputBufferIndex, 0, bytesRead, cur_ts, 0);
                    expected_ts = cur_ts + 1000000L * ((long) bytesRead / sampleSize) / (long) this.audioEncoder_.getSampleRate();
                    this.getAudioFrame(this.audioEncoder_.getEncoder());
                }
            }
        } catch (Exception var13) {
            LLog.e("AudioListener", "audio capture failed");
            LLog.e("AudioListener", Log.getStackTraceString(var13));
        }

        this.streamBuffer_.setAudioFormatParams((StreamBuffer.AudioFormatParams) null);
        if (null != this.mAudioRecord) {
            this.mAudioRecord.stop();
            this.mAudioRecord.release();
        }

        if (null != this.audioEncoder_.getEncoder()) {
            this.audioEncoder_.getEncoder().stop();
            this.audioEncoder_ = null;
        }

        this.setAudioCaptureState(Streamer.CAPTURE_STATE.STOPPED);
    }

    void getAudioFrame(MediaCodec encoder) {
        try {
            if (null == encoder) {
                LLog.e("AudioListener", "audio codec is null");
            } else if (null == this.bufferInfo) {
                LLog.e("AudioListener", "bufferInfo is null");
            } else {
                while (true) {
                    while (true) {
                        int e = encoder.dequeueOutputBuffer(this.bufferInfo, 0L);
                        StreamBuffer.AudioFormatParams bufferItem;
                        if (-2 == e) {
                            MediaFormat outBuffer1 = encoder.getOutputFormat();
                            LLog.d("AudioListener", "audio format changed " + outBuffer1);
                            bufferItem = new StreamBuffer.AudioFormatParams();
                            bufferItem.config_buf = outBuffer1.getByteBuffer("csd-0").array();
                            bufferItem.config_len = bufferItem.config_buf.length;
                            bufferItem.sampleRate = outBuffer1.getInteger("sample-rate");
                            bufferItem.channelCount = outBuffer1.getInteger("channel-count");
                            this.streamBuffer_.setAudioFormatParams(bufferItem);
                        } else {
                            if (e < 0) {
                                return;
                            }

                            ByteBuffer outBuffer = encoder.getOutputBuffers()[e];
                            if ((2 & this.bufferInfo.flags) == 2) {
                                bufferItem = new StreamBuffer.AudioFormatParams();
                                bufferItem.config_buf = new byte[this.bufferInfo.size];
                                bufferItem.config_len = this.bufferInfo.size;
                                outBuffer.get(bufferItem.config_buf, 0, bufferItem.config_len);
                                this.streamBuffer_.setAudioFormatParams(bufferItem);
                            } else {
                                BufferItem bufferItem1 = BufferItem.newAudioBuffer((long) (this.frameId++), this.bufferInfo.size);
                                bufferItem1.setTimestamp(this.bufferInfo.presentationTimeUs);
                                bufferItem1.setFlags(this.bufferInfo.flags);
                                outBuffer.get(bufferItem1.getData(), 0, this.bufferInfo.size);
                                this.streamBuffer_.putItem(bufferItem1);
                            }

                            encoder.releaseOutputBuffer(e, false);
                        }
                    }
                }
            }
        } catch (Exception var5) {
            LLog.e("AudioListener", Log.getStackTraceString(var5));
            this.setAudioCaptureState(Streamer.CAPTURE_STATE.FAILED);
        }
    }
}
