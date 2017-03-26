package com.patloew.rxlocation;

import android.location.Location;
import android.support.annotation.NonNull;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import io.reactivex.MaybeEmitter;

class LocationLastMaybeOfOnSubscribe extends RxLocationMaybeOnSubscribe<Location> {

  LocationLastMaybeOfOnSubscribe(@NonNull RxLocation rxLocation) {
    super(rxLocation, null, null);
  }

  @Override
  protected void onGoogleApiClientReady(GoogleApiClient apiClient, MaybeEmitter<Location> emitter) {
    //noinspection MissingPermission
    Location location = LocationServices.FusedLocationApi.getLastLocation(apiClient);

    if (location != null) {
      emitter.onSuccess(location);
    } else {
      emitter.onError(new LocationUnavailableException());
    }
  }
}
