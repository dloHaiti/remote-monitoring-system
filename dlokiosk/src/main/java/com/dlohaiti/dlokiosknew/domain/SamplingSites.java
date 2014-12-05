package com.dlohaiti.dlokiosknew.domain;


import com.dlohaiti.dlokiosknew.exception.NoSamplingSiteWithGivenIdException;

import java.util.ArrayList;
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
