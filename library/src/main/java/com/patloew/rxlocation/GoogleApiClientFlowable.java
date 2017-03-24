package com.patloew.rxlocation;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;

/* Copyright (C) 2015 Michał Charmas (http://blog.charmas.pl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * ---------------
 *
 * FILE MODIFIED by Patrick Löwenstein, 2016
 *
 */
public class GoogleApiClientFlowable extends RxLocationFlowableOnSubscribe<GoogleApiClient> {

    GoogleApiClientFlowable(Context ctx, Api<? extends Api.ApiOptions.NotRequiredOptions>[] apis, Scope[] scopes) {
        super(ctx, apis, scopes);
    }

    @SafeVarargs
    public static Flowable<GoogleApiClient> create(@NonNull Context context, @NonNull Api<? extends Api.ApiOptions.NotRequiredOptions>... apis) {
        return Flowable.create(new GoogleApiClientFlowable(context, apis, null), BackpressureStrategy.LATEST);
    }

    public static Flowable<GoogleApiClient> create(@NonNull Context context, @NonNull Api<? extends Api.ApiOptions.NotRequiredOptions>[] apis, Scope[] scopes) {
        return Flowable.create(new GoogleApiClientFlowable(context, apis, scopes), BackpressureStrategy.LATEST);
    }

    @Override
    protected void onGoogleApiClientReady(GoogleApiClient apiClient, FlowableEmitter<GoogleApiClient> emitter) {
        emitter.onNext(apiClient);
    }
}
