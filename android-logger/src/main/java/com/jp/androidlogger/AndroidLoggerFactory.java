package com.jp.androidlogger;

import android.util.Log;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An implementation of {@link ILoggerFactory} which always returns {@link AndroidLogger} instances.
 */
public class AndroidLoggerFactory implements ILoggerFactory {
    private static final boolean EXTRA_LOGGING = false;
    private static final String TAG = "AndroidLoggerFactory";

    private static final int TAG_MAX_LENGTH = 23;
    private String tagPrefix;
    private int logLevel = Log.VERBOSE;
    private boolean isMultilineLoggingEnabled;
    private int maxCharactersPerLine = 4000;

    // -- for logging to file --
    private static final String DEFAULT_DATE_FORMAT = "MM-dd HH:mm:ss";
    private File logFile;
    private SimpleDateFormat simpleDateFormat;
    private final ConcurrentHashMap<String, AndroidLogger> nameToLogMap = new ConcurrentHashMap<>();

    /**
     * set a short prefix string to the TAG field
     * NOTE: total tag length is only 23 characters so you'll want to make this VERY short
     * Example: "blr" -> "blr_className"
     * - if not set just the "className" will be logged (instead of "com.package.something.className")
     */
    public void setTagPrefix(String prefix) {
        if (prefix == null || prefix.equals(tagPrefix)) {
            // no change
            return;
        }
        tagPrefix = prefix;

        // update name for any existing loggers
        for (Map.Entry<String, AndroidLogger> entry : nameToLogMap.entrySet()) {
            String name = entry.getKey();
            AndroidLogger logger = entry.getValue();
            String tag = getTag(name);
            if (EXTRA_LOGGING) Log.v(TAG, "setTagPrefix: updating: " + name + " from: " + logger.getName() + " to: " + tag);
            logger.setName(tag);
        }
    }

    /**
     * set min debug level threshold for which logging will occur
     * examples:
     * Log.VERBOSE -> log VERBOSE and higher (DEBUG, INFO, WARN, ERROR)
     * Log.WARN -> log WARN and higher (ERROR)
     * - defaults to Log.VERBOSE
     */
    public void setDebugLevel(int level) {
        logLevel = level;
        if (EXTRA_LOGGING) Log.v(TAG, "setDebugLevel: " + level);
    }

    /**
     * android's Log.XX() methods will only log ~4000 characters
     * set this to true if you want to log everything
     * - useful for logging server responses for BuildConfig.DEBUG for example
     */
    public void setMultilineLoggingEnabled(boolean isEnabled) {
        isMultilineLoggingEnabled = isEnabled;
        if (EXTRA_LOGGING) Log.v(TAG, "setMultilineLoggingEnabled: " + isEnabled);
    }

    /**
     * max number of characters that can be logged in a single Log.x() line. This seems to vary per
     * device but 4000 has worked on most devices I've tested on (Samsung S4, S5, S7)
     * NOTE: only used if isMultilineLoggingEnabled = true
     * - defaults to 4000 characters
     */
    public void setMaxCharactersPerLine(int maxCharactersPerLine) {
        maxCharactersPerLine = maxCharactersPerLine;
        if (EXTRA_LOGGING) Log.v(TAG, "setMaxCharactersPerLine: " + maxCharactersPerLine);
    }

    /**
     * log to file
     */
    public void logToFile(File logFile, boolean append) {
        this.logFile = logFile;
        if (!append) {
            logFile.delete();
        }
        if (EXTRA_LOGGING) Log.v(TAG, "logging to: " + logFile.getAbsolutePath() + ", append:" + append);
    }

    @Override
    public Logger getLogger(final String name) {
        AndroidLogger logger = this.nameToLogMap.get(name);
        if (logger == null) {
            String tag = getTag(name);
            logger = new AndroidLogger(tag, this);
            if (EXTRA_LOGGING) Log.v(TAG, "getLogger: name:" + name + ", tag: " + tag);

            AndroidLogger existingLogger = this.nameToLogMap.putIfAbsent(name, logger);
            if (existingLogger != null) {
                logger = existingLogger;
            }
        }

        return logger;
    }

    private String getTag(final String name) {
        if (name == null) {
            Log.w(TAG, "getTag: name is NULL!");
            return "";
        }

        // change com.package.name.className to just "className"
        String tag = name;
        int indexOfLastDot = name.lastIndexOf('.');
        //dot must not be the first or last character
        if (indexOfLastDot > 0 && indexOfLastDot < name.length() - 2) {
            String prefix = tagPrefix != null ? (tagPrefix + "_") : "";
            tag = prefix + name.substring(indexOfLastDot + 1);

            if (tag.length() > TAG_MAX_LENGTH) {
                tag = tag.substring(0, TAG_MAX_LENGTH);
            }
        }

        return tag;
    }

    /**
     * @return true if app should log this logLevel
     */
    boolean isEnabled(int logLevel) {
        return this.logLevel <= logLevel;
    }

    int getMaxCharactersPerLine() {
        return maxCharactersPerLine;
    }

    /**
     * @return true if app should split LONG log lines up so ENTIRE message is logged
     */
    boolean isLongLoggingEnabled() {
        return isMultilineLoggingEnabled;
    }

    void logToFile(int logLevel, String name, String message) {
        // ignore if not logging to file
        if (logFile == null) {
            return;
        }

        if (simpleDateFormat == null) {
            // TODO - SimpleDateFormat is not thread-safe
            simpleDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.US);
        }

        char level;
        switch (logLevel) {
            case Log.DEBUG:
                level = 'D';
                break;
            case Log.VERBOSE:
                level = 'V';
                break;
            case Log.INFO:
                level = 'I';
                break;
            case Log.ERROR:
                level = 'E';
                break;
            default:
                level = Character.forDigit(logLevel, 10);
                break;
        }

        FileWriter logWriter;
        try {
            logWriter = new FileWriter(logFile, true);
            BufferedWriter out = new BufferedWriter(logWriter);
            out.write(simpleDateFormat.format(new Date()));
            out.write(" " + level + " " + " " + name + " ");
            out.write(message);
            out.newLine();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
