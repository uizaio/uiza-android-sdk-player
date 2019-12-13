package vn.uiza.restapi.uiza.model.v2.getlinkplay;

import com.squareup.moshi.Json;

import java.util.List;

import vn.uiza.restapi.uiza.model.v2.getlinkdownload.Hl;
import vn.uiza.restapi.uiza.model.v2.getlinkdownload.HlsT;
import vn.uiza.restapi.uiza.model.v2.getlinkdownload.Mpd;

/**
 * Created by LENOVO on 4/13/2018.
 */

public class GetLinkPlay {

    @Json(name = "message")
    private String message;
    @Json(name = "hls")
    private List<Hl> hls = null;
    @Json(name = "hls_ts")
    private List<HlsT> hlsTs = null;
    @Json(name = "hevc")
    private List<Object> hevc = null;
    @Json(name = "mpd")
    private List<Mpd> mpd = null;
    @Json(name = "version")
    private int version;
    @Json(name = "datetime")
    private String datetime;
    @Json(name = "name")
    private String name;
    @Json(name = "code")
    private int code;
    @Json(name = "type")
    private String type;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

    public List<Object> getHevc() {
        return hevc;
    }

    public void setHevc(List<Object> hevc) {
        this.hevc = hevc;
    }

    public List<Mpd> getMpd() {
        return mpd;
    }

    public void setMpd(List<Mpd> mpd) {
        this.mpd = mpd;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}