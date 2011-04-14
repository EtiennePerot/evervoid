package com.evervoid.utils;

import java.io.File;
import java.net.URL;

public class ResourceUtils
{
	private static String sResourcePath = null;

	public static String getResourceDir()
	{
		if (sResourcePath == null) {
			final URL url = ResourceUtils.class.getResource("");
			final String urlPath = url.toString();
			String path = "";
			if (url.getProtocol().equals("file")) {
				// for testing
				path = urlPath.substring("file:".length(), urlPath.lastIndexOf("bin"));
			}
			else if (url.getProtocol().equals("jar")) {
				// for deployment
				path = urlPath.substring("jar:file:/".length(), urlPath.lastIndexOf("everVoid.jar"));
				// this gives the relative path, now time to find abs
				final File f = new File(path);
				path = f.getAbsolutePath() + "/";
			}
			else {
				System.out.println("Could not find jar file! " + url.getProtocol() + " unrecognized");
				System.exit(-1);
			}
			// append res/ to it
			sResourcePath = path + "res/";
		}
		return sResourcePath;
	}
}
