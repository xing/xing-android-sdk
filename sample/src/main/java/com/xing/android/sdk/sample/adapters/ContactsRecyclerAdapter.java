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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.xing.android.sdk.model.user.XingUser;
import com.xing.android.sdk.sample.R;

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
        holder.contactDisplayNameView.setText(item.getDisplayName());
        // TODO Re-Enable this
//        if (item.getProfessionalExperience() != null) {
            holder.contactOccupationTitleView.setText(item.getPrimaryOccupationName());
            holder.contactCompanyView.setText(item.getPrimaryInstitutionName());
//        } else {
//            holder.contactOccupationTitleView.setText(item.getEducationBackground().getDegree());
//            holder.contactCompanyView.setText(item.getEducationBackground().getPrimarySchool().getName());
//        }
        glideRequestManager.load(item.getPhotoUrls().getPhotoSize256Url().toString()).into(holder.contactPictureView);
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
