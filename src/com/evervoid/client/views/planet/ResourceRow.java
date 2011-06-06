package com.evervoid.client.views.planet;

import com.evervoid.client.ui.ImageControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.VerticalCenteredControl;
import com.evervoid.state.EVGameState;
import com.evervoid.state.data.ResourceData;
import com.evervoid.state.player.ResourceAmount;
import com.jme3.math.ColorRGBA;

public class ResourceRow extends UIControl
{
	public ResourceRow(final EVGameState state, final String prefix, final ResourceAmount resources)
	{
		this(state, prefix, resources, null);
	}

	public ResourceRow(final EVGameState state, final String prefix, final ResourceAmount resources, final Integer turns)
	{
		super(BoxDirection.HORIZONTAL);
		setDefaultTextColor(ColorRGBA.White);
		if (prefix != null && !prefix.isEmpty()) {
			addString(prefix, BoxDirection.VERTICAL);
			addSpacer(12, 1);
		}
		boolean first = true;
		for (final String resName : resources.getNames()) {
			if (resources.getValue(resName) <= 0) {
				continue;
			}
			if (!first) {
				addSpacer(12, 1);
			}
			first = false;
			final ResourceData resData = state.getResourceData(resName);
			addUI(new VerticalCenteredControl(new ImageControl(resData.getIconSpriteURL())));
			addSpacer(4, 1);
			addString(resources.getFormattedValue(resName), BoxDirection.VERTICAL);
		}
		if (turns != null) {
			addFlexSpacer(1);
			addUI(new VerticalCenteredControl(new ImageControl("icons/resources/time.png")));
			addSpacer(4, 1);
			addString(turns + " turn" + (turns == 1 ? "" : "s"), BoxDirection.VERTICAL);
		}
	}
}
