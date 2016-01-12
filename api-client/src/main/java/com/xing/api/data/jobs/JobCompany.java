package com.xing.api.data.jobs;

import java.io.Serializable;

/**
 * @author cristian.monforte
 */
public class JobCompany implements Serializable{
    private static final long serialVersionUID = 1L;

    private final String name;
    private final CompanyLinks links;

    public JobCompany(String name, CompanyLinks links) {
        this.name = name;
        this.links = links;
    }

    public String name() {
        return name;
    }

    public CompanyLinks links() {
        return links;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobCompany that = (JobCompany) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return !(links != null ? !links.equals(that.links) : that.links != null);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (links != null ? links.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "JobCompany{" +
              "name='" + name + '\'' +
              ", links=" + links +
              '}';
    }
}
