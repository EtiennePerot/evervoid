package com.evervoid.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

public class ResourceUtils
{
	private static String sAppPath = null;
	private static String sMainPath = null;

	/**
	 * Attempts to detect the local Operating System. If detection fails, defaults to *nix formatting. Based on the OS, creates
	 * the AppPath variable. Format is as such: Windows: %APPDATA%/everVoid/ Macintosh: /Library/Application Support/everVoid/
	 * Linux: /.everVoid/ . If the folder does not exist, the functions creates it.
	 * 
	 * @return The string path to the local everVoid application directory.
	 */
	public static String getAppDir()
	{
		if (sAppPath == null) {
			// create the everVoid application path
			// switch on operating system
			final String os = System.getProperty("os.name");
			if (os.toLowerCase().contains("win")) {
				// windows
				sAppPath = System.getenv("APPDATA") + "/everVoid/";
			}
			else if (os.toLowerCase().contains("mac")) {
				// Macintosh
				sAppPath = System.getProperty("user.home") + "/Library/Application Support/everVoid/";
			}
			else {
				// default - assume *nix
				sAppPath = System.getProperty("user.home") + "/.everVoid/";
			}
			// attempt to locate the folder; if it doesn't exist, create it.
			final File f = new File(sAppPath);
			if (!f.exists()) {
				f.mkdirs();
			}
		}
		// sAppPath is guarenteed to be initailized, return it now.
		return sAppPath;
	}

	/**
	 * Finds and returns the path to the everVoid resource directory.
	 * 
	 * @return The path to the resource folder.
	 */
	public static String getResourceDir()
	{
		// For now, res/ must be a sibling to the jar file.
		// user getMainDir() in case sMainPath has not been initialized yet.
		return getSrcDir() + "res/";
	}

	/**
	 * Finds and returns the location of everVoid's source files. Detects either class or jar file. If this method fails to
	 * interpret the source folder location, it exits the application with status -1.
	 * 
	 * @return The absolute path to the source directory.
	 */
	public static String getSrcDir()
	{
		if (sMainPath == null) {
			// initialize sMainPath
			// This gets a little messy, It is attempting to find the location of ResourceUtils on disk, then reverse engineer
			// that to get the parent source directory
			final URL url = ResourceUtils.class.getResource("");
			final String urlPath = url.toString();
			String path = "";
			// The code may either be a .jar, or a bunch of .class files
			if (url.getProtocol().equals("file")) {
				// This is specific to eclipse, the .class files be under the /bin/[package] directory
				// find the absolute path of the class file, then remove the beginning "file:' part
				// and cut off the useless bin/[package] information
				path = urlPath.substring("file:".length(), urlPath.lastIndexOf("bin"));
			}
			else if (url.getProtocol().equals("jar")) {
				// This means the code was found in a jar, likely deployment
				// Find the path to the ResourceUtils within the jar and remove the descriptor then remove the tail
				// part of the string, which will contain information as to ResourceUtils' location within the jar.
				path = urlPath.substring("jar:file:/".length(), urlPath.lastIndexOf("everVoid.jar"));
				// The above returned a relative path, determined by where the jar was launched from. We have to convert
				// this to an absolute path. To do this, we create a temporary "File" (Java's terminology, as it turn out
				// this File will actually be a directory) in the current working directory; from it we can extract the
				// absolute path.
				final File f = new File(path);
				path = f.getAbsolutePath() + "/";
			}
			else {
				// we somehow failed to detect the code location; there are many ways this can happen. A jar within a jar
				// structure could cause this, as well as a third party wrapper. Whatever the cause, we could not detect
				// file location, and so we must exit the application.
				System.out.println("Could not find jar file! " + url.getProtocol() + " unrecognized");
				System.exit(-1);
			}
			try {
				// Path is currently URL encoded, we must decode it for the operating system to accept it.
				sMainPath = URLDecoder.decode(path, "UTF-8");
			}
			catch (final UnsupportedEncodingException e) {
				// we just failed to decode, well damn. Not much we can do about it.
				e.printStackTrace();
			}
		}
		// sMainPath guaranteed to be initialized, return it
		return sMainPath;
	}
}
