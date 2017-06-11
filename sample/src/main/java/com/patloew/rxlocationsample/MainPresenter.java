package com.patloew.rxlocationsample;

import android.location.Address;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.patloew.rxlocation.DataBufferFlowable;
import com.patloew.rxlocation.RxLocation;

import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

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
public class MainPresenter {

    private final CompositeDisposable disposable = new CompositeDisposable();

    private final RxLocation rxLocation;
    private final LocationRequest locationRequest;
    private final LatLngBounds bounds;
    private final AutocompleteFilter filter;

    private MainView view;

    public MainPresenter(RxLocation rxLocation) {
        this.rxLocation = rxLocation;

        this.locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000);

        this.bounds = new LatLngBounds(new LatLng(37.773967, -122.431302), new LatLng(37.773977, -122.431292));
        this.filter = new AutocompleteFilter.Builder()
            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
            .setCountry(Locale.US.getCountry())
            .build();
    }

    public void attachView(MainView view) {
        this.view = view;
        startLocationRefresh();
    }

    public void detachView() {
        this.view = null;
        disposable.clear();
    }

    public void onAutocompleteQueryChanged(String query) {
        disposable.add(
            rxLocation.geoData().autocompletePredictions(query, bounds, filter)
                .flatMapPublisher(DataBufferFlowable::from)
                .map(autocompletePrediction -> autocompletePrediction.getFullText(null).toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toList()
                .subscribe(view::onAutocompleteResultsUpdate, throwable -> Log.e("MainPresenter", "Error fetching autocomplete predictions", throwable))

        );
    }


    public void startLocationRefresh() {
        disposable.add(
                rxLocation.settings().checkAndHandleResolution(locationRequest)
                        .flatMapObservable(this::getAddressObservable)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(view::onAddressUpdate, throwable -> Log.e("MainPresenter", "Error fetching location/address updates", throwable))
        );
    }

    private Observable<Address> getAddressObservable(boolean success) {
        if(success) {
            return rxLocation.location().updates(locationRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .doOnNext(view::onLocationUpdate)
                            .flatMap(this::getAddressFromLocation);

        } else {
            view.onLocationSettingsUnsuccessful();

            return rxLocation.location().lastLocation()
                            .doOnSuccess(view::onLocationUpdate)
                            .flatMapObservable(this::getAddressFromLocation);
        }
    }

    private Observable<Address> getAddressFromLocation(Location location) {
        return rxLocation.geocoding().fromLocation(location).toObservable()
                .subscribeOn(Schedulers.io());
    }


}
