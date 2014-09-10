package com.dlohaiti.dlokiosk.domain;


import com.dlohaiti.dlokiosk.exception.NoSamplingSiteWithGivenIdException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SamplingSites extends ArrayList<SamplingSite> {

    public SamplingSites(List<SamplingSite> samplingSites) {
        super(samplingSites);
    }
    public SamplingSite findSamplingSiteById(long id){
        for (SamplingSite samplingSite : this) {
            if (samplingSite.getId() == id) {
                return samplingSite;
            }
        }
        throw new NoSamplingSiteWithGivenIdException(id);
    }
}
