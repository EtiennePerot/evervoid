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

/**
 * A ShapedButtonControl acts the same as a {@link ButtonControl}, but supports any arbitrary shape of clickable-ness due to
 * using an image as shape mask. It also supports hover effect (an alternate image) with a fade ebtween the two states.
 */
public class ShapedButtonControl extends ImageControl
{
	/**
	 * Set of {@link ButtonListener}s, must like {@link ButtonControl} has.
	 */
	private final Set<ButtonListener> aButtonObservers = new HashSet<ButtonListener>();
	/**
	 * The {@link ImageControl} to display when the mouse is over the clickable area of the {@link ShapedButtonControl}
	 */
	private final ImageControl aButtonOn;
	/**
	 * The {@link AnimatedAlpha} used to perform the fade effect to switch between the two states.
	 */
	private final AnimatedAlpha aButtonOnAlpha;
	/**
	 * The image mask loaded from the given sprite.
	 */
	private BufferedImage aHitZone;
	/**
	 * The offset in Y to apply to convert from/to mouse coordinates and mask coordinates
	 */
	private int aOffSpriteHeight;
	/**
	 * The {@link SpriteData} of the button
	 */
	private SpriteData aOffSpriteInfo;

	/**
	 * Constructor
	 * 
	 * @param spriteOff
	 *            {@link SpriteData} of the button in its non-hovered state
	 * @param spriteOn
	 *            {@link SpriteData} of the button in its hovered state
	 */
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

	/**
	 * Constructor
	 * 
	 * @param spriteOff
	 *            Sprite name of the button in its non-hovered state
	 * @param spriteOn
	 *            Sprite name of the button in its hovered state
	 */
	public ShapedButtonControl(final String spriteOff, final String spriteOn)
	{
		this(new SpriteData(spriteOff), new SpriteData(spriteOn));
	}

	/**
	 * Add a {@link ButtonListener} to the list of {@link ButtonListener}s.
	 * 
	 * @param listener
	 *            The {@link ButtonListener} to add.
	 */
	public void addButtonListener(final ButtonListener listener)
	{
		aButtonObservers.add(listener);
	}

	@Override
	public boolean click(final Vector2f point)
	{
		if (!inBounds(point)) {
			return false; // Out of bounds
		}
		for (final ButtonListener listener : aButtonObservers) {
			listener.buttonClicked(this);
		}
		return true;
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
