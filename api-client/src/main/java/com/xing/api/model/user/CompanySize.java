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
package com.xing.api.model.user;

/**
 * Possible values for the company size.
 * <p/>
 *
 * @see <a href="https://dev.xing.com/docs/get/users/:id">User Profile</a>
 */
public enum CompanySize {
    SIZE_1("1"),
    SIZE_1_10("1-10"),
    SIZE_11_50("11-50"),
    SIZE_51_200("51-200"),
    SIZE_201_500("201-500"),
    SIZE_501_1000("501-1000"),
    SIZE_1001_5000("1001-5000"),
    SIZE_5001_10000("5001-10000"),
    SIZE_10001PLUS("10001+");

    private final String value;

    CompanySize(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
