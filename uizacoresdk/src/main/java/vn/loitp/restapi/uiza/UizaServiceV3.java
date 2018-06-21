package vn.loitp.restapi.uiza;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import rx.Observable;
import vn.loitp.restapi.uiza.model.v3.UizaWorkspaceInfo;
import vn.loitp.restapi.uiza.model.v3.createmetadata.CreateMetadata;
import vn.loitp.restapi.uiza.model.v3.getlistmetadata.ResultGetListMetadata;
import vn.loitp.restapi.uiza.model.v3.gettoken.ResultGetToken;

/**
 * @author loitp
 */

public interface UizaServiceV3 {
    //http://dev-docs.uizadev.io/#get-token
    @POST("/api/public/v3/admin/user/auth")
    Observable<ResultGetToken> getToken(@Body UizaWorkspaceInfo uizaWorkspaceInfo);

    //http://dev-docs.uizadev.io/#check-token
    @POST("/api/public/v3/admin/user/auth/check-token")
    Observable<ResultGetToken> checkToken();

    //http://dev-docs.uizadev.io/#get-list-metadata
    @GET("/api/public/v3/media/metadata")
    Observable<ResultGetListMetadata> getListMetadata();

    //http://dev-docs.uizadev.io/#create-metadata
    @POST("/api/public/v3/media/metadata")
    Observable<Object> createMetadata(@Body CreateMetadata createMetadata);
}
