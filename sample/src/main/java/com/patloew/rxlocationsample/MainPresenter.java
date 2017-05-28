package com.patloew.rxlocationsample;

import android.location.Address;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationRequest;
import com.patloew.rxlocation.RxLocation;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
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

    private MainView view;

    public MainPresenter(RxLocation rxLocation) {
        this.rxLocation = rxLocation;

        this.locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000);
    }

    public void attachView(MainView view) {
        this.view = view;
        startLocationRefresh();
    }

    public void detachView() {
        this.view = null;
        disposable.clear();
    }

    public void startLocationRefresh() {
        disposable.add(
                rxLocation.settings().checkAndHandleResolution(locationRequest)
                        .flatMapObservable(new Function<Boolean, ObservableSource<Address>>() {
                            @Override
                            public ObservableSource<Address> apply(Boolean success) throws Exception {
                                return MainPresenter.this.getAddressObservable(success);
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Address>() {
                            @Override
                            public void accept(Address address) throws Exception {
                                view.onAddressUpdate(address);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.e("MainPresenter", "Error fetching location/address updates", throwable);
                            }
                        })
        );
    }

    private Observable<Address> getAddressObservable(boolean success) {
        if(success) {
            return rxLocation.location().updates(locationRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                    .doOnNext(new Consumer<Location>() {
                        @Override
                        public void accept(Location location) throws Exception {
                            view.onLocationUpdate(location);
                        }
                    })
                    .flatMap(new Function<Location, ObservableSource<? extends Address>>() {
                        @Override
                        public ObservableSource<? extends Address> apply(Location location) throws Exception {
                            return MainPresenter.this.getAddressFromLocation(location);
                        }
                    });

        } else {
            view.onLocationSettingsUnsuccessful();

            return rxLocation.location().lastLocation()
                    .doOnSuccess(new Consumer<Location>() {
                        @Override
                        public void accept(Location location) throws Exception {
                            view.onLocationUpdate(location);
                        }
                    })
                    .flatMapObservable(new Function<Location, ObservableSource<? extends Address>>() {
                        @Override
                        public ObservableSource<? extends Address> apply(Location location) throws Exception {
                            return MainPresenter.this.getAddressFromLocation(location);
                        }
                    });
        }
    }

    private Observable<Address> getAddressFromLocation(Location location) {
        return rxLocation.geocoding().fromLocation(location).toObservable()
                .subscribeOn(Schedulers.io());
    }


}
