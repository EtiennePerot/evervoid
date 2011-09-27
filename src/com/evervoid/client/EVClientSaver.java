package com.evervoid.client;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import com.evervoid.json.Json;
import com.evervoid.state.EVGameState;

/**
 * This class deals with saving everVoid games to disk. It determines the extension name and location of these save files.
 */
public class EVClientSaver
{
	/**
	 * The singleton instance of the client saver.
	 */
	private static EVClientSaver sInstance = null;
	/**
	 * The default everVoid save file extension name.
	 */
	public static final String sSaveFileExtension = ".evervoid";

	/**
	 * @return The name of all save files on disk.
	 */
	public static List<File> getAvailableSaveFiles()
	{
		return getInstance().getSaveFiles();
	}

	/**
	 * @return The current saver instance, will create one if none exists.
	 */
	private static EVClientSaver getInstance()
	{
		if (sInstance == null) {
			sInstance = new EVClientSaver();
		}
		return sInstance;
	}

	/**
	 * @return The directory in which everVoid games are saved.
	 */
	public static File getSaveFilesDirectory()
	{
		final File directory = EverVoidClient.getSettings().getAppDataDirectory();
		if (!directory.isDirectory()) {
			if (directory.exists()) {
				directory.delete();
			}
			directory.mkdirs();
		}
		return directory;
	}

	/**
	 * Saves a state to the file location on disk.
	 * 
	 * @param file
	 *            The location on disk where to save the game.
	 * @param state
	 *            The state to save.
	 * @return Whether the save was successfully executed.
	 */
	public static boolean save(final File file, final EVGameState state)
	{
		return getInstance().saveGame(file, state);
	}

	/**
	 * Hidden constructor.
	 */
	private EVClientSaver()
	{
	}

	/**
	 * @return All save files on disk
	 */
	private List<File> getSaveFiles()
	{
		final File[] children = getSaveFilesDirectory().listFiles(new FilenameFilter()
		{
			@Override
			public boolean accept(final File dir, final String name)
			{
				return name.toLowerCase().endsWith(sSaveFileExtension.toLowerCase());
			}
		});
		if (children == null) {
			return new ArrayList<File>();
		}
		final List<File> saveFiles = new ArrayList<File>(children.length);
		for (final File f : children) {
			saveFiles.add(f);
		}
		return saveFiles;
	}

	/**
	 * Loads an everVoid game from disk.
	 * 
	 * @param filename
	 *            The location of the file on disk.
	 * @return The Json contained by the parameter file.
	 */
	public Json loadGame(final String filename)
	{
		return Json.fromFile(new File(getSaveFilesDirectory(), filename));
	}

	/**
	 * Saves a state to the file location on disk.
	 * 
	 * @param file
	 *            The location on disk where to save the game.
	 * @param state
	 *            The state to save.
	 * @return Whether the save was successfully executed.
	 */
	private boolean saveGame(final File file, final EVGameState state)
	{
		return state.toJson().toFile(file);
	}
}
