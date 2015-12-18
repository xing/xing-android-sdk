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

import com.xing.android.sdk.model.JsonEnum;

/**
 * Possible values for the company size.
 * <p/>
 *
 * @author serj.lotutovici
 * @see <a href="https://dev.xing.com/docs/get/users/:id">User Profile</a>
 */
public enum CompanySize implements JsonEnum {

    SIZE_1("1"),
    SIZE_1_10("1-10"),
    SIZE_11_50("11-50"),
    SIZE_51_200("51-200"),
    SIZE_201_500("201-500"),
    SIZE_501_1000("501-1000"),
    SIZE_1001_5000("1001-5000"),
    SIZE_5001_10000("5001-10000"),
    SIZE_10001PLUS("10001+");

    private final String text;

    CompanySize(@NonNull String text) {
        this.text = text;
    }

    @Override
    public String getJsonValue() {
        return text;
    }
}
