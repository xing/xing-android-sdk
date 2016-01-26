package com.xing.api.data.jobs;

import java.io.Serializable;

/**
 * @author cristian.monforte
 */
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
