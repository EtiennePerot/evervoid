package com.evervoid.client.sound;

import static com.evervoid.utils.ResourceUtils.getResourceDir;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

/**
 * This class is used to play audi files and contain information about said files.
 */
public class MP3 implements Cloneable
{
	/**
	 * Length of the audio file.
	 */
	private final int duration;
	/**
	 * Actual physical filename
	 */
	private final String filename;
	/**
	 * Javazoom audio player
	 */
	private Player player;
	/**
	 * Thread containing the javazoom player
	 */
	private Thread playerThread;

	/**
	 * Constructor without file length (defaults to 0).
	 * 
	 * @param filename
	 *            Audio filename
	 */
	public MP3(final String filename)
	{
		this(filename, 0);
	}

	/**
	 * Constructor with specified filename and file duration.
	 * 
	 * @param filename
	 *            The file's filename.
	 * @param duration
	 *            The audio file's length.
	 */
	public MP3(final String filename, final int duration)
	{
		this.filename = filename;
		this.duration = duration;
	}

	@Override
	public MP3 clone()
	{
		return new MP3(filename, duration);
	}

	/**
	 * Properly closes the MP3 file.
	 */
	public void close()
	{
		if (player != null) {
			player.close();
		}
	}

	/**
	 * returns the length of this mp3 in seconds, 0 if unspecified.
	 * 
	 * @return The length of this mp3 file, returns 0 if length is unspecified.
	 */
	public int getDuration()
	{
		return duration;
	}

	/**
	 * Gives the name of the sound file using the filename.
	 * 
	 * @return The relative path and filename of the file.
	 */
	public String getSoundName()
	{
		return "snd" + File.separator + filename;
	}

	/**
	 * Tell whether a MP3 has finished playing or not.
	 * 
	 * @return False if the mp3 is currently playing, true otherwise.
	 */
	public boolean isComplete()
	{
		return player.isComplete();
	}

	/**
	 * Start to play the mp3 file.
	 * 
	 * @throws Exception
	 *             If the file does not exist, is broken, or cannot be played.
	 */
	public void play() throws Exception
	{
		playerThread = new Thread()
		{
			@Override
			public void run()
			{
				try {
					final FileInputStream ins = new FileInputStream(getResourceDir() + getSoundName());
					final BufferedInputStream bins = new BufferedInputStream(ins);
					player = new Player(bins);
					player.play();
				}
				catch (final Exception e) {
					System.out.println(e);
				}
			}
		};
		playerThread.setName("Music");
		playerThread.setDaemon(true);
		playerThread.start();
	}
}
