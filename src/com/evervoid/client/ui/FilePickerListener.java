package com.evervoid.client.ui;

import java.io.File;

import com.evervoid.client.ui.FilePicker.FilePickerMode;

/**
 * Classes implementing the FilePickerListener interface will receive events from {@link FilePicker}s.
 */
public interface FilePickerListener
{
	/**
	 * Called when the user has selected a file and confirmed his selection.
	 * 
	 * @param picker
	 *            The {@link FilePicker} that was interacted with by the user
	 * @param mode
	 *            The {@link FilePickerMode} of the {@link FilePicker}
	 * @param file
	 *            The {@link File} that the user selected
	 */
	public void filePicked(FilePicker picker, FilePickerMode mode, File file);

	/**
	 * Called when the user has canceled or closed the {@link FilePicker}
	 * 
	 * @param picker
	 *            The {@link FilePicker} that was interacted with by the user
	 * @param mode
	 *            The {@link FilePickerMode} of the {@link FilePicker}
	 */
	public void filePickerCanceled(FilePicker picker, FilePickerMode mode);
}
