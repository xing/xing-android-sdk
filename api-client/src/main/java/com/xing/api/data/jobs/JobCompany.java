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
package com.xing.api.data.jobs;

import java.io.Serializable;

public class JobCompany implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final CompanyLinks links;

    public JobCompany(String name, CompanyLinks links) {
        this.name = name;
        this.links = links;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobCompany company = (JobCompany) o;
        return name != null ? name.equals(company.name) : company.name == null
              && (links != null ? links.equals(company.links) : company.links == null);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (links != null ? links.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "JobCompany{"
              + "name='" + name + '\''
              + ", links=" + links
              + '}';
    }

    public String name() {
        return name;
    }

    public CompanyLinks links() {
        return links;
    }
}
