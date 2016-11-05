package com.patloew.rxlocation;

import android.app.PendingIntent;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.reactivex.Single;

import static org.mockito.Mockito.when;

@SuppressWarnings("MissingPermission")
@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ LocationServices.class, com.google.android.gms.location.ActivityRecognition.class, Status.class, ConnectionResult.class, RxLocationBaseOnSubscribe.class })
public class ActivityOnSubscribeTest extends BaseOnSubscribeTest {

    @Mock PendingIntent pendingIntent;

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        super.setup();
    }

    // ActivityRequestUpdatesSingle

    @Test
    public void ActivityRequestUpdatesSingle_Success() {
        ActivityRequestUpdatesSingleOnSubscribe single = PowerMockito.spy(new ActivityRequestUpdatesSingleOnSubscribe(rxLocation, 1L, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(activityRecognitionApi.requestActivityUpdates(apiClient, 1L, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void ActivityRequestUpdatesSingle_StatusException() {
        ActivityRequestUpdatesSingleOnSubscribe single = PowerMockito.spy(new ActivityRequestUpdatesSingleOnSubscribe(rxLocation, 1L, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(activityRecognitionApi.requestActivityUpdates(apiClient, 1L, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

    // ActivityRemoveUpdatesSingle

    @Test
    public void ActivityRemoveUpdatesSingle_Success() {
        ActivityRemoveUpdatesSingleOnSubscribe single = PowerMockito.spy(new ActivityRemoveUpdatesSingleOnSubscribe(rxLocation, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(true);
        when(activityRecognitionApi.removeActivityUpdates(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), status);
    }

    @Test
    public void ActivityRemoveUpdatesSingle_StatusException() {
        ActivityRemoveUpdatesSingleOnSubscribe single = PowerMockito.spy(new ActivityRemoveUpdatesSingleOnSubscribe(rxLocation, pendingIntent, null, null));

        setPendingResultValue(status);
        when(status.isSuccess()).thenReturn(false);
        when(activityRecognitionApi.removeActivityUpdates(apiClient, pendingIntent)).thenReturn(pendingResult);

        setupBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }
}
