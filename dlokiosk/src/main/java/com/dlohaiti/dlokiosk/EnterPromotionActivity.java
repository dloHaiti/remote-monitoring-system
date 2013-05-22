package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import com.dlohaiti.dlokiosk.db.PromotionRepository;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;

public class EnterPromotionActivity extends RoboActivity {

    @Inject private PromotionRepository promotionRepository;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
