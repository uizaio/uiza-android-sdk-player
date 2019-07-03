package uizacoresdk.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheEvictor;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.SimpleCache;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.File;
import java.lang.ref.WeakReference;

import vn.uiza.core.common.Constants;
import vn.uiza.restapi.uiza.model.v3.linkplay.getlinkplay.ResultGetLinkPlay;
import vn.uiza.restapi.uiza.model.v3.linkplay.gettokenstreaming.ResultGetTokenStreaming;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;

public class UZCache {

    private static UZCache instance;

    private WeakReference<Context> context;
    private SharedPreferences shared;

    public static UZCache init(Context context) {
        if (instance == null) {
            instance = new UZCache(context);
        }
        return instance;
    }

    /**
     * You should call UZCache#init(Context context) first
     *
     * @return a {@link UZCache}
     */
    public static UZCache get() {
        if (instance == null) {
            throw new NullPointerException("u should init first");
        }
        return instance;
    }

    private UZCache(Context context) {
        this.context = new WeakReference<>(context);
        this.shared = context.getSharedPreferences(Constants.USER_AGENT, Context.MODE_PRIVATE);
    }

    private File cacheDirectory;
    private Cache cache;
    private CacheEvictor evictor;
    private long maxBytes = 0L;
    private byte[] secretKey;
    private static final String DOWNLOAD_CONTENT_DIRECTORY = "caches";

    public void setMaxBytes(long maxBytes) {
        this.maxBytes = maxBytes;
    }

    /**
     * @param secretKey If not null, cache keys will be stored encrypted on filesystem using AES/CBC.
     *                  The key must be 16 bytes long. The cache will delete any unrecognized files from the directory.
     *                  Hence the directory cannot be used to store other files.
     */
    public void setSecretKey(byte[] secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * {@link LeastRecentlyUsedCacheEvictor} that will evict/remove least recently used cache files first.
     * For example if your cache size is 10MB, when the size is reached out,
     * it will automatically find and remove files which least recently used.
     * <p>
     * {@link NoOpCacheEvictor} that doesn't ever evict/remove cache files.
     * Based on location of your cache folder,
     * if it's in internal storage, the folder will be removed when users clear app data or uninstall app.
     *
     * @return a Cache
     */
    public synchronized Cache getCache() {
        if (cache == null) {
            if (maxBytes > 0L) {
                evictor = new LeastRecentlyUsedCacheEvictor(maxBytes);
            } else {
                evictor = new NoOpCacheEvictor();
            }
            File cacheContentDirectory = new File(getCacheDirectory(), DOWNLOAD_CONTENT_DIRECTORY);
            cache = new SimpleCache(cacheContentDirectory, evictor, secretKey);
        }
        return cache;
    }

    /**
     * @return The path of the directory holding application files.
     * @see Context#getFilesDir
     */
    public File getCacheDirectory() {
        if (cacheDirectory == null) {
            cacheDirectory = context.get().getExternalFilesDir(null);
            if (cacheDirectory == null) {
                cacheDirectory = context.get().getFilesDir();
            }
        }
        return cacheDirectory;
    }

    /**
     * ============ CACHE ENTITY For UZVideo ==========
     */
    public void put(String entityId, ResultGetLinkPlay result) {
        shared.edit().putString("reslinkplay_" + entityId, (new Gson()).toJson(result)).apply();
    }

    public ResultGetLinkPlay getResultGetLinkPlay(String entityId) {
        String json = shared.getString("reslinkplay_" + entityId, "");
        if (!TextUtils.isEmpty(json)) {
            try {
                return (new Gson()).fromJson(json, ResultGetLinkPlay.class);
            } catch (JsonParseException ex) {
                return null;
            }

        }
        return null;
    }

    public void put(String entityId, Data data) {
        shared.edit().putString("data_" + entityId, (new Gson()).toJson(data)).apply();
    }

    public Data getData(String entityId) {

        String json = shared.getString("data_" + entityId, "");
        if (!TextUtils.isEmpty(json)) {
            try {
                return (new Gson()).fromJson(json, Data.class);
            } catch (JsonParseException ex) {
                return null;
            }

        }
        return null;
    }

    public void put(String entityId, ResultGetTokenStreaming token) {
        shared.edit().putString("tokenstream_" + entityId, (new Gson()).toJson(token)).apply();
    }


    public ResultGetTokenStreaming getTokenStreaming(String entityId) {
        String json = shared.getString("tokenstream_" + entityId, "");
        if (!TextUtils.isEmpty(json)) {
            try {
                return (new Gson()).fromJson(json, ResultGetTokenStreaming.class);
            } catch (JsonParseException ex) {
                return null;
            }

        }
        return null;
    }

    public boolean isCacheEntity(String entityId) {
        return shared.contains("reslinkplay_" + entityId)
                && shared.contains("data_" + entityId)
                && shared.contains("tokenstream_" + entityId);
    }
}
