package com.evervoid.client.views;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.state.Dimension;

public class TopBarView extends EverView
{
	private final Sprite aLeftSprite;
	private final Sprite aMiddleSprite;
	private final Transform aMiddleTransform;
	private final Transform aRightOffset;
	private final Sprite aRightSprite;
	private final Transform aScreenOffset;

	protected TopBarView(final Dimension pDimension)
	{
		super();
		aLeftSprite = new Sprite("ui/topbar_left.png").bottomLeftAsOrigin();
		aMiddleSprite = new Sprite("ui/topbar_middle.png").bottomLeftAsOrigin();
		aRightSprite = new Sprite("ui/topbar_right.png").bottomLeftAsOrigin();
		addNode(aLeftSprite);
		addNode(aMiddleSprite);
		addNode(aRightSprite);
		aScreenOffset = getNewTransform();
		aRightOffset = aRightSprite.getNewTransform();
		aMiddleTransform = aMiddleSprite.getNewTransform();
		resolutionChanged();
	}

	@Override
	public void resolutionChanged()
	{
		super.resolutionChanged();
		final float barHeight = aLeftSprite.getHeight() * Sprite.sSpriteScale;
		final Dimension windowDimension = EverVoidClient.getWindowDimension();
		aScreenOffset.translate(0, windowDimension.height - barHeight);
		aRightOffset.translate(windowDimension.width - aRightSprite.getWidth() * Sprite.sSpriteScale, 0);
		final float middleWidth = windowDimension.width - (aLeftSprite.getWidth() * Sprite.sSpriteScale)
				- (aRightSprite.getWidth() * Sprite.sSpriteScale);
		aMiddleTransform.translate(aLeftSprite.getWidth() * Sprite.sSpriteScale, 0).setScale(
				middleWidth / aMiddleSprite.getWidth() / Sprite.sSpriteScale, 1f, 1f);
	}
}
