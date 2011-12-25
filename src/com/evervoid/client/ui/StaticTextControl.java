package com.evervoid.client.ui;

import com.evervoid.client.views.Bounds;
import com.evervoid.state.geometry.Dimension;
import com.jme3.font.LineWrapMode;
import com.jme3.math.ColorRGBA;

/**
 * A StaticTextControl is simply a {@link UIControl} with the purpose of displaying text.
 */
public class StaticTextControl extends UIControl
{
	/**
	 * Default font to use when font is not specified
	 */
	public final static String sDefaultFont = "bitvoid";
	/**
	 * Default font size to use when font size is not specified
	 */
	public final static int sDefaultSize = 20;
	/**
	 * The {@link Dimension} that the actual text takes; necessary to keep here to accomodate the {@link BaseText}
	 */
	private Dimension aDimension;
	/**
	 * Whether to keep the text boundaries when the text changes or not (if true, text may be cut off but stay in place; if
	 * false, the overall layout will be affected).
	 */
	private boolean aKeepBounds = true;
	/**
	 * The {@link BaseText} used to actually draw the text on the screen
	 */
	private final BaseText aLabel;

	/**
	 * Constructor
	 * 
	 * @param text
	 *            The text to display
	 * @param color
	 *            The {@link ColorRGBA} to display the text with
	 */
	public StaticTextControl(final String text, final ColorRGBA color)
	{
		this(text, color, sDefaultFont, sDefaultSize);
	}

	/**
	 * Constructor
	 * 
	 * @param text
	 *            The text to display
	 * @param color
	 *            The {@link ColorRGBA} to display the text with
	 * @param font
	 *            The font to use
	 * @param size
	 *            The font size to use
	 */
	public StaticTextControl(final String text, final ColorRGBA color, final String font, final int size)
	{
		aLabel = new BaseText("", color, font, size);
		addNode(aLabel);
		setText(text);
	}

	/**
	 * @return The height taken up by the {@link BaseText}, in pixels
	 */
	public float getHeight()
	{
		return aDimension.height;
	}

	/**
	 * @return The line-height used for the {@link BaseText}
	 */
	public float getLineHeight()
	{
		return aLabel.getLineHeight();
	}

	/**
	 * @return The number of lines currently being used in the {@link BaseText}
	 */
	public int getLines()
	{
		return aLabel.getLines();
	}

	@Override
	public Dimension getMinimumSize()
	{
		return aDimension;
	}

	/**
	 * @return The text currently being displayed
	 */
	public String getText()
	{
		return aLabel.getText();
	}

	/**
	 * @return The width taken up by the {@link BaseText}, in pixels
	 */
	public float getWidth()
	{
		return aDimension.width;
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		// Do not pass x and y because the label is already a child of this node.
		// Passing it x and y would translate it further.
		aLabel.setRenderBounds(new Bounds(0, 0, bounds.width, bounds.height));
	}

	/**
	 * Change the color of the text
	 * 
	 * @param color
	 *            The new {@link ColorRGBA} to use. Will overwrite any previously-defined color ranges
	 */
	public void setColor(final ColorRGBA color)
	{
		aLabel.setColor(color);
	}

	/**
	 * Define a color range on the text
	 * 
	 * @param start
	 *            The index of the beginning of the range
	 * @param end
	 *            The index of the end of the range
	 * @param color
	 *            The {@link ColorRGBA} to use
	 */
	public void setColor(final int start, final int end, final ColorRGBA color)
	{
		aLabel.setColor(start, end, color);
	}

	/**
	 * Set whether to keep the boundaries when the text changes (if true, text may be cut off but stay in place; if false, the
	 * overall layout will be affected)
	 * 
	 * @param keepBounds
	 *            Whether to keep the text boundaries on changes or not
	 */
	public void setKeepBoundsOnChange(final boolean keepBounds)
	{
		aKeepBounds = keepBounds;
	}

	/**
	 * Set the {@link LineWrapMode} on the {@link BaseText}
	 * 
	 * @param mode
	 *            The {@link LineWrapMode} to use
	 */
	public void setLineWrapMode(final LineWrapMode mode)
	{
		aLabel.setLineWrapMode(mode);
	}

	/**
	 * Change the text currently being displayed
	 * 
	 * @param text
	 *            The new text
	 */
	public void setText(final String text)
	{
		setText(text, aKeepBounds);
	}

	/**
	 * Change the text currently being displayed
	 * 
	 * @param text
	 *            The new text
	 * @param keepBounds
	 *            Whether to keep current boundaries when changing the text. This can be set to a preferred value using
	 *            setKeepBoundsOnChange(), to avoid specifying it each time.
	 */
	public void setText(final String text, final boolean keepBounds)
	{
		if (!keepBounds) {
			aLabel.setRenderBounds(null); // Reset bounds on the text, otherwise dimension computation is wrong
		}
		if (!getText().equals(text)) {
			aLabel.setText(text);
		}
		aDimension = new Dimension((int) aLabel.getWidth(), (int) aLabel.getHeight());
		recomputeAllBounds();
	}
}
