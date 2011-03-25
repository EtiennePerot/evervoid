package com.evervoid.client.views.credits;

import com.evervoid.client.EVViewManager;
import com.evervoid.client.EVViewManager.ViewType;
import com.evervoid.client.ui.BoxControl;
import com.evervoid.client.ui.ButtonControl;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.jme3.math.ColorRGBA;

public class CreditsPanel extends BoxControl implements ButtonListener
{
	private final ButtonControl aMainMenuButton;
	private final StaticTextControl aStaticName;

	public CreditsPanel()
	{
		super(BoxDirection.VERTICAL);
		aStaticName = new StaticTextControl("Project Management.\n" + "\t Team Leader\n" + "\t\t Valentin Bonnet\n" + "\n"
				+ "Software development\n" + "\t Senior Software Engineer\n" + "\t\t Etienne Perot\n"
				+ "\t Software Engineer\n" + "\t\t Valentin Bonnet\n" + "\t Junior Software Engineer\n"
				+ "\t\t Pier-Luc Gagnon", ColorRGBA.Orange);
		addUI(aStaticName);
		addSpacer(1, 20);
		aMainMenuButton = new ButtonControl("Main Menu");
		aMainMenuButton.addButtonListener(this);
		addUI(aMainMenuButton);
	}

	@Override
	public void buttonClicked(final UIControl button)
	{
		if (button.equals(aMainMenuButton)) {
			EVViewManager.switchTo(ViewType.MAINMENU);
		}
	}
}
