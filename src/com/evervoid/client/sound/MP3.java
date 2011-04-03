package com.evervoid.client.sound;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class MP3
{
	private final String filename;
	private Player player;
	private Thread playerThread;

	public MP3(final String filename)
	{
		this.filename = filename;
	}

	public void close()
	{
		if (player != null) {
			player.close();
		}
	}

	public void play() throws Exception
	{
		final FileInputStream ins = new FileInputStream(filename);
		final BufferedInputStream bins = new BufferedInputStream(ins);
		player = new Player(bins);
		playerThread = new Thread()
		{
			@Override
			public void run()
			{
				try {
					player.play();
				}
				catch (final Exception e) {
					System.out.println(e);
				}
			}
		};
		playerThread.start();
	}
}
