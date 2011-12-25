package com.evervoid.client.ui;

import com.evervoid.client.views.Bounds;

/**
 * A special background that activates on hover to any {@link UIControl}
 */
public class UIHoverSelect extends WrapperControl
{
	/**
	 * The offset between the bottom left corner of the parent control and the hover background
	 */
	private static final int sXYHoverOffset = 4;
	/**
	 * The parent {@link UIControl} that has the background
	 */
	private final UIControl aParent;

	/**
	 * Constructor
	 * 
	 * @param parent
	 *            The parent {@link UIControl} that will have the background
	 */
	public UIHoverSelect(final UIControl parent)
	{
		super(new MarginSpacer(0, 0, sXYHoverOffset, sXYHoverOffset, new UIControl()), BoxDirection.HORIZONTAL);
		aParent = parent;
		final BackgroundedUIControl bg = new BackgroundedUIControl(BoxDirection.HORIZONTAL, "ui/selectedbackground.png");
		bg.addChildUI(aContained, 1);
		addChildUI(bg, 1);
		getNewTransform().translate(-sXYHoverOffset, -sXYHoverOffset, -UIControl.sChildZOffset / 2f);
		parentBoundsChanged();
		parent.addNode(this);
	}

	/**
	 * Called by the parent {@link UIControl} whenever its bounds change.
	 */
	void parentBoundsChanged()
	{
		final Bounds parentBounds = aParent.getAbsoluteComputedBounds();
		setBounds(new Bounds(0, 0, parentBounds.width + 2 * sXYHoverOffset, parentBounds.height + 2 * sXYHoverOffset));
	}
}
