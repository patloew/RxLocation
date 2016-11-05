package com.patloew.rxlocation;

import android.app.PendingIntent;
import android.support.annotation.CallSuper;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionApi;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.GeofencingApi;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.SettingsApi;

import org.mockito.Matchers;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.reflect.Whitebox;

import io.reactivex.FlowableEmitter;
import io.reactivex.MaybeEmitter;
import io.reactivex.SingleEmitter;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

public abstract class BaseOnSubscribeTest extends BaseTest {

    @Mock GoogleApiClient apiClient;
    @Mock Status status;
    @Mock ConnectionResult connectionResult;
    @Mock PendingResult pendingResult;
    @Mock PendingIntent pendingIntent;

    @Mock FusedLocationProviderApi fusedLocationProviderApi;
    @Mock ActivityRecognitionApi activityRecognitionApi;
    @Mock GeofencingApi geofencingApi;
    @Mock SettingsApi settingsApi;

    @CallSuper
    public void setup() throws Exception {
        PowerMockito.mockStatic(LocationServices.class);
        PowerMockito.mockStatic(ActivityRecognition.class);
        Whitebox.setInternalState(LocationServices.class, fusedLocationProviderApi);
        Whitebox.setInternalState(LocationServices.class, geofencingApi);
        Whitebox.setInternalState(LocationServices.class, settingsApi);
        Whitebox.setInternalState(ActivityRecognition.class, activityRecognitionApi);

        doReturn(status).when(status).getStatus();

        super.setup();
    }

    // Mock GoogleApiClient connection success behaviour
    protected <T> void setupBaseFlowableSuccess(final RxLocationFlowableOnSubscribe<T> rxLocationFlowableOnSubscribe) {
        setupBaseFlowableSuccess(rxLocationFlowableOnSubscribe, apiClient);
    }

    // Mock GoogleApiClient connection success behaviour
    protected <T> void setupBaseFlowableSuccess(final RxLocationFlowableOnSubscribe<T> rxLocationFlowableOnSubscribe, final GoogleApiClient apiClient) {
        doAnswer(invocation -> {
            final FlowableEmitter<T> subscriber = ((RxLocationFlowableOnSubscribe.ApiClientConnectionCallbacks)invocation.getArguments()[0]).emitter;

            doAnswer(invocation1 -> {
                rxLocationFlowableOnSubscribe.onGoogleApiClientReady(apiClient, subscriber);
                return null;
            }).when(apiClient).connect();

            return apiClient;
        }).when(rxLocationFlowableOnSubscribe).createApiClient(Matchers.any(RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class));
    }

    // Mock GoogleApiClient connection success behaviour
    protected <T> void setupBaseSingleSuccess(final RxLocationSingleOnSubscribe<T> rxLocationSingleOnSubscribe) {
        setupBaseSingleSuccess(rxLocationSingleOnSubscribe, apiClient);
    }

    // Mock GoogleApiClient connection success behaviour
    protected <T> void setupBaseSingleSuccess(final RxLocationSingleOnSubscribe<T> rxLocationSingleOnSubscribe, final GoogleApiClient apiClient) {
        doAnswer(invocation -> {
            final SingleEmitter<T> subscriber = ((RxLocationSingleOnSubscribe.ApiClientConnectionCallbacks)invocation.getArguments()[0]).emitter;

            doAnswer(invocation1 -> {
                rxLocationSingleOnSubscribe.onGoogleApiClientReady(apiClient, subscriber);
                return null;
            }).when(apiClient).connect();

            return apiClient;
        }).when(rxLocationSingleOnSubscribe).createApiClient(Matchers.any(RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class));
    }

    // Mock GoogleApiClient connection success behaviour
    protected <T> void setupBaseMaybeSuccess(final RxLocationMaybeOnSubscribe<T> baseSingle) {
        setupBaseMaybeSuccess(baseSingle, apiClient);
    }

    // Mock GoogleApiClient connection success behaviour
    protected <T> void setupBaseMaybeSuccess(final RxLocationMaybeOnSubscribe<T> baseSingle, final GoogleApiClient apiClient) {
        doAnswer(invocation -> {
            final MaybeEmitter<T> subscriber = ((RxLocationMaybeOnSubscribe.ApiClientConnectionCallbacks)invocation.getArguments()[0]).emitter;

            doAnswer(invocation1 -> {
                baseSingle.onGoogleApiClientReady(apiClient, subscriber);
                return null;
            }).when(apiClient).connect();

            return apiClient;
        }).when(baseSingle).createApiClient(Matchers.any(RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class));
    }

    // Mock GoogleApiClient connection error behaviour
    protected <T> void setupBaseSingleError(final RxLocationSingleOnSubscribe<T> rxLocationSingleOnSubscribe) {
        doAnswer(invocation -> {
            final SingleEmitter<T> subscriber = ((RxLocationSingleOnSubscribe.ApiClientConnectionCallbacks)invocation.getArguments()[0]).emitter;

            doAnswer(invocation1 -> {
                subscriber.onError(new GoogleAPIConnectionException("Error connecting to GoogleApiClient.", connectionResult));
                return null;
            }).when(apiClient).connect();

            return apiClient;
        }).when(rxLocationSingleOnSubscribe).createApiClient(Matchers.any(RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks.class));
    }

    @SuppressWarnings("unchecked")
    protected void setPendingResultValue(final Result result) {
        doAnswer(invocation -> {
            ((ResultCallback)invocation.getArguments()[0]).onResult(result);
            return null;
        }).when(pendingResult).setResultCallback(Matchers.<ResultCallback>any());
    }

    protected static void assertError(TestObserver sub, Class<? extends Throwable> errorClass) {
        sub.assertError(errorClass);
        sub.assertNoValues();
    }

    @SuppressWarnings("unchecked")
    protected static void assertSingleValue(TestObserver sub, Object value) {
        sub.assertComplete();
        sub.assertValue(value);
    }

    protected static void assertNoValue(TestObserver sub) {
        sub.assertComplete();
        sub.assertNoValues();
    }
}
