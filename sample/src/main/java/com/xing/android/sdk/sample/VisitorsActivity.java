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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.xing.android.sdk.sample.adapters.VisitorsRecyclerAdapter;
import com.xing.android.sdk.sample.utils.EndlessRecyclerOnScrollListener;
import com.xing.android.sdk.sample.utils.RecyclerItemClickListener;
import com.xing.android.sdk.sample.utils.RecyclerItemClickListener.OnItemClickListener;
import com.xing.android.sdk.model.user.ProfileVisit;
import com.xing.android.sdk.network.XingController;
import com.xing.android.sdk.task.OnTaskFinishedListener;
import com.xing.android.sdk.task.profile_visits.VisitsTask;

import java.util.List;

public class VisitorsActivity extends BaseActivity implements
        OnTaskFinishedListener<List<ProfileVisit>>, OnItemClickListener {

    private VisitorsRecyclerAdapter adapter;

    //Boolean to see if the load more functionality should be triggered
    private boolean shouldLoadMore = true;

    //The amount of contacts that should be loaded at a time
    private static final int VISITS_BATCH_SIZE = 10;

    private static final String ME = "me";

    private VisitsTask mVisitsTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitors);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Executing the ContactsTask with the parameters specified above
        mVisitsTask = new VisitsTask(ME, null, null, null, true, this, this);
        XingController.getInstance().executeAsync(mVisitsTask);

        //Initialize the RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.visitors_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

        //Setting the adapter passing the contact list of a user
        // and the default on which we contacts will be rendered
        adapter = new VisitorsRecyclerAdapter(this, R.layout.visitor_view);

        //Adding the onScrollListener to the recyclerView
        // in order to get notified when the user reaches the end of the list
        recyclerView.addOnScrollListener(
                new EndlessRecyclerOnScrollListener(layoutManager) {
                    @Override
                    public void onLoadMore(int currentPage) {
                        loadMore();
                    }
                });

        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_visitors, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(@Nullable List<ProfileVisit> result) {
        if (result != null) {
            //After the first contacts are loaded successfully we set them to the adapter
            adapter.addItems(result);
            if (result.size() < VISITS_BATCH_SIZE) {
                //If the amount of received contacts is smaller than the batch size we know
                //that we've reached the end of the list so the next time load more should not be triggered
                shouldLoadMore = false;
            }
        } else {
            shouldLoadMore = false;
        }
    }

    @Override
    public void onError(Exception exception) {
        Log.d("Error in visitors", exception.getMessage());
    }

    private void loadMore() {
        if (shouldLoadMore) {
            XingController.getInstance().executeAsync(mVisitsTask);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        String userId = adapter.getItem(position).getUserId();
        if (userId != null && !TextUtils.isEmpty(userId)) {
            showToast("Item clicked with id " + userId);
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra(ProfileActivity.EXTRA_USER_ID, userId);
            startActivity(intent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        XingController.getInstance().cancelExecution(this);
    }
}


