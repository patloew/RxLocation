package com.patloew.rxlocation;

import android.app.PendingIntent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

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
class GeofencingAddSingleOnSubscribe extends RxLocationSingleOnSubscribe<Status> {

    final GeofencingRequest geofencingRequest;
    final PendingIntent pendingIntent;

    GeofencingAddSingleOnSubscribe(RxLocation rxLocation, GeofencingRequest geofencingRequest, PendingIntent pendingIntent, Long timeoutTime, TimeUnit timeoutUnit) {
        super(rxLocation, timeoutTime, timeoutUnit);
        this.geofencingRequest = geofencingRequest;
        this.pendingIntent = pendingIntent;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, SingleEmitter<Status> emitter) {
        //noinspection MissingPermission
        setupLocationPendingResult(
                LocationServices.GeofencingApi.addGeofences(apiClient, geofencingRequest, pendingIntent),
                SingleResultCallBack.get(emitter)
        );
    }
}
