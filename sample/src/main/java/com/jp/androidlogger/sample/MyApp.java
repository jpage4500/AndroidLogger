package com.jp.androidlogger.sample;

import android.app.Application;
import android.util.Log;

import com.jp.androidlogger.AndroidLoggerFactory;

import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;

// Lombok's @Slf4j annotation - creates static "log" variable
@Slf4j
public class MyApp extends Application {

    // alternative method of creating Logger (I personally like Lombok's @Slf4j annotation better)
    // static private final Logger log = LoggerFactory.getLogger(MyApp.class);

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidLoggerFactory logger = (AndroidLoggerFactory) LoggerFactory.getILoggerFactory();
        // set prefix for log TAG (ie: "APP_CLASSNAME")
        logger.setTagPrefix("APP");
        // set log level (log everything in DEBUG builds)
        logger.setDebugLevel(BuildConfig.DEBUG ? Log.VERBOSE : Log.INFO);
        // log long lines (> 4000 characters) to multiple log lines
        logger.setMultilineLoggingEnabled(BuildConfig.DEBUG);

        log.trace("hello from my test app!");
    }

}
