package com.evervoid.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.GraphicManager;
import com.evervoid.client.graphics.Sizable;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.views.Bounds;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.LineWrapMode;
import com.jme3.font.Rectangle;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * Base class used for writing text on the screen
 */
public class BaseText extends EverNode implements Sizable
{
	/**
	 * Transform to align the text correctly
	 */
	private final Transform aBottomLeftOffset;
	/**
	 * Color of the text
	 */
	private ColorRGBA aColor;
	/**
	 * For multicolor text, various {@link TextColorRange}s can be used
	 */
	private final List<TextColorRange> aColorRanges = new ArrayList<TextColorRange>();
	/**
	 * The font to use, as a {@link BitmapFont}
	 */
	private final BitmapFont aFont;
	/**
	 * The {@link LineWrapMode} to use on the text
	 */
	private LineWrapMode aLineWrapMode = LineWrapMode.NoWrap;
	/**
	 * If set, constraints the text to be drawn in the given rectangle
	 */
	private Rectangle aRenderBounds = null;
	/**
	 * What the text actually says
	 */
	private String aString;
	/**
	 * The jME3 {@link BitmapText} used to actually write the text
	 */
	private BitmapText aText = null;

	/**
	 * Constructor
	 * 
	 * @param text
	 *            The text to write
	 * @param color
	 *            The color of the text
	 * @param font
	 *            The font to use
	 * @param size
	 *            The size of the font to use
	 */
	public BaseText(final String text, final ColorRGBA color, final String font, final int size)
	{
		aString = text;
		aColor = color;
		aFont = GraphicManager.getFont(font, size);
		aBottomLeftOffset = getNewTransform();
		updateText();
	}

	@Override
	public Vector2f getDimensions()
	{
		return new Vector2f(getWidth(), getHeight());
	}

	@Override
	public float getHeight()
	{
		return aText.getHeight();
	}

	/**
	 * @return The height of one line of text with the given font and size, in pixels
	 */
	public float getLineHeight()
	{
		return aText.getLineHeight();
	}

	/**
	 * @return The number of lines used by this text
	 */
	public int getLines()
	{
		return aText.getLineCount();
	}

	/**
	 * @return The string currently being displayed
	 */
	public String getText()
	{
		return aString;
	}

	@Override
	public float getWidth()
	{
		// Yay we got this jME3 bug fixed:
		// http://jmonkeyengine.org/groups/gui/forum/topic/bitmaptext-getlinewidth-always-returns-0/
		return aText.getLineWidth();
	}

	@Override
	public void setAlpha(final float alpha)
	{
		aText.setColor(new ColorRGBA(aColor.r, aColor.g, aColor.b, aColor.a * alpha));
		for (final TextColorRange range : aColorRanges) {
			aText.setColor(range.start, range.end, range.getAtAlpha(alpha));
		}
	}

	/**
	 * Set the color of the entire text. Will overwrite any previously-defined color ranges
	 * 
	 * @param color
	 *            The color of the entire text
	 */
	public void setColor(final ColorRGBA color)
	{
		// Yay we got that jME3 bug fixed too
		aColor = color;
		aColorRanges.clear();
		updateText();
	}

	/**
	 * Add a special text range to color differently
	 * 
	 * @param start
	 *            The start index of the range
	 * @param end
	 *            The end index of the range
	 * @param color
	 *            The {@link ColorRGBA} to use on this range
	 */
	public void setColor(final int start, final int end, final ColorRGBA color)
	{
		aColorRanges.add(new TextColorRange(start, end, color));
		updateText();
	}

	/**
	 * Set the line wrap mode to a new value
	 * 
	 * @param mode
	 *            The {@link LineWrapMode} to use
	 */
	public void setLineWrapMode(final LineWrapMode mode)
	{
		aLineWrapMode = mode;
	}

	/**
	 * Constrains the text to be drawn in certain bounds.
	 * 
	 * @param bounds
	 *            The bounds to use, or null to remove the constraint
	 */
	public void setRenderBounds(final Bounds bounds)
	{
		if (bounds == null && aRenderBounds != null) {
			aRenderBounds = null;
			updateText();
		}
		else if (bounds != null) {
			final Rectangle newBounds = new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height);
			// jME's Rectangle doesn't have .equals, so we gotta do this manually...
			if (aRenderBounds == null || aRenderBounds.x != newBounds.x || aRenderBounds.y != newBounds.y
					|| aRenderBounds.width != newBounds.width || aRenderBounds.height != newBounds.height) {
				aRenderBounds = newBounds;
				updateText();
			}
		}
	}

	/**
	 * Set the text to actually write
	 * 
	 * @param text
	 *            The text to write on the screen
	 */
	public void setText(final String text)
	{
		aString = text;
		updateText();
	}

	/**
	 * Updates the on-screen display
	 */
	private void updateText()
	{
		if (aText != null) {
			detachChild(aText);
		}
		final float alpha = getComputedAlpha();
		aText = new BitmapText(aFont);
		aText.setText(aString);
		aText.setColor(new ColorRGBA(aColor.r, aColor.g, aColor.b, aColor.a * alpha));
		for (final TextColorRange range : aColorRanges) {
			aText.setColor(range.start, range.end, range.getAtAlpha(alpha));
		}
		attachChild(aText);
		if (aRenderBounds != null) {
			aText.setLineWrapMode(aLineWrapMode);
			aText.setEllipsisChar('_');
			aText.setBox(aRenderBounds);
		}
		// BitmapTexts are drawn towards the bottom, so we gotta compensate for that
		aBottomLeftOffset.translate(0, getHeight());
	}
}
