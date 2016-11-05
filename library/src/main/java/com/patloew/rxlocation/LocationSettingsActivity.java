package com.patloew.rxlocation;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.gms.common.api.Status;

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
public class LocationSettingsActivity extends Activity {

    protected static final String ARG_STATUS = "status";
    protected static final String ARG_ID = "id";

    static final int REQUEST_CODE_RESOLUTION = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            handleIntent();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent();
    }

    void handleIntent() {
        Status status = getIntent().getParcelableExtra(ARG_STATUS);

        try {
            status.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (IntentSender.SendIntentException | NullPointerException e) {

            setResolutionResultAndFinish(Activity.RESULT_CANCELED);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_RESOLUTION) {
            setResolutionResultAndFinish(resultCode);
        } else {
            setResolutionResultAndFinish(Activity.RESULT_CANCELED);
        }
    }

    void setResolutionResultAndFinish(int resultCode) {
        SettingsCheckHandleSingleOnSubscribe.onResolutionResult(getIntent().getStringExtra(ARG_ID), resultCode);
        finish();
    }
}