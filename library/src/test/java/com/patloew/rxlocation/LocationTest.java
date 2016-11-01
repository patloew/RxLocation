package com.patloew.rxlocation;

import android.app.PendingIntent;
import android.location.Location;
import android.os.Looper;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;

@SuppressWarnings("MissingPermission")
@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ Flowable.class, Maybe.class, Single.class, Looper.class, LocationRequest.class, LocationServices.class, com.google.android.gms.location.ActivityRecognition.class, Status.class, ConnectionResult.class })
public class LocationTest extends BaseTest {

    @Mock PendingIntent pendingIntent;
    @Mock LocationRequest locationRequest;
    @Mock Location location;
    @Mock Looper looper;

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.spy(Single.class);
        PowerMockito.spy(Maybe.class);
        PowerMockito.spy(Flowable.class);
        super.setup();
    }

    @Test
    public void Flush() throws Exception {
        ArgumentCaptor<LocationFlushSingle> captor = ArgumentCaptor.forClass(LocationFlushSingle.class);

        rxLocation.location().flush();
        rxLocation.location().flush(TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        LocationFlushSingle single = captor.getAllValues().get(0);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertTimeoutSet(single);
    }

    @Test
    public void LastLocation() throws Exception {
        ArgumentCaptor<LocationLastMaybe> captor = ArgumentCaptor.forClass(LocationLastMaybe.class);

        rxLocation.location().lastLocation();

        PowerMockito.verifyStatic(times(1));
        Maybe.create(captor.capture());

        LocationLastMaybe single = captor.getAllValues().get(0);
        assertNoTimeoutSet(single);
    }

    @Test
    public void LocationAvailable() throws Exception {
        ArgumentCaptor<LocationAvailabilitySingle> captor = ArgumentCaptor.forClass(LocationAvailabilitySingle.class);

        rxLocation.location().isLocationAvailable();

        PowerMockito.verifyStatic(times(1));
        Single.create(captor.capture());

        LocationAvailabilitySingle single = captor.getAllValues().get(0);
        assertNoTimeoutSet(single);
    }

    @Test
    public void RequestUpdates() throws Exception {
        ArgumentCaptor<LocationRequestUpdatesSingle> captor = ArgumentCaptor.forClass(LocationRequestUpdatesSingle.class);

        rxLocation.location().requestUpdates(locationRequest, pendingIntent);
        rxLocation.location().requestUpdates(locationRequest, pendingIntent, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        LocationRequestUpdatesSingle single = captor.getAllValues().get(0);
        assertEquals(locationRequest, single.locationRequest);
        assertEquals(pendingIntent, single.pendingIntent);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(locationRequest, single.locationRequest);
        assertEquals(pendingIntent, single.pendingIntent);
        assertTimeoutSet(single);
    }

    @Test
    public void RemoveUpdates() throws Exception {
        ArgumentCaptor<LocationRemoveUpdatesSingle> captor = ArgumentCaptor.forClass(LocationRemoveUpdatesSingle.class);

        rxLocation.location().removeUpdates(pendingIntent);
        rxLocation.location().removeUpdates(pendingIntent, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        LocationRemoveUpdatesSingle single = captor.getAllValues().get(0);
        assertEquals(pendingIntent, single.pendingIntent);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(pendingIntent, single.pendingIntent);
        assertTimeoutSet(single);
    }

    // Location Updates

    @Test
    public void LocationUpdates() throws Exception {
        ArgumentCaptor<LocationUpdatesFlowable> captor = ArgumentCaptor.forClass(LocationUpdatesFlowable.class);

        rxLocation.location().updates(locationRequest);
        rxLocation.location().updates(locationRequest, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Flowable.create(captor.capture(), eq(BackpressureStrategy.MISSING));

        LocationUpdatesFlowable single = captor.getAllValues().get(0);
        assertEquals(locationRequest, single.locationRequest);
        assertNull(single.looper);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(locationRequest, single.locationRequest);
        assertNull(single.looper);
        assertTimeoutSet(single);
    }

    @Test
    public void LocationUpdates_Looper() throws Exception {
        ArgumentCaptor<LocationUpdatesFlowable> captor = ArgumentCaptor.forClass(LocationUpdatesFlowable.class);

        rxLocation.location().updates(locationRequest, looper);
        rxLocation.location().updates(locationRequest, looper, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Flowable.create(captor.capture(), eq(BackpressureStrategy.MISSING));

        LocationUpdatesFlowable single = captor.getAllValues().get(0);
        assertEquals(locationRequest, single.locationRequest);
        assertEquals(looper, single.looper);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(locationRequest, single.locationRequest);
        assertEquals(looper, single.looper);
        assertTimeoutSet(single);
    }

    @Test
    public void LocationUpdates_BackpressureStrategy() throws Exception {
        ArgumentCaptor<LocationUpdatesFlowable> captor = ArgumentCaptor.forClass(LocationUpdatesFlowable.class);

        rxLocation.location().updates(locationRequest, BackpressureStrategy.LATEST);
        rxLocation.location().updates(locationRequest, TIMEOUT_TIME, TIMEOUT_TIMEUNIT, BackpressureStrategy.LATEST);

        PowerMockito.verifyStatic(times(2));
        Flowable.create(captor.capture(), eq(BackpressureStrategy.LATEST));

        LocationUpdatesFlowable single = captor.getAllValues().get(0);
        assertEquals(locationRequest, single.locationRequest);
        assertNull(single.looper);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(locationRequest, single.locationRequest);
        assertNull(single.looper);
        assertTimeoutSet(single);
    }

    @Test
    public void LocationUpdates_Looper_BackpressureStrategy() throws Exception {
        ArgumentCaptor<LocationUpdatesFlowable> captor = ArgumentCaptor.forClass(LocationUpdatesFlowable.class);

        rxLocation.location().updates(locationRequest, looper, BackpressureStrategy.LATEST);
        rxLocation.location().updates(locationRequest, looper, TIMEOUT_TIME, TIMEOUT_TIMEUNIT, BackpressureStrategy.LATEST);

        PowerMockito.verifyStatic(times(2));
        Flowable.create(captor.capture(), eq(BackpressureStrategy.LATEST));

        LocationUpdatesFlowable single = captor.getAllValues().get(0);
        assertEquals(locationRequest, single.locationRequest);
        assertEquals(looper, single.looper);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(locationRequest, single.locationRequest);
        assertEquals(looper, single.looper);
        assertTimeoutSet(single);
    }
}
