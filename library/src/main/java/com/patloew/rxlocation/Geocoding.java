package com.patloew.rxlocation;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.util.List;

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

    private final android.location.Geocoder geocoder;

    Geocoding(Context context) {
        this.geocoder = getGeocoder(context);
    }

    Geocoder getGeocoder(Context context) {
        return new Geocoder(context);
    }


    public Maybe<Address> fromLocation(Location location) {
        return fromLocation(location, 1).flatMapMaybe(ADDRESS_MAYBE_FUNCTION);
    }

    public Single<List<Address>> fromLocation(Location location, int maxResults) {
        return Single.fromCallable(() -> geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), maxResults));
    }


    public Maybe<Address> fromLocation(double latitude, double longitude) {
        return fromLocation(latitude, longitude, 1).flatMapMaybe(ADDRESS_MAYBE_FUNCTION);
    }

    public Single<List<Address>> fromLocation(double latitude, double longitude, int maxResults) {
        return Single.fromCallable(() -> geocoder.getFromLocation(latitude, longitude, maxResults));
    }


    public Maybe<Address> fromLocationName(String locationName, double lowerLeftLatitude, double lowerLeftLongitude, double upperRightLatitude, double upperRightLongitude) {
        return fromLocationName(locationName, 1, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude).flatMapMaybe(ADDRESS_MAYBE_FUNCTION);
    }

    public Single<List<Address>> fromLocationName(String locationName, int maxResults, double lowerLeftLatitude, double lowerLeftLongitude, double upperRightLatitude, double upperRightLongitude) {
        return Single.fromCallable(() -> geocoder.getFromLocationName(locationName, maxResults, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude));
    }


    public Maybe<Address> fromLocationName(String locationName) {
        return fromLocationName(locationName, 1).flatMapMaybe(ADDRESS_MAYBE_FUNCTION);
    }

    public Single<List<Address>> fromLocationName(String locationName, int maxResults) {
        return Single.fromCallable(() -> geocoder.getFromLocationName(locationName, maxResults));
    }

}
