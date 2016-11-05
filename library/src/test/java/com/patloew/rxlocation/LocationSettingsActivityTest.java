package com.patloew.rxlocation;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;

import com.google.android.gms.common.api.Status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.UUID;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.spy;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ SettingsCheckHandleSingleOnSubscribe.class, Status.class })
public class LocationSettingsActivityTest  {

    @Mock Status status;
    @Mock Intent intent;
    LocationSettingsActivity activity;

    final String observableId = UUID.randomUUID().toString();

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.spy(SettingsCheckHandleSingleOnSubscribe.class);

        activity = spy(new LocationSettingsActivity());

        doReturn(observableId).when(intent).getStringExtra(LocationSettingsActivity.ARG_ID);
        doReturn(status).when(intent).getParcelableExtra(LocationSettingsActivity.ARG_STATUS);
        doReturn(intent).when(activity).getIntent();
    }

    @Test
    public void onCreate() {
        activity.onCreate(null);
        doAnswer(invocation -> null).when(activity).handleIntent();
        verify(activity).handleIntent();
    }

    @Test
    public void onNewIntent() {
        activity.onNewIntent(intent);

        doAnswer(invocation -> null).when(activity).handleIntent();
        verify(activity).setIntent(intent);
        verify(activity).handleIntent();
    }

    @Test
    public void handleIntent() throws IntentSender.SendIntentException {
        activity.handleIntent();
        verify(status).startResolutionForResult(activity, LocationSettingsActivity.REQUEST_CODE_RESOLUTION);
    }

    @Test
    public void handleIntent_SendIntentException() throws IntentSender.SendIntentException {
        doThrow(new IntentSender.SendIntentException()).when(status).startResolutionForResult(activity, LocationSettingsActivity.REQUEST_CODE_RESOLUTION);
        activity.handleIntent();

    }

    @Test
    public void onActivityResult() {
        activity.onActivityResult(LocationSettingsActivity.REQUEST_CODE_RESOLUTION, Activity.RESULT_OK, null);
        verify(activity).setResolutionResultAndFinish(Activity.RESULT_OK);
    }

    @Test
    public void onActivityResult_wrongRequestCode() {
        activity.onActivityResult(-123, Activity.RESULT_OK, null);
        verify(activity).setResolutionResultAndFinish(Activity.RESULT_CANCELED);
    }

    @Test
    public void setResolutionResultAndFinish_OK() {
        activity.setResolutionResultAndFinish(Activity.RESULT_OK);

        PowerMockito.verifyStatic();
        SettingsCheckHandleSingleOnSubscribe.onResolutionResult(observableId, Activity.RESULT_OK);

        verify(activity).finish();
    }

    @Test
    public void setResolutionResultAndFinish_Canceled() {
        activity.setResolutionResultAndFinish(Activity.RESULT_CANCELED);

        PowerMockito.verifyStatic();
        SettingsCheckHandleSingleOnSubscribe.onResolutionResult(observableId, Activity.RESULT_CANCELED);

        verify(activity).finish();
    }

}
