package com.patloew.rxlocation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
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
class SettingsCheckHandleSingleOnSubscribe extends RxLocationSingleOnSubscribe<Boolean> {

    static final Map<String, WeakReference<SettingsCheckHandleSingleOnSubscribe>> observableMap = new HashMap<>();

    final Context context;
    final LocationSettingsRequest locationSettingsRequest;
    private WeakReference<SingleEmitter<Boolean>> emitterWeakRef;

    SettingsCheckHandleSingleOnSubscribe(RxLocation rxLocation, LocationSettingsRequest locationSettingsRequest, Long timeoutTime, TimeUnit timeoutUnit) {
        super(rxLocation, timeoutTime, timeoutUnit);
        this.context = rxLocation.ctx;
        this.locationSettingsRequest = locationSettingsRequest;
    }

    static void onResolutionResult(String observableId, int resultCode) {
        if (observableMap.containsKey(observableId)) {
            SettingsCheckHandleSingleOnSubscribe observable = observableMap.get(observableId).get();

            if (observable != null && observable.emitterWeakRef != null) {
                SingleEmitter<Boolean> observer = observable.emitterWeakRef.get();

                if (observer != null) {
                    observer.onSuccess(resultCode == Activity.RESULT_OK);
                }
            }

            observableMap.remove(observableId);
        }

        observableMapCleanup();
    }

    static void observableMapCleanup() {
        if(!observableMap.isEmpty()) {
            Iterator<Map.Entry<String, WeakReference<SettingsCheckHandleSingleOnSubscribe>>> it = observableMap.entrySet().iterator();

            while(it.hasNext()) {
                Map.Entry<String, WeakReference<SettingsCheckHandleSingleOnSubscribe>> entry = it.next();
                if(entry.getValue().get() == null) { it.remove(); }
            }
        }
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, SingleEmitter<Boolean> emitter) {
        emitterWeakRef = new WeakReference<>(emitter);

        setupLocationPendingResult(
                LocationServices.SettingsApi.checkLocationSettings(apiClient, locationSettingsRequest),
                result -> {
                    Status status = result.getStatus();

                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can initialize location
                            // requests here.
                            emitter.onSuccess(true);
                            break;

                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user
                            // a dialog.

                            if (context != null) {
                                String observableId = UUID.randomUUID().toString();
                                observableMap.put(observableId, new WeakReference<>(SettingsCheckHandleSingleOnSubscribe.this));

                                Intent intent = new Intent(context, LocationSettingsActivity.class);
                                intent.putExtra(LocationSettingsActivity.ARG_STATUS, status);
                                intent.putExtra(LocationSettingsActivity.ARG_ID, observableId);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            } else {
                                emitter.onSuccess(false);
                            }

                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the
                            // settings so we won't show the dialog.

                            emitter.onSuccess(false);
                            break;

                        default:
                            emitter.onError(new StatusException(status));
                            break;
                    }
                }
        );
    }
}
