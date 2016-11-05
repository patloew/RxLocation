package com.patloew.rxlocation;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import java.util.concurrent.TimeUnit;

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
class SettingsCheckSingleOnSubscribe extends RxLocationSingleOnSubscribe<LocationSettingsResult> {

    final LocationSettingsRequest locationSettingsRequest;

    SettingsCheckSingleOnSubscribe(RxLocation rxLocation, LocationSettingsRequest locationSettingsRequest, Long timeoutTime, TimeUnit timeoutUnit) {
        super(rxLocation, timeoutTime, timeoutUnit);
        this.locationSettingsRequest = locationSettingsRequest;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, SingleEmitter<LocationSettingsResult> emitter) {
        setupLocationPendingResult(
                LocationServices.SettingsApi.checkLocationSettings(apiClient, locationSettingsRequest),
                SingleResultCallBack.get(emitter)
        );
    }
}
