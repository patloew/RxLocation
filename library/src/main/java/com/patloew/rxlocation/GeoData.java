package com.patloew.rxlocation;

import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.concurrent.TimeUnit;

import io.reactivex.Single;

/* Copyright 2017 Muhammad Qureshi
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
public class GeoData {

    private final RxLocation rxLocation;

    GeoData(RxLocation rxLocation) {
        this.rxLocation = rxLocation;
    }

    public Single<AutocompletePredictionBuffer> autocompletePredictions(String query, LatLngBounds bounds, AutocompleteFilter filter) {
        return autocompletePredictionsInternal(query, bounds, filter, null, null);
    }
    public Single<AutocompletePredictionBuffer> autocompletePredictions(String query, LatLngBounds bounds, AutocompleteFilter filter, Long timeout, TimeUnit timeUnit) {
        return autocompletePredictionsInternal(query, bounds, filter, timeout, timeUnit);
    }

    private Single<AutocompletePredictionBuffer> autocompletePredictionsInternal(String query, LatLngBounds bounds, AutocompleteFilter filter, Long timeout, TimeUnit timeUnit) {
        return Single.create(new AutocompletePredictionsSingleOnSubscribe(rxLocation, query, bounds, filter, timeout, timeUnit));
    }
}
