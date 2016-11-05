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

import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.SingleEmitter;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ ContextCompat.class, Status.class, LocationServices.class, ActivityRecognition.class, ConnectionResult.class, SingleEmitter.class })
public class RxLocationMaybeOnSubscribeTest extends BaseOnSubscribeTest {

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setup();
    }

    @Test
    public void ApiClient_Connected() {
        final Object object = new Object();
        RxLocationMaybeOnSubscribe<Object> maybeOnSubscribe = spy(new RxLocationMaybeOnSubscribe<Object>(ctx, new Api[] {}, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, MaybeEmitter<? super Object> emitter) {
                emitter.onSuccess(object);
            }
        });

        doAnswer(invocation -> {
            RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks callbacks = invocation.getArgumentAt(0, RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class);
            callbacks.setClient(apiClient);
            callbacks.onConnected(null);
            return apiClient;
        }).when(maybeOnSubscribe).createApiClient(Matchers.any(RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class));

        TestObserver<Object> sub = Maybe.create(maybeOnSubscribe).test();

        sub.assertValue(object);
        sub.assertComplete();
    }


    @Test
    public void ApiClient_Connected_Dispose() {
        RxLocationMaybeOnSubscribe<Object> maybeOnSubscribe = spy(new RxLocationMaybeOnSubscribe<Object>(ctx, new Api[] {}, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, MaybeEmitter<? super Object> emitter) { }
        });

        doAnswer(invocation -> {
            RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks callbacks = invocation.getArgumentAt(0, RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class);
            callbacks.setClient(apiClient);
            callbacks.onConnected(null);
            return apiClient;
        }).when(maybeOnSubscribe).createApiClient(Matchers.any(RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class));

        doReturn(true).when(apiClient).isConnected();

        Maybe.create(maybeOnSubscribe).subscribe().dispose();

        verify(maybeOnSubscribe).onUnsubscribed(apiClient);
        verify(apiClient).disconnect();
    }

    @Test
    public void ApiClient_ConnectionSuspended() {
        final Object object = new Object();
        RxLocationMaybeOnSubscribe<Object> maybeOnSubscribe = spy(new RxLocationMaybeOnSubscribe<Object>(ctx, new Api[] {}, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, MaybeEmitter<? super Object> emitter) {
                emitter.onSuccess(object);
            }
        });

        doAnswer(invocation -> {
            RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks callbacks = invocation.getArgumentAt(0, RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class);
            callbacks.setClient(apiClient);
            callbacks.onConnectionSuspended(0);
            return apiClient;
        }).when(maybeOnSubscribe).createApiClient(Matchers.any(RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class));

        TestObserver<Object> sub = Maybe.create(maybeOnSubscribe).test();

        sub.assertNoValues();
        sub.assertError(GoogleAPIConnectionSuspendedException.class);
    }

    @Test
    public void ApiClient_ConnectionFailed() {
        final Object object = new Object();
        RxLocationMaybeOnSubscribe<Object> maybeOnSubscribe = spy(new RxLocationMaybeOnSubscribe<Object>(ctx, new Api[] {}, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, MaybeEmitter<? super Object> emitter) {
                emitter.onSuccess(object);
            }
        });

        doReturn(false).when(connectionResult).hasResolution();

        doAnswer(invocation -> {
            RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks callbacks = invocation.getArgumentAt(0, RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class);
            callbacks.setClient(apiClient);
            callbacks.onConnectionFailed(connectionResult);
            return apiClient;
        }).when(maybeOnSubscribe).createApiClient(Matchers.any(RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class));

        TestObserver<Object> sub = Maybe.create(maybeOnSubscribe).test();

        sub.assertNoValues();
        sub.assertError(GoogleAPIConnectionException.class);
    }
}
