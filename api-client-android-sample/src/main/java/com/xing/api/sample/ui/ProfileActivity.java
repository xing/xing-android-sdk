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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.xing.api.HttpError;
import com.xing.api.Response;
import com.xing.api.XingApi;
import com.xing.api.data.profile.Address;
import com.xing.api.data.profile.XingUser;
import com.xing.api.resources.UserProfilesResource;
import com.xing.api.sample.BuildConfig;
import com.xing.api.sample.Prefs;
import com.xing.api.sample.R;

import java.util.List;

public class ProfileActivity extends BaseActivity {
    private TextView userDisplayNameView;
    private TextView userPositionView;
    private TextView userCompanyView;
    private ImageView userProfilePictureView;

    private TextView userPrivateAddress;
    private TextView userWorkAddress;
    private TextView userHaves;
    private TextView userInterests;
    private TextView userWants;

    private String mUserId = "";

    public static final String EXTRA_USER_ID = "userid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar_actionbar));

        userDisplayNameView = (TextView) findViewById(R.id.user_display_name);
        userPositionView = (TextView) findViewById(R.id.user_position);
        userCompanyView = (TextView) findViewById(R.id.user_company);
        userProfilePictureView = (ImageView) findViewById(R.id.user_profile_picture);
        userPrivateAddress = (TextView) findViewById(R.id.user_private_address);
        userWorkAddress = (TextView) findViewById(R.id.user_work_address);
        userHaves = (TextView) findViewById(R.id.user_haves);
        userInterests = (TextView) findViewById(R.id.user_interests);
        userWants = (TextView) findViewById(R.id.user_wants);

        if (getIntent() != null) {
            mUserId = getIntent().getStringExtra(EXTRA_USER_ID);
            if (!TextUtils.isEmpty(mUserId)) {
            } else {
                //Execute the task in order to get the users profile
                new OwnProfileTask().execute(this);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (TextUtils.isEmpty(mUserId)) {
            //If User is on his own profile go ahead and show the menu
            getMenuInflater().inflate(R.menu.menu_profile, menu);
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_contacts:
                startActivity(new Intent(this, ContactsActivity.class));
                return true;
            case R.id.action_logout:
                Prefs.getInstance(this).logout();
                startActivity(new Intent(this, MainActivity.class).
                      setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                return true;
            case R.id.action_visitors:
                startActivity(new Intent(this, VisitorsActivity.class));
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static void populateTextView(TextView textView, @Nullable Object value) {
        textView.setText(value != null ? String.valueOf(value) : "");
    }

    /**
     * Takes a List of Strings and returns one String with '#' separated by a comma(,).
     *
     * @return hashedTaggedString String with all strings of a list
     */
    private static String getHashTaggedString(List<String> list) {
        StringBuilder hashTaggedString = new StringBuilder();
        int size = list == null ? 0 : list.size();
        for (int i = 0; i < size; i++) {
            if (i == size - 1) {
                hashTaggedString.append('#').append(list.get(i));
            } else {
                hashTaggedString.append('#').append(list.get(i)).append(", ");
            }
        }
        return hashTaggedString.toString();
    }

    /**
     * Takes a XingAddress and returns a 3 line String with an address.
     * Be aware that the country field only returns the country code (Germany => DE).
     */
    private static String formatAddress(Address address) {
        if (address != null) {
            StringBuilder sb = new StringBuilder();
            if (!TextUtils.isEmpty(address.street())) {
                sb.append(address.street()).append('\n');
            }
            if (!TextUtils.isEmpty(address.zipCode())) {
                sb.append(address.zipCode()).append(' ');
            }
            if (!TextUtils.isEmpty(address.city())) {
                sb.append(address.city()).append('\n');
            }
            if (!TextUtils.isEmpty(address.country())) {
                sb.append(address.country());
            }
            return sb.toString();
        } else {
            return "-";
        }
    }

    public class OwnProfileTask extends AsyncTask<Activity, Void, XingUser> {

        @Nullable
        @Override
        protected XingUser doInBackground(Activity... params) {
            XingApi api = new XingApi.Builder()
                  .consumerKey(BuildConfig.OAUTH_CONSUMER_KEY)
                  .consumerSecret(BuildConfig.OAUTH_CONSUMER_SECRET)
                  .accessToken(Prefs.getInstance(ProfileActivity.this).getOauthToken())
                  .accessSecret(Prefs.getInstance(ProfileActivity.this).getOauthSecret())
                  .apiEndpoint("https://api.xing.com")
                  .build();
            UserProfilesResource profilesResource = api.resource(UserProfilesResource.class);
            Response<XingUser, HttpError> response = null;
            try {
                response = profilesResource.getOwnProfile().execute();
                Response<List<XingUser>, HttpError> xingUsers = profilesResource.findUsersByKeyword("Rocco Bruno")
                      .queryParam("user_fields", "display_name, id")
                      .execute();
                XingUser ss = xingUsers.body().get(0);

                Log.w("Search ResultUser", ss.toString());
                Log.w("Search Request", xingUsers.body().toString());
                return response.body();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (response != null) {
                return response.body();
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(XingUser xingUser) {
            super.onPostExecute(xingUser);
            if (xingUser.photoUrls().photoSize256Url() != null) {
                Glide.with(userProfilePictureView.getContext())
                      .load(xingUser.photoUrls().photoSize256Url())
                      .into(userProfilePictureView);
            }
            populateTextView(userDisplayNameView, xingUser.displayName());
            populateTextView(userCompanyView, xingUser.primaryInstitutionName());
            populateTextView(userPositionView, xingUser.primaryOccupationName());
            populateTextView(userPrivateAddress, formatAddress(xingUser.privateAddress()));
            populateTextView(userWorkAddress, formatAddress(xingUser.businessAddress()));
            populateTextView(userHaves, xingUser.haves());
            populateTextView(userInterests, xingUser.interests());
            populateTextView(userWants, xingUser.wants());
        }
    }
}
