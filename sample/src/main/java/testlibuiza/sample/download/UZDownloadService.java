package testlibuiza.sample.download;

import android.app.Notification;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.offline.DownloadManager;
import com.google.android.exoplayer2.offline.DownloadManager.TaskState;
import com.google.android.exoplayer2.offline.DownloadService;
import com.google.android.exoplayer2.scheduler.PlatformScheduler;
import com.google.android.exoplayer2.scheduler.Scheduler;
import com.google.android.exoplayer2.ui.DownloadNotificationUtil;
import com.google.android.exoplayer2.util.NotificationUtil;
import com.google.android.exoplayer2.util.Util;

import testlibuiza.app.LSApplication;
import uizacoresdk.R;

public class UZDownloadService extends DownloadService {

    private static final String TAG = "UZDownloadService";

    private static final String CHANNEL_ID = "download_channel";
    private static final int FOREGROUND_NOTIFICATION_ID = 1;
    private static final int JOB_ID = 1;

    public UZDownloadService() {
        super(FOREGROUND_NOTIFICATION_ID,
                DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
                CHANNEL_ID,
                R.string.exo_download_notification_channel_name);
    }

    @Override
    protected DownloadManager getDownloadManager() {
        return ((LSApplication) getApplication()).getDownloadManager();
    }

    @Nullable
    @Override
    protected Scheduler getScheduler() {
        return Util.SDK_INT >= 21 ? new PlatformScheduler(this, JOB_ID) : null;
    }

    @Override
    protected Notification getForegroundNotification(TaskState[] taskStates) {
        float totalPercentage = 0;
        int downloadTaskCount = 0;
        boolean allDownloadPercentagesUnknown = true;
        boolean haveDownloadedBytes = false;
        boolean haveDownloadTasks = false;
        for (TaskState taskState : taskStates) {
            if (taskState.state != TaskState.STATE_STARTED
                    && taskState.state != TaskState.STATE_COMPLETED) {
                continue;
            }
            if (taskState.action.isRemoveAction) {
                continue;
            }
            haveDownloadTasks = true;
            if (taskState.downloadPercentage != C.PERCENTAGE_UNSET) {
                allDownloadPercentagesUnknown = false;
                totalPercentage += taskState.downloadPercentage;
            }
            haveDownloadedBytes |= taskState.downloadedBytes > 0;
            downloadTaskCount++;
        }
        int progress = 0;
        boolean indeterminate = true;
        if (haveDownloadTasks) {
            progress = (int) (totalPercentage / downloadTaskCount);
            indeterminate = allDownloadPercentagesUnknown && haveDownloadedBytes;
        }
        return DownloadNotificationUtil.buildProgressNotification(
                /* context= */ this,
                R.drawable.exo_controls_pause,
                CHANNEL_ID,
                /* contentIntent= */ null,
                /* message= */ progress + " %",
                taskStates);
    }

    @Override
    protected void onTaskStateChanged(TaskState taskState) {
        if (taskState.action.isRemoveAction) {
            return;
        }
        Notification notification = null;
        if (taskState.state == TaskState.STATE_COMPLETED) {
            notification =
                    DownloadNotificationUtil.buildDownloadCompletedNotification(
                            /* context= */ this,
                            R.drawable.exo_controls_play,
                            CHANNEL_ID,
                            /* contentIntent= */ null,
                            Util.fromUtf8Bytes(taskState.action.data));
        } else if (taskState.state == TaskState.STATE_FAILED) {
            notification =
                    DownloadNotificationUtil.buildDownloadFailedNotification(
                            /* context= */ this,
                            R.drawable.exo_controls_play,
                            CHANNEL_ID,
                            /* contentIntent= */ null,
                            Util.fromUtf8Bytes(taskState.action.data));
        }
        int notificationId = FOREGROUND_NOTIFICATION_ID + 1 + taskState.taskId;
        NotificationUtil.setNotification(this, notificationId, notification);
    }
}
