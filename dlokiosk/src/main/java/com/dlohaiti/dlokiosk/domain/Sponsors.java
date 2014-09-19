package com.dlohaiti.dlokiosk.domain;

import java.util.ArrayList;
import java.util.Collection;

public class Sponsors extends ArrayList<Sponsor> {
    public Sponsors(Collection<Sponsor> sponsors) {
        super(sponsors);
    }

    public Sponsors() {
    }
}
