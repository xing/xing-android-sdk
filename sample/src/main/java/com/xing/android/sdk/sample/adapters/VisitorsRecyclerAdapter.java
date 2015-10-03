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

package com.xing.android.sdk.sample.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.xing.android.sdk.model.user.ProfileVisit;
import com.xing.android.sdk.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * The adapter for the recycler view that displays the users visitors.
 *
 * @author daniel.hartwich
 */
public class VisitorsRecyclerAdapter extends RecyclerView.Adapter<VisitorsRecyclerAdapter.ViewHolder> {
    private static final String PLACEHOLDER_URL =
            "https://x1.xingassets.com/assets/frontend_minified/img/users/nobody_m.256x256.jpg";

    private final List<ProfileVisit> items;
    private final int itemLayoutRes;
    private final RequestManager glideRequestManager;
    private final LayoutInflater layoutInflater;

    public VisitorsRecyclerAdapter(Context ctx, int itemLayoutResId) {
        items = new ArrayList<>(0);
        itemLayoutRes = itemLayoutResId;
        glideRequestManager = Glide.with(ctx);
        layoutInflater = LayoutInflater.from(ctx);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(itemLayoutRes, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProfileVisit item = items.get(position);
        if (!TextUtils.isEmpty(item.getDisplayName())) {
            holder.visitorDisplayName.setText(item.getDisplayName());
            holder.visitorCompanyName.setText(item.getCompanyName());
            holder.visitorJobTitle.setText(item.getJobTitle());
            holder.visitorVisitCount.setText(item.getVisitCount() + ". visit");
        } else {
            holder.visitorDisplayName.setText("External Visitor \n" + item.getReason());
            holder.visitorCompanyName.setText("---");
            holder.visitorJobTitle.setText(("---"));
            holder.visitorVisitCount.setText(("---"));
        }
        if (item.getPhotoUrls().getPhotoSize256Url() != null) {
            glideRequestManager.
                    load(item.getPhotoUrls().getPhotoSize256Url().toString()).
                    into(holder.visitorPictureView);
        } else {
            glideRequestManager.
                    load(PLACEHOLDER_URL).
                    into(holder.visitorPictureView);
        }
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ProfileVisit getItem(int position) {
        return items.get(position);
    }

    public void addItems(@Nullable List<ProfileVisit> profileVisits) {
        if (profileVisits != null && !profileVisits.isEmpty()) {
            int count = getItemCount();
            items.addAll(profileVisits);
            notifyItemRangeInserted(count, items.size());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView visitorDisplayName;
        public TextView visitorCompanyName;
        public TextView visitorJobTitle;
        public TextView visitorVisitCount;
        public ImageView visitorPictureView;

        public ViewHolder(View itemView) {
            super(itemView);
            visitorDisplayName = (TextView) itemView.findViewById(R.id.visitor_display_name);
            visitorCompanyName = (TextView) itemView.findViewById(R.id.visitor_company_name);
            visitorJobTitle = (TextView) itemView.findViewById(R.id.visitor_job_title);
            visitorVisitCount = (TextView) itemView.findViewById(R.id.visitor_visit_count);
            visitorPictureView = (ImageView) itemView.findViewById(R.id.visitor_image);
        }
    }
}
