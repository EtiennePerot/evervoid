package com.evervoid.client.ui;

import java.io.File;

import com.evervoid.client.ui.FilePicker.FilePickerMode;

public interface FilePickerListener
{
	public void filePicked(FilePicker picker, FilePickerMode mode, File file);

	public void filePickerCanceled(FilePicker picker, FilePickerMode mode);
}
