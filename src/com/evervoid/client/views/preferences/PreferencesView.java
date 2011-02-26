package com.evervoid.client.views.preferences;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.ui.CenteredControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverView;
import com.jme3.math.Vector2f;

public class PreferencesView extends EverView
{
	private final UIControl aControl;

	public PreferencesView()
	{
		aControl = new UIControl(BoxDirection.VERTICAL);
		aControl.addUI(new CenteredControl(new PreferencePanel()), 1);
		addNode(aControl);
		resolutionChanged();
	}

	@Override
	public boolean onLeftClick(final Vector2f position, final float tpf)
	{
		aControl.click(position);
		return true;
	}

	@Override
	public void resolutionChanged()
	{
		aControl.setBounds(new Bounds(0, 0, EverVoidClient.getWindowDimension().width,
				EverVoidClient.getWindowDimension().height));
	}
}
