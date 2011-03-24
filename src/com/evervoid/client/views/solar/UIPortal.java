package com.evervoid.client.views.solar;

import com.evervoid.client.EVFrameManager;
import com.evervoid.client.graphics.FrameUpdate;
import com.evervoid.client.graphics.SphericalSprite;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.interfaces.EVFrameObserver;
import com.evervoid.client.ui.HorizontalCenteredControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.prop.Portal;
import com.evervoid.state.prop.Star;
import com.evervoid.utils.MathUtils;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;

public class UIPortal extends UIProp implements EVFrameObserver
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
	public UIControl buildPanelUI()
	{
		// FIXME: Hax for demo
		final float starScale = 0.2f;
		final Star otherSide = aPortal.getDestination().getContainer().getStar();
		final SphericalSprite spr = new SphericalSprite(otherSide.getSprite()).bottomLeftAsOrigin();
		spr.getNewTransform().setScale(starScale);
		spr.setRotationTime(otherSide.getLocation().dimension.getAverageSize() * 15 / starScale);
		spr.setClipPixels(1);
		final UIControl container = new UIControl(BoxDirection.VERTICAL);
		container.addFlexSpacer(1);
		container.addUI(new HorizontalCenteredControl(new StaticTextControl("Wormhole to:", ColorRGBA.White, "redensek", 24)));
		container.addSpacer(1, 10);
		final UIControl starContainer = new UIControl();
		starContainer.setDesiredDimension(new Dimension((int) (spr.getWidth() * starScale * SpriteData.sDefaultSpriteScale),
				(int) (spr.getHeight() * starScale * SpriteData.sDefaultSpriteScale)));
		starContainer.addNode(spr);
		final Sprite border = new Sprite(otherSide.getBorderSprite()).bottomLeftAsOrigin();
		border.getNewTransform().setScale(starScale);
		starContainer.addNode(border);
		container.addUI(new HorizontalCenteredControl(starContainer));
		container.addFlexSpacer(1);
		return container;
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
}
