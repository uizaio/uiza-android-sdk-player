package io.uiza.player.analytic.event;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import io.uiza.core.api.UzApiMaster;
import io.uiza.core.api.UzServiceApi;
import io.uiza.core.api.client.UzRestClientTracking;
import io.uiza.core.api.request.tracking.UizaTracking;
import io.uiza.core.api.util.ApiSubscriber;
import io.uiza.core.model.UzLinkPlayData;
import io.uiza.core.util.SharedPreferenceUtil;
import io.uiza.core.util.UzDateTimeUtil;
import io.uiza.core.util.UzOsUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.player.analytic.TmpParamData;
import io.uiza.player.util.UzPlayerData;

public final class EventTrackingManager {

    private static final String PREFERENCES_FILE_NAME = "EventTrackingManager";
    private static final String PAGE_TYPE = "app";
    private static final String NULL = "null";
    private Context context;
    private boolean isTracked25;
    private boolean isTracked50;
    private boolean isTracked75;
    private boolean isTracked100;

    public EventTrackingManager(@NonNull Context context) {
        this.context = context;
    }

    /**
     * Reset the local tracking progress.
     */
    public void resetTracking() {
        markEventTracked(context, TrackingEvent.E_DISPLAY, false);
        markEventTracked(context, TrackingEvent.E_PLAYS_REQUESTED, false);
        markEventTracked(context, TrackingEvent.E_VIDEO_STARTS, false);
        markEventTracked(context, TrackingEvent.E_VIEW, false);
        markEventTracked(context, TrackingEvent.E_PLAY_THROUGH_25, false);
        markEventTracked(context, TrackingEvent.E_PLAY_THROUGH_50, false);
        markEventTracked(context, TrackingEvent.E_PLAY_THROUGH_75, false);
        markEventTracked(context, TrackingEvent.E_PLAY_THROUGH_100, false);
        isTracked25 = false;
        isTracked50 = false;
        isTracked75 = false;
        isTracked100 = false;
    }

    public void markEventTracked(Context context, @TrackingEvent String event,
            boolean hasDone) {
        if (context == null) {
            return;
        }
        SharedPreferenceUtil.put(getPrivatePreference(context), event, hasDone);
    }

    public boolean isEventTracked(Context context, @TrackingEvent String event) {
        if (context == null) {
            return false;
        }
        return (boolean) SharedPreferenceUtil.get(getPrivatePreference(context), event, false);
    }

    public void trackProgress(int percent) {
        if (percent >= Constants.PLAYTHROUGH_100) {
            if (isTracked100) {
                return;
            }
            if (isEventTracked(context, TrackingEvent.E_PLAY_THROUGH_100)) {
                isTracked100 = true;
            } else {
                trackUizaEvent(TrackingEvent.E_PLAY_THROUGH_100);
            }
        } else if (percent >= Constants.PLAYTHROUGH_75) {
            if (isTracked75) {
                return;
            }
            if (isEventTracked(context, TrackingEvent.E_PLAY_THROUGH_75)) {
                isTracked75 = true;
            } else {
                trackUizaEvent(TrackingEvent.E_PLAY_THROUGH_75);
            }
        } else if (percent >= Constants.PLAYTHROUGH_50) {
            if (isTracked50) {
                return;
            }
            if (isEventTracked(context, TrackingEvent.E_PLAY_THROUGH_50)) {
                isTracked50 = true;
            } else {
                trackUizaEvent(TrackingEvent.E_PLAY_THROUGH_50);
            }
        } else if (percent >= Constants.PLAYTHROUGH_25) {
            if (isTracked25) {
                return;
            }
            if (isEventTracked(context, TrackingEvent.E_PLAY_THROUGH_25)) {
                isTracked25 = true;
            } else {
                trackUizaEvent(TrackingEvent.E_PLAY_THROUGH_25);
            }
        }
    }

    public void trackUizaEvent(@TrackingEvent final String event) {
        if (isEventTracked(context, event)) {
            return;
        }
        @VideoTrackingEvent String videoTrackingEvent = VideoTrackingEvent.EVENT_TYPE_PLAY_THROUGH;
        String playThrough = "0";
        switch (event) {
            case TrackingEvent.E_DISPLAY:
                videoTrackingEvent = VideoTrackingEvent.EVENT_TYPE_DISPLAY;
                break;
            case TrackingEvent.E_PLAYS_REQUESTED:
                videoTrackingEvent = VideoTrackingEvent.EVENT_TYPE_PLAYS_REQUESTED;
                break;
            case TrackingEvent.E_VIDEO_STARTS:
                videoTrackingEvent = VideoTrackingEvent.EVENT_TYPE_VIDEO_STARTS;
                break;
            case TrackingEvent.E_VIEW:
                videoTrackingEvent = VideoTrackingEvent.EVENT_TYPE_VIEW;
                break;
            case TrackingEvent.E_PLAY_THROUGH_25:
                playThrough = "25";
                break;
            case TrackingEvent.E_PLAY_THROUGH_50:
                playThrough = "50";
                break;
            case TrackingEvent.E_PLAY_THROUGH_75:
                playThrough = "75";
                break;
            case TrackingEvent.E_PLAY_THROUGH_100:
                playThrough = "100";
                break;
            default:
                break;

        }
        callApiTrackUiza(createTrackingInput(context, playThrough, videoTrackingEvent),
                new UizaTrackingCallback() {
                    @Override
                    public void onTrackingSuccess() {
                        markEventTracked(context, event, true);
                    }
                });
    }

    private UizaTracking createTrackingInput(Context context, String playThrough,
            @VideoTrackingEvent String eventType) {
        if (context == null) {
            return null;
        }
        UizaTracking.Builder builder = new UizaTracking.Builder();
        builder.appId(UzPlayerData.getInstance().getAppId()).pageType(PAGE_TYPE)
                .viewerUserId(UzOsUtil.getDeviceId(context)).userAgent(Constants.USER_AGENT)
                .referrer(TmpParamData.getInstance().getReferrer())
                .deviceId(UzOsUtil.getDeviceId(context))
                .timestamp(UzDateTimeUtil.getCurrent(UzDateTimeUtil.FORMAT_1))
                .playerId(String.valueOf(UzPlayerData.getInstance().getCurrentSkinRes()))
                .playerName(Constants.PLAYER_NAME)
                .playerVersion(Constants.PLAYER_SDK_VERSION);
        UzLinkPlayData uzLinkPlayData = UzPlayerData.getInstance().getUzLinkPlayData();
        if (uzLinkPlayData == null || uzLinkPlayData.getVideoData() == null) {
            builder.entityId(NULL).entityName(NULL);
        } else {
            builder.entityId(uzLinkPlayData.getVideoData().getId())
                    .entityName(uzLinkPlayData.getVideoData().getName());
        }
        builder.entitySeries(TmpParamData.getInstance().getEntitySeries())
                .entityProducer(TmpParamData.getInstance().getEntityProducer())
                .entityContentType(TmpParamData.getInstance().getEntityContentType())
                .entityLanguageCode(TmpParamData.getInstance().getEntityLanguageCode())
                .entityVariantName(TmpParamData.getInstance().getEntityVariantName())
                .entityVariantId(TmpParamData.getInstance().getEntityVariantId())
                .entityDuration(TmpParamData.getInstance().getEntityDuration())
                .entityStreamType(TmpParamData.getInstance().getEntityStreamType())
                .entityEncodingVariant(TmpParamData.getInstance().getEntityEncodingVariant())
                .entityCdn(TmpParamData.getInstance().getEntityCnd())
                .playThrough(playThrough)
                .eventType(eventType);
        return builder.build();
    }

    private void callApiTrackUiza(final UizaTracking uizaTracking,
            final EventTrackingManager.UizaTrackingCallback uizaTrackingCallback) {
        UzServiceApi service = UzRestClientTracking.createService(UzServiceApi.class);
        UzApiMaster.getInstance()
                .subscribe(service.track(uizaTracking), new ApiSubscriber<Object>() {
                    @Override
                    public void onSuccess(Object tracking) {
                        if (uizaTrackingCallback != null) {
                            uizaTrackingCallback.onTrackingSuccess();
                        }
                    }

                    @Override
                    public void onFail(Throwable e) {
                        //TODO iplm if track fail
                    }
                });
    }

    private SharedPreferences getPrivatePreference(Context context) {
        return context.getSharedPreferences(PREFERENCES_FILE_NAME, 0);
    }

    public interface UizaTrackingCallback {

        void onTrackingSuccess();
    }
}
