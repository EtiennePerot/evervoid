package com.evervoid.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.GraphicManager;
import com.evervoid.client.graphics.Sizeable;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.views.Bounds;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.LineWrapMode;
import com.jme3.font.Rectangle;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class BaseText extends EverNode implements Sizeable
{
	private final Transform aBottomLeftOffset;
	private ColorRGBA aColor;
	private final List<TextColorRange> aColorRanges = new ArrayList<TextColorRange>();
	private final BitmapFont aFont;
	private LineWrapMode aLineWrapMode = LineWrapMode.NoWrap;
	private Rectangle aRenderBounds = null;
	private String aString;
	private BitmapText aText = null;

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

	public float getLineHeight()
	{
		return aText.getLineHeight();
	}

	public int getLines()
	{
		return aText.getLineCount();
	}

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

	public void setColor(final ColorRGBA color)
	{
		// Yay we got that jME3 bug fixed too
		aColor = color;
		aColorRanges.clear();
		updateText();
	}

	public void setColor(final int start, final int end, final ColorRGBA color)
	{
		aColorRanges.add(new TextColorRange(start, end, color));
		updateText();
	}

	public void setLineWrapMode(final LineWrapMode mode)
	{
		aLineWrapMode = mode;
	}

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

	public void setText(final String text)
	{
		aString = text;
		updateText();
	}

	private void updateText()
	{
		if (aText != null) {
			detachChild(aText);
		}
		aText = new BitmapText(aFont);
		aText.setText(aString);
		aText.setColor(aColor);
		for (final TextColorRange range : aColorRanges) {
			aText.setColor(range.start, range.end, range.color);
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
