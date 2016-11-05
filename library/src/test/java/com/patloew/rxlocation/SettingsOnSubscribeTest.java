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
@PrepareOnlyThisForTest({ LocationSettingsRequest.class, LocationSettingsResult.class, LocationAvailability.class, LocationServices.class, com.google.android.gms.location.ActivityRecognition.class, Status.class, ConnectionResult.class, RxLocationBaseOnSubscribe.class })
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
        SettingsCheckSingleOnSubscribe single = PowerMockito.spy(new SettingsCheckSingleOnSubscribe(rxLocation, locationSettingsRequest, null, null));

        setPendingResultValue(locationSettingsResult);
        doReturn(status).when(locationSettingsResult).getStatus();
        doReturn(true).when(status).isSuccess();
        when(settingsApi.checkLocationSettings(apiClient, locationSettingsRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), locationSettingsResult);
    }

    @Test
    public void SettingsCheckSingle_StatusException() {
        SettingsCheckSingleOnSubscribe single = PowerMockito.spy(new SettingsCheckSingleOnSubscribe(rxLocation, locationSettingsRequest, null, null));

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
        SettingsCheckHandleSingleOnSubscribe single = PowerMockito.spy(new SettingsCheckHandleSingleOnSubscribe(rxLocation, locationSettingsRequest, null, null));

        setPendingResultValue(locationSettingsResult);
        doReturn(status).when(locationSettingsResult).getStatus();
        doReturn(LocationSettingsStatusCodes.SUCCESS).when(status).getStatusCode();
        when(settingsApi.checkLocationSettings(apiClient, locationSettingsRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), true);
    }

    @Test
    public void SettingsCheckHandleSingle_ChangeUnavailable() {
        SettingsCheckHandleSingleOnSubscribe single = PowerMockito.spy(new SettingsCheckHandleSingleOnSubscribe(rxLocation, locationSettingsRequest, null, null));

        setPendingResultValue(locationSettingsResult);
        doReturn(status).when(locationSettingsResult).getStatus();
        doReturn(LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE).when(status).getStatusCode();
        when(settingsApi.checkLocationSettings(apiClient, locationSettingsRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), false);
    }

    @Test
    public void SettingsCheckHandleSingle_ResolutionRequired_Success() {
        SettingsCheckHandleSingleOnSubscribe single = PowerMockito.spy(new SettingsCheckHandleSingleOnSubscribe(rxLocation, locationSettingsRequest, null, null));

        setPendingResultValue(locationSettingsResult);
        doReturn(status).when(locationSettingsResult).getStatus();
        doReturn(LocationSettingsStatusCodes.RESOLUTION_REQUIRED).when(status).getStatusCode();
        when(settingsApi.checkLocationSettings(apiClient, locationSettingsRequest)).thenReturn(pendingResult);

        doAnswer(invocation -> {
            String key = (String) SettingsCheckHandleSingleOnSubscribe.observableMap.keySet().toArray()[0];
            SettingsCheckHandleSingleOnSubscribe.onResolutionResult(key, Activity.RESULT_OK);
            return null;
        }).when(ctx).startActivity(any(Intent.class));

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), true);
        assertTrue(SettingsCheckHandleSingleOnSubscribe.observableMap.isEmpty());
    }

    @Test
    public void SettingsCheckHandleSingle_ResolutionRequired_Canceled() {
        SettingsCheckHandleSingleOnSubscribe single = PowerMockito.spy(new SettingsCheckHandleSingleOnSubscribe(rxLocation, locationSettingsRequest, null, null));

        setPendingResultValue(locationSettingsResult);
        doReturn(status).when(locationSettingsResult).getStatus();
        doReturn(LocationSettingsStatusCodes.RESOLUTION_REQUIRED).when(status).getStatusCode();
        when(settingsApi.checkLocationSettings(apiClient, locationSettingsRequest)).thenReturn(pendingResult);

        doAnswer(invocation -> {
            String key = (String) SettingsCheckHandleSingleOnSubscribe.observableMap.keySet().toArray()[0];
            SettingsCheckHandleSingleOnSubscribe.onResolutionResult(key, Activity.RESULT_CANCELED);
            return null;
        }).when(ctx).startActivity(any(Intent.class));

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), false);
        assertTrue(SettingsCheckHandleSingleOnSubscribe.observableMap.isEmpty());
    }

    @Test
    public void SettingsCheckHandleSingle_StatusException() {
        SettingsCheckHandleSingleOnSubscribe single = PowerMockito.spy(new SettingsCheckHandleSingleOnSubscribe(rxLocation, locationSettingsRequest, null, null));

        setPendingResultValue(locationSettingsResult);
        doReturn(status).when(locationSettingsResult).getStatus();
        doReturn(CommonStatusCodes.TIMEOUT).when(status).getStatusCode();
        when(settingsApi.checkLocationSettings(apiClient, locationSettingsRequest)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

}
