package com.jp.androidlogger.log;

import android.util.Log;

import com.jp.androidlogger.BuildConfig;

import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AndroidLogger extends MarkerIgnoringBase {

    private static final long serialVersionUID = -1227274521521287937L;

    private static final String DEFAULT_DATE_FORMAT = "MM-dd HH:mm:ss";

    private File logFile;
    private SimpleDateFormat simpleDateFormat;

    protected AndroidLogger(final String name) {
        this.name = name;
    }

    protected void setFile(File file) {
        logFile = file;
    }

    protected void setDateFormat(String format) {
        simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss ", Locale.US);
    }

    private boolean isDebug() {
        return BuildConfig.DEBUG;
    }

    /**
     * Only log trace and debug output for debug builds
     */
    @Override
    public boolean isTraceEnabled() {
        return isDebug();
    }

    @Override
    public void trace(final String msg) {
        if (!isTraceEnabled()) return;
        Log.v(this.name, msg);

        if (logFile != null) {
            logToFile('V', msg);
        }
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

        if (logFile != null) {
            logToFile('V', msg);
        }
    }

    /**
     * Only log trace and debug lines if user has enabled debug mode
     */
    @Override
    public boolean isDebugEnabled() {
        return isDebug();
    }

    @Override
    public void debug(final String msg) {
        if (!isDebugEnabled()) return;
        Log.d(this.name, msg);

        if (logFile != null) {
            logToFile('D', msg);
        }
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

        if (logFile != null) {
            logToFile('D', msg);
        }
    }

    @Override
    public boolean isInfoEnabled() {
        return true;
    }

    @Override
    public void info(final String msg) {
        Log.i(this.name, msg);

        if (logFile != null) {
            logToFile('I', msg);
        }
    }

    @Override
    public void info(final String format, final Object arg) {
        logInfo(MessageFormatter.format(format, arg));
    }

    @Override
    public void info(final String format, final Object arg1, final Object arg2) {
        logInfo(MessageFormatter.format(format, arg1, arg2));
    }

    @Override
    public void info(final String format, final Object... arguments) {
        logInfo(MessageFormatter.arrayFormat(format, arguments));
    }

    @Override
    public void info(final String msg, final Throwable t) {
        Log.i(this.name, msg, t);

        if (logFile != null) {
            logToFile('I', msg);
        }
    }

    @Override
    public boolean isWarnEnabled() {
        return true;
    }

    @Override
    public void warn(final String msg) {
        Log.w(this.name, msg);

        if (logFile != null) {
            logToFile('W', msg);
        }
    }

    @Override
    public void warn(final String format, final Object arg) {
        logWarning(MessageFormatter.format(format, arg));
    }

    @Override
    public void warn(final String format, final Object arg1, final Object arg2) {
        logWarning(MessageFormatter.format(format, arg1, arg2));
    }

    @Override
    public void warn(final String format, final Object... arguments) {
        logWarning(MessageFormatter.arrayFormat(format, arguments));
    }

    @Override
    public void warn(final String msg, final Throwable t) {
        Log.w(this.name, msg, t);

        if (logFile != null) {
            logToFile('W', msg);
        }
    }

    @Override
    public boolean isErrorEnabled() {
        return true;
    }

    @Override
    public void error(final String msg) {
        Log.e(this.name, msg);
    }

    @Override
    public void error(final String format, final Object arg) {
        logError(MessageFormatter.format(format, arg));
    }

    @Override
    public void error(final String format, final Object arg1, final Object arg2) {
        logError(MessageFormatter.format(format, arg1, arg2));
    }

    @Override
    public void error(final String format, final Object... arguments) {
        logError(MessageFormatter.arrayFormat(format, arguments));
    }

    @Override
    public void error(final String msg, final Throwable t) {
        Log.e(this.name, msg, t);
    }

    private void logVerbose(FormattingTuple ft) {
        if (ft.getThrowable() == null) {
            Log.v(this.name, ft.getMessage());
        } else {
            Log.v(this.name, ft.getMessage(), ft.getThrowable());
        }

        if (logFile != null) {
            logToFile('V', ft.getMessage());
        }
    }

    private void logDebug(FormattingTuple ft) {
        if (ft.getThrowable() == null) {
            Log.d(this.name, ft.getMessage());
        } else {
            Log.d(this.name, ft.getMessage(), ft.getThrowable());
        }

        if (logFile != null) {
            logToFile('D', ft.getMessage());
        }
    }

    private void logInfo(FormattingTuple ft) {
        if (ft.getThrowable() == null) {
            Log.i(this.name, ft.getMessage());
        } else {
            Log.i(this.name, ft.getMessage(), ft.getThrowable());
        }

        if (logFile != null) {
            logToFile('I', ft.getMessage());
        }
    }

    private void logWarning(FormattingTuple ft) {
        if (ft.getThrowable() == null) {
            Log.w(this.name, ft.getMessage());
        } else {
            Log.w(this.name, ft.getMessage(), ft.getThrowable());
        }

        if (logFile != null) {
            logToFile('W', ft.getMessage());
        }
    }

    private void logError(FormattingTuple ft) {
        if (ft.getThrowable() == null) {
            Log.e(this.name, ft.getMessage());
        } else {
            Log.e(this.name, ft.getMessage(), ft.getThrowable());
        }

        if (logFile != null) {
            logToFile('E', ft.getMessage());
        }
    }

    private void logToFile(char level, String line) {
        if (logFile == null) {
            return;
        }

        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.US);
        }

        FileWriter logWriter = null;
        try {
            logWriter = new FileWriter(logFile, true);
            BufferedWriter out = new BufferedWriter(logWriter);
            out.write(simpleDateFormat.format(new Date()));
            out.write(" " + level + " " + " " + name + " ");
            out.write(line);
            out.newLine();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
