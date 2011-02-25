package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.Sprite;
import com.evervoid.state.prop.Portal;

public class UIPortal extends UIProp
{
	private final Portal aPortal;

	public UIPortal(final SolarGrid grid, final Portal portal)
	{
		super(grid, portal.getLocation(), portal);
		aPortal = portal;
		buildProp();
	}

	@Override
	protected void buildSprite()
	{
		// TODO: Make this look fancier
		addSprite(new Sprite("space/wormhole.png"));
		if (aPortal.getHeight() == 4) {
			faceTowards((float) Math.PI / 2);
		}
	}

	@Override
	protected void finishedMoving()
	{
		// cannot move
	}
}
