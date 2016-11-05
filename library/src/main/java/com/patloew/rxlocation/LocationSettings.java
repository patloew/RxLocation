package com.patloew.rxlocation;

import android.support.annotation.NonNull;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
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
public class LocationSettings {

    private final RxLocation rxLocation;

    LocationSettings(RxLocation rxLocation) {
        this.rxLocation = rxLocation;
    }


    LocationSettingsRequest.Builder getLocationSettingsRequestBuilder() {
        return new LocationSettingsRequest.Builder();
    }

    // Check

    public Single<LocationSettingsResult> check(@NonNull LocationRequest locationRequest) {
        return checkInternal(getLocationSettingsRequestBuilder().addLocationRequest(locationRequest).build(), null, null);
    }

    public Single<LocationSettingsResult> check(@NonNull LocationRequest locationRequest, long timeoutTime, @NonNull TimeUnit timeoutUnit) {
        return checkInternal(getLocationSettingsRequestBuilder().addLocationRequest(locationRequest).build(), timeoutTime, timeoutUnit);
    }

    public Single<LocationSettingsResult> check(@NonNull LocationSettingsRequest locationSettingsRequest) {
        return checkInternal(locationSettingsRequest, null, null);
    }

    public Single<LocationSettingsResult> check(@NonNull LocationSettingsRequest locationSettingsRequest, long timeoutTime, @NonNull TimeUnit timeoutUnit) {
        return checkInternal(locationSettingsRequest, timeoutTime, timeoutUnit);
    }

    private Single<LocationSettingsResult> checkInternal(LocationSettingsRequest locationSettingsRequest, Long timeoutTime, TimeUnit timeoutUnit) {
        return Single.create(new SettingsCheckSingleOnSubscribe(rxLocation, locationSettingsRequest, timeoutTime, timeoutUnit));
    }

    // Check and handle resolution

    private static final Function<Boolean, Completable> CHECK_SETTINGS_COMPLETABLE_FUNCTION = success -> success ? Completable.complete() : Completable.error(new LocationSettingsNotSatisfiedException());


    public Completable checkAndHandleResolutionCompletable(@NonNull LocationRequest locationRequest) {
        return checkAndHandleResolution(locationRequest).flatMapCompletable(CHECK_SETTINGS_COMPLETABLE_FUNCTION);
    }

    public Single<Boolean> checkAndHandleResolution(@NonNull LocationRequest locationRequest) {
        return checkAndHandleResolutionInternal(getLocationSettingsRequestBuilder().addLocationRequest(locationRequest).build(), null, null);
    }


    public Completable checkAndHandleResolutionCompletable(@NonNull LocationRequest locationRequest, long timeoutTime, @NonNull TimeUnit timeoutUnit) {
        return checkAndHandleResolution(locationRequest, timeoutTime, timeoutUnit).flatMapCompletable(CHECK_SETTINGS_COMPLETABLE_FUNCTION);
    }

    public Single<Boolean> checkAndHandleResolution(@NonNull LocationRequest locationRequest, long timeoutTime, @NonNull TimeUnit timeoutUnit) {
        return checkAndHandleResolutionInternal(getLocationSettingsRequestBuilder().addLocationRequest(locationRequest).build(), timeoutTime, timeoutUnit);
    }


    public Completable checkAndHandleResolutionCompletable(@NonNull LocationSettingsRequest locationSettingsRequest) {
        return checkAndHandleResolutionInternal(locationSettingsRequest, null, null).flatMapCompletable(CHECK_SETTINGS_COMPLETABLE_FUNCTION);
    }

    public Single<Boolean> checkAndHandleResolution(@NonNull LocationSettingsRequest locationSettingsRequest) {
        return checkAndHandleResolutionInternal(locationSettingsRequest, null, null);
    }


    public Completable checkAndHandleResolutionCompletable(@NonNull LocationSettingsRequest locationSettingsRequest, long timeoutTime, @NonNull TimeUnit timeoutUnit) {
        return checkAndHandleResolutionInternal(locationSettingsRequest, timeoutTime, timeoutUnit).flatMapCompletable(CHECK_SETTINGS_COMPLETABLE_FUNCTION);
    }

    public Single<Boolean> checkAndHandleResolution(@NonNull LocationSettingsRequest locationSettingsRequest, long timeoutTime, @NonNull TimeUnit timeoutUnit) {
        return checkAndHandleResolutionInternal(locationSettingsRequest, timeoutTime, timeoutUnit);
    }


    private Single<Boolean> checkAndHandleResolutionInternal(LocationSettingsRequest locationSettingsRequest, Long timeoutTime, TimeUnit timeoutUnit) {
        return Single.create(new SettingsCheckHandleSingleOnSubscribe(rxLocation, locationSettingsRequest, timeoutTime, timeoutUnit));
    }

}
