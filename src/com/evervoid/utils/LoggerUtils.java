package com.evervoid.utils;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerUtils
{
	public static Logger sEverVoidLogger = null;

	public static Logger getLogger()
	{
		if (sEverVoidLogger == null) {
			sEverVoidLogger = Logger.getLogger(LoggerUtils.class.getName());
			sEverVoidLogger.setLevel(Level.ALL);
			try {
				final FileHandler fh = new FileHandler(ResourceUtils.getAppDir() + "everVoid.log", false);
				sEverVoidLogger.addHandler(fh);
			}
			catch (final Exception e) {
				// well shit
				e.printStackTrace();
			}
		}
		return sEverVoidLogger;
	}
}
