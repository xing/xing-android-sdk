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
package com.xing.api.sample.ui.adapters;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.xing.api.data.profile.XingUser;
import com.xing.api.sample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * The adapter for the recycler view that displays all contacts of a user.
 *
 * @author daniel.hartwich
 */
public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ViewHolder> {
    private final ArrayList<XingUser> items;
    private final int itemLayoutRes;
    private final RequestManager glideRequestManager;
    private final LayoutInflater layoutInflater;

    public ContactsRecyclerAdapter(Context ctx, int itemLayoutResId) {
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
        XingUser item = items.get(position);
        holder.contactDisplayNameView.setText(item.displayName());
        // TODO Re-Enable this
//        if (item.getProfessionalExperience() != null) {
            holder.contactOccupationTitleView.setText(item.primaryOccupationName());
            holder.contactCompanyView.setText(item.primaryInstitutionName());
//        } else {
//            holder.contactOccupationTitleView.setText(item.getEducationBackground().getDegree());
//            holder.contactCompanyView.setText(item.getEducationBackground().getPrimarySchool().getName());
//        }
        glideRequestManager.load(item.photoUrls().photoSize256Url()).into(holder.contactPictureView);
        holder.itemView.setTag(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public XingUser getItem(int position) {
        return items.get(position);
    }

    public void addItems(@Nullable List<XingUser> newUsers) {
        if (newUsers != null && !newUsers.isEmpty()) {
            int count = getItemCount();
            items.addAll(newUsers);
            notifyItemRangeInserted(count, items.size());
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView contactDisplayNameView;
        public TextView contactOccupationTitleView;
        public TextView contactCompanyView;
        public ImageView contactPictureView;

        public ViewHolder(View itemView) {
            super(itemView);
            contactDisplayNameView = (TextView) itemView.findViewById(R.id.contact_display_name);
            contactOccupationTitleView = (TextView) itemView.findViewById(R.id.contact_position);
            contactCompanyView = (TextView) itemView.findViewById(R.id.contact_company);
            contactPictureView = (ImageView) itemView.findViewById(R.id.contact_picture);
        }
    }
}
