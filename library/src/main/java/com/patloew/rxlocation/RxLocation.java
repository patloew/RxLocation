package com.patloew.rxlocation;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

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
 * limitations under the License.
 *
 * -----------------------------
 *
 * Make sure to have the Location permission from on Marshmallow on,
 * if they are needed by your requests.
 */
public class RxLocation {

    final Context ctx;
    private final ActivityRecognition activityRecognition = new ActivityRecognition(this);
    private final FusedLocation fusedLocation = new FusedLocation(this);
    private final Geocoding geocoding;
    private final Geofencing geofencing = new Geofencing(this);
    private final LocationSettings locationSettings = new LocationSettings(this);
    Long timeoutTime = null;
    TimeUnit timeoutUnit = null;


    /* Creates a new RxLocation instance.
     *
     * @param ctx Context.
     */
    public RxLocation(@NonNull Context ctx) {
        this.ctx = ctx.getApplicationContext();
        this.geocoding = new Geocoding(ctx.getApplicationContext());
    }

    /* Set a default timeout for all requests to the Location APIs made in the lib.
     * When a timeout occurs, onError() is called with a StatusException.
     */
    public void setDefaultTimeout(long time, @NonNull TimeUnit timeUnit) {
        if (timeUnit != null) {
            timeoutTime = time;
            timeoutUnit = timeUnit;
        } else {
            throw new IllegalArgumentException("timeUnit parameter must not be null");
        }
    }

    /* Reset the default timeout.
     */
    public void resetDefaultTimeout() {
        timeoutTime = null;
        timeoutUnit = null;
    }


    public ActivityRecognition activity() {
        return activityRecognition;
    }

    public Geocoding geocoding() {
        return geocoding;
    }

    public Geofencing geofencing() {
        return geofencing;
    }

    public FusedLocation location() {
        return fusedLocation;
    }

    public LocationSettings settings() {
        return locationSettings;
    }

}
