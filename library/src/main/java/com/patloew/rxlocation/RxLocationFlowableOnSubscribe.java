package com.patloew.rxlocation;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

import java.util.concurrent.TimeUnit;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/* Copyright (C) 2015 Michał Charmas (http://blog.charmas.pl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ---------------
 *
 * FILE MODIFIED by Patrick Löwenstein, 2016
 *
 */
abstract class RxLocationFlowableOnSubscribe<T> extends RxLocationBaseOnSubscribe<T> implements FlowableOnSubscribe<T> {

    protected RxLocationFlowableOnSubscribe(@NonNull RxLocation rxLocation, Long timeout, TimeUnit timeUnit) {
        super(rxLocation, timeout, timeUnit);
    }

    protected RxLocationFlowableOnSubscribe(@NonNull Context ctx, @NonNull Api<? extends Api.ApiOptions.NotRequiredOptions>[] services, Scope[] scopes) {
        super(ctx, services, scopes);
    }

    @Override
    public final void subscribe(FlowableEmitter<T> emitter) throws Exception {
        final GoogleApiClient apiClient = createApiClient(new ApiClientConnectionCallbacks(emitter));

        try {
            apiClient.connect();
        } catch (Throwable ex) {
            emitter.onError(ex);
        }

        emitter.setCancellable(() -> {
            if (apiClient.isConnected()) {
                onUnsubscribed(apiClient);
            }

            apiClient.disconnect();
        });
    }

    protected abstract void onGoogleApiClientReady(GoogleApiClient apiClient, FlowableEmitter<T> emitter);

    protected class ApiClientConnectionCallbacks extends RxLocationBaseOnSubscribe.ApiClientConnectionCallbacks {

        final protected FlowableEmitter<T> emitter;

        private GoogleApiClient apiClient;

        private ApiClientConnectionCallbacks(FlowableEmitter<T> emitter) {
            this.emitter = emitter;
        }

        @Override
        public void onConnected(Bundle bundle) {
            try {
                onGoogleApiClientReady(apiClient, emitter);
            } catch (Throwable ex) {
                emitter.onError(ex);
            }
        }

        @Override
        public void onConnectionSuspended(int cause) {
            emitter.onError(new GoogleAPIConnectionSuspendedException(cause));
        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            emitter.onError(new GoogleAPIConnectionException("Error connecting to GoogleApiClient.", connectionResult));
        }

        public void setClient(GoogleApiClient client) {
            this.apiClient = client;
        }
    }
}
