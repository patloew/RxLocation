package com.patloew.rxlocation;

import android.app.PendingIntent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.spy;

@SuppressWarnings("MissingPermission")
@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ Single.class, LocationServices.class, com.google.android.gms.location.ActivityRecognition.class, Status.class, ConnectionResult.class })
public class GeocodingTest extends BaseTest {

    @Mock PendingIntent pendingIntent;
    @Mock Geocoder geocoder;
    @Mock Address address;
    @Mock Location location;
    Locale locale = new Locale("en");

    final double latitude = 1.0;
    final double longitude = 2.0;

    final String locationName = "name";
    final double lowerLeftLatitude = 1.0;
    final double lowerLeftLongitude = 2.0;
    final double upperRightLatitude = 3.0;
    final double upperRightLongitude = 4.0;

    Geocoding geocoding;
    List<Address> addressList;

    @Override
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        spy(Single.class);
        super.setup();

        geocoding = spy(rxLocation.geocoding());
        doReturn(geocoder).when(geocoding).getGeocoder(any());
        doReturn(latitude).when(location).getLatitude();
        doReturn(longitude).when(location).getLongitude();

        addressList = new ArrayList<>(1);
        addressList.add(address);
    }

    private void verifyGeocoderWithLocale() {
        verify(geocoding).getGeocoder(locale);
        verify(geocoding, never()).getGeocoder(null);
    }

    private void verifyGeocoderWithNull() {
        verify(geocoding, never()).getGeocoder(locale);
        verify(geocoding).getGeocoder(null);
    }

    // fromLocation with Location

    @Test
    public void FromLocation_Location_Maybe_NoLocale() throws Exception {
        doReturn(addressList).when(geocoder).getFromLocation(latitude, longitude, 1);

        geocoding.fromLocation(location).test()
                .assertValue(address)
                .assertComplete();

        verifyGeocoderWithNull();
    }

    @Test
    public void FromLocation_Location_Maybe_Locale() throws Exception {
        doReturn(addressList).when(geocoder).getFromLocation(latitude, longitude, 1);

        geocoding.fromLocation(locale, location).test()
                .assertValue(address)
                .assertComplete();

        verifyGeocoderWithLocale();
    }

    @Test
    public void FromLocation_Location_Single_NoLocale() throws Exception {
        doReturn(addressList).when(geocoder).getFromLocation(latitude, longitude, 1);

        geocoding.fromLocation(location, 1).test()
                .assertValue(addressList)
                .assertComplete();

        verifyGeocoderWithNull();
    }

    @Test
    public void FromLocation_Location_Single_Locale() throws Exception {
        doReturn(addressList).when(geocoder).getFromLocation(latitude, longitude, 1);

        geocoding.fromLocation(locale, location, 1).test()
                .assertValue(addressList)
                .assertComplete();

        verifyGeocoderWithLocale();
    }

    // fromLocation with LatLong

    @Test
    public void FromLocation_LatLong_Maybe_NoLocale() throws Exception {
        doReturn(addressList).when(geocoder).getFromLocation(latitude, longitude, 1);

        geocoding.fromLocation(latitude, longitude).test()
                .assertValue(address)
                .assertComplete();

        verifyGeocoderWithNull();
    }

    @Test
    public void FromLocation_LatLong_Maybe_Locale() throws Exception {
        doReturn(addressList).when(geocoder).getFromLocation(latitude, longitude, 1);

        geocoding.fromLocation(locale, latitude, longitude).test()
                .assertValue(address)
                .assertComplete();

        verifyGeocoderWithLocale();
    }

    @Test
    public void FromLocation_LatLong_Single_NoLocale() throws Exception {
        doReturn(addressList).when(geocoder).getFromLocation(latitude, longitude, 1);

        geocoding.fromLocation(latitude, longitude, 1).test()
                .assertValue(addressList)
                .assertComplete();

        verifyGeocoderWithNull();
    }

    @Test
    public void FromLocation_LatLong_Single_Locale() throws Exception {
        doReturn(addressList).when(geocoder).getFromLocation(latitude, longitude, 1);

        geocoding.fromLocation(locale, latitude, longitude, 1).test()
                .assertValue(addressList)
                .assertComplete();

        verifyGeocoderWithLocale();
    }

    // fromLocationName with LatLong Rect

    @Test
    public void FromLocationName_LatLong_Maybe_NoLocale() throws Exception {
        doReturn(addressList).when(geocoder).getFromLocationName(locationName, 1, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude);

        geocoding.fromLocationName(locationName, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude).test()
                .assertValue(address)
                .assertComplete();

        verifyGeocoderWithNull();
    }

    @Test
    public void FromLocationName_LatLong_Maybe_Locale() throws Exception {
        doReturn(addressList).when(geocoder).getFromLocationName(locationName, 1, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude);

        geocoding.fromLocationName(locale, locationName, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude).test()
                .assertValue(address)
                .assertComplete();

        verifyGeocoderWithLocale();
    }

    @Test
    public void FromLocationName_LatLong_Single_NoLocale() throws Exception {
        doReturn(addressList).when(geocoder).getFromLocationName(locationName, 1, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude);

        geocoding.fromLocationName(locationName, 1, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude).test()
                .assertValue(addressList)
                .assertComplete();

        verifyGeocoderWithNull();
    }

    @Test
    public void FromLocationName_LatLong_Single_Locale() throws Exception {
        doReturn(addressList).when(geocoder).getFromLocationName(locationName, 1, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude);

        geocoding.fromLocationName(locale, locationName, 1, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude).test()
                .assertValue(addressList)
                .assertComplete();

        verifyGeocoderWithLocale();
    }

    // fromLocationName

    @Test
    public void FromLocationName_Maybe_NoLocale() throws Exception {
        doReturn(addressList).when(geocoder).getFromLocationName(locationName, 1);

        geocoding.fromLocationName(locationName).test()
                .assertValue(address)
                .assertComplete();

        verifyGeocoderWithNull();
    }

    @Test
    public void FromLocationName_Maybe_Locale() throws Exception {
        doReturn(addressList).when(geocoder).getFromLocationName(locationName, 1);

        geocoding.fromLocationName(locale, locationName).test()
                .assertValue(address)
                .assertComplete();

        verifyGeocoderWithLocale();
    }

    @Test
    public void FromLocationName_Single_NoLocale() throws Exception {
        doReturn(addressList).when(geocoder).getFromLocationName(locationName, 1);

        geocoding.fromLocationName(locationName, 1).test()
                .assertValue(addressList)
                .assertComplete();

        verifyGeocoderWithNull();
    }

    @Test
    public void FromLocationName_Single_Locale() throws Exception {
        doReturn(addressList).when(geocoder).getFromLocationName(locationName, 1);

        geocoding.fromLocationName(locale, locationName, 1).test()
                .assertValue(addressList)
                .assertComplete();

        verifyGeocoderWithLocale();
    }

}
