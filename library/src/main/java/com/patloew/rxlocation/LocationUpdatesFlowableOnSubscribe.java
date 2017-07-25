package com.patloew.rxlocation;

import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import io.reactivex.FlowableEmitter;

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
class LocationUpdatesFlowableOnSubscribe extends RxLocationFlowableOnSubscribe<Location> {

    final LocationRequest locationRequest;
    final Looper looper;
    LeakSafeRxLocationListener locationListener;

    protected LocationUpdatesFlowableOnSubscribe(@NonNull RxLocation rxLocation, LocationRequest locationRequest, Looper looper, Long timeout, TimeUnit timeUnit) {
        super(rxLocation, timeout, timeUnit);
        this.locationRequest = locationRequest;
        this.looper = looper;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, FlowableEmitter<Location> emitter) {
        locationListener = new LeakSafeRxLocationListener(emitter);

        //noinspection MissingPermission
        setupLocationPendingResult(
                LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, locationListener, looper),
                new StatusErrorResultCallBack(emitter)
        );
    }

    @Override
    protected void onUnsubscribed(GoogleApiClient apiClient) {
        LocationServices.FusedLocationApi.removeLocationUpdates(apiClient, locationListener);
        locationListener.onUnsubscribed();
        locationListener = null;
    }

    /**
     * WeakReference mitigates a bug in Google Play services which causes a memory leak of the
     * registered {@link LocationListener} even if {@link
     * FusedLocationProviderApi#removeLocationUpdates(GoogleApiClient, LocationListener)} is called
     * correctly. If the {@link LocationListener} is directly referencing an activity context, it will
     * leak heavy stuff (View hierarchy etc.). Using {@link LeakSafeRxLocationListener} only the
     * listener will leak and not the containing observer.
     *
     * @see <a
     *     href="https://issuetracker.google.com/issues/37126862">https://issuetracker.google.com/issues/37126862</a>
     */
    static class LeakSafeRxLocationListener implements LocationListener {
        private WeakReference<FlowableEmitter<? super Location>> weakRefEmitter;

        LeakSafeRxLocationListener(FlowableEmitter<? super Location> emitter) {
            this.weakRefEmitter = new WeakReference<>(emitter);
        }

        void onUnsubscribed() {
            weakRefEmitter = null;
        }

        @Override
        public void onLocationChanged(Location location) {
            if (weakRefEmitter.get() == null) {
                return;
            }
            weakRefEmitter.get().onNext(location);
        }
    }
}
