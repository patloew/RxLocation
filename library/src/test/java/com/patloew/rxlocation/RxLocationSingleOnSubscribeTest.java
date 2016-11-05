package com.patloew.rxlocation;

import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.LocationServices;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ ContextCompat.class, Status.class, LocationServices.class, ActivityRecognition.class, ConnectionResult.class, SingleEmitter.class })
public class RxLocationSingleOnSubscribeTest extends BaseOnSubscribeTest {

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setup();
    }

    @Test
    public void ApiClient_Connected() {
        final Object object = new Object();
        RxLocationSingleOnSubscribe<Object> single = spy(new RxLocationSingleOnSubscribe<Object>(ctx, new Api[] {}, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, SingleEmitter<? super Object> emitter) {
                emitter.onSuccess(object);
            }
        });

        doAnswer(invocation -> {
            RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks callbacks = invocation.getArgumentAt(0, RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class);
            callbacks.setClient(apiClient);
            callbacks.onConnected(null);
            return apiClient;
        }).when(single).createApiClient(Matchers.any(RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class));

        TestObserver<Object> sub = Single.create(single).test();

        sub.assertValue(object);
        sub.assertComplete();
    }

    @Test
    public void ApiClient_Connected_Dispose() {
        RxLocationSingleOnSubscribe<Object> single = spy(new RxLocationSingleOnSubscribe<Object>(ctx, new Api[] {}, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, SingleEmitter<? super Object> emitter) { }
        });

        doAnswer(invocation -> {
            RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks callbacks = invocation.getArgumentAt(0, RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class);
            callbacks.setClient(apiClient);
            callbacks.onConnected(null);
            return apiClient;
        }).when(single).createApiClient(Matchers.any(RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class));

        doReturn(true).when(apiClient).isConnected();

        Single.create(single).subscribe().dispose();

        verify(single).onUnsubscribed(apiClient);
        verify(apiClient).disconnect();
    }

    @Test
    public void ApiClient_ConnectionSuspended() {
        final Object object = new Object();
        RxLocationSingleOnSubscribe<Object> single = spy(new RxLocationSingleOnSubscribe<Object>(ctx, new Api[] {}, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, SingleEmitter<? super Object> emitter) {
                emitter.onSuccess(object);
            }
        });

        doAnswer(invocation -> {
            RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks callbacks = invocation.getArgumentAt(0, RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class);
            callbacks.setClient(apiClient);
            callbacks.onConnectionSuspended(0);
            return apiClient;
        }).when(single).createApiClient(Matchers.any(RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class));

        TestObserver<Object> sub = Single.create(single).test();

        sub.assertNoValues();
        sub.assertError(GoogleAPIConnectionSuspendedException.class);
    }

    @Test
    public void ApiClient_ConnectionFailed() {
        final Object object = new Object();
        RxLocationSingleOnSubscribe<Object> single = spy(new RxLocationSingleOnSubscribe<Object>(ctx, new Api[] {}, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, SingleEmitter<? super Object> emitter) {
                emitter.onSuccess(object);
            }
        });

        doReturn(false).when(connectionResult).hasResolution();

        doAnswer(invocation -> {
            RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks callbacks = invocation.getArgumentAt(0, RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class);
            callbacks.setClient(apiClient);
            callbacks.onConnectionFailed(connectionResult);
            return apiClient;
        }).when(single).createApiClient(Matchers.any(RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class));

        TestObserver<Object> sub = Single.create(single).test();

        sub.assertNoValues();
        sub.assertError(GoogleAPIConnectionException.class);
    }
}
