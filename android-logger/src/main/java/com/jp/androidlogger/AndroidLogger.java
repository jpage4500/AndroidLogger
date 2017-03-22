package com.jp.androidlogger;

import android.util.Log;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

public class AndroidLogger extends MarkerIgnoringBase {

    private static final long serialVersionUID = -1227274521521287937L;

    private AndroidLoggerFactory androidLoggerFactory;

    protected AndroidLogger(String name, AndroidLoggerFactory androidLoggerFactory) {
        this.name = name;
        this.androidLoggerFactory = androidLoggerFactory;
    }

    void setName(String tagName) {
        this.name = tagName;
    }

    /**
     * Only log trace and debug lines if user has enabled debug mode
     *
     * @return true if this log level is enabled
     */
    @Override
    public boolean isTraceEnabled() {
        return androidLoggerFactory.isEnabled(Log.VERBOSE);
    }

    @Override
    public void trace(final String msg) {
        if (!isTraceEnabled()) return;
        log(Log.VERBOSE, msg, null);
    }

    @Override
    public void trace(final String format, final Object arg) {
        if (!isTraceEnabled()) return;
        logVerbose(MessageFormatter.format(format, arg));
    }

    @Override
    public void trace(final String format, final Object arg1, final Object arg2) {
        if (!isTraceEnabled()) return;
        logVerbose(MessageFormatter.format(format, arg1, arg2));
    }

    @Override
    public void trace(final String format, final Object... arguments) {
        if (!isTraceEnabled()) return;
        logVerbose(MessageFormatter.arrayFormat(format, arguments));
    }

    @Override
    public void trace(final String msg, final Throwable t) {
        if (!isTraceEnabled()) return;
        Log.v(this.name, msg, t);
    }

    /**
     * Only log trace and debug lines if user has enabled debug mode
     *
     * @return true if this log level is enabled
     */
    @Override
    public boolean isDebugEnabled() {
        return androidLoggerFactory.isEnabled(Log.DEBUG);
    }

    @Override
    public void debug(final String msg) {
        if (!isDebugEnabled()) return;
        log(Log.DEBUG, msg, null);
    }

    @Override
    public void debug(final String format, final Object arg) {
        if (!isDebugEnabled()) return;
        logDebug(MessageFormatter.format(format, arg));
    }

    @Override
    public void debug(final String format, final Object arg1, final Object arg2) {
        if (!isDebugEnabled()) return;
        logDebug(MessageFormatter.format(format, arg1, arg2));
    }

    @Override
    public void debug(final String format, final Object... arguments) {
        if (!isDebugEnabled()) return;
        logDebug(MessageFormatter.arrayFormat(format, arguments));
    }

    @Override
    public void debug(final String msg, final Throwable t) {
        if (!isDebugEnabled()) return;
        Log.d(this.name, msg, t);
    }

    @Override
    public boolean isInfoEnabled() {
        return androidLoggerFactory.isEnabled(Log.INFO);
    }

    @Override
    public void info(final String msg) {
        if (!isInfoEnabled()) return;
        log(Log.INFO, msg, null);
    }

    @Override
    public void info(final String format, final Object arg) {
        if (!isInfoEnabled()) return;
        logInfo(MessageFormatter.format(format, arg));
    }

    @Override
    public void info(final String format, final Object arg1, final Object arg2) {
        if (!isInfoEnabled()) return;
        logInfo(MessageFormatter.format(format, arg1, arg2));
    }

    @Override
    public void info(final String format, final Object... arguments) {
        if (!isInfoEnabled()) return;
        logInfo(MessageFormatter.arrayFormat(format, arguments));
    }

    @Override
    public void info(final String msg, final Throwable t) {
        if (!isInfoEnabled()) return;
        log(Log.INFO, msg, t);
    }

    @Override
    public boolean isWarnEnabled() {
        return androidLoggerFactory.isEnabled(Log.WARN);
    }

    @Override
    public void warn(final String msg) {
        if (!isWarnEnabled()) return;
        log(Log.WARN, msg, null);
    }

    @Override
    public void warn(final String format, final Object arg) {
        if (!isWarnEnabled()) return;
        logWarning(MessageFormatter.format(format, arg));
    }

    @Override
    public void warn(final String format, final Object arg1, final Object arg2) {
        if (!isWarnEnabled()) return;
        logWarning(MessageFormatter.format(format, arg1, arg2));
    }

    @Override
    public void warn(final String format, final Object... arguments) {
        if (!isWarnEnabled()) return;
        logWarning(MessageFormatter.arrayFormat(format, arguments));
    }

    @Override
    public void warn(final String msg, final Throwable t) {
        if (!isWarnEnabled()) return;
        Log.w(this.name, msg, t);
    }

    @Override
    public boolean isErrorEnabled() {
        return androidLoggerFactory.isEnabled(Log.ERROR);
    }

    @Override
    public void error(final String msg) {
        if (!isErrorEnabled()) return;
        log(Log.ERROR, msg, null);
    }

    @Override
    public void error(final String format, final Object arg) {
        if (!isErrorEnabled()) return;
        logError(MessageFormatter.format(format, arg));
    }

    @Override
    public void error(final String format, final Object arg1, final Object arg2) {
        if (!isErrorEnabled()) return;
        logError(MessageFormatter.format(format, arg1, arg2));
    }

    @Override
    public void error(final String format, final Object... arguments) {
        if (!isErrorEnabled()) return;
        logError(MessageFormatter.arrayFormat(format, arguments));
    }

    @Override
    public void error(final String msg, final Throwable t) {
        if (!isErrorEnabled()) return;
        Log.e(this.name, msg, t);
    }

    private void logVerbose(FormattingTuple ft) {
        log(Log.VERBOSE, ft.getMessage(), ft.getThrowable());
    }

    private void logDebug(FormattingTuple ft) {
        log(Log.DEBUG, ft.getMessage(), ft.getThrowable());
    }

    private void logInfo(FormattingTuple ft) {
        log(Log.INFO, ft.getMessage(), ft.getThrowable());
    }

    private void logWarning(FormattingTuple ft) {
        log(Log.WARN, ft.getMessage(), ft.getThrowable());
    }

    private void logError(FormattingTuple ft) {
        log(Log.ERROR, ft.getMessage(), ft.getThrowable());
    }

    /**
     * handle everything log() method:
     * - logs to file if enabled
     * - logs LONG output to multiple lines if enabled
     */
    private void log(int logLevel, String message, Throwable tr) {
        if (tr != null) {
            // append throwable if set
            message += '\n' + Log.getStackTraceString(tr);
        }

        String origMessage = null;
        if (androidLoggerFactory.isLongLoggingEnabled() && message.length() > androidLoggerFactory.getMaxCharactersPerLine()) {
            origMessage = message;
            message = message.substring(0, androidLoggerFactory.getMaxCharactersPerLine());
        }

        switch (logLevel) {
            case Log.VERBOSE:
                Log.v(name, message);
                break;
            case Log.DEBUG:
                Log.d(name, message);
                break;
            case Log.INFO:
                Log.i(name, message);
                break;
            case Log.WARN:
                Log.w(name, message);
                break;
            case Log.ERROR:
                Log.e(name, message);
                break;
        }

        // log to file if set
        androidLoggerFactory.logToFile(logLevel, name, message);

        if (origMessage != null) {
            // recursive call to log remaining message
            log(logLevel, origMessage.substring(androidLoggerFactory.getMaxCharactersPerLine()), null);
        }
    }

}
