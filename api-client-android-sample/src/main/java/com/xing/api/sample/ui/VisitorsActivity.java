/*
 * Copyright 2016 XING SE (http://xing.com/)
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

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.xing.api.sample.R;
import com.xing.api.sample.ui.adapters.VisitorsRecyclerAdapter;
import com.xing.api.sample.ui.adapters.EndlessRecyclerOnScrollListener;
import com.xing.api.sample.ui.adapters.RecyclerItemClickListener;
import com.xing.api.sample.ui.adapters.RecyclerItemClickListener.OnItemClickListener;

public class VisitorsActivity extends BaseActivity implements OnItemClickListener {

    //The amount of contacts that should be loaded at a time
    private static final int VISITS_BATCH_SIZE = 10;
    private static final String ME = "me";
    private VisitorsRecyclerAdapter adapter;
    //Boolean to see if the load more functionality should be triggered
    private boolean shouldLoadMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visitors);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
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

    private void loadMore() {
        if (shouldLoadMore) {
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        String userId = adapter.getItem(position).userId();
        if (userId != null && !TextUtils.isEmpty(userId)) {
            showToast("Item clicked with id " + userId);
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra(ProfileActivity.EXTRA_USER_ID, userId);
            startActivity(intent);
        }
    }

}


