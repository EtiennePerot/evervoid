package com.evervoid.client.ui;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.evervoid.client.EVClientSaver;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class FilePicker extends UIControl implements ClickObserver
{
	public enum FilePickerMode
	{
		LOAD, SAVE;
		public String getTitle()
		{
			switch (this) {
				case LOAD:
					return "Load file";
				case SAVE:
					return "Save file";
			}
			return null;
		}
	}

	private final Map<StaticTextControl, File> aFiles = new HashMap<StaticTextControl, File>();
	private final ScrollingControl aFileScrolling;
	private final Set<FilePickerListener> aListeners = new HashSet<FilePickerListener>();
	private final PanelControl aMainPanel;
	private final FilePickerMode aMode;

	public FilePicker(final FilePickerMode mode)
	{
		aMode = mode;
		aMainPanel = new PanelControl(mode.getTitle());
		aFileScrolling = new ScrollingControl(200, 400);
		for (final File saveFile : EVClientSaver.getAvailableSaveFiles()) {
			final StaticTextControl text = new StaticTextControl(saveFile.getName(), ColorRGBA.White);
			text.registerClickObserver(this);
			aFiles.put(text, saveFile);
			aFileScrolling.addUI(text);
		}
		aMainPanel.addUI(new DarkBoxControl(aFileScrolling));
		addUI(new CenteredControl(aMainPanel), 1);
	}

	@Override
	public boolean click(final Vector2f point)
	{
		if (!aMainPanel.inAbsoluteBounds(point)) { // If user clicks outside, cancel
			close();
			return false;
		}
		return super.click(point);
	}

	public void close()
	{
		for (final FilePickerListener listener : aListeners) {
			listener.filePickerCancelled(this, aMode);
		}
	}

	public void registerListener(final FilePickerListener listener)
	{
		aListeners.add(listener);
	}

	@Override
	public void uiClicked(final UIControl clicked)
	{
		final File selected = aFiles.get(clicked);
		if (selected == null) {
			return;
		}
		for (final FilePickerListener listener : aListeners) {
			listener.filePicked(this, aMode, selected);
		}
	}
}
