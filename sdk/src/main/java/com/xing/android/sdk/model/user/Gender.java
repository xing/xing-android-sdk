/*
 * Copyright (С) 2015 XING AG (http://xing.com/)
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
 * limitations under the License.
 */
package com.xing.android.sdk.model.user;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.ToJson;
import com.xing.android.sdk.model.JsonEnum;

/**
 * Represents users gender.
 *
 * @author david.gonzalez
 */
public enum Gender implements JsonEnum {
    MALE("m"),
    FEMALE("f");

    private final String gender;

    Gender(@NonNull String gender) {
        this.gender = gender;
    }

    @Override
    public String getJsonValue() {
        return gender;
    }

    @Nullable
    @FromJson
    public Gender fromJson(String value) {
        switch (value) {
            case "m":
                return MALE;
            case "f":
                return FEMALE;
            default:
                return null;
        }
    }

    @ToJson
    public String toJson(Gender gender) {
        return gender.gender;
    }

}
