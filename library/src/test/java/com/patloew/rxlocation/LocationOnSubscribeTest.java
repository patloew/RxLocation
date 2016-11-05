package com.patloew.rxlocation;

import android.app.PendingIntent;
import android.location.Location;
import android.os.Looper;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SuppressWarnings("MissingPermission")
@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ LocationRequest.class, LocationAvailability.class, LocationServices.class, com.google.android.gms.location.ActivityRecognition.class, Status.class, ConnectionResult.class, RxLocationBaseOnSubscribe.class })
public class LocationOnSubscribeTest extends BaseOnSubscribeTest {

    @Mock PendingIntent pendingIntent;
    @Mock LocationRequest locationRequest;
    @Mock Location location;

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setup();
    }

    // LocationAvailabilitySingle

    @Test
    public void LocationAvailabilitySingle_Success() {
        LocationAvailabilitySingleOnSubscribe single = PowerMockito.spy(new LocationAvailabilitySingleOnSubscribe(rxLocation));

        LocationAvailability locationAvailability = Mockito.mock(LocationAvailability.class);
        doReturn(true).when(locationAvailability).isLocationAvailable();
        when(fusedLocationProviderApi.getLocationAvailability(apiClient)).thenReturn(locationAvailability);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), true);
    }

    @Test
    public void LocationAvailabilitySingle_Null_False() {
        LocationAvailabilitySingleOnSubscribe single = PowerMockito.spy(new LocationAvailabilitySingleOnSubscribe(rxLocation));

        when(fusedLocationProviderApi.getLocationAvailability(apiClient)).thenReturn(null);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), false);
    }

    // LocationFlushSingle

    @Test
    public void LocationFlushSingle_Success() {
        LocationFlushSingleOnSubscribe single = PowerMockito.spy(new LocationFlushSingleOnSubscribe(rxLocation, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(fusedLocationProviderApi.flushLocations(apiClient)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void LocationFlushSingle_StatusException() {
        LocationFlushSingleOnSubscribe single = PowerMockito.spy(new LocationFlushSingleOnSubscribe(rxLocation, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(fusedLocationProviderApi.flushLocations(apiClient)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    // LocationLastMaybe

    @Test
    public void LocationLastMaybe_Success() {
        LocationLastMaybeOnSubscribe maybe = PowerMockito.spy(new LocationLastMaybeOnSubscribe(rxLocation));

        Location location = Mockito.mock(Location.class);
        when(fusedLocationProviderApi.getLastLocation(apiClient)).thenReturn(location);

        setupBaseMaybeSuccess(maybe);

        assertSingleValue(Maybe.create(maybe).test(), location);
    }

    @Test
    public void LocationLastMaybe_Null_False() {
        LocationLastMaybeOnSubscribe maybe = PowerMockito.spy(new LocationLastMaybeOnSubscribe(rxLocation));

        when(fusedLocationProviderApi.getLocationAvailability(apiClient)).thenReturn(null);

        setupBaseMaybeSuccess(maybe);

        assertNoValue(Maybe.create(maybe).test());
    }

    // LocationRemoveUpdatesSingle

    @Test
    public void LocationRemoveUpdatesSingle_Success() {
        LocationRemoveUpdatesSingleOnSubscribe single = PowerMockito.spy(new LocationRemoveUpdatesSingleOnSubscribe(rxLocation, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(fusedLocationProviderApi.removeLocationUpdates(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void LocationRemoveUpdatesSingle_StatusException() {
        LocationRemoveUpdatesSingleOnSubscribe single = PowerMockito.spy(new LocationRemoveUpdatesSingleOnSubscribe(rxLocation, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(fusedLocationProviderApi.removeLocationUpdates(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    // LocationRequestUpdatesSingle

    @Test
    public void LocationRequestUpdatesSingle_Success() {
        LocationRequestUpdatesSingleOnSubscribe single = PowerMockito.spy(new LocationRequestUpdatesSingleOnSubscribe(rxLocation, locationRequest, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(fusedLocationProviderApi.requestLocationUpdates(apiClient, locationRequest, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void LocationRequestUpdatesSingle_StatusException() {
        LocationRequestUpdatesSingleOnSubscribe single = PowerMockito.spy(new LocationRequestUpdatesSingleOnSubscribe(rxLocation, locationRequest, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(fusedLocationProviderApi.requestLocationUpdates(apiClient, locationRequest, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    // LocationUpdatesFlowable

    @Test
    public void LocationUpdatesFlowable_Success() {
        LocationUpdatesFlowableOnSubscribe single = PowerMockito.spy(new LocationUpdatesFlowableOnSubscribe(rxLocation, locationRequest, null, null, null));

        setPendingResultValue(status);

        doReturn(true).when(status).isSuccess();
        doAnswer(invocation -> {
            single.locationListener.onLocationChanged(location);
            return pendingResult;
        }).when(fusedLocationProviderApi).requestLocationUpdates(eq(apiClient), eq(locationRequest), any(LocationListener.class), isNull(Looper.class));

        setupBaseFlowableSuccess(single);

        Flowable.create(single, BackpressureStrategy.BUFFER).test()
                .assertValue(location)
                .assertNotTerminated();

    }

    @Test
    public void LocationUpdatesFlowable_StatusException() {
        LocationUpdatesFlowableOnSubscribe single = PowerMockito.spy(new LocationUpdatesFlowableOnSubscribe(rxLocation, locationRequest, null, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        doReturn(pendingResult).when(fusedLocationProviderApi).requestLocationUpdates(eq(apiClient), eq(locationRequest), any(LocationListener.class), isNull(Looper.class));

        setupBaseFlowableSuccess(single);

        Flowable.create(single, BackpressureStrategy.BUFFER).test()
                .assertNoValues()
                .assertError(StatusException.class);
    }

}
