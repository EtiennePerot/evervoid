package com.evervoid.client;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.utils.LoggerUtils;
import com.evervoid.utils.MathUtils;
import com.evervoid.utils.ResourceUtils;

/**
 * Gets and sets preferences for the client instance. Preferences are saved to the preferences.json file in the application
 * directory.
 */
public class EVClientSettings implements Jsonable
{
	/**
	 * The default name for the everVoid preference file.
	 */
	private static final String sPreferencesFileName = "preferences.json";

	/**
	 * @return A random name for a player.
	 */
	public static String getRandomName()
	{
		final List<Json> names = Json.fromFile("schema/players.json").getListAttribute("names");
		return (MathUtils.getRandomElement(names)).getString();
	}

	/**
	 * The directory in which the preference file is saved.
	 */
	private final File aAppDataDirectory;
	/**
	 * The local player's nickname.
	 */
	private String aPlayerNickname = null;
	/**
	 * Whether music should play.
	 */
	private boolean aShouldPlayMusic = true;
	/**
	 * Whether sound effects should play.
	 */
	private boolean aShouldPlaySfx = true;

	/**
	 * Loads client settings from the file on disk or uses a random name and default settings.
	 */
	public EVClientSettings()
	{
		// detect OS in oder to save to correct location
		aAppDataDirectory = new File(ResourceUtils.getAppDir());
		final File pref = getPreferencesFile();
		if (pref.exists()) {
			// name loading
			loadSettings();
		}
		else {
			LoggerUtils.info("Local preference file does not exist (on operating system "
					+ System.getProperty("os.name").toLowerCase() + ")");
		}
		if (aPlayerNickname == null) {
			// load random name
			aPlayerNickname = getRandomName();
		}
	}

	/**
	 * @return The directory in which application data will be saved.
	 */
	public File getAppDataDirectory()
	{
		return aAppDataDirectory;
	}

	/**
	 * @return The local player's u nickname.
	 */
	public String getPlayerNickname()
	{
		return aPlayerNickname;
	}

	/**
	 * @return The file in which preferences are saved
	 */
	private File getPreferencesFile()
	{
		return new File(aAppDataDirectory, sPreferencesFileName);
	}

	/**
	 * Loads the setting from the local preference file.
	 * 
	 * @return Whether the settings were loaded successfully.
	 */
	public boolean loadSettings()
	{
		final Json j = Json.fromFile(getPreferencesFile());
		try {
			if (j.hasAttribute("name")) {
				aPlayerNickname = j.getStringAttribute("name");
			}
			if (j.hasAttribute("sfx")) {
				aShouldPlaySfx = j.getBooleanAttribute("sfx");
			}
			if (j.hasAttribute("sound")) {
				aShouldPlayMusic = j.getBooleanAttribute("sound");
			}
		}
		catch (final Exception e) {
			LoggerUtils.warning("Troubles reading the settings file");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param nickname
	 *            The player's new nickname.
	 */
	public void setPlayerNickname(final String nickname)
	{
		aPlayerNickname = nickname;
	}

	/**
	 * @param shouldPlay
	 *            Whether music should be played.
	 */
	public void setShouldPlayMusic(final boolean shouldPlay)
	{
		aShouldPlayMusic = shouldPlay;
	}

	/**
	 * @param shouldPlay
	 *            Whether sound effects should be played.
	 */
	public void setShouldPlaySfx(final boolean shouldPlay)
	{
		aShouldPlaySfx = shouldPlay;
	}

	/**
	 * @return Whether background music should be played.
	 */
	public boolean shouldPlayMusic()
	{
		return aShouldPlayMusic;
	}

	/**
	 * @return Whether sound effects should be played.
	 */
	public boolean shouldPlaySfx()
	{
		return aShouldPlaySfx;
	}

	@Override
	public Json toJson()
	{
		final Json j = new Json();
		j.setAttribute("name", aPlayerNickname);
		j.setAttribute("sfx", aShouldPlaySfx);
		j.setAttribute("sound", aShouldPlayMusic);
		return j;
	}

	@Override
	public String toString()
	{
		return toJson().toPrettyString();
	}

	/**
	 * Writes the preferences file to disk; will create the file if it doens't exist.
	 */
	public void writeToDisk()
	{
		final File file = getPreferencesFile();
		try {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		catch (final IOException e) {
			// file already exists
		}
		toJson().toFile(file);
	}
}
