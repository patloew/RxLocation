package com.patloew.rxlocation;

import android.app.PendingIntent;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import com.google.android.gms.common.api.Status;

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
public class ActivityRecognition {

    private final RxLocation rxLocation;

    ActivityRecognition(RxLocation rxLocation) {
        this.rxLocation = rxLocation;
    }


    // Request Updates

    @RequiresPermission("com.google.android.gms.permission.ACTIVITY_RECOGNITION")
    public Single<Status> requestUpdates(long detectionIntervalMillis, @NonNull PendingIntent pendingIntent) {
        return requestUpdatesInternal(detectionIntervalMillis, pendingIntent, null, null);
    }

    @RequiresPermission("com.google.android.gms.permission.ACTIVITY_RECOGNITION")
    public Single<Status> requestUpdates(long detectionIntervalMillis, @NonNull PendingIntent pendingIntent, long timeoutTime, @NonNull TimeUnit timeoutUnit) {
        return requestUpdatesInternal(detectionIntervalMillis, pendingIntent, timeoutTime, timeoutUnit);
    }

    private Single<Status> requestUpdatesInternal(long detectionIntervalMillis, PendingIntent pendingIntent, Long timeout, TimeUnit timeUnit) {
        return Single.create(new ActivityRequestUpdatesSingleOnSubscribe(rxLocation, detectionIntervalMillis, pendingIntent, timeout, timeUnit));
    }

    // Remove Updates

    @RequiresPermission("com.google.android.gms.permission.ACTIVITY_RECOGNITION")
    public Single<Status> removeUpdates(@NonNull PendingIntent pendingIntent) {
        return removeUpdatesInternal(pendingIntent, null, null);
    }

    @RequiresPermission("com.google.android.gms.permission.ACTIVITY_RECOGNITION")
    public Single<Status> removeUpdates(@NonNull PendingIntent pendingIntent, long timeoutTime, @NonNull TimeUnit timeoutUnit) {
        return removeUpdatesInternal(pendingIntent, timeoutTime, timeoutUnit);
    }

    private Single<Status> removeUpdatesInternal(PendingIntent pendingIntent, Long timeout, TimeUnit timeUnit) {
        return Single.create(new ActivityRemoveUpdatesSingleOnSubscribe(rxLocation, pendingIntent, timeout, timeUnit));
    }

}
