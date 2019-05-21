package vn.uiza.utils;

import android.content.Context;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import rx.Observable;
import vn.uiza.restapi.UZAPIMaster;
import vn.uiza.restapi.restclient.UZRestClient;
import vn.uiza.restapi.uiza.UZService;
import vn.uiza.restapi.uiza.model.v3.livestreaming.retrievealive.ResultRetrieveALive;
import vn.uiza.restapi.uiza.model.v3.metadata.getdetailofmetadata.Data;
import vn.uiza.restapi.uiza.model.v3.videoondeman.retrieveanentity.ResultRetrieveAnEntity;
import vn.uiza.rxandroid.ApiSubscriber;

import static org.mockito.ArgumentMatchers.any;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ UZRestClient.class, UZAPIMaster.class })
public class UZUtilBaseTest {

    @Mock
    Context context;
    @Mock
    CallbackGetDetailEntity callbackGetDetailEntity;

    private String entityId = "entityId";
    private String appId = "appId";
    private final String API_VERSION_3 = "v3";

    @Before
    public void setUp() {
        PowerMockito.mockStatic(UZRestClient.class, UZAPIMaster.class);
    }

    @Test
    public void test_getDetailEntity_success_with_data() {
        Data mockData = Mockito.mock(Data.class);
        String mockDataId = "None-Empty-Id";

        ArgumentCaptor<Observable> arg1 = ArgumentCaptor.forClass(Observable.class);
        ArgumentCaptor<ApiSubscriber> arg2 = ArgumentCaptor.forClass(ApiSubscriber.class);

        // For correct result
        ResultRetrieveAnEntity result = Mockito.mock(ResultRetrieveAnEntity.class);
        Mockito.when(result.getData()).thenReturn(mockData);
        Mockito.when(result.getData().getId()).thenReturn(mockDataId);

        // When
        PowerMockito.when(UZRestClient.createService(UZService.class))
                .thenReturn(Mockito.mock(UZService.class));
        PowerMockito.when(UZAPIMaster.getInstance()).thenReturn(Mockito.mock(UZAPIMaster.class));

        // Given
        UZUtilBase.getDetailEntity(context, API_VERSION_3, entityId, appId,
                callbackGetDetailEntity);

        // Then
        PowerMockito.verifyStatic(UZRestClient.class);
        UZRestClient.createService(UZService.class);

        Mockito.verify(UZAPIMaster.getInstance()).subscribe(arg1.capture(), arg2.capture());
        arg2.getValue().onSuccess(result);
        Mockito.verify(callbackGetDetailEntity).onSuccess(result.getData());
    }

    @Test
    public void test_getDetailEntity_success_wrongData_then_getDataFromEntityIdLIVE() {
        ArgumentCaptor<Observable> arg1 = ArgumentCaptor.forClass(Observable.class);
        ArgumentCaptor<ApiSubscriber> arg2 = ArgumentCaptor.forClass(ApiSubscriber.class);

        // For incorrect result (null ResultRetrieveAnEntity)
        ResultRetrieveAnEntity result = Mockito.mock(ResultRetrieveAnEntity.class);
        Mockito.when(result.getData()).thenReturn(null);

        // When
        PowerMockito.when(UZRestClient.createService(UZService.class))
                .thenReturn(Mockito.mock(UZService.class));
        PowerMockito.when(UZAPIMaster.getInstance()).thenReturn(Mockito.mock(UZAPIMaster.class));

        // Given
        UZUtilBase.getDetailEntity(context, API_VERSION_3, entityId, appId,
                callbackGetDetailEntity);

        // Then
        PowerMockito.verifyStatic(UZRestClient.class);
        UZRestClient.createService(UZService.class);

        Mockito.verify(UZAPIMaster.getInstance()).subscribe(arg1.capture(), arg2.capture());
        arg2.getValue().onSuccess(result);
        Mockito.verify(callbackGetDetailEntity, Mockito.never()).onSuccess(result.getData());

        PowerMockito.verifyStatic(UZUtilBase.class);
        UZUtilBase.getDataFromEntityIdLIVE(context, API_VERSION_3, entityId, appId,
                callbackGetDetailEntity);
    }

    @Test
    public void test_getDetailEntity_error() {
        ArgumentCaptor<Observable> arg1 = ArgumentCaptor.forClass(Observable.class);
        ArgumentCaptor<ApiSubscriber> arg2 = ArgumentCaptor.forClass(ApiSubscriber.class);

        // For error result
        Throwable error = Mockito.mock(Throwable.class);

        // When
        PowerMockito.when(UZRestClient.createService(UZService.class))
                .thenReturn(Mockito.mock(UZService.class));
        PowerMockito.when(UZAPIMaster.getInstance()).thenReturn(Mockito.mock(UZAPIMaster.class));

        // Given
        UZUtilBase.getDetailEntity(context, API_VERSION_3, entityId, appId,
                callbackGetDetailEntity);

        // Then
        PowerMockito.verifyStatic(UZRestClient.class);
        UZRestClient.createService(UZService.class);

        Mockito.verify(UZAPIMaster.getInstance()).subscribe(arg1.capture(), arg2.capture());
        arg2.getValue().onError(error);
        Mockito.verify(callbackGetDetailEntity).onError(error);
    }

    @Test
    public void test_getDataFromEntityIdLIVE_success_with_data() {
        Data mockData = Mockito.mock(Data.class);
        String mockDataId = "None-Empty-Id";

        ArgumentCaptor<Observable> arg1 = ArgumentCaptor.forClass(Observable.class);
        ArgumentCaptor<ApiSubscriber> arg2 = ArgumentCaptor.forClass(ApiSubscriber.class);

        // For correct result
        ResultRetrieveALive result = Mockito.mock(ResultRetrieveALive.class);
        Mockito.when(result.getData()).thenReturn(mockData);
        Mockito.when(result.getData().getId()).thenReturn(mockDataId);

        // When
        PowerMockito.when(UZRestClient.createService(UZService.class))
                .thenReturn(Mockito.mock(UZService.class));
        PowerMockito.when(UZAPIMaster.getInstance()).thenReturn(Mockito.mock(UZAPIMaster.class));

        // Given
        UZUtilBase.getDataFromEntityIdLIVE(context, API_VERSION_3, entityId, appId,
                callbackGetDetailEntity);

        // Then
        PowerMockito.verifyStatic(UZRestClient.class);
        UZRestClient.createService(UZService.class);

        Mockito.verify(UZAPIMaster.getInstance()).subscribe(arg1.capture(), arg2.capture());
        arg2.getValue().onSuccess(result);
        Mockito.verify(callbackGetDetailEntity).onSuccess(result.getData());
    }

    @Test
    public void test_getDataFromEntityIdLIVE_success_with_wrongData_then_error() {
        ArgumentCaptor<Observable> arg1 = ArgumentCaptor.forClass(Observable.class);
        ArgumentCaptor<ApiSubscriber> arg2 = ArgumentCaptor.forClass(ApiSubscriber.class);

        // For incorrect result
        ResultRetrieveALive result = Mockito.mock(ResultRetrieveALive.class);
        Mockito.when(result.getData()).thenReturn(null);

        // When
        PowerMockito.when(UZRestClient.createService(UZService.class))
                .thenReturn(Mockito.mock(UZService.class));
        PowerMockito.when(UZAPIMaster.getInstance()).thenReturn(Mockito.mock(UZAPIMaster.class));

        // Given
        UZUtilBase.getDataFromEntityIdLIVE(context, API_VERSION_3, entityId, appId,
                callbackGetDetailEntity);

        // Then
        PowerMockito.verifyStatic(UZRestClient.class);
        UZRestClient.createService(UZService.class);

        Mockito.verify(UZAPIMaster.getInstance()).subscribe(arg1.capture(), arg2.capture());
        arg2.getValue().onSuccess(result);
        Mockito.verify(callbackGetDetailEntity).onError(any(Exception.class));
    }

    @Test
    public void test_getDataFromEntityIdLIVE_error() {
        ArgumentCaptor<Observable> arg1 = ArgumentCaptor.forClass(Observable.class);
        ArgumentCaptor<ApiSubscriber> arg2 = ArgumentCaptor.forClass(ApiSubscriber.class);

        // For error result
        Throwable error = Mockito.mock(Throwable.class);

        // When
        PowerMockito.when(UZRestClient.createService(UZService.class))
                .thenReturn(Mockito.mock(UZService.class));
        PowerMockito.when(UZAPIMaster.getInstance()).thenReturn(Mockito.mock(UZAPIMaster.class));

        // Given
        UZUtilBase.getDataFromEntityIdLIVE(context, API_VERSION_3, entityId, appId,
                callbackGetDetailEntity);

        // Then
        PowerMockito.verifyStatic(UZRestClient.class);
        UZRestClient.createService(UZService.class);

        Mockito.verify(UZAPIMaster.getInstance()).subscribe(arg1.capture(), arg2.capture());
        arg2.getValue().onError(error);
        Mockito.verify(callbackGetDetailEntity).onError(error);
    }
}
