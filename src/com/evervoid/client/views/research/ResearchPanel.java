package com.evervoid.client.views.research;

import com.evervoid.client.ui.BoxControl;
import com.evervoid.client.ui.StaticTextControl;
import com.jme3.math.ColorRGBA;

public class ResearchPanel extends BoxControl
{
	private final StaticTextControl aStaticName;

	public ResearchPanel()
	{
		super(BoxDirection.VERTICAL);
		aStaticName = new StaticTextControl("Hello world, I am the research view.", ColorRGBA.Orange);
		addUI(aStaticName);
	}
}
