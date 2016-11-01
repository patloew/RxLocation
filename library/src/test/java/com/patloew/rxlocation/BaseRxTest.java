package com.patloew.rxlocation;

import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationServices;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ ContextCompat.class, Status.class, LocationServices.class, ActivityRecognition.class, ConnectionResult.class, GoogleApiClient.Builder.class })
public class BaseRxTest extends BaseOnSubscribeTest {

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setup();
    }

    @Test
    public void setupFitnessPendingResult_NoTimeout() {
        BaseRx<Object> baseRx = spy(new BaseRx<Object>(rxLocation, null, null) { });

        ResultCallback resultCallback = Mockito.mock(ResultCallback.class);

        baseRx.setupLocationPendingResult(pendingResult, resultCallback);

        verify(pendingResult).setResultCallback(resultCallback);
    }

    @Test
    public void setupFitnessPendingResult_Timeout() {
        BaseRx<Object> baseRx = spy(new BaseRx<Object>(rxLocation, TIMEOUT_TIME, TIMEOUT_TIMEUNIT) { });

        ResultCallback resultCallback = Mockito.mock(ResultCallback.class);

        baseRx.setupLocationPendingResult(pendingResult, resultCallback);

        verify(pendingResult).setResultCallback(resultCallback, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);
    }

    @Test
    public void createApiClient_NoScopes() {
        GoogleApiClient.Builder builder = Mockito.mock(GoogleApiClient.Builder.class);

        BaseRx<Object> baseRx = spy(new BaseRx<Object>(ctx, new Api[]{ LocationServices.API }, null) { });

        doReturn(builder).when(baseRx).getApiClientBuilder();
        doReturn(apiClient).when(builder).build();

        BaseRx.ApiClientConnectionCallbacks callbacks = Mockito.mock(BaseRx.ApiClientConnectionCallbacks.class);

        assertEquals(apiClient, baseRx.createApiClient(callbacks));
        verify(builder).addApi(LocationServices.API);
        verify(builder).addConnectionCallbacks(callbacks);
        verify(builder).addOnConnectionFailedListener(callbacks);
        verify(builder, never()).addScope(Matchers.any(Scope.class));
        verify(callbacks).setClient(Matchers.any(GoogleApiClient.class));
    }

    @Test
    public void createApiClient_Scopes() {
        GoogleApiClient.Builder builder = Mockito.mock(GoogleApiClient.Builder.class);

        Scope scope = new Scope("Test");
        BaseRx<Object> baseRx = spy(new BaseRx<Object>(ctx, new Api[]{ LocationServices.API }, new Scope[]{ scope } ) { });

        doReturn(builder).when(baseRx).getApiClientBuilder();
        doReturn(apiClient).when(builder).build();

        BaseRx.ApiClientConnectionCallbacks callbacks = Mockito.mock(BaseRx.ApiClientConnectionCallbacks.class);

        assertEquals(apiClient, baseRx.createApiClient(callbacks));
        verify(builder).addApi(LocationServices.API);
        verify(builder).addScope(scope);
        verify(builder).addConnectionCallbacks(callbacks);
        verify(builder).addOnConnectionFailedListener(callbacks);
        verify(callbacks).setClient(Matchers.any(GoogleApiClient.class));
    }

}
