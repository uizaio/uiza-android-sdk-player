package uizacoresdk.cache;


import android.content.Context;

import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloaderConstructorHelper;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.FileDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSource;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor;
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor;
import com.google.android.exoplayer2.util.Util;

import java.io.File;

import vn.uiza.core.common.Constants;

public class UZCacheHelper {

    public static final String TAG = "UZCacheHelper";
    private static final String DOWNLOAD_ACTION_FILE = "actions";
    private static final String DOWNLOAD_TRACKER_ACTION_FILE = "tracked_actions";
    private static final int MAX_SIMULTANEOUS_DOWNLOADS = 2;

    private Context context;
    private DownloadManager downloadManager;
    private DownloadTracker downloadTracker;
    private CacheUtil uzCache;

    private static UZCacheHelper instance;


    public static UZCacheHelper init(Context context) {
        if (instance == null) {
            instance = new UZCacheHelper(context);
        }
        return instance;
    }

    /**
     * You should call UZCacheHelper#init(Context context) first
     *
     * @return a {@link UZCacheHelper}
     */
    public static UZCacheHelper get() {
        if (instance == null) {
            throw new NullPointerException("u should init first");
        }
        return instance;
    }

    /**
     * Constructs the UZCacheHelper.
     *
     * @param context A valid context of the calling application.
     */

    private UZCacheHelper(Context context) {
        this.context = context;
        uzCache = CacheUtil.init(context);
    }

    /**
     * @param cacheSize, if it is zero then use {@link NoOpCacheEvictor}
     *                  else {@link LeastRecentlyUsedCacheEvictor}
     */
    protected void setCacheSize(long cacheSize) {
        uzCache.setMaxBytes(cacheSize);
    }

    /**
     * Returns a {@link DataSource.Factory}.
     */
    public DataSource.Factory buildDataSourceFactory() {
        DefaultDataSourceFactory upstreamFactory =
                new DefaultDataSourceFactory(context, buildHttpDataSourceFactory());
        return buildReadOnlyCacheDataSource(upstreamFactory, uzCache.getCache());
    }

    /**
     * Returns a {@link HttpDataSource.Factory}.
     */
    public HttpDataSource.Factory buildHttpDataSourceFactory() {
        return new DefaultHttpDataSourceFactory(
                Util.getUserAgent(context, Constants.USER_AGENT),
                new DefaultBandwidthMeter() /* listener */,
                60 * 1000,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true /* allowCrossProtocolRedirects */
        );
    }

    /**
     * Returns a {@link DownloadManager}.
     */
    public DownloadManager getDownloadManager() {
        initDownloadManager();
        return downloadManager;
    }

    /**
     * Returns a {@link DownloadTracker}.
     */
    public DownloadTracker getDownloadTracker() {
        initDownloadManager();
        return downloadTracker;
    }

    private synchronized void initDownloadManager() {
        if (downloadManager == null) {
            DownloaderConstructorHelper downloaderConstructorHelper =
                    new DownloaderConstructorHelper(uzCache.getCache(), buildHttpDataSourceFactory());
            downloadManager =
                    new DownloadManager(
                            downloaderConstructorHelper,
                            MAX_SIMULTANEOUS_DOWNLOADS,
                            DownloadManager.DEFAULT_MIN_RETRY_COUNT,
                            new File(uzCache.getCacheDirectory(), DOWNLOAD_ACTION_FILE));
            downloadTracker =
                    new DownloadTracker(
                            /* context= */ context,
                            buildDataSourceFactory(),
                            new File(uzCache.getCacheDirectory(), DOWNLOAD_TRACKER_ACTION_FILE));
            downloadManager.addListener(downloadTracker);
        }
    }


    private static CacheDataSourceFactory buildReadOnlyCacheDataSource(
            DefaultDataSourceFactory upstreamFactory, Cache cache) {
        return new CacheDataSourceFactory(
                cache,
                upstreamFactory,
                new FileDataSourceFactory(),
                /* cacheWriteDataSinkFactory= */ null,
                CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR,
                /* eventListener= */ null);
    }

}
