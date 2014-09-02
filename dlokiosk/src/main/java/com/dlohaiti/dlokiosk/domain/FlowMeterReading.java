package com.dlohaiti.dlokiosk.domain;

import static org.apache.commons.lang3.StringUtils.upperCase;

public class FlowMeterReading  implements Comparable<FlowMeterReading>  {
    private Long id;
    private Long parameterId;
    private String samplingName;
    private String parameterName;
    private String quantity;

    public FlowMeterReading(Long id, Long parameterId, String sampling_name, String parameterName,String quantity) {
        this.id = id;
        this.parameterId = parameterId;
        this.samplingName = sampling_name;
        this.parameterName =parameterName;
        this.quantity = quantity;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getParameterId() {
        return parameterId;
    }

    public String getSamplingName() {
        return samplingName;
    }

    public FlowMeterReading(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlowMeterReading that = (FlowMeterReading) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (!samplingName.equals(that.samplingName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + samplingName.hashCode();
        return result;
    }

    @Override
    public int compareTo(FlowMeterReading another) {
        return upperCase(samplingName).compareTo(upperCase(another.samplingName));
    }
}
