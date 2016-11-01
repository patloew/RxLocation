package com.patloew.rxlocationsample;

import android.location.Address;
import android.location.Location;

import com.google.android.gms.location.LocationRequest;
import com.patloew.rxlocation.FusedLocation;
import com.patloew.rxlocation.Geocoding;
import com.patloew.rxlocation.RxLocation;
import com.patloew.rxlocation.LocationSettings;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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
@RunWith(JUnit4.class)
public class MainPresenterTest {

    @Rule public RxSchedulersOverrideRule rxSchedulersOverrideRule = new RxSchedulersOverrideRule();

    @Mock RxLocation rxLocation;
    @Mock FusedLocation fusedLocation;
    @Mock
    LocationSettings locationSettings;
    @Mock Geocoding geocoding;

    @Mock MainView mainView;

    MainPresenter mainPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        doReturn(fusedLocation).when(rxLocation).location();
        doReturn(locationSettings).when(rxLocation).settings();
        doReturn(geocoding).when(rxLocation).geocoding();

        mainPresenter = new MainPresenter(rxLocation);
    }

    @Test
    public void settingsSatisfied_1Location() {
        Location loc = Mockito.mock(Location.class);
        Address address = Mockito.mock(Address.class);

        doReturn(Single.just(true)).when(locationSettings).checkAndHandleResolution(Matchers.any(LocationRequest.class));
        doReturn(Observable.just(loc)).when(fusedLocation).updates(Matchers.any(LocationRequest.class));
        doReturn(Maybe.just(address)).when(geocoding).fromLocation(Matchers.any(android.location.Location.class));

        mainPresenter.attachView(mainView);

        verify(mainView).onLocationUpdate(loc);
        verify(mainView).onAddressUpdate(address);
        verifyNoMoreInteractions(mainView);
    }

    @Test
    public void settingsNotSatisfied_LastLocation() {
        Location loc = Mockito.mock(Location.class);
        Address address = Mockito.mock(Address.class);

        doReturn(Single.just(false)).when(locationSettings).checkAndHandleResolution(Matchers.any(LocationRequest.class));
        doReturn(Maybe.just(loc)).when(fusedLocation).lastLocation();
        doReturn(Maybe.just(address)).when(geocoding).fromLocation(Matchers.any(android.location.Location.class));

        mainPresenter.attachView(mainView);

        verify(mainView).onLocationSettingsUnsuccessful();
        verify(mainView).onLocationUpdate(loc);
        verify(mainView).onAddressUpdate(address);
        verifyNoMoreInteractions(mainView);
    }

    @Test
    public void settingsNotSatisfied_NoLastLocation() {
        doReturn(Single.just(false)).when(locationSettings).checkAndHandleResolution(Matchers.any(LocationRequest.class));
        doReturn(Maybe.empty()).when(fusedLocation).lastLocation();
        mainPresenter.attachView(mainView);

        verify(mainView).onLocationSettingsUnsuccessful();
        verifyNoMoreInteractions(mainView);
    }
}
