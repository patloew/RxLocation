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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.subscribers.TestSubscriber;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ ContextCompat.class, LocationServices.class, ActivityRecognition.class, Status.class, ConnectionResult.class })
public class RxLocationFlowableOnSubscribeTest extends BaseOnSubscribeTest {

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setup();
    }

    @Test
    public void BaseObservable_ApiClient_Connected() {
        final Object object = new Object();
        RxLocationFlowableOnSubscribe<Object> observable = spy(new RxLocationFlowableOnSubscribe<Object>(ctx, new Api[] {}, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, FlowableEmitter<? super Object> subscriber) {
                subscriber.onNext(object);
                subscriber.onComplete();
            }
        });

        doAnswer(invocation -> {
            RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks callbacks = invocation.getArgumentAt(0, RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class);
            callbacks.setClient(apiClient);
            callbacks.onConnected(null);
            return apiClient;
        }).when(observable).createApiClient(Matchers.any(RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class));

        TestSubscriber<Object> sub = Flowable.create(observable, BackpressureStrategy.MISSING).test();

        sub.assertValue(object);
        sub.assertComplete();
    }

    @Test
    public void BaseObservable_ApiClient_Connected_Dispose() {
        final Object object = new Object();
        RxLocationFlowableOnSubscribe<Object> observable = spy(new RxLocationFlowableOnSubscribe<Object>(ctx, new Api[] {}, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, FlowableEmitter<? super Object> subscriber) {
                subscriber.onNext(object);
            }
        });

        doAnswer(invocation -> {
            RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks callbacks = invocation.getArgumentAt(0, RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class);
            callbacks.setClient(apiClient);
            callbacks.onConnected(null);
            return apiClient;
        }).when(observable).createApiClient(Matchers.any(RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class));

        doReturn(true).when(apiClient).isConnected();

        Flowable.create(observable, BackpressureStrategy.MISSING).subscribe().dispose();

        verify(observable).onUnsubscribed(apiClient);
        verify(apiClient).disconnect();
    }

    @Test
    public void BaseObservable_ApiClient_ConnectionSuspended() {
        final Object object = new Object();
        RxLocationFlowableOnSubscribe<Object> observable = spy(new RxLocationFlowableOnSubscribe<Object>(ctx, new Api[] {}, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, FlowableEmitter<? super Object> subscriber) {
                subscriber.onNext(object);
                subscriber.onComplete();
            }
        });

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks callbacks = invocation.getArgumentAt(0, RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class);
                callbacks.setClient(apiClient);
                callbacks.onConnectionSuspended(0);
                return apiClient;
            }
        }).when(observable).createApiClient(Matchers.any(RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class));

        TestSubscriber<Object> sub = Flowable.create(observable, BackpressureStrategy.MISSING).test();

        sub.assertNoValues();
        sub.assertError(GoogleAPIConnectionSuspendedException.class);
    }

    @Test
    public void BaseObservable_ApiClient_ConnectionFailed() {
        final Object object = new Object();
        RxLocationFlowableOnSubscribe<Object> observable = spy(new RxLocationFlowableOnSubscribe<Object>(ctx, new Api[] {}, null) {
            @Override
            protected void onGoogleApiClientReady(GoogleApiClient apiClient, FlowableEmitter<? super Object> subscriber) {
                subscriber.onNext(object);
                subscriber.onComplete();
            }
        });

        doReturn(false).when(connectionResult).hasResolution();

        doAnswer(invocation -> {
            RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks callbacks = invocation.getArgumentAt(0, RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class);
            callbacks.setClient(apiClient);
            callbacks.onConnectionFailed(connectionResult);
            return apiClient;
        }).when(observable).createApiClient(Matchers.any(RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class));

        TestSubscriber<Object> sub = Flowable.create(observable, BackpressureStrategy.MISSING).test();

        sub.assertNoValues();
        sub.assertError(GoogleAPIConnectionException.class);
    }

}
