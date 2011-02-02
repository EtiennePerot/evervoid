package com.evervoid.client.views;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.Sizeable;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.state.Dimension;
import com.jme3.math.Vector2f;

public class TopBarView extends EverView implements Sizeable
{
	private final Sprite aLeftSprite;
	private final Sprite aMiddleSprite;
	private final Transform aMiddleTransform;
	private final Transform aRightOffset;
	private final Sprite aRightSprite;
	private final Transform aScreenOffset;

	protected TopBarView()
	{
		aLeftSprite = new Sprite("ui/topbar/left.png").bottomLeftAsOrigin();
		aMiddleSprite = new Sprite("ui/topbar/middle.png").bottomLeftAsOrigin();
		aRightSprite = new Sprite("ui/topbar/right.png").bottomLeftAsOrigin();
		addNode(aLeftSprite);
		addNode(aMiddleSprite);
		addNode(aRightSprite);
		aScreenOffset = getNewTransform();
		aRightOffset = aRightSprite.getNewTransform();
		aMiddleTransform = aMiddleSprite.getNewTransform();
		resolutionChanged();
	}

	@Override
	public Vector2f getDimensions()
	{
		return new Vector2f(getWidth(), getHeight());
	}

	@Override
	public float getHeight()
	{
		return aMiddleSprite.getHeight();
	}

	@Override
	public float getWidth()
	{
		// Don't add up all widths; simply get the offset of the right corner and add the width of the right corner
		return aRightOffset.getTranslation2f().x + aRightSprite.getWidth();
	}

	@Override
	public void resolutionChanged()
	{
		super.resolutionChanged();
		setBounds(new Bounds(0, EverVoidClient.getWindowDimension().height - getHeight(),
				EverVoidClient.getWindowDimension().width, getHeight()));
		final float barHeight = aLeftSprite.getHeight();
		final Dimension windowDimension = EverVoidClient.getWindowDimension();
		aScreenOffset.translate(0, windowDimension.height - barHeight);
		aRightOffset.translate(windowDimension.width - aRightSprite.getWidth(), 0);
		final float middleWidth = windowDimension.width - aLeftSprite.getWidth() - aRightSprite.getWidth();
		aMiddleTransform.translate(aLeftSprite.getWidth(), 0).setScale(middleWidth / aMiddleSprite.getWidth(), 1f, 1f);
	}
}
