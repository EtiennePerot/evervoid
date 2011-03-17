package com.evervoid.client.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import com.evervoid.client.graphics.GraphicManager;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.views.Bounds;
import com.evervoid.state.data.SpriteData;
import com.jme3.math.Vector2f;

public class ShapedButtonControl extends ImageControl
{
	private final Set<ButtonListener> aButtonObservers = new HashSet<ButtonListener>();
	private final ImageControl aButtonOn;
	private final AnimatedAlpha aButtonOnAlpha;
	private BufferedImage aHitZone;
	private int aOffSpriteHeight;
	private SpriteData aOffSpriteInfo;

	public ShapedButtonControl(final SpriteData spriteOff, final SpriteData spriteOn)
	{
		super(spriteOff);
		aOffSpriteInfo = spriteOff;
		aOffSpriteHeight = (int) (getHeight() / aOffSpriteInfo.scale);
		aButtonOn = new ImageControl(spriteOn);
		try {
			aHitZone = ImageIO.read(new File(GraphicManager.getSpritePath(spriteOff.sprite)));
		}
		catch (final IOException e) {
			aHitZone = null;
		}
		aButtonOnAlpha = aButtonOn.getNewAlphaAnimation();
		aButtonOnAlpha.setDuration(0.5).translate(0, 0, 1).setAlpha(0); // Put "on" button in front, invisible
		addNode(aButtonOn);
	}

	public ShapedButtonControl(final String spriteOff, final String spriteOn)
	{
		this(new SpriteData(spriteOff), new SpriteData(spriteOn));
	}

	public void addButtonListener(final ButtonListener listener)
	{
		aButtonObservers.add(listener);
	}

	@Override
	protected boolean inBounds(final Vector2f point)
	{
		if (aHitZone == null || !super.inBounds(point)) {
			return false;
		}
		final Vector2f localPoint = point.subtract(getComputedBounds().x, getComputedBounds().y).divide(aOffSpriteInfo.scale);
		try {
			return ((aHitZone.getRGB((int) localPoint.x, (int) (aOffSpriteHeight - localPoint.y)) >> 24) & 0xFF) > 127;
		}
		catch (final ArrayIndexOutOfBoundsException e) {
			return false;
		}
	}

	@Override
	public boolean onMouseMove(final Vector2f point)
	{
		super.onMouseMove(point);
		final float alpha = aButtonOnAlpha.getTargetAlpha();
		final boolean mouseIn = inBounds(point);
		if (alpha == 0 && mouseIn) {
			aButtonOnAlpha.setTargetAlpha(1).start();
			return true;
		}
		if (alpha == 1 && !mouseIn) {
			aButtonOnAlpha.setTargetAlpha(0).start();
			return false;
		}
		return false;
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		super.setBounds(new Bounds(bounds.x, bounds.y, getWidth(), getHeight()));
	}

	@Override
	public void setSprite(final SpriteData sprite)
	{
		super.setSprite(sprite);
		aOffSpriteInfo = sprite;
		aOffSpriteHeight = (int) (getHeight() / aOffSpriteInfo.scale);
	}
}
