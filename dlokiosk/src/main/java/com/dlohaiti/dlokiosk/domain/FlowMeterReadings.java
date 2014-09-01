package com.dlohaiti.dlokiosk.domain;

import java.util.ArrayList;
import java.util.Collection;

public class FlowMeterReadings extends ArrayList<FlowMeterReading> {

    public FlowMeterReadings(Collection<FlowMeterReading> flowMeterReadings) {
        super(flowMeterReadings);
    }
}
