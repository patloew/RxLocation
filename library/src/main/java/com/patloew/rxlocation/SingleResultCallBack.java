package com.patloew.rxlocation;

import android.support.annotation.NonNull;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;

import io.reactivex.SingleEmitter;
import io.reactivex.functions.Function;

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
class SingleResultCallBack<T extends Result, R> implements ResultCallback<T> {

    private static final Function ID_FUNC = input -> input;

    private final SingleEmitter<R> emitter;
    private final Function<T, R> mapper;

    private SingleResultCallBack(@NonNull SingleEmitter<R> emitter, @NonNull Function<T, R> mapper) {
        this.emitter = emitter;
        this.mapper = mapper;
    }

    static <T extends Result, R> ResultCallback<T> get(@NonNull SingleEmitter<R> emitter, @NonNull Function<T, R> mapper) {
        return new SingleResultCallBack<>(emitter, mapper);
    }

    static <T extends Result> ResultCallback<T> get(@NonNull SingleEmitter<T> emitter) {
        //noinspection unchecked
        return new SingleResultCallBack<>(emitter, ID_FUNC);
    }

    @Override
    public void onResult(@NonNull T result) {
        if (!result.getStatus().isSuccess()) {
            emitter.onError(new StatusException(result.getStatus()));
        } else {
            try {
                emitter.onSuccess(mapper.apply(result));
            } catch (Exception e) {
                emitter.onError(e);
            }
        }
    }
}
