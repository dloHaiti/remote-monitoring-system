package com.dlohaiti.dlokiosk;

import android.content.Context;

import com.dlohaiti.dlokiosk.db.ConfigurationKey;
import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

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
