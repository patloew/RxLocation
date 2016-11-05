package com.patloew.rxlocation;

import android.app.PendingIntent;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

import static org.mockito.Mockito.when;

@SuppressWarnings("MissingPermission")
@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ LocationServices.class, com.google.android.gms.location.ActivityRecognition.class, Status.class, ConnectionResult.class, RxLocationBaseOnSubscribe.class })
public class GeofencingOnSubscribeTest extends BaseOnSubscribeTest {

    @Mock GeofencingRequest geofencingRequest;
    @Mock PendingIntent pendingIntent;
    List<String> geofenceRequestIds;

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setup();
        geofenceRequestIds = new ArrayList<>(0);
    }

    // GeofencingAddSingle

    @Test
    public void GeofencingAddSingle_Success() {
        GeofencingAddSingleOnSubscribe single = PowerMockito.spy(new GeofencingAddSingleOnSubscribe(rxLocation, geofencingRequest, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(geofencingApi.addGeofences(apiClient, geofencingRequest, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void GeofencingAddSingle_StatusException() {
        GeofencingAddSingleOnSubscribe single = PowerMockito.spy(new GeofencingAddSingleOnSubscribe(rxLocation, geofencingRequest, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(geofencingApi.addGeofences(apiClient, geofencingRequest, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    // GeofencingRemoveSingle

    @Test
    public void GeofencingRemoveSingle_PendingIntent_Success() {
        GeofencingRemoveSingleOnSubscribe single = PowerMockito.spy(new GeofencingRemoveSingleOnSubscribe(rxLocation, null, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(geofencingApi.removeGeofences(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void GeofencingRemoveSingle_PendingIntent_StatusException() {
        GeofencingRemoveSingleOnSubscribe single = PowerMockito.spy(new GeofencingRemoveSingleOnSubscribe(rxLocation, null, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(geofencingApi.removeGeofences(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    @Test
    public void GeofencingRemoveSingle_IdList_Success() {
        GeofencingRemoveSingleOnSubscribe single = PowerMockito.spy(new GeofencingRemoveSingleOnSubscribe(rxLocation, geofenceRequestIds, null, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(geofencingApi.removeGeofences(apiClient, geofenceRequestIds)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void GeofencingRemoveSingle_IdList_StatusException() {
        GeofencingRemoveSingleOnSubscribe single = PowerMockito.spy(new GeofencingRemoveSingleOnSubscribe(rxLocation, geofenceRequestIds, null, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(geofencingApi.removeGeofences(apiClient, geofenceRequestIds)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }
}
