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
@PrepareOnlyThisForTest({ LocationRequest.class, LocationAvailability.class, LocationServices.class, com.google.android.gms.location.ActivityRecognition.class, Status.class, ConnectionResult.class, BaseRx.class })
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
        LocationAvailabilitySingle single = PowerMockito.spy(new LocationAvailabilitySingle(rxLocation));

        LocationAvailability locationAvailability = Mockito.mock(LocationAvailability.class);
        doReturn(true).when(locationAvailability).isLocationAvailable();
        when(fusedLocationProviderApi.getLocationAvailability(apiClient)).thenReturn(locationAvailability);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), true);
    }

    @Test
    public void LocationAvailabilitySingle_Null_False() {
        LocationAvailabilitySingle single = PowerMockito.spy(new LocationAvailabilitySingle(rxLocation));

        when(fusedLocationProviderApi.getLocationAvailability(apiClient)).thenReturn(null);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), false);
    }

    // LocationFlushSingle

    @Test
    public void LocationFlushSingle_Success() {
        LocationFlushSingle single = PowerMockito.spy(new LocationFlushSingle(rxLocation, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(fusedLocationProviderApi.flushLocations(apiClient)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void LocationFlushSingle_StatusException() {
        LocationFlushSingle single = PowerMockito.spy(new LocationFlushSingle(rxLocation, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(fusedLocationProviderApi.flushLocations(apiClient)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    // LocationLastMaybe

    @Test
    public void LocationLastMaybe_Success() {
        LocationLastMaybe maybe = PowerMockito.spy(new LocationLastMaybe(rxLocation));

        Location location = Mockito.mock(Location.class);
        when(fusedLocationProviderApi.getLastLocation(apiClient)).thenReturn(location);

        setupBaseMaybeSuccess(maybe);

        assertSingleValue(Maybe.create(maybe).test(), location);
    }

    @Test
    public void LocationLastMaybe_Null_False() {
        LocationLastMaybe maybe = PowerMockito.spy(new LocationLastMaybe(rxLocation));

        when(fusedLocationProviderApi.getLocationAvailability(apiClient)).thenReturn(null);

        setupBaseMaybeSuccess(maybe);

        assertNoValue(Maybe.create(maybe).test());
    }

    // LocationRemoveUpdatesSingle

    @Test
    public void LocationRemoveUpdatesSingle_Success() {
        LocationRemoveUpdatesSingle single = PowerMockito.spy(new LocationRemoveUpdatesSingle(rxLocation, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(fusedLocationProviderApi.removeLocationUpdates(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void LocationRemoveUpdatesSingle_StatusException() {
        LocationRemoveUpdatesSingle single = PowerMockito.spy(new LocationRemoveUpdatesSingle(rxLocation, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(fusedLocationProviderApi.removeLocationUpdates(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    // LocationRequestUpdatesSingle

    @Test
    public void LocationRequestUpdatesSingle_Success() {
        LocationRequestUpdatesSingle single = PowerMockito.spy(new LocationRequestUpdatesSingle(rxLocation, locationRequest, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(fusedLocationProviderApi.requestLocationUpdates(apiClient, locationRequest, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void LocationRequestUpdatesSingle_StatusException() {
        LocationRequestUpdatesSingle single = PowerMockito.spy(new LocationRequestUpdatesSingle(rxLocation, locationRequest, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(fusedLocationProviderApi.requestLocationUpdates(apiClient, locationRequest, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    // LocationUpdatesFlowable

    @Test
    public void LocationUpdatesFlowable_Success() {
        LocationUpdatesFlowable single = PowerMockito.spy(new LocationUpdatesFlowable(rxLocation, locationRequest, null, null, null));

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
        LocationUpdatesFlowable single = PowerMockito.spy(new LocationUpdatesFlowable(rxLocation, locationRequest, null, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        doReturn(pendingResult).when(fusedLocationProviderApi).requestLocationUpdates(eq(apiClient), eq(locationRequest), any(LocationListener.class), isNull(Looper.class));

        setupBaseFlowableSuccess(single);

        Flowable.create(single, BackpressureStrategy.BUFFER).test()
                .assertNoValues()
                .assertError(StatusException.class);
    }

}
