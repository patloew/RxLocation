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
        ArgumentCaptor<LocationFlushSingleOnSubscribe> captor = ArgumentCaptor.forClass(LocationFlushSingleOnSubscribe.class);

        rxLocation.location().flush();
        rxLocation.location().flush(TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        LocationFlushSingleOnSubscribe single = captor.getAllValues().get(0);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertTimeoutSet(single);
    }

    @Test
    public void LastLocation() throws Exception {
        ArgumentCaptor<LocationLastMaybeOnSubscribe> captor = ArgumentCaptor.forClass(LocationLastMaybeOnSubscribe.class);

        rxLocation.location().lastLocation();

        PowerMockito.verifyStatic(times(1));
        Maybe.create(captor.capture());

        LocationLastMaybeOnSubscribe single = captor.getAllValues().get(0);
        assertNoTimeoutSet(single);
    }

    @Test
    public void LocationAvailable() throws Exception {
        ArgumentCaptor<LocationAvailabilitySingleOnSubscribe> captor = ArgumentCaptor.forClass(LocationAvailabilitySingleOnSubscribe.class);

        rxLocation.location().isLocationAvailable();

        PowerMockito.verifyStatic(times(1));
        Single.create(captor.capture());

        LocationAvailabilitySingleOnSubscribe single = captor.getAllValues().get(0);
        assertNoTimeoutSet(single);
    }

    @Test
    public void RequestUpdates() throws Exception {
        ArgumentCaptor<LocationRequestUpdatesSingleOnSubscribe> captor = ArgumentCaptor.forClass(LocationRequestUpdatesSingleOnSubscribe.class);

        rxLocation.location().requestUpdates(locationRequest, pendingIntent);
        rxLocation.location().requestUpdates(locationRequest, pendingIntent, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        LocationRequestUpdatesSingleOnSubscribe single = captor.getAllValues().get(0);
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
        ArgumentCaptor<LocationRemoveUpdatesSingleOnSubscribe> captor = ArgumentCaptor.forClass(LocationRemoveUpdatesSingleOnSubscribe.class);

        rxLocation.location().removeUpdates(pendingIntent);
        rxLocation.location().removeUpdates(pendingIntent, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        LocationRemoveUpdatesSingleOnSubscribe single = captor.getAllValues().get(0);
        assertEquals(pendingIntent, single.pendingIntent);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(pendingIntent, single.pendingIntent);
        assertTimeoutSet(single);
    }

    // Location Updates

    @Test
    public void LocationUpdates() throws Exception {
        ArgumentCaptor<LocationUpdatesFlowableOnSubscribe> captor = ArgumentCaptor.forClass(LocationUpdatesFlowableOnSubscribe.class);

        rxLocation.location().updates(locationRequest);
        rxLocation.location().updates(locationRequest, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Flowable.create(captor.capture(), eq(BackpressureStrategy.MISSING));

        LocationUpdatesFlowableOnSubscribe single = captor.getAllValues().get(0);
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
        ArgumentCaptor<LocationUpdatesFlowableOnSubscribe> captor = ArgumentCaptor.forClass(LocationUpdatesFlowableOnSubscribe.class);

        rxLocation.location().updates(locationRequest, looper);
        rxLocation.location().updates(locationRequest, looper, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Flowable.create(captor.capture(), eq(BackpressureStrategy.MISSING));

        LocationUpdatesFlowableOnSubscribe single = captor.getAllValues().get(0);
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
        ArgumentCaptor<LocationUpdatesFlowableOnSubscribe> captor = ArgumentCaptor.forClass(LocationUpdatesFlowableOnSubscribe.class);

        rxLocation.location().updates(locationRequest, BackpressureStrategy.LATEST);
        rxLocation.location().updates(locationRequest, TIMEOUT_TIME, TIMEOUT_TIMEUNIT, BackpressureStrategy.LATEST);

        PowerMockito.verifyStatic(times(2));
        Flowable.create(captor.capture(), eq(BackpressureStrategy.LATEST));

        LocationUpdatesFlowableOnSubscribe single = captor.getAllValues().get(0);
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
        ArgumentCaptor<LocationUpdatesFlowableOnSubscribe> captor = ArgumentCaptor.forClass(LocationUpdatesFlowableOnSubscribe.class);

        rxLocation.location().updates(locationRequest, looper, BackpressureStrategy.LATEST);
        rxLocation.location().updates(locationRequest, looper, TIMEOUT_TIME, TIMEOUT_TIMEUNIT, BackpressureStrategy.LATEST);

        PowerMockito.verifyStatic(times(2));
        Flowable.create(captor.capture(), eq(BackpressureStrategy.LATEST));

        LocationUpdatesFlowableOnSubscribe single = captor.getAllValues().get(0);
        assertEquals(locationRequest, single.locationRequest);
        assertEquals(looper, single.looper);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(locationRequest, single.locationRequest);
        assertEquals(looper, single.looper);
        assertTimeoutSet(single);
    }
}
