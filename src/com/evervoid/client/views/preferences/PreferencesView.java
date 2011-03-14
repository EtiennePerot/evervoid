package com.evervoid.client.views.preferences;

import com.evervoid.client.ui.CenteredControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverUIView;

public class PreferencesView extends EverUIView
{
	public PreferencesView()
	{
		super(new UIControl(BoxDirection.VERTICAL));
		addUI(new CenteredControl(new PreferencePanel()), 1);
		setBounds(Bounds.getWholeScreenBounds());
	}
}
