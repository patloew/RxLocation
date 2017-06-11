package com.patloew.rxlocation;

import com.google.android.gms.common.data.DataBuffer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;


import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class DataBufferFlowableTest {
    @Mock DataBuffer<Integer> buffer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void from_1buffer() {
        when(buffer.iterator()).thenReturn(Collections.singletonList(1).iterator());

        DataBufferFlowable.from(buffer).test()
            .assertValue(1)
            .assertComplete();

        verify(buffer).release();
    }

    @Test
    public void from_0buffer() {
        when(buffer.iterator()).thenReturn(Collections.<Integer>emptyList().iterator());

        DataBufferFlowable.from(buffer).test()
            .assertNoValues()
            .assertComplete();

        verify(buffer).release();
    }

    @Test
    public void from_100000buffer() {
        List<Integer> list = Observable.range(1, 100000).toList().blockingGet();
        when(buffer.iterator()).thenReturn(list.iterator());

        DataBufferFlowable.from(buffer).test()
            .assertValues(list.toArray(new Integer[list.size()]))
            .assertComplete();

        verify(buffer).release();
    }
}
