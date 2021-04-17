# Reactive Location API Library for Android

[![Build Status](https://travis-ci.org/patloew/RxLocation.svg?branch=master)](https://travis-ci.org/patloew/RxLocation) [![codecov](https://codecov.io/gh/patloew/RxLocation/branch/master/graph/badge.svg)](https://codecov.io/gh/patloew/RxLocation) [![Download](https://api.bintray.com/packages/patloew/maven/RxLocation/images/download.svg) ](https://bintray.com/patloew/maven/RxLocation/_latestVersion) [![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14)

**This library is now deprecated and not maintained anymore. Please switch to the [CoLocation](https://github.com/patloew/CoLocation) library.**

This library wraps the Location APIs in [RxJava 2](https://github.com/ReactiveX/RxJava/tree/2.x) Observables, Singles, Maybes and Completables. No more managing GoogleApiClients! Also, the resolution of the location settings check is optionally handled by the lib.

For [RxJava 1](https://github.com/ReactiveX/RxJava/tree/1.x), please take a look at the [Android-ReactiveLocation](https://github.com/mcharmas/Android-ReactiveLocation) library by Michał Charmas.

# Usage

Create an RxLocation instance once, preferably in your Application's `onCreate()` or by using a dependency injection framework. The RxLocation class is very similar to the classes provided by the Location APIs. Instead of `LocationServices.FusedLocationApi.getLastLocation(apiClient)` you can use `rxLocation.location().lastLocation()`. Make sure to have the Location and Activity Recognition permissions from Marshmallow on, if they are needed by your API requests.

Example:

```java
// Create one instance and share it
RxLocation rxLocation = new RxLocation(context);

LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000);

rxLocation.location().updates(locationRequest)
		.flatMap(location -> rxLocation.geocoding().fromLocation(location).toObservable())
		.subscribe(address -> {
			/* do something */
		});
```

The following APIs are wrapped by this library:

* `ActivityRecognition.ActivityRecognitionApi` via `rxLocation.activity()`
* `LocationServices.FusedLocationApi` via `rxLocation.location()`
* `LocationServices.GeofencingApi` via `rxLocation.geofencing()`
* `LocationServices.SettingsApi` via `rxLocation.settings()`
* `Geocoder` via `rxLocation.geocoding()`

Checking the location settings is simplified with this library, by providing a `Single<Boolean>` via `rxLocation.settings().checkAndHandleResolution(locationRequest)`, which handles showing the resolution dialog if the location settings do not satisfy your request. It returns `true` if the settings are satisfied (optionally after showing the dialog, if a resolution is possible), and `false` otherwise. If you want to handle the `LocationSettingsResult` yourself, you can do so via `rxLocation.settings().check(locationRequest)`.

An optional global default timeout for all Location API requests made through the library can be set via `rxLocation.setDefaultTimeout(...)`. In addition, timeouts can be set when creating a new Observable by providing timeout parameters, e.g. `rxLocation.geofencing().add(geofencingRequest, pendingIntent, 15, TimeUnit.SECONDS)`. These parameters override the default timeout. When a timeout occurs, a StatusException is provided via `onError()`. Keep in mind that these timeouts only apply to calls to the Location API, e.g. when registering a location update listener. As an example, the timeout provided to `rxLocation.location().updates(locationRequest, 15, TimeUnit.Seconds)` does *not* mean that you will not receive location updates anymore after 15 seconds. Use `setExpirationDuration()` on your locationRequest for this use case.

Don't forget to dispose of your Subscriber/Observer when you are finished:

```java
Disposable disposable = rxLocation.location().updates(locationRequest).subscribe();

// Dispose of your Observer when you no longer need updates
disposable.dispose();
```

As an alternative, multiple Disposables can be collected to dipose of at once via `CompositeDisposable`:

```java
CompositeDisposable disposable = new CompositeDisposable();
disposable.add(rxLocation.location().updates(locationRequest).subscribe());

// Dispose of all collected Disposables at once, e.g. in onDestroy()
disposable.clear();
```

You can also obtain a `Flowable<GoogleApiClient>`, which connects on subscribe and disconnects on dispose via `GoogleApiClientFlowable.create(...)`.

The following Exceptions are thrown in the lib and provided via `onError()`:

* `StatusException`: When the call to the Location APIs was not successful or timed out
* `GoogleApiConnectionException`: When connecting to the GoogleApiClient was not successful.
* `GoogleApiConnectionSuspendedException`: When the GoogleApiClient connection was suspended.
* `SecurityException`: When you try to call an API without proper permissions.
* `LocationSettingsNotSatisfiedException`: When you use `rxLocation.settings().checkAndHandleResolutionCompletable(...)` and the location settings were not satisfied, even after handling the resolution.

When using the Geocoding component of RxLocation, exceptions can be thrown even after disposing, which can lead to app crashes. To prevent this, install a global `RxJavaPlugins.setErrorHandler()`, e.g. in your Application class.

# Sample

A basic sample app is available in the `sample` project.

# Setup

The latest version 1.0.5 of this library is available on Maven Central. Add the following to your `build.gradle`:

```groovy
dependencies {
    compile 'com.patloew.rxlocation:rxlocation:1.0.5'
    compile 'com.google.android.gms:play-services-location:15.0.0'
}
```

If you want to use a newer version of Google Play Services, declare the newer version in your `build.gradle`. This then overrides the version declared in the library.

From v1.0.4 on, RxLocation only works with Android gradle plugin 3.0.0 or higher, since it uses Java 8 language features.  
And don't forget to set the source code compatibility to Java 8:

```groovy
android {
  compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
  }
}
```

# Testing

When unit testing your app's classes, RxLocation behavior can be mocked easily. See the `MainPresenterTest` in the `sample` project for an example test.

# Credits

The code for managing the GoogleApiClient was adapted from the [Android-ReactiveLocation](https://github.com/mcharmas/Android-ReactiveLocation) library by Michał Charmas, which is licensed under the Apache License, Version 2.0.

# Donations

If you like the library and want to support the creator for development/maintenance, you can make a donation in Bitcoin to `bc1q5uejfyl2kskhhveg7lx4fcwgv8hz88r92yzjsu`. Thank you!

# License

	Copyright 2016 Patrick Löwenstein

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	    http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
