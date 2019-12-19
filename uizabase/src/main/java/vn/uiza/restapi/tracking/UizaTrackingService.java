package vn.uiza.restapi.tracking;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import vn.uiza.restapi.model.tracking.UizaTracking;
import vn.uiza.restapi.model.tracking.UizaTrackingCCU;
import vn.uiza.restapi.model.tracking.muiza.Muiza;

public interface UizaTrackingService {
    //@Headers("Content-Type: application/json")
    @POST("/analytic-tracking/v1/tracking/mobile")
    Observable<Object> track(@Body UizaTracking uizaTracking);

    //@Headers("Content-Type: application/json")
    @POST("/analytic-tracking/v1/ccu/mobile")
    Observable<Object> trackCCU(@Body UizaTrackingCCU uizaTrackingCCU);

    //@Headers("Content-Type: application/json")
    @POST("/analytic-tracking/v2/muiza/eventbulk/mobile")
    Observable<Object> trackMuiza(@Body List<Muiza> muizaList);
}
