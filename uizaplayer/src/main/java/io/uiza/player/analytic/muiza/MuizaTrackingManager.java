package io.uiza.player.analytic.muiza;

import android.content.Context;
import android.os.Build;
import io.uiza.core.api.client.UzRestClientTracking;
import io.uiza.core.api.request.tracking.muiza.Muiza;
import io.uiza.core.api.response.video.VideoData;
import io.uiza.core.exception.UzException;
import io.uiza.core.util.UzCoreUtil;
import io.uiza.core.util.UzDateTimeUtil;
import io.uiza.core.util.UzOsUtil;
import io.uiza.core.util.constant.Constants;
import io.uiza.player.analytic.TmpParamData;
import io.uiza.player.util.UzPlayerData;
import java.util.ArrayList;
import java.util.List;

public final class MuizaTrackingManager {

    private static final String PAGE_TYPE = "app";
    private static final String NULL = "null";
    private static final String ANDROID = "Android ";
    private static final String API_LEVEL = "Api level ";
    private List<Muiza> muizaList = new ArrayList<>();

    public void initTracking(String trackingDomain, String accessToken) {
        UzRestClientTracking.init(trackingDomain);
        UzRestClientTracking.addAccessToken(accessToken);
        UzPlayerData.setApiTrackEndPoint(UzCoreUtil.getContext(), trackingDomain);
    }

    public List<Muiza> getMuizaList() {
        return muizaList;
    }

    public boolean isMuizaListEmpty() {
        return muizaList.isEmpty();
    }

    public void clearMuizaList() {
        muizaList.clear();
    }

    public void addListTrackingMuiza(List<Muiza> muizas) {
        this.muizaList.addAll(muizas);
    }

    public void addTrackingMuiza(Context context, @MuizaEvent String event, UzException e) {
        if (context == null || event == null || event.isEmpty()) {
            return;
        }
        TmpParamData.getInstance().addPlayerSequenceNumber();
        TmpParamData.getInstance().addViewSequenceNumber();
        Muiza.Builder muizaBuilder = new Muiza.Builder();
        muizaBuilder.beaconDomain(UzPlayerData.getInstance().getTrackingDomain())
                .entityCdn(TmpParamData.getInstance().getEntityCnd())
                .entityContentType(TmpParamData.getInstance().getEntityContentType())
                .entityDuration(TmpParamData.getInstance().getEntityDuration())
                .entityEncodingVariant(TmpParamData.getInstance().getEntityEncodingVariant())
                .entityLanguageCode(TmpParamData.getInstance().getEntityLanguageCode());
        VideoData videoData = UzPlayerData.getInstance().getVideoData();
        if (videoData == null) {
            muizaBuilder.entityId(NULL).entityName(NULL);
        } else {
            muizaBuilder.entityId(videoData.getId()).entityName(videoData.getName());
        }
        muizaBuilder.entityPosterUrl(TmpParamData.getInstance().getEntityPosterUrl())
                .entityProducer(TmpParamData.getInstance().getEntityProducer())
                .entitySeries(TmpParamData.getInstance().getEntitySeries())
                .entitySourceDomain(TmpParamData.getInstance().getEntitySourceDomain())
                .entitySourceDuration(TmpParamData.getInstance().getEntitySourceDuration())
                .entitySourceHeight(TmpParamData.getInstance().getEntitySourceHeight())
                .entitySourceHostname(TmpParamData.getInstance().getEntitySourceHostname())
                .entitySourceIsLive(UzPlayerData.getInstance().isLivestream())
                .entitySourceMimeType(TmpParamData.getInstance().getEntitySourceMimeType())
                .entitySourceUrl(TmpParamData.getInstance().getEntitySourceUrl())
                .entitySourceWidth(TmpParamData.getInstance().getEntitySourceWidth())
                .entityStreamType(TmpParamData.getInstance().getEntityStreamType())
                .entityVariantId(TmpParamData.getInstance().getEntityVariantId())
                .entityVariantName(TmpParamData.getInstance().getEntityVariantName())
                .pageType(PAGE_TYPE)
                .pageUrl(TmpParamData.getInstance().getPageUrl())
                .playerAutoplayOn(TmpParamData.getInstance().isPlayerAutoPlayOn())
                .playerHeight(TmpParamData.getInstance().getPlayerHeight())
                .playerIsFullscreen(TmpParamData.getInstance().isPlayerIsFullscreen())
                .playerIsPaused(TmpParamData.getInstance().isPlayerIsPaused())
                .playerLanguageCode(TmpParamData.getInstance().getPlayerLanguageCode())
                .playerName(Constants.PLAYER_NAME)
                .playerPlayheadTime(TmpParamData.getInstance().getPlayerPlayHeadTime())
                .playerPreloadOn(TmpParamData.getInstance().getPlayerPreloadOn())
                .playerSequenceNumber(TmpParamData.getInstance().getPlayerSequenceNumber())
                .playerSoftwareName(TmpParamData.getInstance().getPlayerSoftwareName())
                .playerSoftwareVersion(TmpParamData.getInstance().getPlayerSoftwareVersion())
                .playerVersion(Constants.PLAYER_SDK_VERSION)
                .playerWidth(TmpParamData.getInstance().getPlayerWidth())
                .sessionExpires(System.currentTimeMillis() + 5 * 60 * 1000)
                .sessionId(TmpParamData.getInstance().getSessionId())
                .timestamp(UzDateTimeUtil.getCurrent(UzDateTimeUtil.FORMAT_1))
                .viewId(UzOsUtil.getDeviceId(context))
                .viewSequenceNumber(TmpParamData.getInstance().getViewSequenceNumber())
                .viewerApplicationEngine(TmpParamData.getInstance().getViewerApplicationEngine())
                .viewerApplicationName(TmpParamData.getInstance().getViewerApplicationName())
                .viewerApplicationVersion(TmpParamData.getInstance().getViewerApplicationVersion())
                .viewerDeviceManufacturer(android.os.Build.MANUFACTURER)
                .viewerDeviceName(android.os.Build.MODEL)
                .viewerOsArchitecture(UzOsUtil.getViewerOsArchitecture())
                .viewerOsFamily(ANDROID + Build.VERSION.RELEASE)
                .viewerOsVersion(API_LEVEL + Build.VERSION.SDK_INT)
                .viewerTime(System.currentTimeMillis())
                .viewerUserId(UzOsUtil.getDeviceId(context))
                .appId(UzPlayerData.getInstance().getAppId())
                .referrer(TmpParamData.getInstance().getReferrer())
                .pageLoadTime(TmpParamData.getInstance().getPageLoadTime())
                .playerId(String.valueOf(UzPlayerData.getInstance().getCurrentSkinRes()))
                .playerInitTime(TmpParamData.getInstance().getPlayerInitTime())
                .playerStartupTime(TmpParamData.getInstance().getPlayerStartupTime())
                .sessionStart(TmpParamData.getInstance().getSessionStart())
                .playerViewCount(TmpParamData.getInstance().getPlayerViewCount())
                .viewStart(TmpParamData.getInstance().getViewStart())
                .viewWatchTime(TmpParamData.getInstance().getViewWatchTime())
                .viewTimeToFirstFrame(TmpParamData.getInstance().getViewTimeToFirstFrame())
                .viewAggregateStartupTime(TmpParamData.getInstance().getViewStart()
                        + TmpParamData.getInstance().getViewWatchTime())
                .viewAggregateStartupTotalTime(TmpParamData.getInstance().getViewTimeToFirstFrame()
                        + (TmpParamData.getInstance().getPlayerInitTime()
                        - TmpParamData.getInstance()
                        .getTimeFromInitEntityIdToAllApiCalledSuccess()))
                .event(event);
        switch (event) {
            case MuizaEvent.MUIZA_EVENT_ERROR:
                if (e != null) {
                    muizaBuilder.playerErrorCode(e.getErrorCode())
                            .playerErrorMessage(e.getMessage());
                }
                break;
            case MuizaEvent.MUIZA_EVENT_SEEKING:
            case MuizaEvent.MUIZA_EVENT_SEEKED:
                muizaBuilder.viewSeekCount(TmpParamData.getInstance().getViewSeekCount())
                        .viewSeekDuration(TmpParamData.getInstance().getViewSeekDuration())
                        .viewMaxSeekTime(TmpParamData.getInstance().getViewMaxSeekTime());
                break;
            case MuizaEvent.MUIZA_EVENT_REBUFFERSTART:
            case MuizaEvent.MUIZA_EVENT_REBUFFEREND:
                muizaBuilder.viewRebufferCount(TmpParamData.getInstance().getViewRebufferCount())
                        .viewRebufferDuration(TmpParamData.getInstance().getViewRebufferDuration());
                if (TmpParamData.getInstance().getViewWatchTime() == 0) {
                    muizaBuilder.viewRebufferFrequency(0f).viewRebufferPercentage(0f);
                } else {
                    muizaBuilder.viewRebufferFrequency(
                            ((float) TmpParamData.getInstance().getViewRebufferCount()
                                    / (float) TmpParamData.getInstance().getViewWatchTime()))
                            .viewRebufferPercentage(
                                    ((float) TmpParamData.getInstance().getViewRebufferDuration()
                                            / (float) TmpParamData.getInstance()
                                            .getViewWatchTime()));
                }
                break;
            default:
                break;
        }
        muizaList.add(muizaBuilder.build());
    }
}
