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

package com.xing.android.sdk.model.user.field;

import com.xing.android.sdk.json.Field;

/**
 * Represents the photos of a user.
 *
 * @author david.gonzalez
 */
@SuppressWarnings("unused")
public class PhotoUrlsField extends Field {

    public static final PhotoUrlsField LARGE = new PhotoUrlsField("large");
    public static final PhotoUrlsField MAXI_THUMB = new PhotoUrlsField("maxi_thumb");
    public static final PhotoUrlsField MEDIUM_THUMB = new PhotoUrlsField("medium_thumb");
    public static final PhotoUrlsField MINI_THUMB = new PhotoUrlsField("mini_thumb");
    public static final PhotoUrlsField THUMB = new PhotoUrlsField("thumb");
    public static final PhotoUrlsField SIZE_32X32 = new PhotoUrlsField("size_32x32");
    public static final PhotoUrlsField SIZE_48X48 = new PhotoUrlsField("size_48x48");
    public static final PhotoUrlsField SIZE_64X64 = new PhotoUrlsField("size_64x64");
    public static final PhotoUrlsField SIZE_96X96 = new PhotoUrlsField("size_96x96");
    public static final PhotoUrlsField SIZE_128X128 = new PhotoUrlsField("size_128x128");
    public static final PhotoUrlsField SIZE_192X192 = new PhotoUrlsField("size_192x192");
    public static final PhotoUrlsField SIZE_256X256 = new PhotoUrlsField("size_256x256");
    public static final PhotoUrlsField SIZE_1024X1024 = new PhotoUrlsField("size_1024x1024");
    public static final PhotoUrlsField SIZE_ORIGINAL = new PhotoUrlsField("size_original");

    private PhotoUrlsField(String name) {
        super(name);
    }
}
