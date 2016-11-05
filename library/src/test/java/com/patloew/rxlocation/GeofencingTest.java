package com.patloew.rxlocation;

import android.app.PendingIntent;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.GeofencingRequest;
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

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.times;

@SuppressWarnings("MissingPermission")
@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ Single.class, LocationServices.class, com.google.android.gms.location.ActivityRecognition.class, Status.class, ConnectionResult.class })
public class GeofencingTest extends BaseTest {

    @Mock GeofencingRequest geofencingRequest;
    @Mock PendingIntent pendingIntent;
    List<String> geofenceRequestIds;

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.spy(Single.class);
        super.setup();
        geofenceRequestIds = new ArrayList<>(0);
    }

    // Add

    @Test
    public void Add() throws Exception {
        ArgumentCaptor<GeofencingAddSingleOnSubscribe> captor = ArgumentCaptor.forClass(GeofencingAddSingleOnSubscribe.class);

        rxLocation.geofencing().add(geofencingRequest, pendingIntent);
        rxLocation.geofencing().add(geofencingRequest, pendingIntent, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        GeofencingAddSingleOnSubscribe single = captor.getAllValues().get(0);
        assertEquals(geofencingRequest, single.geofencingRequest);
        assertEquals(pendingIntent, single.pendingIntent);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(geofencingRequest, single.geofencingRequest);
        assertEquals(pendingIntent, single.pendingIntent);
        assertTimeoutSet(single);
    }

    // Remove PendingIntent

    @Test
    public void Remove_PendingIntent() throws Exception {
        ArgumentCaptor<GeofencingRemoveSingleOnSubscribe> captor = ArgumentCaptor.forClass(GeofencingRemoveSingleOnSubscribe.class);

        rxLocation.geofencing().remove(pendingIntent);
        rxLocation.geofencing().remove(pendingIntent, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        GeofencingRemoveSingleOnSubscribe single = captor.getAllValues().get(0);
        assertEquals(pendingIntent, single.pendingIntent);
        assertNull(single.geofenceRequestIds);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(pendingIntent, single.pendingIntent);
        assertNull(single.geofenceRequestIds);
        assertTimeoutSet(single);
    }

    @Test
    public void Remove_IdList() throws Exception {
        ArgumentCaptor<GeofencingRemoveSingleOnSubscribe> captor = ArgumentCaptor.forClass(GeofencingRemoveSingleOnSubscribe.class);

        rxLocation.geofencing().remove(geofenceRequestIds);
        rxLocation.geofencing().remove(geofenceRequestIds, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        GeofencingRemoveSingleOnSubscribe single = captor.getAllValues().get(0);
        assertEquals(geofenceRequestIds, single.geofenceRequestIds);
        assertNull(single.pendingIntent);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(geofenceRequestIds, single.geofenceRequestIds);
        assertNull(single.pendingIntent);
        assertTimeoutSet(single);
    }

}
