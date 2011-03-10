package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.SphericalSprite;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.AnimatedRotation;
import com.evervoid.client.graphics.geometry.Smoothing;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.ui.HorizontalCenteredControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.state.prop.Portal;
import com.evervoid.state.prop.Star;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;

public class UIPortal extends UIProp
{
	private final Portal aPortal;
	private AnimatedRotation aPortalRotation;
	private Transform aScaling;

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
		final Sprite spr = new Sprite("space/wormhole.png");
		aPortalRotation = spr.getNewRotationAnimation();
		final EverNode wrapper = new EverNode(spr);
		addSprite(wrapper);
		aScaling = wrapper.getNewTransform();
		aScaling.setScale(1, 0.25f);
		aPortalRotation.setDuration(1).setSmoothing(Smoothing.LINEAR);
		infiniteRotation();
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

	private void infiniteRotation()
	{
		aPortalRotation.setTargetPitch(aPortalRotation.getRotationPitch() - FastMath.HALF_PI).start(new Runnable()
		{
			@Override
			public void run()
			{
				// Scheduled on other thread to prevent stack overflow
				infiniteRotation();
			}
		});
	}
}
