package com.evervoid.client.graphics;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetLocator;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;

/**
 * Clone of {@link FileLocator} with replacements between path separator characters in order to avoid cross-platform issues
 */
public class EverFileLocator implements AssetLocator
{
	public static class AssetInfoFile extends AssetInfo
	{
		private final File file;

		public AssetInfoFile(final AssetManager manager, @SuppressWarnings("rawtypes") final AssetKey key, final File file)
		{
			super(manager, key);
			this.file = file;
		}

		public File getFile()
		{
			return file;
		}

		@Override
		public InputStream openStream()
		{
			try {
				return new FileInputStream(file);
			}
			catch (final FileNotFoundException ex) {
				return null;
			}
		}
	}

	private File root;

	@Override
	public AssetInfo locate(final AssetManager manager, @SuppressWarnings("rawtypes") final AssetKey key)
	{
		final String name = key.getName().replace("/", File.separator).replace("\\", File.separator);
		final File file = new File(root, name);
		if (file.exists() && file.isFile()) {
			return new AssetInfoFile(manager, key, file);
		}
		else {
			return null;
		}
	}

	@Override
	public void setRootPath(final String rootPath)
	{
		if (rootPath == null) {
			throw new NullPointerException();
		}
		root = new File(rootPath.replace("/", File.separator).replace("\\", File.separator));
		if (!root.isDirectory()) {
			throw new IllegalArgumentException("Given root path \"" + root + "\" not a directory");
		}
	}
}