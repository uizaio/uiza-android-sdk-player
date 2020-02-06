package vn.uiza.helpers;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

import timber.log.Timber;

/**
 * Created by Loitp on 5/6/2017.
 */

public class TTSHelper implements TextToSpeech.OnInitListener {
    private final String TAG = getClass().getSimpleName();
    private TextToSpeech tts;
    private static final TTSHelper ourInstance = new TTSHelper();

    // Bill Pugh Singleton Implementation
    private static class TTSPrivateHelper {
        private static final TTSHelper INSTANCE = new TTSHelper();
    }

    public static TTSHelper getInstance() {
        return TTSPrivateHelper.INSTANCE;
    }

    private TTSHelper() {
    }

    public void setupTTS(Context context) {
        tts = new TextToSpeech(context, this);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Timber.d("This Language is not supported");
            }
        } else {
            Timber.d("Initialization Failed!");
        }
    }

    public void speakOut(String text) {
        if (tts != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            } else {
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    public void destroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
    }
}
