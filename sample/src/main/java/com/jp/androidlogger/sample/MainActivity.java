package com.jp.androidlogger.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                log.debug("onClick: ");
            }
        });
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
