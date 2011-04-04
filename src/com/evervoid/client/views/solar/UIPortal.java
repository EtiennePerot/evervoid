package com.evervoid.client.views.solar;

import com.evervoid.client.EVFrameManager;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.interfaces.EVFrameObserver;
import com.evervoid.client.ui.ClickObserver;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.game.GameView;
import com.evervoid.client.views.game.GameView.PerspectiveType;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.prop.Portal;
import com.evervoid.state.prop.Star;
import com.evervoid.utils.MathUtils;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;

public class UIPortal extends UIProp implements EVFrameObserver, ClickObserver
{
	private static final float sRotationSpeed = 0.05f;
	private float aCurrentAngle = 0f;
	private final Portal aPortal;
	private PortalSprite aPortalSprite;

	public UIPortal(final SolarGrid grid, final Portal portal)
	{
		super(grid, portal.getLocation(), portal);
		aPortal = portal;
		buildProp();
	}

	@Override
	protected void buildSprite()
	{
		aPortalSprite = new PortalSprite();
		final Dimension dim = aPortal.getDimension();
		if (dim.width > dim.height) {
			aPortalSprite.getNewTransform().setScale(1, 2 * (float) dim.height / dim.width);
		}
		else {
			aPortalSprite.getNewTransform().setScale(2 * (float) dim.width / dim.height, 1);
		}
		addSprite(aPortalSprite);
		EVFrameManager.register(this);
	}

	@Override
	protected void finishedMoving()
	{
		// Cannot move
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		aCurrentAngle = MathUtils.mod(aCurrentAngle + f.aTpf * sRotationSpeed, FastMath.TWO_PI);
		aPortalSprite.setRotationAngle(aCurrentAngle);
	}

	@Override
	public UIControl getPanelUI()
	{
		final Star otherStar = aPortal.getDestinationPortal().getContainer().getStar();
		final UIControl container = new UIControl(BoxDirection.VERTICAL);
		container.addString("Wormhole to:", ColorRGBA.White, "redensek", 24, BoxDirection.HORIZONTAL);
		container.addString(otherStar.getSolarSystem().getName(), ColorRGBA.Red, "redensek", 24, BoxDirection.HORIZONTAL);
		container.addSpacer(1, 10);
		container.addUI(UIStar.getStarUI(otherStar), 1);
		container.registerClickObserver(this);
		return container;
	}

	@Override
	public void uiClicked(final UIControl clicked)
	{
		// Called when the panel UI gets clicked -> Jump view to other solar system
		GameView.changePerspective(PerspectiveType.SOLAR, aPortal.getDestinationPortal().getContainer());
	}
}
