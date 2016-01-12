package com.xing.api.data.jobs;

import com.squareup.moshi.Json;

import java.io.Serializable;

/**
 * @author cristian.monforte
 */
public class JobLinks implements Serializable{
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

    public String pdf() {
        return pdf;
    }

    public String self() {
        return self;
    }

    public String xing() {
        return xing;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobLinks jobLinks = (JobLinks) o;

        if (pdf != null ? !pdf.equals(jobLinks.pdf) : jobLinks.pdf != null) return false;
        if (self != null ? !self.equals(jobLinks.self) : jobLinks.self != null) return false;
        return !(xing != null ? !xing.equals(jobLinks.xing) : jobLinks.xing != null);
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
        return "JobLinks{" +
              "pdf='" + pdf + '\'' +
              ", self='" + self + '\'' +
              ", xing='" + xing + '\'' +
              '}';
    }
}
