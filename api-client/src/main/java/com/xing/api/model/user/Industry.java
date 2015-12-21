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
package com.xing.api.model.user;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Represent the industry contained in a {@link ExperienceCompany}.
 *
 * @author angelo.marchesin
 */
@SuppressWarnings("unused")
public class Industry implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final float FIRST_CATEGORY_CONVERSION = 10000f;

    private final int id;
    private final List<Segment> segments;
    private String typeName;

    public static Industry newInstanceFromCompoundId(int compoundId) {
        Segment segment = new Segment(compoundId, "");
        return new Industry(extractFirstCategoryIndex(compoundId), "", Collections.singletonList(segment));
    }

    private static int extractFirstCategoryIndex(int encodedId) {
        return (int) (Math.floor(encodedId / FIRST_CATEGORY_CONVERSION) * FIRST_CATEGORY_CONVERSION);
    }

    public Industry(int id, String type) {
        this(id, type, null);
    }

    public Industry(int id, @NonNull String typeName, @Nullable List<Segment> segments) {
        this.id = id;
        this.typeName = typeName;
        this.segments = segments;
    }

    public int getId() {
        return id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return typeName;
    }

    @Nullable
    public List<Segment> getSegments() {
        return segments;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Industry && id == ((Industry) object).id;
    }

    @Override
    public int hashCode() {
        return typeName.hashCode();
    }

    /**
     * Represent a segment contained in the {@link Industry}'s list.
     *
     * @author angelo.marchesin
     */
    public static class Segment implements Serializable {
        private static final long serialVersionUID = -3150915425802346415L;

        private final int id;
        private String typeName;

        public Segment(int id, String typeName) {
            this.id = id;
            this.typeName = typeName;
        }

        public int getId() {
            return id;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        @Override
        public boolean equals(Object object) {
            return object instanceof Segment && id == ((Segment) object).id;
        }

        @Override
        public int hashCode() {
            return typeName.hashCode();
        }

        @Override
        public String toString() {
            return typeName;
        }
    }
}
