package com.jp.androidlogger;

import android.os.Environment;
import android.util.Log;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An implementation of {@link ILoggerFactory} which always returns {@link AndroidLogger} instances.
 */
public class AndroidLoggerFactory implements ILoggerFactory {

    private static final int TAG_MAX_LENGTH = 23;
    private static String tagPrefix;
    private static File logFile;

    private final ConcurrentHashMap<String, AndroidLogger> nameToLogMap = new ConcurrentHashMap<>();

    public static void setTagPrefix(String prefix) {
        tagPrefix = prefix;
    }

    /**
     * log to file
     * NOTE: will open up more options later
     */
    public static void logToFile(boolean logToFile) {
        if (logToFile) {
            logFile = new File(Environment.getExternalStorageDirectory(), "firestarter.log");
            Log.d("AndroidLoggerFactory", "logging to: " + logFile.getAbsolutePath());
        } else {
            logFile = null;
        }
    }

    @Override
    public Logger getLogger(final String name) {
        AndroidLogger logger = this.nameToLogMap.get(name);
        if (logger == null) {
            String tag = getTag(name);
            logger = new AndroidLogger(tag);
            logger.setFile(logFile);

            AndroidLogger existingLogger = this.nameToLogMap.putIfAbsent(name, logger);
            if (existingLogger != null) {
                logger = existingLogger;
            }
        }

        return logger;
    }

    private String getTag(String name) {
        if (name == null) {
            return null;
        }

        int indexOfLastDot = name.lastIndexOf('.');
        //dot must not be the first or last character
        if (indexOfLastDot > 0 && indexOfLastDot < name.length() - 2) {
            // assume it is a class
            String prefix = tagPrefix != null ? tagPrefix : "";
            name = prefix + name.substring(indexOfLastDot + 1);

            if (name.length() > TAG_MAX_LENGTH) {
                name = name.substring(0, TAG_MAX_LENGTH);
            }
        }

        return name;
    }
}
