/*
 * Copyright (С) 2018 XING SE (http://xing.com/)
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
package com.xing.api.data.groups;

import com.squareup.moshi.Json;

import java.io.Serializable;

/**
 * Logo Urls model representation that is returned by the Groups call.
 */
public class LogoUrls implements Serializable {
    private static final long serialVersionUID = 1L;

    @Json(name = "size_40x40")
    private String logoSize40Url;
    @Json(name = "size_70x70")
    private String logoSize70Url;
    @Json(name = "size_140x140")
    private String logoSize140Url;
    @Json(name = "size_280x280")
    private String logoSize280Url;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LogoUrls logoUrls = (LogoUrls) o;

        if (logoSize40Url != null ? !logoSize40Url.equals(logoUrls.logoSize40Url) : logoUrls.logoSize40Url != null) {
            return false;
        }
        if (logoSize70Url != null ? !logoSize70Url.equals(logoUrls.logoSize70Url) : logoUrls.logoSize70Url != null) {
            return false;
        }
        if (logoSize140Url != null ? !logoSize140Url.equals(logoUrls.logoSize140Url) : logoUrls.logoSize140Url != null) {
            return false;
        }
        return logoSize280Url != null ? logoSize280Url.equals(logoUrls.logoSize280Url) : logoUrls.logoSize280Url == null;
    }

    @Override
    public int hashCode() {
        int result = logoSize40Url != null ? logoSize40Url.hashCode() : 0;
        result = 31 * result + (logoSize70Url != null ? logoSize70Url.hashCode() : 0);
        result = 31 * result + (logoSize140Url != null ? logoSize140Url.hashCode() : 0);
        result = 31 * result + (logoSize280Url != null ? logoSize280Url.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LogoUrls{"
              + "logoSize40Url='" + logoSize40Url + '\''
              + ", logoSize70Url='" + logoSize70Url + '\''
              + ", logoSize140Url='" + logoSize140Url + '\''
              + ", logoSize280Url='" + logoSize280Url + '\''
              + '}';
    }

    public String logoSize40Url() {
        return logoSize40Url;
    }

    public LogoUrls logoSize40Url(String logoSize40Url) {
        this.logoSize40Url = logoSize40Url;
        return this;
    }

    public String logoSize70Url() {
        return logoSize70Url;
    }

    public LogoUrls logoSize70Url(String logoSize70Url) {
        this.logoSize70Url = logoSize70Url;
        return this;
    }

    public String logoSize140Url() {
        return logoSize140Url;
    }

    public LogoUrls logoSize140Url(String logoSize140Url) {
        this.logoSize140Url = logoSize140Url;
        return this;
    }

    public String logoSize280Url() {
        return logoSize280Url;
    }

    public LogoUrls logoSize280Url(String logoSize280Url) {
        this.logoSize280Url = logoSize280Url;
        return this;
    }
}
