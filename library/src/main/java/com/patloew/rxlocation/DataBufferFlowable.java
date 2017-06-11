package com.patloew.rxlocation;

import android.support.annotation.NonNull;

import com.google.android.gms.common.data.DataBuffer;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.Disposables;

/* Copyright 2017 Muhammad Qureshi
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

public class DataBufferFlowable {
    private DataBufferFlowable() {
        // no instances
    }

    public static <T> Flowable<T> from(@NonNull final DataBuffer<T> buffer) {
        return Flowable.create(new DataBufferFlowableOnSubscribe<>(buffer), BackpressureStrategy.BUFFER);
    }

    private static class DataBufferFlowableOnSubscribe<T> implements FlowableOnSubscribe<T> {
        private final DataBuffer<T> buffer;

        private DataBufferFlowableOnSubscribe(DataBuffer<T> buffer) {
            this.buffer = buffer;
        }

        @Override
        public void subscribe(FlowableEmitter<T> e) throws Exception {
            e.setDisposable(Disposables.fromAction(buffer::release));
            for (T item : buffer) {
                e.onNext(item);
            }
            e.onComplete();
        }
    }
}
