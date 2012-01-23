package com.evervoid.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.views.Bounds;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.utils.MathUtils;
import com.jme3.math.Vector2f;

// TODO: Add smooth scrolling, with AnimatedTranslations and AniamtedAlpha for the top/bottom controls to fade in/out.
/**
 * A ScrollingControl is a specialized {@link UIControl} that can hold more {@link UIControl}s than it displays, and allows the
 * user to scroll vertically to show a certain subset of them. It has severe limitations due to lack of clipping in jME3; if
 * that can be fixed, then this whole class can be simplified a lot.
 */
public class ScrollingControl extends UIControl
{
	/**
	 * Scrolling by one mouse wheel unit scrolls by this amount of pixels
	 */
	private static final float sScrollMultiplier = 12;
	/**
	 * Whether all child elements fit in the {@link ScrollingControl} without the need to do any scrolling. This is computed
	 * automatically and changes the display behavior.
	 */
	private boolean aAllFitsIn = true;
	/**
	 * Automatically add a spacer of this height between each child
	 */
	private float aAutoSpacer = 0;
	/**
	 * All the {@link UIControl} currently visible to the viewer
	 */
	private final List<UIControl> aDisplayedControls = new ArrayList<UIControl>();
	/**
	 * The maximum height allowed to use for this {@link ScrollingControl}
	 */
	private float aMaxHeight;
	/**
	 * The vertical offset between the top of the {@link ScrollingControl} and the top of the first visible {@link UIControl}
	 * (not necessarily the first child {@link UIControl})
	 */
	private float aOffset = 0;
	/**
	 * Actual list of all children {@link UIControl}s, whether they are visible or not. Ordered by scrolling order.
	 */
	private final List<UIControl> aScrollingChildren = new ArrayList<UIControl>();
	/**
	 * Cumulative height of all the children {@link UIControl}s.
	 */
	private float aTotalHeight = 0;

	/**
	 * Constructor
	 */
	public ScrollingControl()
	{
		super(BoxDirection.VERTICAL);
	}

	/**
	 * Constructor with configurable desired size
	 * 
	 * @param minWidth
	 *            Minimum width to occupy
	 * @param minHeight
	 *            Minimum height to occupy
	 */
	public ScrollingControl(final float minWidth, final float minHeight)
	{
		this();
		setDesiredDimension(new Dimension(minWidth, minHeight));
	}

	@Override
	public void delAllChildUIs()
	{
		aScrollingChildren.clear();
		setBounds(getComputedBounds());
	}

	@Override
	public void deleteChildUI(final UIControl control)
	{
		if (aScrollingChildren.remove(control)) {
			aSprings.remove(control);
			control.aParent = null;
			delNode(control);
			// If removal was successful, recompute bounds
			recomputeAllBounds();
		}
	}

	@Override
	public List<UIControl> getChildrenUIs()
	{
		return aDisplayedControls;
	}

	/**
	 * @param control
	 *            The element in question.
	 * @return The index of the child control in the node stack.
	 */
	public int getChildUIIndex(final UIControl control)
	{
		return aScrollingChildren.indexOf(control);
	}

	@Override
	public Dimension getMinimumSize()
	{
		final Dimension min = super.getMinimumSize();
		return new Dimension(min.width, aMaxHeight);
	}

	@Override
	void insertChildUI(final int index, final UIControl control, int spring)
	{
		if (control == null) {
			return;
		}
		if (spring != 0) {
			System.err.println("CAUTION: Trying to add a non-zero-spring control to a ScrollingArea. Overriding to 0 spring!");
			spring = 0;
		}
		if (aScrollingChildren.contains(control)) {
			System.err.println("Warning: Trying to add the same UIControl twice.");
		}
		aScrollingChildren.add(MathUtils.mod(index, aScrollingChildren.size() + 1), control);
		// Recompute total height
		aTotalHeight = 0;
		for (final UIControl c : aScrollingChildren) {
			aTotalHeight += c.getDesiredSize().height + aAutoSpacer;
		}
		aTotalHeight -= aAutoSpacer;
		control.aParent = this;
		recomputeAllBounds();
	}

	@Override
	public boolean onMouseWheelDown(final float delta, final Vector2f position)
	{
		if (aAllFitsIn || !inBounds(position)) {
			return false;
		}
		aOffset = Math.min(aTotalHeight - aComputedBounds.height, aOffset + delta * sScrollMultiplier);
		setBounds(getComputedBounds());
		return true;
	}

	@Override
	public boolean onMouseWheelUp(final float delta, final Vector2f position)
	{
		if (aAllFitsIn || !inBounds(position)) {
			return false;
		}
		aOffset = Math.max(0, aOffset - delta * sScrollMultiplier);
		setBounds(getComputedBounds());
		return true;
	}

	/**
	 * Set automatic spacing to a certain amount of pixels between all children in the {@link ScrollingControl}.
	 * 
	 * @param space
	 *            The vertical spacing, in pixels
	 */
	public void setAutomaticSpacer(final int space)
	{
		aAutoSpacer = space;
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		if (bounds == null) {
			return;
		}
		aMaxHeight = bounds.height;
		aComputedBounds = bounds;
		setControlOffset(bounds.x, bounds.y);
		delAllNodes();
		aDisplayedControls.clear();
		if (aScrollingChildren.isEmpty()) {
			aAllFitsIn = true;
			return; // Well, that's all folks
		}
		float heightSoFar = 0;
		int firstChild = 0;
		while (heightSoFar < aOffset && firstChild < aScrollingChildren.size()) {
			heightSoFar += aScrollingChildren.get(firstChild).getDesiredSize().height + aAutoSpacer;
			firstChild++;
		}
		float yOffset = heightSoFar - aOffset;
		int lastChild = firstChild;
		while (heightSoFar <= aOffset + aMaxHeight && lastChild < aScrollingChildren.size()) {
			heightSoFar += aScrollingChildren.get(lastChild).getDesiredSize().height + aAutoSpacer;
			lastChild++;
		}
		if (heightSoFar > aOffset + aMaxHeight + aAutoSpacer) {
			lastChild--; // Prevent overlapping child at the end
		}
		// Guarantee at least one child displayed, even if out of bounds
		lastChild = Math.min(aScrollingChildren.size(), Math.max(firstChild + 1, lastChild));
		if (firstChild == 0 && lastChild == aScrollingChildren.size()) {
			aAllFitsIn = true;
			aOffset = 0;
			yOffset = 0;
		}
		else {
			aAllFitsIn = false;
		}
		for (int i = firstChild; i < lastChild; i++) {
			final UIControl child = aScrollingChildren.get(i);
			aDisplayedControls.add(child);
			addNode(child);
			final int minHeight = child.getDesiredSize().height;
			yOffset += minHeight + aAutoSpacer;
			child.setBounds(new Bounds(0, bounds.height - yOffset, bounds.width, minHeight));
		}
	}

	@Override
	public void setDesiredDimension(final Dimension desiredSize)
	{
		super.setDesiredDimension(desiredSize);
		aMaxHeight = desiredSize.getHeightFloat();
		recomputeAllBounds();
	}
}
