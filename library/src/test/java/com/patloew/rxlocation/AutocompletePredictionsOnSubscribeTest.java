package com.patloew.rxlocation;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.reactivex.Single;


import static org.mockito.Mockito.*;

@SuppressWarnings("MissingPermission")
@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ LocationServices.class, LatLngBounds.class, com.google.android.gms.location.ActivityRecognition.class,
    Places.class, Status.class, ConnectionResult.class, RxLocationBaseOnSubscribe.class
})
public class AutocompletePredictionsOnSubscribeTest extends BaseOnSubscribeTest {

    @Mock AutocompletePredictionBuffer autocompletePredictionBuffer;
    @Mock LatLngBounds bounds;
    @Mock AutocompleteFilter filter;
    final String query = "some query";

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        doReturn(status).when(autocompletePredictionBuffer).getStatus();
        super.setup();
    }

    // AutocompletePredictionsSingle

    @Test
    public void AutocompletePredictionsSingle_Success() {
        AutocompletePredictionsSingleOnSubscribe single = PowerMockito.spy(new AutocompletePredictionsSingleOnSubscribe(rxLocation, query, bounds, filter, null, null));

        setPendingResultValue(autocompletePredictionBuffer);
        when(status.isSuccess()).thenReturn(true);
        when(geoDataApi.getAutocompletePredictions(apiClient, query, bounds, filter)).thenReturn(pendingResult);

        setUpBaseSingleSuccess(single);

        assertSingleValue(Single.create(single).test(), autocompletePredictionBuffer);
    }

    @Test
    public void AutocompletePredictionsSingle_StatusException() {
        AutocompletePredictionsSingleOnSubscribe single = PowerMockito.spy(new AutocompletePredictionsSingleOnSubscribe(rxLocation, query, bounds, filter, null, null));

        setPendingResultValue(autocompletePredictionBuffer);
        when(status.isSuccess()).thenReturn(false);
        when(geoDataApi.getAutocompletePredictions(apiClient, query, bounds, filter)).thenReturn(pendingResult);

        setUpBaseSingleSuccess(single);

        assertError(Single.create(single).test(), StatusException.class);
    }

}
