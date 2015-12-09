/*
 * Copyright (c) 2015 XING AG (http://xing.com/)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.xing.android.sdk.sample;

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

import com.squareup.okhttp.logging.HttpLoggingInterceptor.Level;
import com.xing.android.sdk.Response;
import com.xing.android.sdk.XingApi;
import com.xing.android.sdk.model.SearchResult;
import com.xing.android.sdk.model.user.XingAddress;
import com.xing.android.sdk.model.user.XingUser;
import com.xing.android.sdk.network.XingController;
import com.xing.android.sdk.resources.UserProfilesResource;
import com.xing.android.sdk.sample.prefs.Prefs;
import com.xing.android.sdk.sample.utils.DownloadImageTask;
import com.xing.android.sdk.task.OnTaskFinishedListener;
import com.xing.android.sdk.task.profile_visits.CreateVisitTask;
import com.xing.android.sdk.task.user.UserDetailsTask;

import java.util.List;

public class ProfileActivity extends BaseActivity implements OnTaskFinishedListener<XingUser> {
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

    private Response<XingUser, Object> response;

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
                UserDetailsTask userDetailsTask = new UserDetailsTask(mUserId, null, this, this);
                XingController.getInstance().executeAsync(userDetailsTask);
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

    @Override
    public void onSuccess(@Nullable XingUser result) {
        if (result != null) {
            //Save the user id to the preferences since it might be needed in other parts of the app
            if (!TextUtils.isEmpty(mUserId)) {
                Prefs.getInstance(this).setUserId(result.getId());
                CreateVisitTask visitTask =
                      new CreateVisitTask(result.getId(), this, new OnTaskFinishedListener<Void>() {
                          @Override
                          public void onSuccess(@Nullable Void result) {
                          }

                          @Override
                          public void onError(Exception exception) {
                          }
                      });
                XingController.getInstance().executeAsync(visitTask);
            }

            if (result.getPhotoUrls().getPhotoSize256Url() != null) {
                new DownloadImageTask(userProfilePictureView).
                      execute(result.getPhotoUrls().getPhotoSize256Url().toString());
            }

            //After the request was succesfully executed update all fields with the appropriate values
            populateTextView(userDisplayNameView, result.getDisplayName());
            populateTextView(userCompanyView, result.getPrimaryInstitutionName());
            populateTextView(userPositionView, result.getPrimaryOccupationName());
            populateTextView(userPrivateAddress, formatAddress(result.getPrivateAddress()));
            populateTextView(userWorkAddress, formatAddress(result.getBusinessAddress()));
            populateTextView(userHaves, result.getHaves());
            populateTextView(userInterests, result.getInterests());
            populateTextView(userWants, result.getWants());
        }
    }

    private static void populateTextView(TextView textView, String value) {
        textView.setText(!TextUtils.isEmpty(value) ? value : "");
    }

    @Override
    public void onError(Exception exception) {
        Log.d("Error loading profile", exception.getMessage());
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
    private static String formatAddress(XingAddress address) {
        if (address != null) {
            StringBuilder sb = new StringBuilder();
            if (!TextUtils.isEmpty(address.getStreet())) {
                sb.append(address.getStreet()).append('\n');
            }
            if (!TextUtils.isEmpty(address.getZipCode())) {
                sb.append(address.getZipCode()).append(' ');
            }
            if (!TextUtils.isEmpty(address.getCity())) {
                sb.append(address.getCity()).append('\n');
            }
            if (!TextUtils.isEmpty(address.getCountry())) {
                sb.append(address.getCountry());
            }
            return sb.toString();
        } else {
            return "-";
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        XingController.getInstance().cancelExecution(this);
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
                  .logLevel(Level.BODY)
                  .build();
            UserProfilesResource profilesResource = api.resource(UserProfilesResource.class);

            Response<XingUser, Object> response = null;
            try {
                response = profilesResource.getYourProfile().execute();
                Response<List<SearchResult>, Object> xingUsers = profilesResource.findUsersByKeyword("Rocco Bruno")
                      .queryParam("user_fields", "display_name, id")
                      .execute();
                XingUser ss = xingUsers.body().get(0).getSearchResultItem();

                Log.d("Search ResultUser", ss.toString());

                Log.d("Search Request", xingUsers.body().toString());
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
            if (xingUser.getPhotoUrls().getPhotoSize256Url() != null) {
                new DownloadImageTask(userProfilePictureView).
                      execute(xingUser.getPhotoUrls().getPhotoSize256Url().toString());
            }
            populateTextView(userDisplayNameView, xingUser.getDisplayName());
            populateTextView(userCompanyView, xingUser.getPrimaryInstitutionName());
            populateTextView(userPositionView, xingUser.getPrimaryOccupationName());
            populateTextView(userPrivateAddress, formatAddress(xingUser.getPrivateAddress()));
            populateTextView(userWorkAddress, formatAddress(xingUser.getBusinessAddress()));
            populateTextView(userHaves, xingUser.getHaves());
            populateTextView(userInterests, xingUser.getInterests());
            populateTextView(userWants, xingUser.getWants());
        }
    }
}
