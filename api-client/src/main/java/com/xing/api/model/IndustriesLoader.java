/*
 * Copyright (С) 2015 XING AG (http://xing.com/)
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
package com.xing.api.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.xing.api.model.user.Industry;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Utility singleton used to retrieve a complete list of {@link Industry} objects starting from a JSON file
 * stored in the assets.
 *
 * @author angelo.marchesin
 */
public final class IndustriesLoader {
    private static final String TAG = IndustriesLoader.class.getSimpleName();
    private static final String EN = "en";
    private static final String DE = "de";
    private static final String ID = "id";
    private static final String LOCALIZED_NAME = "localized_name";
    private static final String SEGMENTS = "segments";
    private static final String INDUSTRIES_PATH_TO_FORMAT = "industries_%s.json";
    private static final String UTF_8 = "UTF-8";

    /**
     * Parses and returns the new industry list models. These are composed by a list of {@link Industry} object, in
     * which every instance can contain a {@link Industry.Segment} object representing the specification of the
     * parent class.
     *
     * @param context the context needed to access to the JSON file containing the resources
     * @return the list of industries completed with relative segments
     */
    @Nullable
    public static List<Industry> getIndustriesList(@NonNull Context context) {
        List<Industry> industries = null;
        try {
            Locale currentLocale = context.getResources().getConfiguration().locale;
            String pathSuffix = Locale.GERMANY.getLanguage().equals(currentLocale.getLanguage()) ? DE : EN;
            InputStream inputStream = context.getAssets().open(String.format(INDUSTRIES_PATH_TO_FORMAT, pathSuffix));
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
            reader.setLenient(true);
            industries = readJson(reader);
        } catch (Exception exception) {
            Log.e(TAG, exception.toString());
        }
        return industries;
    }

    /**
     * Parses and returns the new industry list models from a given JSON string.
     *
     * @param json    the JSON string to be parsed
     * @return the list of industries completed with relative segments
     */
    @Nullable
    public static List<Industry> getIndustriesList(@NonNull String json) {
        List<Industry> industries = null;
        try {
            InputStream inputStream = new ByteArrayInputStream(json.getBytes(UTF_8));
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
            reader.setLenient(true);
            industries = readJson(reader);
        } catch (Exception exception) {
            Log.e(TAG, exception.toString());
        }
        return industries;
    }

    @Nullable
    static List<Industry> readJson(@NonNull JsonReader reader) throws Exception {
        List<Industry> industries = null;
        if (reader.hasNext()) {
            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case EN:
                    case DE:
                        industries = readLocalizedList(reader);
                        break;
                }
            }
            reader.endObject();
        }
        return industries;
    }

    private static List<Industry> readLocalizedList(@NonNull JsonReader reader) throws Exception {
        List<Industry> industries = new ArrayList<>(0);
        reader.beginArray();
        while (reader.hasNext()) {
            industries.add(readIndustry(reader));
        }
        reader.endArray();
        return industries;
    }

    private static Industry readIndustry(@NonNull JsonReader reader) throws Exception {
        int industryId = 0;
        String industryTypeName = "";
        List<Industry.Segment> segments = new ArrayList<>(0);
        reader.beginObject();
        while (reader.hasNext()) {
            if (reader.peek() != JsonToken.END_OBJECT) {
                switch (reader.nextName()) {
                    case ID:
                        industryId = reader.nextInt();
                        break;

                    case LOCALIZED_NAME:
                        industryTypeName = reader.nextString();
                        break;

                    case SEGMENTS:
                        segments = readSegments(reader);
                        break;

                    default:
                }
            }
        }
        reader.endObject();
        return new Industry(industryId, industryTypeName, segments);
    }

    static List<Industry.Segment> readSegments(@NonNull JsonReader reader) throws Exception {
        List<Industry.Segment> segments = new ArrayList<>(0);
        reader.beginArray();
        while (reader.hasNext()) {
            segments.add(readSegment(reader));
        }
        reader.endArray();
        return segments;
    }

    private static Industry.Segment readSegment(@NonNull JsonReader reader) throws Exception {
        int segmentId = 0;
        String segmentTypeName = "";
        reader.beginObject();
        while (reader.hasNext()) {
            if (reader.peek() != JsonToken.END_OBJECT) {
                switch (reader.nextName()) {
                    case ID:
                        segmentId = reader.nextInt();
                        break;

                    case LOCALIZED_NAME:
                        segmentTypeName = reader.nextString();
                        break;

                    case SEGMENTS:
                        reader.skipValue();
                        break;

                    default:
                }
            }
        }
        reader.endObject();
        return new Industry.Segment(segmentId, segmentTypeName);
    }

    private IndustriesLoader() {
        throw new AssertionError("No instances.");
    }
}
