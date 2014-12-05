package com.dlohaiti.dlokiosknew.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

public class Sponsors extends ArrayList<Sponsor> {
    public Sponsors(Collection<Sponsor> sponsors) {
        super(sponsors);
    }

    public Sponsors() {
    }

    public List<String> getSponsorNames() {
        List<String> names = new ArrayList<String>();
        for (Sponsor s : this) {
            names.add(s.getName());
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
            if (sponsor.getName().equalsIgnoreCase(name)) {
                return sponsor;
            }
        }
        return null;
    }

    public List<Sponsor> filterBySponsorName(String sponsorName) {
        List<Sponsor> sponsors = new ArrayList<Sponsor>();
        for (Sponsor s : this) {
            if (containsIgnoreCase(s.getName(), sponsorName)) {
                sponsors.add(s);
            }
        }
        return sponsors;
    }
}
