package com.patloew.rxlocation;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Locale;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.functions.Function;

/* Copyright 2016 Patrick Löwenstein
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. */
public class Geocoding {

    private static final Function<List<Address>, Maybe<Address>> ADDRESS_MAYBE_FUNCTION = addresses -> addresses.isEmpty() ? Maybe.empty(): Maybe.just(addresses.get(0));

    private final Context context;

    Geocoding(Context context) {
        this.context = context;
    }

    Geocoder getGeocoder(Locale locale) {
        if(locale != null) {
            return new Geocoder(context, locale);
        } else {
            return new Geocoder(context);
        }
    }



    public Maybe<Address> fromLocation(@NonNull Location location) {
        return fromLocation(null, location, 1).flatMapMaybe(ADDRESS_MAYBE_FUNCTION);
    }

    public Maybe<Address> fromLocation(Locale locale, @NonNull Location location) {
        return fromLocation(locale, location, 1).flatMapMaybe(ADDRESS_MAYBE_FUNCTION);
    }

    public Single<List<Address>> fromLocation(@NonNull Location location, int maxResults) {
        return fromLocation(null, location, maxResults);
    }

    public Single<List<Address>> fromLocation(Locale locale, @NonNull Location location, int maxResults) {
        return Single.fromCallable(() -> getGeocoder(locale).getFromLocation(location.getLatitude(), location.getLongitude(), maxResults));
    }



    public Maybe<Address> fromLocation(double latitude, double longitude) {
        return fromLocation(null, latitude, longitude, 1).flatMapMaybe(ADDRESS_MAYBE_FUNCTION);
    }

    public Maybe<Address> fromLocation(Locale locale, double latitude, double longitude) {
        return fromLocation(locale, latitude, longitude, 1).flatMapMaybe(ADDRESS_MAYBE_FUNCTION);
    }

    public Single<List<Address>> fromLocation(double latitude, double longitude, int maxResults) {
        return fromLocation(null, latitude, longitude, maxResults);
    }

    public Single<List<Address>> fromLocation(Locale locale, double latitude, double longitude, int maxResults) {
        return Single.fromCallable(() -> getGeocoder(locale).getFromLocation(latitude, longitude, maxResults));
    }



    public Maybe<Address> fromLocationName(@NonNull String locationName, double lowerLeftLatitude, double lowerLeftLongitude, double upperRightLatitude, double upperRightLongitude) {
        return fromLocationName(null, locationName, 1, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude).flatMapMaybe(ADDRESS_MAYBE_FUNCTION);
    }

    public Maybe<Address> fromLocationName(Locale locale, @NonNull String locationName, double lowerLeftLatitude, double lowerLeftLongitude, double upperRightLatitude, double upperRightLongitude) {
        return fromLocationName(locale, locationName, 1, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude).flatMapMaybe(ADDRESS_MAYBE_FUNCTION);
    }

    public Single<List<Address>> fromLocationName(@NonNull String locationName, int maxResults, double lowerLeftLatitude, double lowerLeftLongitude, double upperRightLatitude, double upperRightLongitude) {
        return fromLocationName(null, locationName, maxResults, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude);
    }

    public Single<List<Address>> fromLocationName(Locale locale, @NonNull String locationName, int maxResults, double lowerLeftLatitude, double lowerLeftLongitude, double upperRightLatitude, double upperRightLongitude) {
        return Single.fromCallable(() -> getGeocoder(locale).getFromLocationName(locationName, maxResults, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude));
    }



    public Maybe<Address> fromLocationName(@NonNull String locationName) {
        return fromLocationName(null, locationName, 1).flatMapMaybe(ADDRESS_MAYBE_FUNCTION);
    }

    public Maybe<Address> fromLocationName(Locale locale, @NonNull String locationName) {
        return fromLocationName(locale, locationName, 1).flatMapMaybe(ADDRESS_MAYBE_FUNCTION);
    }

    public Single<List<Address>> fromLocationName(@NonNull String locationName, int maxResults) {
        return fromLocationName(null, locationName, maxResults);
    }

    public Single<List<Address>> fromLocationName(Locale locale, @NonNull String locationName, int maxResults) {
        return Single.fromCallable(() -> getGeocoder(locale).getFromLocationName(locationName, maxResults));
    }

}
