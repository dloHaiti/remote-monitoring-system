package com.dlohaiti.dlokiosknew.domain;

import java.util.Date;
import java.util.Set;

public class Reading {
    private final Long id;
    private final String samplingSiteName;
    private final Date createdDate;
    private final Set<Measurement> measurements;
    private boolean isSynced;

    public Reading(SamplingSite samplingSite, Set<Measurement> measurements, Date createdDate) {
        this(null, samplingSite.getName(), measurements, createdDate);
    }

    public Reading(Long id, String samplingSiteName, Set<Measurement> measurements, Date createdDate) {
        this.id = id;
        this.samplingSiteName = samplingSiteName;
        this.measurements = measurements;
        this.createdDate = createdDate;
        this.isSynced=false;
    }

    public Reading(Long id, String samplingSiteName, Set<Measurement> measurements, Date createdDate,boolean synced) {
        this.id = id;
        this.samplingSiteName = samplingSiteName;
        this.measurements = measurements;
        this.createdDate = createdDate;
        this.isSynced=synced;
    }

    public Long getId() {
        return id;
    }

    public String getSamplingSiteName() {
        return samplingSiteName.trim();
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public Set<Measurement> getMeasurements() {
        return measurements;
    }

    public Measurement getMeasurement(String parameterName){
        for(Measurement m: measurements){
          if(m.getParameterName().equalsIgnoreCase(parameterName)){
              return m;
          }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reading reading = (Reading) o;

        if (createdDate != null ? !createdDate.equals(reading.createdDate) : reading.createdDate != null) return false;
        if (id != null ? !id.equals(reading.id) : reading.id != null) return false;
        if (measurements != null ? !measurements.equals(reading.measurements) : reading.measurements != null)
            return false;
        if (samplingSiteName != null ? !samplingSiteName.equals(reading.samplingSiteName) : reading.samplingSiteName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (samplingSiteName != null ? samplingSiteName.hashCode() : 0);
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (measurements != null ? measurements.hashCode() : 0);
        return result;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        this.isSynced = synced;
    }
}
