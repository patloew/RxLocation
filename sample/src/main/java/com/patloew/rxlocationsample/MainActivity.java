package com.patloew.rxlocationsample;

import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.patloew.rxlocation.RxLocation;

import java.text.DateFormat;
import java.util.Date;
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
 * limitations under the License. */
public class MainActivity extends AppCompatActivity implements MainView {

    private static final DateFormat DATE_FORMAT = DateFormat.getDateTimeInstance();

    private TextView lastUpdate;
    private TextView locationText;
    private TextView addressText;

    private RxLocation rxLocation;

    private MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lastUpdate = (TextView) findViewById(R.id.tv_last_update);
        locationText = (TextView) findViewById(R.id.tv_current_location);
        addressText = (TextView) findViewById(R.id.tv_current_address);

        rxLocation = new RxLocation(this);
        rxLocation.setDefaultTimeout(15, TimeUnit.SECONDS);

        presenter = new MainPresenter(rxLocation);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.attachView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.detachView();
    }

    // View Interface

    @Override
    public void onLocationUpdate(Location location) {
        lastUpdate.setText(DATE_FORMAT.format(new Date()));
        locationText.setText(location.getLatitude() + ", " + location.getLongitude());
    }

    @Override
    public void onAddressUpdate(Address address) {
        addressText.setText(getAddressText(address));
    }

    @Override
    public void onLocationSettingsUnsuccessful() {
        Snackbar.make(lastUpdate, "Location settings requirements not satisfied. Showing last known location if available.", Snackbar.LENGTH_INDEFINITE)
                .setAction("Retry", view -> presenter.startLocationRefresh())
                .show();
    }

    private String getAddressText(Address address) {
        String addressText = "";
        final int maxAddressLineIndex = address.getMaxAddressLineIndex();

        for(int i=0; i<=maxAddressLineIndex; i++) {
            addressText += address.getAddressLine(i);
            if(i != maxAddressLineIndex) { addressText += "\n"; }
        }

        return addressText;
    }

}
