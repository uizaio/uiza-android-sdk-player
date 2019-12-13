
package vn.uiza.restapi.uiza.model.v2.getlinkdownload;

import com.squareup.moshi.Json;

import java.util.List;

public class Datum {

    @Json(name = "hls")
    private List<Hl> hls = null;
    @Json(name = "hls_ts")
    private List<HlsT> hlsTs = null;
    @Json(name = "hevc")
    private List<Hevc> hevc = null;
    @Json(name = "mpd")
    private List<Mpd> mpd = null;
    @Json(name = "entityInfo")
    private EntityInfo entityInfo;
    @Json(name = "download")
    private List<Download> download = null;

    public List<Hl> getHls() {
        return hls;
    }

    public void setHls(List<Hl> hls) {
        this.hls = hls;
    }

    public List<HlsT> getHlsTs() {
        return hlsTs;
    }

    public void setHlsTs(List<HlsT> hlsTs) {
        this.hlsTs = hlsTs;
    }

    public List<Hevc> getHevc() {
        return hevc;
    }

    public void setHevc(List<Hevc> hevc) {
        this.hevc = hevc;
    }

    public List<Mpd> getMpd() {
        return mpd;
    }

    public void setMpd(List<Mpd> mpd) {
        this.mpd = mpd;
    }

    public EntityInfo getEntityInfo() {
        return entityInfo;
    }

    public void setEntityInfo(EntityInfo entityInfo) {
        this.entityInfo = entityInfo;
    }

    public List<Download> getDownload() {
        return download;
    }

    public void setDownload(List<Download> download) {
        this.download = download;
    }

}
