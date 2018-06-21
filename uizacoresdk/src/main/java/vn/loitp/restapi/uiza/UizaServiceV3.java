package vn.loitp.restapi.uiza;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;
import vn.loitp.restapi.uiza.model.v3.UizaWorkspaceInfo;
import vn.loitp.restapi.uiza.model.v3.gettoken.ResultGetToken;

/**
 * @author loitp
 */

public interface UizaServiceV3 {
    //http://dev-docs.uizadev.io/#get-token
    //@FormUrlEncoded
    @POST("/api/public/v3/admin/user/auth")
    Observable<ResultGetToken> getToken(@Body UizaWorkspaceInfo uizaWorkspaceInfo);
}
