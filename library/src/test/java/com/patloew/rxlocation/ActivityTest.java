package com.patloew.rxlocation;

import android.app.PendingIntent;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
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

import io.reactivex.Single;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.times;

@SuppressWarnings("MissingPermission")
@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ Single.class, LocationServices.class, com.google.android.gms.location.ActivityRecognition.class, Status.class, ConnectionResult.class })
public class ActivityTest extends BaseTest {

    @Mock PendingIntent pendingIntent;

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.spy(Single.class);
        super.setup();
    }

    // Request Updates

    @Test
    public void Activity_RequestUpdates() throws Exception {
        ArgumentCaptor<ActivityRequestUpdatesSingleOnSubscribe> captor = ArgumentCaptor.forClass(ActivityRequestUpdatesSingleOnSubscribe.class);

        long detectionIntervalMillis = 123L;
        rxLocation.activity().requestUpdates(detectionIntervalMillis,pendingIntent);
        rxLocation.activity().requestUpdates(detectionIntervalMillis,pendingIntent, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        ActivityRequestUpdatesSingleOnSubscribe single = captor.getAllValues().get(0);
        assertEquals(detectionIntervalMillis, single.detectionIntervalMillis);
        assertEquals(pendingIntent, single.pendingIntent);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(detectionIntervalMillis, single.detectionIntervalMillis);
        assertEquals(pendingIntent, single.pendingIntent);
        assertTimeoutSet(single);
    }

    // Remove Updates

    @Test
    public void Activity_RemoveUpdates() throws Exception {
        ArgumentCaptor<ActivityRemoveUpdatesSingleOnSubscribe> captor = ArgumentCaptor.forClass(ActivityRemoveUpdatesSingleOnSubscribe.class);

        rxLocation.activity().removeUpdates(pendingIntent);
        rxLocation.activity().removeUpdates(pendingIntent, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(times(2));
        Single.create(captor.capture());

        ActivityRemoveUpdatesSingleOnSubscribe single = captor.getAllValues().get(0);
        assertEquals(pendingIntent, single.pendingIntent);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(pendingIntent, single.pendingIntent);
        assertTimeoutSet(single);
    }


}
