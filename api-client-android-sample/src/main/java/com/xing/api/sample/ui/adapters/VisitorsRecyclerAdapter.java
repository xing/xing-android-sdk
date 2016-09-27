/*
 * Copyright 2016 XING AG (http://xing.com/)
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
package com.xing.api.sample.ui.adapters;

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
import com.xing.api.data.profile.ProfileVisit;
import com.xing.api.sample.R;

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
        if (!TextUtils.isEmpty(item.displayName())) {
            holder.visitorDisplayName.setText(item.displayName());
            holder.visitorCompanyName.setText(item.companyName());
            holder.visitorJobTitle.setText(item.jobTitle());
            holder.visitorVisitCount.setText(item.visitCount() + ". visit");
        } else {
            holder.visitorDisplayName.setText("External Visitor \n" + item.reason());
            holder.visitorCompanyName.setText("---");
            holder.visitorJobTitle.setText(("---"));
            holder.visitorVisitCount.setText(("---"));
        }
        if (item.photoUrls().photoSize256Url() != null) {
            glideRequestManager
                  .load(item.photoUrls().photoSize256Url())
                  .into(holder.visitorPictureView);
        } else {
            glideRequestManager
                  .load(PLACEHOLDER_URL)
                  .into(holder.visitorPictureView);
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
