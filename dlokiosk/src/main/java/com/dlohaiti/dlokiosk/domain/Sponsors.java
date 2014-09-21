package com.dlohaiti.dlokiosk.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Sponsors extends ArrayList<Sponsor> {
    public Sponsors(Collection<Sponsor> sponsors) {
        super(sponsors);
    }

    public Sponsors() {
    }

    public List<String> getSponsorNames() {
        List<String> names = new ArrayList<String>();
        for (Sponsor s : this) {
            names.add(s.name());
        }
        return names;
    }

    public Sponsors getSponsorsFromName(List<String> names) {
        List<Sponsor> sponsors = new ArrayList<Sponsor>();
        for (String name : names) {
            Sponsor s = this.findSponsorByName(name);
            if (s != null) {
                sponsors.add(s);
            }
        }
        return new Sponsors(sponsors);
    }

    private Sponsor findSponsorByName(String name) {
        for (Sponsor sponsor : this) {
            if (sponsor.name().equalsIgnoreCase(name)) {
                return sponsor;
            }
        }
        return null;
    }
}
