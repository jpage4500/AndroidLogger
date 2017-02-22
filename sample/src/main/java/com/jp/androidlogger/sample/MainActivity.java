package com.jp.androidlogger.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import lombok.extern.slf4j.Slf4j;

// Lombok's @Slf4j annotation - creates static "log" variable
@Slf4j
public class MainActivity extends AppCompatActivity {

    // alternative method of creating Logger (I personally like Lombok's @Slf4j annotation better)
    // static private final Logger log = LoggerFactory.getLogger(MainActivity.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        log.debug("onCreate: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        log.debug("onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        log.debug("onPause: ");
    }

}
