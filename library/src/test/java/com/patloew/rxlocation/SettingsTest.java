package com.patloew.rxlocation;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.reactivex.Single;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.spy;

@SuppressWarnings("MissingPermission")
@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ Single.class, LocationRequest.class, LocationSettingsRequest.Builder.class, LocationSettingsRequest.class, LocationSettingsResult.class, LocationServices.class, com.google.android.gms.location.ActivityRecognition.class, Status.class, ConnectionResult.class })
public class SettingsTest extends BaseTest {

    @Mock LocationSettingsRequest.Builder locationSettingsRequestBuilder;
    @Mock LocationSettingsRequest locationSettingsRequest;
    @Mock LocationSettingsResult locationSettingsResult;
    @Mock LocationRequest locationRequest;

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        spy(Single.class);
        super.setup();
    }

    @Test
    public void Check_LocationRequest() throws Exception {
        ArgumentCaptor<SettingsCheckSingleOnSubscribe> captor = ArgumentCaptor.forClass(SettingsCheckSingleOnSubscribe.class);

        doReturn(locationSettingsRequestBuilder).when(locationSettingsRequestBuilder).addLocationRequest(locationRequest);
        doReturn(locationSettingsRequest).when(locationSettingsRequestBuilder).build();

        LocationSettings settings = spy(rxLocation.settings());
        doReturn(locationSettingsRequestBuilder).when(settings).getLocationSettingsRequestBuilder();

        settings.check(locationRequest);
        settings.check(locationRequest, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        SettingsCheckSingleOnSubscribe single = captor.getAllValues().get(0);
        assertEquals(locationSettingsRequest, single.locationSettingsRequest);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(locationSettingsRequest, single.locationSettingsRequest);
        assertTimeoutSet(single);
    }

    @Test
    public void Check_LocationSettingsRequest() throws Exception {
        ArgumentCaptor<SettingsCheckSingleOnSubscribe> captor = ArgumentCaptor.forClass(SettingsCheckSingleOnSubscribe.class);

        rxLocation.settings().check(locationSettingsRequest);
        rxLocation.settings().check(locationSettingsRequest, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        SettingsCheckSingleOnSubscribe single = captor.getAllValues().get(0);
        assertEquals(locationSettingsRequest, single.locationSettingsRequest);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(locationSettingsRequest, single.locationSettingsRequest);
        assertTimeoutSet(single);
    }

    @Test
    public void CheckAndHandleResolution_Completable_LocationRequest() throws Exception {
        ArgumentCaptor<SettingsCheckHandleSingleOnSubscribe> captor = ArgumentCaptor.forClass(SettingsCheckHandleSingleOnSubscribe.class);

        doReturn(locationSettingsRequestBuilder).when(locationSettingsRequestBuilder).addLocationRequest(locationRequest);
        doReturn(locationSettingsRequest).when(locationSettingsRequestBuilder).build();

        LocationSettings settings = spy(rxLocation.settings());
        doReturn(locationSettingsRequestBuilder).when(settings).getLocationSettingsRequestBuilder();

        settings.checkAndHandleResolutionCompletable(locationRequest);
        settings.checkAndHandleResolutionCompletable(locationRequest, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        SettingsCheckHandleSingleOnSubscribe single = captor.getAllValues().get(0);
        assertEquals(locationSettingsRequest, single.locationSettingsRequest);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(locationSettingsRequest, single.locationSettingsRequest);
        assertTimeoutSet(single);
    }

    @Test
    public void CheckAndHandleResolution_LocationRequest() throws Exception {
        ArgumentCaptor<SettingsCheckHandleSingleOnSubscribe> captor = ArgumentCaptor.forClass(SettingsCheckHandleSingleOnSubscribe.class);

        doReturn(locationSettingsRequestBuilder).when(locationSettingsRequestBuilder).addLocationRequest(locationRequest);
        doReturn(locationSettingsRequest).when(locationSettingsRequestBuilder).build();

        LocationSettings settings = spy(rxLocation.settings());
        doReturn(locationSettingsRequestBuilder).when(settings).getLocationSettingsRequestBuilder();

        settings.checkAndHandleResolution(locationRequest);
        settings.checkAndHandleResolution(locationRequest, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        SettingsCheckHandleSingleOnSubscribe single = captor.getAllValues().get(0);
        assertEquals(locationSettingsRequest, single.locationSettingsRequest);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(locationSettingsRequest, single.locationSettingsRequest);
        assertTimeoutSet(single);
    }

    @Test
    public void CheckAndHandleResolution_Completable_LocationSettingsRequest() throws Exception {
        ArgumentCaptor<SettingsCheckHandleSingleOnSubscribe> captor = ArgumentCaptor.forClass(SettingsCheckHandleSingleOnSubscribe.class);

        rxLocation.settings().checkAndHandleResolutionCompletable(locationSettingsRequest);
        rxLocation.settings().checkAndHandleResolutionCompletable(locationSettingsRequest, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        SettingsCheckHandleSingleOnSubscribe single = captor.getAllValues().get(0);
        assertEquals(locationSettingsRequest, single.locationSettingsRequest);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(locationSettingsRequest, single.locationSettingsRequest);
        assertTimeoutSet(single);
    }

    @Test
    public void CheckAndHandleResolution_LocationSettingsRequest() throws Exception {
        ArgumentCaptor<SettingsCheckHandleSingleOnSubscribe> captor = ArgumentCaptor.forClass(SettingsCheckHandleSingleOnSubscribe.class);

        rxLocation.settings().checkAndHandleResolution(locationSettingsRequest);
        rxLocation.settings().checkAndHandleResolution(locationSettingsRequest, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        SettingsCheckHandleSingleOnSubscribe single = captor.getAllValues().get(0);
        assertEquals(locationSettingsRequest, single.locationSettingsRequest);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(locationSettingsRequest, single.locationSettingsRequest);
        assertTimeoutSet(single);
    }
}
