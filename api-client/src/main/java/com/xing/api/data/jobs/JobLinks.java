/*
 * Copyright (C) 2018 XING SE (http://xing.com/)
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
package com.xing.api.data.jobs;

import com.squareup.moshi.Json;

import java.io.Serializable;

public class JobLinks implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "pdf")
    private final String pdf;
    @Json(name = "self")
    private final String self;
    @Json(name = "xing")
    private final String xing;

    public JobLinks(String pdf, String self, String xing) {
        this.pdf = pdf;
        this.self = self;
        this.xing = xing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobLinks jobLinks = (JobLinks) o;

        return pdf != null ? pdf.equals(jobLinks.pdf) : jobLinks.pdf == null
              && (self != null ? self.equals(jobLinks.self) : jobLinks.self == null
              && (xing != null ? xing.equals(jobLinks.xing) : jobLinks.xing == null));
    }

    @Override
    public int hashCode() {
        int result = pdf != null ? pdf.hashCode() : 0;
        result = 31 * result + (self != null ? self.hashCode() : 0);
        result = 31 * result + (xing != null ? xing.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "JobLinks{"
              + "pdf='" + pdf + '\''
              + ", self='" + self + '\''
              + ", xing='" + xing + '\''
              + '}';
    }

    public String pdf() {
        return pdf;
    }

    public String self() {
        return self;
    }

    public String xing() {
        return xing;
    }
}
