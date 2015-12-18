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
package com.xing.android.sdk.sample.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Downloads an Image from an URL asynchronously to the specified ImageView.
 *
 * @author daniel.hartwich
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;

    public DownloadImageTask(ImageView bmImage) {
        imageViewReference = new WeakReference<>(bmImage);
    }

    @Nullable
    @Override
    protected Bitmap doInBackground(String... urls) {
        Bitmap icon = null;
        try {
            icon = BitmapFactory.decodeStream(new URL(urls[0]).openStream());
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
        return icon;
    }

    @Override
    protected void onPostExecute(@Nullable Bitmap result) {
        if (imageViewReference.get() != null && result != null) {
            imageViewReference.get().setImageBitmap(result);
        }
    }
}
