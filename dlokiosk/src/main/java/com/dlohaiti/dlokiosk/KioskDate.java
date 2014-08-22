package com.dlohaiti.dlokiosk;

import com.google.inject.Singleton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Singleton
public class KioskDate {
    public DateFormat getFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
