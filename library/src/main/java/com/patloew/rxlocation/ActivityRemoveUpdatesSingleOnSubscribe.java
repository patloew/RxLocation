package com.patloew.rxlocation;

import android.app.PendingIntent;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;

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
class ActivityRemoveUpdatesSingleOnSubscribe extends RxLocationSingleOnSubscribe<Status> {

    final PendingIntent pendingIntent;

    ActivityRemoveUpdatesSingleOnSubscribe(@NonNull RxLocation rxLocation, PendingIntent pendingIntent, Long timeout, TimeUnit timeUnit) {
        super(rxLocation, timeout, timeUnit);
        this.pendingIntent = pendingIntent;
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, SingleEmitter<Status> emitter) {
        //noinspection MissingPermission
        setupLocationPendingResult(
                ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(apiClient, pendingIntent),
                SingleResultCallBack.get(emitter)
        );
    }
}
