package com.evervoid.client.ui;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.evervoid.client.EVClientSaver;
import com.evervoid.client.KeyboardKey;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * A file picker dialog that allows the user to select and existing file (for loading) or input a (possibly already-existing)
 * filename (for saving).
 */
public class FilePicker extends UIControl implements ClickObserver, ButtonListener, TextInputListener
{
	/**
	 * A {@link FilePicker} may be used to load or save files.
	 */
	public enum FilePickerMode
	{
		/**
		 * Mode to load a file (user will not be able to select non-existing files)
		 */
		LOAD,
		/**
		 * Mode to save files (user will be able to type any filename)
		 */
		SAVE;
		/**
		 * @return The title of the {@link FilePicker} window to use, corresponding to the {@link FilePickerMode} selected
		 */
		private String getTitle()
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

	/**
	 * Color to use for the label of the selected file
	 */
	private static final ColorRGBA sSelectedFileColor = new ColorRGBA(0.95f, 0.95f, 0.95f, 1f);
	/**
	 * Color to use for the label of non-selected files
	 */
	private static final ColorRGBA sUnselectedFileColor = new ColorRGBA(0.8f, 0.8f, 0.95f, 1f);
	/**
	 * Reference to the Cancel {@link ButtonControl}
	 */
	private final ButtonControl aCancelButton;
	/**
	 * Reference to the filename {@link TextInputControl} where the user may type a filename
	 */
	private final TextInputControl aFilenameText;
	/**
	 * Maps the UI-visible {@link StaticTextControl} to actual {@link File} objects corresponding to them
	 */
	private final Map<StaticTextControl, File> aFiles = new HashMap<StaticTextControl, File>();
	/**
	 * {@link ScrollingControl} used to scroll when there are too many files in the current directory
	 */
	private final ScrollingControl aFileScrolling;
	/**
	 * Set of {@link FilePickerListener} that will receive events from this file picker
	 */
	private final Set<FilePickerListener> aListeners = new HashSet<FilePickerListener>();
	/**
	 * Reference to the main {@link PanelControl} used to put all the dialog's UI
	 */
	private final PanelControl aMainPanel;
	/**
	 * The {@link FilePickerMode} of this file picker
	 */
	private final FilePickerMode aMode;
	/**
	 * Reference to the OK {@link ButtonControl}
	 */
	private final ButtonControl aOKButton;
	/**
	 * Label (as a {@link StaticTextControl}) of the selected file, or null if no file is selected
	 */
	private StaticTextControl aSelectedFileText = null;

	/**
	 * Constructor
	 * 
	 * @param mode
	 *            The {@link FilePickerMode} to use
	 */
	public FilePicker(final FilePickerMode mode)
	{
		aMode = mode;
		aMainPanel = new PanelControl(mode.getTitle());
		aFileScrolling = new ScrollingControl(256, 340);
		for (final File saveFile : EVClientSaver.getAvailableSaveFiles()) {
			final StaticTextControl text = new StaticTextControl(saveFile.getName(), sUnselectedFileColor);
			text.registerClickObserver(this);
			aFiles.put(text, saveFile);
			aFileScrolling.addUI(text);
		}
		aMainPanel.addUI(new DarkBoxControl(aFileScrolling));
		aMainPanel.addSpacer(1, 16);
		aFilenameText = new TextInputControl(256);
		aFilenameText.addTextInputListener(this);
		aMainPanel.addUI(aFilenameText);
		aMainPanel.addSpacer(1, 16);
		final UIControl buttonRow = new UIControl(BoxDirection.HORIZONTAL);
		buttonRow.addFlexSpacer(1);
		aCancelButton = new ButtonControl("Cancel");
		aCancelButton.addButtonListener(this);
		buttonRow.addUI(aCancelButton);
		buttonRow.addSpacer(16, 1);
		aOKButton = new ButtonControl("OK");
		aOKButton.addButtonListener(this);
		buttonRow.addUI(aOKButton);
		aMainPanel.addUI(buttonRow);
		addUI(new CenteredControl(aMainPanel), 1);
		aFilenameText.onClick(this);
	}

	@Override
	public void buttonClicked(final UIControl button)
	{
		if (button.equals(aCancelButton)) {
			close();
		}
		else if (button.equals(aOKButton)) {
			confirm();
		}
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

	/**
	 * Called when closing this file picker, causing it to raise a filePickerCanceled event
	 */
	public void close()
	{
		for (final FilePickerListener listener : aListeners) {
			listener.filePickerCanceled(this, aMode);
		}
	}

	/**
	 * Called when the user confirms his file selection, causing it to raise a filePicked event
	 */
	public void confirm()
	{
		String filename = aFilenameText.getText();
		if (!filename.endsWith(EVClientSaver.sSaveFileExtension)) {
			filename += EVClientSaver.sSaveFileExtension;
		}
		final File selected = new File(EVClientSaver.getSaveFilesDirectory(), filename);
		for (final FilePickerListener listener : aListeners) {
			listener.filePicked(this, aMode, selected);
		}
	}

	@Override
	public void onTextInputDefocus(final TextInputControl control)
	{
		// Nothing
	}

	@Override
	public void onTextInputFocus(final TextInputControl control)
	{
		// Nothing
	}

	@Override
	public void onTextInputKey(final TextInputControl control, final KeyboardKey key)
	{
		String filename = aFilenameText.getText();
		if (!filename.endsWith(EVClientSaver.sSaveFileExtension)) {
			filename += EVClientSaver.sSaveFileExtension;
		}
		if (aSelectedFileText != null) {
			aSelectedFileText.setColor(sUnselectedFileColor);
		}
		boolean found = false;
		for (final StaticTextControl text : aFiles.keySet()) {
			if (text.getText().equalsIgnoreCase(filename)) {
				found = true;
				aSelectedFileText = text;
				aSelectedFileText.setColor(sSelectedFileColor);
				break;
			}
		}
		if (!found) {
			aSelectedFileText = null;
		}
		if (key.equals(KeyboardKey.ENTER)) {
			confirm();
		}
	}

	/**
	 * Add a {@link FilePickerListener} to the set of {@link FilePickerListener}s that will receive events from this file picker
	 * 
	 * @param listener
	 *            The {@link FilePickerListener} to add
	 */
	public void registerListener(final FilePickerListener listener)
	{
		aListeners.add(listener);
	}

	@Override
	public boolean uiClicked(final UIControl clicked)
	{
		final File selected = aFiles.get(clicked);
		if (selected == null) {
			return false;
		}
		if (aSelectedFileText != null) {
			aSelectedFileText.setColor(sUnselectedFileColor);
		}
		if (clicked.equals(aSelectedFileText)) { // Clicked twice on the same file
			confirm();
		}
		aSelectedFileText = (StaticTextControl) clicked;
		aSelectedFileText.setColor(sSelectedFileColor);
		aFilenameText.setText(aSelectedFileText.getText());
		aFilenameText.onClick(this);
		return true;
	}
}
