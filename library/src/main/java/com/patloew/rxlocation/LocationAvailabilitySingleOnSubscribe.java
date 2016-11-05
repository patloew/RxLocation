package com.patloew.rxlocation;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationServices;

import io.reactivex.SingleEmitter;

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
class LocationAvailabilitySingleOnSubscribe extends RxLocationSingleOnSubscribe<Boolean> {

    LocationAvailabilitySingleOnSubscribe(@NonNull RxLocation rxLocation) {
        super(rxLocation, null, null);
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, SingleEmitter<Boolean> emitter) {
        //noinspection MissingPermission
        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(apiClient);

        if (locationAvailability != null) {
            emitter.onSuccess(locationAvailability.isLocationAvailable());
        } else {
            emitter.onSuccess(false);
        }
    }
}
