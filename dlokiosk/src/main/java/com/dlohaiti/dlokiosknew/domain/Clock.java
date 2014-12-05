package com.dlohaiti.dlokiosknew.domain;

import java.util.Calendar;
import java.util.Date;

public class Clock {
    public Date now() {
        return new Date();
    }

    public Date today(){
        return new Date();
    }

    public Date yesterday(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
}
