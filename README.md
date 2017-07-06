# AndroidLogger
Android Slf4j logger implementation with tag prefix and optional file logging

This small library is designed to make integrating Slf4j into an Android project super simple without the typical boilerplate classes needed to get running.

## Setup
Add this line to your project's build.gradle file in the dependencies section
```
dependencies {
    compile 'com.github.jpage4500:android-logger:1.0.1'
```

- See this link for latest version: https://bintray.com/jpage4500/maven/android-logger

## Configure
Put this code in your Application's onCreate() method to configure the logging options. All of these steps are optional but show some of the options you can set:

```
        AndroidLoggerFactory logger = (AndroidLoggerFactory) LoggerFactory.getILoggerFactory();
        logger.setTagPrefix("blr");
        int logLevel = Log.INFO;
        if (BuildConfig.DEBUG || isDebugModeEnabled()) {
            logLevel = Log.VERBOSE;
        }
        logger.setDebugLevel(logLevel);
        logger.setMultilineLoggingEnabled(BuildConfig.DEBUG);
        logger.setMaxWrapLines(5);
```

There's also the ability to log to a file on the device - something that could come in handy if you want to debug events after they occur on test devices where you can't get the log right away. This isn't an option I've spent a lot of time using yet which is why I'm not showing it above.. for example, right now there's no file size limit which I think is needed. But, for now you can configure it like this:
```
        logger.logToFile(File file, boolean append);
```

## Log!

The easiest option to use Slf4j is to create a global 'log' variable at the top of each class like this:
```
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(LogExample.class);
```

However, the option I would highly recommend is installing Lombok plugin.. once you do - you can just annotate the class with:

```
@Slf4j
public class MyClass {
```

Next, add your log() lines.. for example:
```
    log.debug("onLocationChangedEvent: {}", event);
```

-----------------------

Why use Slf4j instead of Android's built-in Log.debug()? Flexibility! 

TBD..
