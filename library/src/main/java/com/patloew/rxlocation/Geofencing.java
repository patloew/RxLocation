package com.patloew.rxlocation;

import android.Manifest;
import android.app.PendingIntent;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.GeofencingRequest;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;

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
public class Geofencing {

    private final RxLocation rxLocation;

    Geofencing(RxLocation rxLocation) {
        this.rxLocation = rxLocation;
    }


    // Add

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public Single<Status> add(@NonNull GeofencingRequest geofencingRequest, @NonNull PendingIntent pendingIntent) {
        return addInternal(geofencingRequest, pendingIntent, null, null);
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public Single<Status> add(@NonNull GeofencingRequest geofencingRequest, @NonNull PendingIntent pendingIntent, long timeoutTime, @NonNull TimeUnit timeoutUnit) {
        return addInternal(geofencingRequest, pendingIntent, timeoutTime, timeoutUnit);
    }

    private Single<Status> addInternal(GeofencingRequest geofencingRequest, PendingIntent pendingIntent, Long timeoutTime, TimeUnit timeoutUnit) {
        return Single.create(new GeofencingAddSingleOnSubscribe(rxLocation, geofencingRequest, pendingIntent, timeoutTime, timeoutUnit));
    }


    // Remove

    public Single<Status> remove(@NonNull List<String> geofenceRequestIds) {
        return removeInternal(geofenceRequestIds, null, null, null);
    }

    public Single<Status> remove(@NonNull List<String> geofenceRequestIds, long timeoutTime, @NonNull TimeUnit timeoutUnit) {
        return removeInternal(geofenceRequestIds, null, timeoutTime, timeoutUnit);
    }

    public Single<Status> remove(@NonNull PendingIntent pendingIntent) {
        return removeInternal(null, pendingIntent, null, null);
    }

    public Single<Status> remove(@NonNull PendingIntent pendingIntent, long timeoutTime, @NonNull TimeUnit timeoutUnit) {
        return removeInternal(null, pendingIntent, timeoutTime, timeoutUnit);
    }

    private Single<Status> removeInternal(List<String> geofenceRequestIds, PendingIntent pendingIntent, Long timeoutTime, TimeUnit timeoutUnit) {
        return Single.create(new GeofencingRemoveSingleOnSubscribe(rxLocation, geofenceRequestIds, pendingIntent, timeoutTime, timeoutUnit));
    }
}
