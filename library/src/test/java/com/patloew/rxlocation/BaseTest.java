package com.patloew.rxlocation;

import android.content.Context;
import android.support.annotation.CallSuper;

import org.mockito.Mock;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.when;

public abstract class BaseTest {

    protected static final long TIMEOUT_TIME = 1L;
    protected static final TimeUnit TIMEOUT_TIMEUNIT = TimeUnit.SECONDS;

    @Mock Context ctx;

    RxLocation rxLocation;

    @CallSuper
    public void setup() throws Exception {
        when(ctx.getApplicationContext()).thenReturn(ctx);

        rxLocation = new RxLocation(ctx);
    }

    protected static final void assertNoTimeoutSet(RxLocationBaseOnSubscribe rxLocationBaseOnSubscribe) {
        assertNull(rxLocationBaseOnSubscribe.timeoutTime);
        assertNull(rxLocationBaseOnSubscribe.timeoutUnit);
    }

    protected static final void assertTimeoutSet(RxLocationBaseOnSubscribe rxLocationBaseOnSubscribe) {
        assertEquals(TIMEOUT_TIME, (long) rxLocationBaseOnSubscribe.timeoutTime);
        assertEquals(TIMEOUT_TIMEUNIT, rxLocationBaseOnSubscribe.timeoutUnit);
    }
}
