package com.dlohaiti.dlokiosknew;

import android.content.Context;

import com.dlohaiti.dlokiosknew.db.ConfigurationKey;
import com.dlohaiti.dlokiosknew.db.ConfigurationRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Singleton
public class KioskDate {
    @Inject
    ConfigurationRepository configurationRepository;

    public DateFormat getFormat() {
       return  new SimpleDateFormat(configurationRepository.get(ConfigurationKey.DATE_FORMAT));
    }
    public DateFormat getFormat(Context context) {
        return  new SimpleDateFormat(configurationRepository.get(ConfigurationKey.DATE_FORMAT),context.getResources().getConfiguration().locale);
    }
}
