package com.patloew.rxlocation;

import android.Manifest;
import android.app.PendingIntent;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
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
public class FusedLocation {

    private final RxLocation rxLocation;

    FusedLocation(RxLocation rxLocation) {
        this.rxLocation = rxLocation;
    }


    // Flush

    public Single<Status> flush() {
        return flushInternal(null, null);
    }

    public Single<Status> flush(long timeoutTime, @NonNull TimeUnit timeoutUnit) {
        return flushInternal(timeoutTime, timeoutUnit);
    }

    private Single<Status> flushInternal(Long timeoutTime, TimeUnit timeoutUnit) {
        return Single.create(new LocationFlushSingleOnSubscribe(rxLocation, timeoutTime, timeoutUnit));
    }


    // Last Location

    @RequiresPermission(anyOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public Maybe<Location> lastLocation() {
        return Maybe.create(new LocationLastMaybeOnSubscribe(rxLocation));
    }


    // Location Availability

    @RequiresPermission(anyOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public Single<Boolean> isLocationAvailable() {
        return Single.create(new LocationAvailabilitySingleOnSubscribe(rxLocation));
    }


    // Location Updates

    @RequiresPermission(anyOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public Observable<Location> updates(@NonNull LocationRequest locationRequest) {
        return updatesInternal(locationRequest, null, null, null, BackpressureStrategy.MISSING).toObservable();
    }

    @RequiresPermission(anyOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public Observable<Location> updates(@NonNull LocationRequest locationRequest, long timeoutTime, @NonNull TimeUnit timeoutUnit) {
        return updatesInternal(locationRequest, null, timeoutTime, timeoutUnit, BackpressureStrategy.MISSING).toObservable();
    }

    @RequiresPermission(anyOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public Observable<Location> updates(@NonNull LocationRequest locationRequest, @NonNull Looper looper) {
        return updatesInternal(locationRequest, looper, null, null, BackpressureStrategy.MISSING).toObservable();
    }

    @RequiresPermission(anyOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public Observable<Location> updates(@NonNull LocationRequest locationRequest, @NonNull Looper looper, long timeoutTime, @NonNull TimeUnit timeoutUnit) {
        return updatesInternal(locationRequest, looper, timeoutTime, timeoutUnit, BackpressureStrategy.MISSING).toObservable();
    }

    @RequiresPermission(anyOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public Flowable<Location> updates(@NonNull LocationRequest locationRequest, BackpressureStrategy backpressureStrategy) {
        return updatesInternal(locationRequest, null, null, null, backpressureStrategy);
    }

    @RequiresPermission(anyOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public Flowable<Location> updates(@NonNull LocationRequest locationRequest, long timeoutTime, @NonNull TimeUnit timeoutUnit, BackpressureStrategy backpressureStrategy) {
        return updatesInternal(locationRequest, null, timeoutTime, timeoutUnit, backpressureStrategy);
    }

    @RequiresPermission(anyOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public Flowable<Location> updates(@NonNull LocationRequest locationRequest, @NonNull Looper looper, BackpressureStrategy backpressureStrategy) {
        return updatesInternal(locationRequest, looper, null, null, backpressureStrategy);
    }

    @RequiresPermission(anyOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public Flowable<Location> updates(@NonNull LocationRequest locationRequest, @NonNull Looper looper, long timeoutTime, @NonNull TimeUnit timeoutUnit, BackpressureStrategy backpressureStrategy) {
        return updatesInternal(locationRequest, looper, timeoutTime, timeoutUnit, backpressureStrategy);
    }

    private Flowable<Location> updatesInternal(LocationRequest locationRequest, Looper looper, Long timeoutTime, TimeUnit timeoutUnit, BackpressureStrategy backpressureStrategy) {
        return Flowable.create(new LocationUpdatesFlowableOnSubscribe(rxLocation, locationRequest, looper, timeoutTime, timeoutUnit), backpressureStrategy);
    }


    // Request Updates

    @RequiresPermission(anyOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public Single<Status> requestUpdates(@NonNull LocationRequest locationRequest, @NonNull PendingIntent pendingIntent) {
        return requestUpdatesInternal(locationRequest, pendingIntent, null, null);
    }

    @RequiresPermission(anyOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public Single<Status> requestUpdates(@NonNull LocationRequest locationRequest, @NonNull PendingIntent pendingIntent, long timeoutTime, @NonNull TimeUnit timeoutUnit) {
        return requestUpdatesInternal(locationRequest, pendingIntent, timeoutTime, timeoutUnit);
    }

    private Single<Status> requestUpdatesInternal(LocationRequest locationRequest, PendingIntent pendingIntent, Long timeoutTime, TimeUnit timeoutUnit) {
        return Single.create(new LocationRequestUpdatesSingleOnSubscribe(rxLocation, locationRequest, pendingIntent, timeoutTime, timeoutUnit));
    }


    // Remove Updates

    public Single<Status> removeUpdates(@NonNull PendingIntent pendingIntent) {
        return removeUpdatesInternal(pendingIntent, null, null);
    }

    public Single<Status> removeUpdates(@NonNull PendingIntent pendingIntent, long timeoutTime, @NonNull TimeUnit timeoutUnit) {
        return removeUpdatesInternal(pendingIntent, timeoutTime, timeoutUnit);
    }

    private Single<Status> removeUpdatesInternal(PendingIntent pendingIntent, Long timeoutTime, TimeUnit timeoutUnit) {
        return Single.create(new LocationRemoveUpdatesSingleOnSubscribe(rxLocation, pendingIntent, timeoutTime, timeoutUnit));
    }

}
