
package vn.uiza.restapi.uiza.model.v2.getlinkdownload;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Datum {

    @SerializedName("hls")
    private List<Hl> hls = null;
    @SerializedName("hls_ts")
    private List<HlsT> hlsTs = null;
    @SerializedName("hevc")
    private List<Hevc> hevc = null;
    @SerializedName("mpd")
    private List<Mpd> mpd = null;
    @SerializedName("entityInfo")
    private EntityInfo entityInfo;
    @SerializedName("download")
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
