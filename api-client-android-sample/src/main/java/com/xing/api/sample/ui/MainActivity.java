/*
 * Copyright 2015 XING AG (http://xing.com/)
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
package com.xing.api.sample.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.xing.api.sample.Utils;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Utils.isLoggedIn(this)) {
            //If the user is logged in, send him directly to his Profile
            startActivity(new Intent(this, ProfileActivity.class));
        } else {
            //If the user is not logged in, send him to the Login Screen
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();
    }
}
