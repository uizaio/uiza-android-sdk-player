package uizacoresdk.cache;


import android.content.Context;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.database.DatabaseProvider;
import com.google.android.exoplayer2.database.ExoDatabaseProvider;
import com.google.android.exoplayer2.offline.ActionFileUpgradeUtil;
import com.google.android.exoplayer2.offline.DefaultDownloadIndex;
import com.google.android.exoplayer2.offline.DefaultDownloaderFactory;
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
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.io.IOException;

import vn.uiza.core.common.Constants;
import vn.uiza.core.utilities.LLog;

public class UZCacheHelper {

    public static final String TAG = "UZCacheHelper";
    private static final String DOWNLOAD_ACTION_FILE = "actions";
    private static final String DOWNLOAD_TRACKER_ACTION_FILE = "tracked_actions";

    private Context context;
    private DownloadManager downloadManager;
    private DownloadTracker downloadTracker;
    private DatabaseProvider databaseProvider;
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
        uzCache.setDatabaseProvider(getDatabaseProvider());
    }


    public void setCacheSize(long cacheSize) {
        uzCache.setMaxBytes(cacheSize);
    }


    private DatabaseProvider getDatabaseProvider() {
        if (databaseProvider == null) {
            databaseProvider = new ExoDatabaseProvider(context);
        }
        return databaseProvider;
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
                new DefaultBandwidthMeter.Builder(context).build() /* listener */,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true /* allowCrossProtocolRedirects */
        );
    }

    /**
     * @param extensionRendererMode: see {@link DefaultRenderersFactory.ExtensionRendererMode}
     * @return a {@link DefaultRenderersFactory}
     */
    public RenderersFactory buildRenderersFactory(
            @DefaultRenderersFactory.ExtensionRendererMode
                    int extensionRendererMode) {
        return new DefaultRenderersFactory(/* context= */ context)
                .setExtensionRendererMode(extensionRendererMode);
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
            DefaultDownloadIndex downloadIndex = new DefaultDownloadIndex(getDatabaseProvider());
            upgradeActionFile(
                    DOWNLOAD_ACTION_FILE, downloadIndex, /* addNewDownloadsAsCompleted= */ false);
            upgradeActionFile(
                    DOWNLOAD_TRACKER_ACTION_FILE, downloadIndex, /* addNewDownloadsAsCompleted= */ true);
            DownloaderConstructorHelper downloaderConstructorHelper =
                    new DownloaderConstructorHelper(uzCache.getCache(), buildHttpDataSourceFactory());
            downloadManager =
                    new DownloadManager(
                            context, downloadIndex, new DefaultDownloaderFactory(downloaderConstructorHelper));
            downloadTracker =
                    new DownloadTracker(/* context= */ context, buildDataSourceFactory(), downloadManager);
        }
    }

    private void upgradeActionFile(
            String fileName, DefaultDownloadIndex downloadIndex, boolean addNewDownloadsAsCompleted) {
        try {
            ActionFileUpgradeUtil.upgradeAndDelete(
                    new File(uzCache.getCacheDirectory(), fileName),
                    /* downloadIdProvider= */ null,
                    downloadIndex,
                    /* deleteOnFailure= */ true,
                    addNewDownloadsAsCompleted);
        } catch (IOException e) {
            LLog.e(TAG, "Failed to upgrade action file: " + fileName + ": " + e.getLocalizedMessage());
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
