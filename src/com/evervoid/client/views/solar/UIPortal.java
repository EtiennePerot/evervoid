package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.SphericalSprite;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.ui.HorizontalCenteredControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.prop.Portal;
import com.evervoid.state.prop.Star;
import com.jme3.math.ColorRGBA;

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
		// Cannot move
	}

	@Override
	public UIControl getPanelUI()
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
}
