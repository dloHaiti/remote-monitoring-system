package com.dlohaiti.dlokiosk.domain;

import java.util.Date;
import java.util.Set;

public class Reading {
    private final String sampleSiteName;
    private final Date createdDate;
    private final Set<Measurement> measurements;

    public Reading(SamplingSite samplingSite, Set<Measurement> measurements, Date createdDate) {
        this.sampleSiteName = samplingSite.getName();
        this.measurements = measurements;
        this.createdDate = createdDate;
    }

    public String getSampleSiteName() {
        return sampleSiteName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Set<Measurement> getMeasurements() {
        return measurements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reading reading = (Reading) o;

        if (createdDate != null ? !createdDate.equals(reading.createdDate) : reading.createdDate != null) return false;
        if (measurements != null ? !measurements.equals(reading.measurements) : reading.measurements != null)
            return false;
        if (sampleSiteName != null ? !sampleSiteName.equals(reading.sampleSiteName) : reading.sampleSiteName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sampleSiteName != null ? sampleSiteName.hashCode() : 0;
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (measurements != null ? measurements.hashCode() : 0);
        return result;
    }
}
