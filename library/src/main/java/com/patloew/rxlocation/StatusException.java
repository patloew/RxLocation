package com.patloew.rxlocation;

import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.Status;

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
 * ------------
 *
 *
 * FILE MODIFIED by Patrick Löwenstein 2017
 */
public class StatusException extends RuntimeException {
    private final Result result;

    public StatusException(Result result) {
        super(result.getStatus().toString());
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    public Status getStatus() {
        return result.getStatus();
    }
}
