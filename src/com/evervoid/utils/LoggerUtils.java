package com.evervoid.utils;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * This class initializes the Logger to be used by all of everVoid. All logs are piped to a log file, located at
 * /%APPDIR%/everVoid/everVoid.log, the actual location is defined in ResourceUtils. The file is overwritten at each
 * time the
 * program is launches anew.
 */
public class LoggerUtils
{
    /**
     * The default logger to be used by all of everVoid.
     */
    public static Logger sEverVoidLogger = null;

    /**
     * Creates the logger if that has not already been done, then returns the logger for use.
     * 
     * @return The logger.
     */
    private static Logger getLogger()
    {
        if (sEverVoidLogger == null) {
            // we have not created a logger yet, do so.
            sEverVoidLogger = Logger.getLogger(LoggerUtils.class.getName());
            sEverVoidLogger.setLevel(Level.ALL);
            try {
                // attempt to pipe the logs to the everVoid log file
                final FileHandler fh = new FileHandler(ResourceUtils.getAppDir() + "everVoid.log", false);
                fh.setFormatter(new SimpleFormatter());
                sEverVoidLogger.addHandler(fh);
            } catch (final Exception e) {
                // well shit
                e.printStackTrace();
                // maybe someone's listening
                sEverVoidLogger.warning("Could not open log file at " + ResourceUtils.getAppDir() + "everVoid.log");
            }
        }
        // return the logger for use
        return sEverVoidLogger;
    }

    /**
     * Prints the message to the Logger with level INFO.
     * 
     * @param msg
     *            The message to log.
     */
    public static void info(final String msg)
    {
        // use getLogger() in case the Logger hasn't been created yet
        getLogger().info(msg);
    }

    /**
     * Prints the message to the Logger with level SEVERE.
     * 
     * @param msg
     *            The message to log.
     */
    public static void severe(final String msg)
    {
        // use getLogger() in case the Logger hasn't been created yet
        getLogger().severe(msg);
    }

    /**
     * Prints the message to the Logger with level SEVERE.
     * 
     * @param msg
     *            The message to log.
     * @param t
     *            The throwable object to log.
     */
    public static void severe(final String msg, final Throwable t)
    {
        // use getLogger() in case the Logger hasn't been created yet
        getLogger().log(Level.SEVERE, msg, t);
    }

    /**
     * Prints the message to the Logger with level WARNING
     * 
     * @param msg
     *            The message to log.
     */
    public static void warning(final String msg)
    {
        // use getLogger() in case the Logger hasn't been created yet
        getLogger().warning(msg);
    }
}
