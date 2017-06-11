package com.patloew.rxlocation;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.concurrent.TimeUnit;

import io.reactivex.SingleEmitter;

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
class AutocompletePredictionsSingleOnSubscribe extends RxLocationSingleOnSubscribe<AutocompletePredictionBuffer> {

    final String query;
    final LatLngBounds bounds;
    final AutocompleteFilter filter;

    AutocompletePredictionsSingleOnSubscribe(RxLocation rxLocation, String query, LatLngBounds bounds, AutocompleteFilter filter, Long timeout, TimeUnit timeUnit) {
        super(rxLocation, timeout, timeUnit);
        this.query = query;
        this.bounds = bounds;
        this.filter = filter;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, SingleEmitter<AutocompletePredictionBuffer> emitter) {
        setUpPendingResult(
            Places.GeoDataApi.getAutocompletePredictions(apiClient, this.query, this.bounds, this.filter),
            SingleResultCallback.get(emitter)
        );
    }
}
