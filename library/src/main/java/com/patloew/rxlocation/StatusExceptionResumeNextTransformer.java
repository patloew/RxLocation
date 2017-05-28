package com.patloew.rxlocation;

import com.google.android.gms.common.api.Result;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.functions.Function;

/* Copyright 2017 Patrick Löwenstein
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
public class StatusExceptionResumeNextTransformer {

    public static <R extends Result> FlowableTransformer<R, R> forFlowable() {
        return new FlowableTransformer<R, R>() {
            @Override
            public Publisher<R> apply(Flowable<R> upstream) {
                return upstream.onErrorResumeNext(new Function<Throwable, Publisher<? extends R>>() {
                    @Override
                    public Publisher<? extends R> apply(Throwable throwable) throws Exception {
                        if (throwable instanceof StatusException) {
                            StatusException statusException = (StatusException) throwable;

                            if (statusException.getStatus().hasResolution()) {
                                return Flowable.just((R) statusException.getResult());
                            } else {
                                return Flowable.error(throwable);
                            }

                        } else {
                            return Flowable.error(throwable);
                        }
                    }
                });
            }
        };
    }

    public static <R extends Result> ObservableTransformer<R, R> forObservable() {
        return new ObservableTransformer<R, R>() {
            @Override
            public ObservableSource<R> apply(Observable<R> upstream) {
                return upstream.onErrorResumeNext(new Function<Throwable, ObservableSource<? extends R>>() {
                    @Override
                    public ObservableSource<? extends R> apply(Throwable throwable) throws Exception {
                        if (throwable instanceof StatusException) {
                            StatusException statusException = (StatusException) throwable;

                            if (statusException.getStatus().hasResolution()) {
                                return Observable.just((R) statusException.getResult());
                            } else {
                                return Observable.error(throwable);
                            }

                        } else {
                            return Observable.error(throwable);
                        }
                    }
                });
            }
        };
    }

    public static <R extends Result> SingleTransformer<R, R> forSingle() {
        return new SingleTransformer<R, R>() {
            @Override
            public SingleSource<R> apply(Single<R> upstream) {
                return upstream.onErrorResumeNext(new Function<Throwable, SingleSource<? extends R>>() {
                    @Override
                    public SingleSource<? extends R> apply(Throwable throwable) throws Exception {
                        if (throwable instanceof StatusException) {
                            StatusException statusException = (StatusException) throwable;

                            if (statusException.getStatus().hasResolution()) {
                                return Single.just((R) statusException.getResult());
                            } else {
                                return Single.error(throwable);
                            }

                        } else {
                            return Single.error(throwable);
                        }
                    }
                });
            }
        };
    }
}
