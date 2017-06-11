package com.patloew.rxlocation;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.maps.model.LatLngBounds;

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
import static org.mockito.Mockito.*;

@SuppressWarnings("MissingPermission")
@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ Single.class, LatLngBounds.class, ConnectionResult.class })
public class GeoDataTest extends BaseTest {

    @Mock LatLngBounds bounds;
    @Mock AutocompleteFilter filter;
    final String query = "some query";

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.spy(Single.class);
        super.setup();
    }

    // AutocompletePredictions

    @Test
    public void AutocompletePredictions() throws Exception {
        ArgumentCaptor<AutocompletePredictionsSingleOnSubscribe> captor = ArgumentCaptor.forClass(AutocompletePredictionsSingleOnSubscribe.class);

        rxLocation.geoData().autocompletePredictions(query, bounds, filter);
        rxLocation.geoData().autocompletePredictions(query, bounds, filter, TIMEOUT_TIME, TIMEOUT_TIMEUNIT);

        PowerMockito.verifyStatic(Single.class, times(2));
        Single.create(captor.capture());

        AutocompletePredictionsSingleOnSubscribe single = captor.getAllValues().get(0);
        assertEquals(query, single.query);
        assertEquals(bounds, single.bounds);
        assertEquals(filter, single.filter);
        assertNoTimeoutSet(single);

        single = captor.getAllValues().get(1);
        assertEquals(query, single.query);
        assertEquals(bounds, single.bounds);
        assertEquals(filter, single.filter);
        assertTimeoutSet(single);
    }
}
