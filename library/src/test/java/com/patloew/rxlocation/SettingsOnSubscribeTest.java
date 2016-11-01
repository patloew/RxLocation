package com.patloew.rxlocation;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.reactivex.Single;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SuppressWarnings("MissingPermission")
@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ LocationSettingsRequest.class, LocationSettingsResult.class, LocationAvailability.class, LocationServices.class, com.google.android.gms.location.ActivityRecognition.class, Status.class, ConnectionResult.class, BaseRx.class })
public class SettingsOnSubscribeTest extends BaseOnSubscribeTest {

    @Mock LocationSettingsRequest locationSettingsRequest;
    @Mock LocationSettingsResult locationSettingsResult;

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setup();
    }

    // SettingsCheckSingle

    @Test
    public void SettingsCheckSingle_Success() {
        SettingsCheckSingle single = PowerMockito.spy(new SettingsCheckSingle(rxLocation, locationSettingsRequest, null, null));

        setPendingResultValue(locationSettingsResult);
        doReturn(status).when(locationSettingsResult).getStatus();
        doReturn(true).when(status).isSuccess();
        when(settingsApi.checkLocationSettings(apiClient, locationSettingsRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), locationSettingsResult);
    }

    @Test
    public void SettingsCheckSingle_StatusException() {
        SettingsCheckSingle single = PowerMockito.spy(new SettingsCheckSingle(rxLocation, locationSettingsRequest, null, null));

        setPendingResultValue(locationSettingsResult);
        doReturn(status).when(locationSettingsResult).getStatus();
        doReturn(false).when(status).isSuccess();
        when(settingsApi.checkLocationSettings(apiClient, locationSettingsRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }


    // SettingsCheckHandleSingle

    @Test
    public void SettingsCheckHandleSingle_Success() {
        SettingsCheckHandleSingle single = PowerMockito.spy(new SettingsCheckHandleSingle(rxLocation, locationSettingsRequest, null, null));

        setPendingResultValue(locationSettingsResult);
        doReturn(status).when(locationSettingsResult).getStatus();
        doReturn(LocationSettingsStatusCodes.SUCCESS).when(status).getStatusCode();
        when(settingsApi.checkLocationSettings(apiClient, locationSettingsRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), true);
    }

    @Test
    public void SettingsCheckHandleSingle_ChangeUnavailable() {
        SettingsCheckHandleSingle single = PowerMockito.spy(new SettingsCheckHandleSingle(rxLocation, locationSettingsRequest, null, null));

        setPendingResultValue(locationSettingsResult);
        doReturn(status).when(locationSettingsResult).getStatus();
        doReturn(LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE).when(status).getStatusCode();
        when(settingsApi.checkLocationSettings(apiClient, locationSettingsRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), false);
    }

    @Test
    public void SettingsCheckHandleSingle_ResolutionRequired_Success() {
        SettingsCheckHandleSingle single = PowerMockito.spy(new SettingsCheckHandleSingle(rxLocation, locationSettingsRequest, null, null));

        setPendingResultValue(locationSettingsResult);
        doReturn(status).when(locationSettingsResult).getStatus();
        doReturn(LocationSettingsStatusCodes.RESOLUTION_REQUIRED).when(status).getStatusCode();
        when(settingsApi.checkLocationSettings(apiClient, locationSettingsRequest)).thenReturn(pendingResult);

        doAnswer(invocation -> {
            String key = (String) SettingsCheckHandleSingle.observableMap.keySet().toArray()[0];
            SettingsCheckHandleSingle.onResolutionResult(key, Activity.RESULT_OK);
            return null;
        }).when(ctx).startActivity(any(Intent.class));

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), true);
        assertTrue(SettingsCheckHandleSingle.observableMap.isEmpty());
    }

    @Test
    public void SettingsCheckHandleSingle_ResolutionRequired_Canceled() {
        SettingsCheckHandleSingle single = PowerMockito.spy(new SettingsCheckHandleSingle(rxLocation, locationSettingsRequest, null, null));

        setPendingResultValue(locationSettingsResult);
        doReturn(status).when(locationSettingsResult).getStatus();
        doReturn(LocationSettingsStatusCodes.RESOLUTION_REQUIRED).when(status).getStatusCode();
        when(settingsApi.checkLocationSettings(apiClient, locationSettingsRequest)).thenReturn(pendingResult);

        doAnswer(invocation -> {
            String key = (String) SettingsCheckHandleSingle.observableMap.keySet().toArray()[0];
            SettingsCheckHandleSingle.onResolutionResult(key, Activity.RESULT_CANCELED);
            return null;
        }).when(ctx).startActivity(any(Intent.class));

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), false);
        assertTrue(SettingsCheckHandleSingle.observableMap.isEmpty());
    }

    @Test
    public void SettingsCheckHandleSingle_StatusException() {
        SettingsCheckHandleSingle single = PowerMockito.spy(new SettingsCheckHandleSingle(rxLocation, locationSettingsRequest, null, null));

        setPendingResultValue(locationSettingsResult);
        doReturn(status).when(locationSettingsResult).getStatus();
        doReturn(CommonStatusCodes.TIMEOUT).when(status).getStatusCode();
        when(settingsApi.checkLocationSettings(apiClient, locationSettingsRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

}
