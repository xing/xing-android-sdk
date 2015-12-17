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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.xing.android.sdk.sample.adapters.ContactsRecyclerAdapter;
import com.xing.android.sdk.sample.utils.EndlessRecyclerOnScrollListener;
import com.xing.android.sdk.sample.utils.RecyclerItemClickListener;

public class ContactsActivity extends BaseActivity implements RecyclerItemClickListener.OnItemClickListener {
    //The amount of contacts that should be loaded at a time
    private static final int CONTACT_BATCH_SIZE = 20;
    private static final String ME = "me";
    //Parameter to sort contacts by last name, currently this is the only field supported
    private static final String SORT_LAST_NAME = "last_name";

    private ContactsRecyclerAdapter adapter;

    //Boolean to see if the load more functionality should be triggered
    private boolean shouldLoadMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Setting the adapter passing the contact list of a user
        // and the default on which we contacts will be rendered
        adapter = new ContactsRecyclerAdapter(this, R.layout.contact_view);

        //Initialize the RecyclerView
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.contacts_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, this));

        //Adding the onScrollListener to the recyclerView
        // in order to get notified when the user reaches the end of the list
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadMore();
            }
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
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
        String userId = adapter.getItem(position).getId();
        showToast("Item clicked with id " + userId);
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(ProfileActivity.EXTRA_USER_ID, userId);
        startActivity(intent);
    }
}


