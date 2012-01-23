package com.evervoid.client.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.KeyboardKey;
import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.graphics.geometry.FrameTimer;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.views.Bounds;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.utils.MathUtils;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

/**
 * The base class of all UI elements. Represents a box in which other UIControls can be placed in a row or in a column, based on
 * the box spring model.
 */
public class UIControl extends EverNode
{
	/**
	 * Possible layouts for the box spring model
	 */
	public enum BoxDirection
	{
		/**
		 * Horizontal layout: Every element is placed on a horizontal line, from left to right.
		 */
		HORIZONTAL,
		/**
		 * Vertical layout: Every element is placed on a vertical line, from top to bottom.
		 */
		VERTICAL;
	}

	/**
	 * In order to make children appear in front of their parent element, a small offset is applied to their Z coordinate.
	 */
	static final float sChildZOffset = 0.000001f;
	/**
	 * When a UIControl is set to "disabled" (the user cannot interact with it), its alpha changes to this value
	 */
	private static final float sDisabledAlpha = 0.5f;
	/**
	 * Duration of the "enable"/"disable" animation
	 */
	private static final float sEnableDuration = 0.3f;
	/**
	 * Time necessary for the cursor to hover an element before its tooltip appears
	 */
	private static final float sTooltipTimer = 0.6f;
	/**
	 * Set of {@link ClickObserver}s that will be notified whenever this UIControl is clicked
	 */
	private final Set<ClickObserver> aClickObservers = new HashSet<ClickObserver>(0);
	/**
	 * Once this element has been given a screen position, this holds the {@link Bounds} occupied by this object. It is null
	 * when the object hasn't been laid out on the screen
	 */
	protected Bounds aComputedBounds = null;
	/**
	 * List of children UIControls inside this UIControl
	 */
	private final List<UIControl> aControls = new ArrayList<UIControl>(1);
	/**
	 * When adding text to a UIControl, this is the default color for it
	 */
	private ColorRGBA aDefaultColor = ColorRGBA.White;
	/**
	 * The box layout of this UIControl (See {@link BoxDirection})
	 */
	private final BoxDirection aDirection;
	/**
	 * If this element is ever enabled/disabled, this will hold the {@link AnimatedAlpha} object used to animate the
	 * enabled/disabled state change
	 */
	private AnimatedAlpha aEnableAlpha = null;
	/**
	 * Reference to the child UIControl that currently has keyboard focus
	 */
	private UIInputListener aFocusedElement = null;
	/**
	 * Whether this UIControl has a tooltip or not
	 */
	private boolean aHasTooltip;
	/**
	 * Whether this UIControl should display a hover effect when it is hovered by the mouse or not
	 */
	private boolean aHoverSelectable = false;
	/**
	 * Timer to detect when the mouse went outside of this UIControl in order to remove the selection animation
	 */
	private FrameTimer aHoverSelectTimer;
	/**
	 * Whether this UIControl is enabled (can respond to input events) or not
	 */
	private boolean aIsEnabled = true;
	/**
	 * If set, this will override the automatically-computed minimum size of this UIControl with a custom {@link Dimension}.
	 * Useful to enforce certain elements to have a certain width or height.
	 */
	private Dimension aMinimumDimension = null;
	/**
	 * A {@link Transform} used to position the UIControl on the screen. X and Y are used for screen positioning, while Z is
	 * used to add an offset for this UIControl to be in front of its parent.
	 */
	private final Transform aOffset;
	/**
	 * Reference to the parent UIControl. Null if this UIControl is the root UIControl or hasn't been assigned to a parent yet.
	 */
	protected UIControl aParent = null;
	/**
	 * Whether this UIControl is being hover-selected (with the selection animation) or not
	 */
	private boolean aSelected = false;
	/**
	 * If this UIControl is being hover-selected, this is a reference to the {@link UIHoverSelect} object creating the selection
	 * background effect
	 */
	private UIHoverSelect aSelectNode = null;
	/**
	 * Maps every child of this UIControl to their spring value inside this UIControl.
	 */
	protected final Map<UIControl, Integer> aSprings = new HashMap<UIControl, Integer>(1);
	/**
	 * The tooltip (As a {@link UITooltip}) of this UIControl, or null if this UIControl has no tooltip.
	 */
	private UITooltip aTooltip = null;
	/**
	 * Timer used to detect when the mouse has moved out of the UIControl in order to hide the tooltip
	 */
	private FrameTimer aTooltipTimer = null;

	/**
	 * Constructor. Defaults to a horizontal {@link BoxDirection} UIControl.
	 */
	public UIControl()
	{
		this(BoxDirection.HORIZONTAL);
	}

	/**
	 * Constructor.
	 * 
	 * @param direction
	 *            The direction of the box layout (horizontal or vertical)
	 */
	public UIControl(final BoxDirection direction)
	{
		aDirection = direction;
		aOffset = getNewTransform();
		aOffset.translate(0, 0, sChildZOffset);
		aHoverSelectTimer = null;
	}

	/**
	 * Add a child directly underneath this UIControl. Defaults to a 0 spring. DO NOT USE outside of the UI package; use addUI
	 * instead.
	 * 
	 * @param control
	 *            The {@link UIControl} to add
	 */
	void addChildUI(final UIControl control)
	{
		addChildUI(control, 0);
	}

	/**
	 * Add a child directly underneath this UIControl. DO NOT USE outside of the UI package; use addUI instead.
	 * 
	 * @param control
	 *            The {@link UIControl} to add
	 * @param spring
	 *            The spring of the {@link UIControl}
	 */
	void addChildUI(final UIControl control, final int spring)
	{
		insertChildUI(-1, control, spring);
	}

	/**
	 * Adds a flexible spacer with custom spring
	 * 
	 * @param spring
	 *            The spring of the spacer
	 * @return this, for chainability
	 */
	public UIControl addFlexSpacer(final int spring)
	{
		addUI(new UIControl(), spring);
		return this;
	}

	/**
	 * Add a spacer to the inner UIControl, with no spring.
	 * 
	 * @param width
	 *            The width of the spacer
	 * @param height
	 *            The height of the spacer
	 * @return this, for chainability
	 */
	public UIControl addSpacer(final int width, final int height)
	{
		addUI(new SpacerControl(width, height));
		return this;
	}

	/**
	 * Add a {@link StaticTextControl} as child of this UIControl.
	 * 
	 * @param texts
	 *            The strings to add
	 * @return this, for chainability
	 */
	public UIControl addString(final String... texts)
	{
		for (final String s : texts) {
			addString(s, aDefaultColor);
		}
		return this;
	}

	/**
	 * Add a {@link StaticTextControl} as child of this UIControl.
	 * 
	 * @param text
	 *            The text to add
	 * @param direction
	 *            Horizontal to center the text horizontally, vertical to center the text vertically, null to not center the
	 *            text at all
	 * @return this, for chainability
	 */
	public UIControl addString(final String text, final BoxDirection direction)
	{
		return addString(text, aDefaultColor, StaticTextControl.sDefaultFont, StaticTextControl.sDefaultSize, direction);
	}

	/**
	 * Add a {@link StaticTextControl} as child of this UIControl.
	 * 
	 * @param text
	 *            The text to add
	 * @param color
	 *            The color of the text
	 * @return this, for chainability
	 */
	public UIControl addString(final String text, final ColorRGBA color)
	{
		return addString(text, color, StaticTextControl.sDefaultFont, StaticTextControl.sDefaultSize, null);
	}

	/**
	 * Add a {@link StaticTextControl} as child of this UIControl.
	 * 
	 * @param text
	 *            The text to add
	 * @param color
	 *            The color of the text
	 * @param direction
	 *            Horizontal to center the text horizontally, vertical to center the text vertically, null to not center the
	 *            text at all
	 * @return this, for chainability
	 */
	public UIControl addString(final String text, final ColorRGBA color, final BoxDirection direction)
	{
		return addString(text, color, StaticTextControl.sDefaultFont, StaticTextControl.sDefaultSize, direction);
	}

	/**
	 * Add a {@link StaticTextControl} as child of this UIControl.
	 * 
	 * @param text
	 *            The text to add
	 * @param color
	 *            The color of the text
	 * @param font
	 *            The name of the font
	 * @param size
	 *            The size of the font
	 * @return this, for chainability
	 */
	public UIControl addString(final String text, final ColorRGBA color, final String font, final int size)
	{
		return addString(text, color, font, size, null);
	}

	/**
	 * Add a {@link StaticTextControl} as child of this UIControl.
	 * 
	 * @param text
	 *            The text to add
	 * @param color
	 *            The color of the text
	 * @param font
	 *            The name of the font
	 * @param size
	 *            The size of the font
	 * @param direction
	 *            Horizontal to center the text horizontally, vertical to center the text vertically, null to not center the
	 *            text at all
	 * @return this, for chainability
	 */
	public UIControl addString(final String text, final ColorRGBA color, final String font, final int size,
			final BoxDirection direction)
	{
		if (direction == null) {
			// catch the null case
			addUI(new StaticTextControl(text, color, font, size));
			return this;
		}
		switch (direction) {
			case HORIZONTAL:
				addUI(new HorizontalCenteredControl(new StaticTextControl(text, color, font, size)));
				break;
			case VERTICAL:
				addUI(new VerticalCenteredControl(new StaticTextControl(text, color, font, size)));
				break;
			default:
				// what the?
				addUI(new StaticTextControl(text, color, font, size));
		}
		return this;
	}

	/**
	 * Add a control as a child to this UIControl, with no spring.
	 * 
	 * @param control
	 *            The control to add
	 * @return this, for chainability
	 */
	public UIControl addUI(final UIControl control)
	{
		return addUI(control, 0);
	}

	/**
	 * Add a control as a child to this UIControl. Overridden by container subclasses.
	 * 
	 * @param control
	 *            The control to add
	 * @param spring
	 *            The spring value
	 * @return this, for chainability
	 */
	public UIControl addUI(final UIControl control, final int spring)
	{
		addChildUI(control, spring);
		return this;
	}

	@Override
	protected void cleanUp()
	{
		super.cleanUp();
		if (aTooltip != null) {
			aTooltip.close();
		}
	}

	/**
	 * Tests if the given point is within the boundaries of this UIControl, and if yes, fire a click event at this point
	 * 
	 * @param point
	 *            The point being clicked, relative to the parent node, or to the whole screen if this UIControl is the root
	 *            node
	 * @return Whether the click had any effect on this UIControl or not. If true, it is generally a good idea to stop
	 *         propagating the click event.
	 */
	public boolean click(final Vector2f point)
	{
		if (!inBounds(point)) {
			return false; // Out of bounds
		}
		final UIControl root = getRootUI();
		final UIInputListener focusedNode = root.aFocusedElement;
		if (this instanceof UIInputListener && !equals(focusedNode)) {
			// Got new focused element
			if (focusedNode != null) {
				focusedNode.onDefocus(this);
			}
			((UIInputListener) this).onClick(this);
		}
		final Vector2f newPoint = new Vector2f(point.x - aComputedBounds.x, point.y - aComputedBounds.y);
		for (final UIControl c : new ArrayList<UIControl>(getChildrenUIs())) {
			if (c.click(newPoint)) {
				return true;
			}
		}
		// TODO OMG HAX, CHECK IF THIS BREAK ANYTHING
		boolean acted = false;
		for (final ClickObserver observer : aClickObservers) {
			acted |= observer.uiClicked(this);
		}
		if (acted) {
			return true;
		}
		return false;
	}

	/**
	 * Close the tooltip associated with this UIControl, if it is shown
	 */
	void closeTooltip()
	{
		if (aTooltipTimer != null) {
			aTooltipTimer.stop();
			aTooltipTimer = null;
		}
		if (aTooltip != null) {
			aTooltip.close();
		}
	}

	/**
	 * Delete all children UIControls from this UIControl.
	 */
	public void delAllChildUIs()
	{
		delAllNodes();
		aControls.clear();
		recomputeAllBounds();
	}

	/**
	 * Delete a given child UIControl from this UIControl. Equivalent to calling child.deleteUI().
	 * 
	 * @param control
	 *            The UIControl to delete from this UIControl.
	 */
	public void deleteChildUI(final UIControl control)
	{
		if (aControls.remove(control)) {
			aSprings.remove(control);
			control.aParent = null;
			delNode(control);
			// If removal was successful, recompute bounds
			recomputeAllBounds();
		}
	}

	/**
	 * Delete this UIControl from its parent. Equivalent to calling parent.deleteUI(control).
	 */
	public void deleteUI()
	{
		if (aParent != null) {
			aParent.deleteChildUI(this);
		}
	}

	/**
	 * Set the state of this UIControl to "disabled", making it unresponsive to input events and firing the disable animation,
	 * if it wasn't already disabled.
	 */
	public void disable()
	{
		if (!aIsEnabled) {
			return;
		}
		if (aEnableAlpha == null) {
			aEnableAlpha = getNewAlphaAnimation();
			aEnableAlpha.setDuration(sEnableDuration).setAlpha(1);
		}
		aIsEnabled = false;
		aEnableAlpha.setTargetAlpha(sDisabledAlpha).start();
		if (equals(getRootUI().aFocusedElement) && this instanceof UIInputListener) {
			((UIInputListener) this).onDefocus(this);
		}
	}

	/**
	 * Disable tooltip display on this UIControl
	 */
	public void disableTooltip()
	{
		aHasTooltip = true;
	}

	/**
	 * Set the state of this UIControl to "enabled", making it responsive to input events and firing the enable animation, if it
	 * wasn't already enabled.
	 */
	public void enable()
	{
		if (aIsEnabled) {
			return;
		}
		if (aEnableAlpha == null) {
			aEnableAlpha = getNewAlphaAnimation();
			aEnableAlpha.setDuration(sEnableDuration).setAlpha(sDisabledAlpha);
		}
		aIsEnabled = true;
		aEnableAlpha.setTargetAlpha(1).start();
	}

	/**
	 * Enable tooltip display on this UIControl
	 */
	public void enableTooltip()
	{
		aHasTooltip = true;
	}

	/**
	 * @return The last-computed absolute (screen-based) bounds that this control has. Null if this UIControl has never been
	 *         placed on-screen.
	 */
	public Bounds getAbsoluteComputedBounds()
	{
		if (aComputedBounds == null) {
			return null;
		}
		if (aParent == null) {
			return aComputedBounds;
		}
		final Bounds parentBounds = aParent.getAbsoluteComputedBounds();
		return new Bounds(parentBounds.x + aComputedBounds.x, parentBounds.y + aComputedBounds.y, aComputedBounds.width,
				aComputedBounds.height);
	}

	/**
	 * @return The list of children {@link UIControl}s
	 */
	public List<UIControl> getChildrenUIs()
	{
		return aControls;
	}

	/**
	 * @return The last-computed relative (parent-control-based) bounds that this control has. Null if this UIControl has never
	 *         been placed on-screen.
	 */
	public Bounds getComputedBounds()
	{
		return aComputedBounds;
	}

	/**
	 * @return The last-computed height of this UIControl. Null if this UIControl has never been placed on screen.
	 */
	public Integer getComputedHeight()
	{
		if (aComputedBounds == null) {
			return null;
		}
		return aComputedBounds.height;
	}

	/**
	 * @return The last-computed width of this UIControl. Null if this UIControl has never been placed on screen.
	 */
	public Integer getComputedWidth()
	{
		if (aComputedBounds == null) {
			return null;
		}
		return aComputedBounds.width;
	}

	/**
	 * @return The minimum size that this control wishes to have. Takes into account the custom minimum size, thus there
	 *         shouldn't ever be a need to override this method.
	 */
	public final Dimension getDesiredSize()
	{
		final Dimension minimum = getMinimumSize();
		int totalWidth = minimum.width;
		int totalHeight = minimum.height;
		if (aMinimumDimension != null) {
			totalWidth = Math.max(aMinimumDimension.width, totalWidth);
			totalHeight = Math.max(aMinimumDimension.height, totalHeight);
		}
		return new Dimension(totalWidth, totalHeight);
	}

	@Override
	public Set<EverNode> getEffectiveChildren()
	{
		final Set<EverNode> normalChildren = super.getEffectiveChildren();
		if (aTooltip == null) {
			return normalChildren;
		}
		final Set<EverNode> withExtras = new HashSet<EverNode>(normalChildren.size() + 1);
		withExtras.addAll(normalChildren);
		if (aTooltip != null) {
			withExtras.add(aTooltip);
		}
		return withExtras;
	}

	/**
	 * @return A Z-offset that, if used on any element, would put that element above all the UI elements inside this UIControl.
	 *         Used to display things above the UI.
	 */
	public float getMaxZOffset()
	{
		float maxZ = 0;
		for (final UIControl control : getChildrenUIs()) {
			maxZ = Math.max(maxZ, control.getMaxZOffset());
		}
		return sChildZOffset + maxZ + (aParent == null ? sChildZOffset : 0);
	}

	/**
	 * @return The minimum height that this control can handle. To override, override getMinimumSize() instead.
	 */
	public final int getMinimumHeight()
	{
		return getMinimumSize().height;
	}

	/**
	 * @return The minimum size that this control can handle (overridable by subclasses)
	 */
	public Dimension getMinimumSize()
	{
		int totalWidth = 0;
		int totalHeight = 0;
		for (final UIControl c : getChildrenUIs()) {
			final Dimension d = c.getDesiredSize();
			if (aDirection.equals(BoxDirection.HORIZONTAL)) {
				totalWidth += d.width;
				totalHeight = Math.max(totalHeight, d.height);
			}
			else {
				totalWidth = Math.max(totalWidth, d.width);
				totalHeight += d.height;
			}
		}
		return new Dimension(totalWidth, totalHeight);
	}

	/**
	 * @return The minimum width that this control can handle. To override, override getMinimumSize() instead.
	 */
	public int getMinimumWidth()
	{
		return getMinimumSize().width;
	}

	/**
	 * @return The number of children {@link UIControl}s of this UIControl
	 */
	public int getNumChildrenUIs()
	{
		return getChildrenUIs().size();
	}

	/**
	 * @return A reference to the root UIControl inside this UI tree.
	 */
	protected UIControl getRootUI()
	{
		if (aParent == null) {
			return this;
		}
		UIControl parent = aParent;
		while (parent.aParent != null) {
			parent = parent.aParent;
		}
		return parent;
	}

	/**
	 * @return Whether the control has any children nodes;
	 */
	public boolean hasChildren()
	{
		return getChildren().isEmpty();
	}

	/**
	 * @param point
	 *            A point, in screen-based (absolute) coordinates
	 * @return Whether this point is within the bounds of this UIControl
	 */
	protected boolean inAbsoluteBounds(final Vector2f point)
	{
		return aComputedBounds != null && point != null && getAbsoluteComputedBounds().contains(point.x, point.y);
	}

	/**
	 * @param point
	 *            A point, in parent-control-based (relative) coordinates
	 * @return Whether this point is within the bounds of this UIControl
	 */
	protected boolean inBounds(final Vector2f point)
	{
		return point != null && aComputedBounds != null && aComputedBounds.contains(point);
	}

	/**
	 * Add a child directly underneath this UIControl. Defaults to a 0 spring. DO NOT USE outside of the UI package; use addUI
	 * instead.
	 * 
	 * @param index
	 *            The index at which to insert the {@link UIControl}. Negative values go from the end of the list, so an index
	 *            of -1 will insert the {@link UIControl} at the end (Python style)
	 * @param control
	 *            The {@link UIControl} to add
	 */
	void insertChildUI(final int index, final UIControl control)
	{
		insertChildUI(index, control, 0);
	}

	/**
	 * Add a child directly underneath this UIControl. DO NOT USE outside of the UI package; use addUI instead.
	 * 
	 * @param index
	 *            The index at which to insert the {@link UIControl}. Negative values go from the end of the list, so an index
	 *            of -1 will insert the {@link UIControl} at the end (Python style)
	 * @param control
	 *            The {@link UIControl} to add
	 * @param spring
	 *            The spring of the {@link UIControl}
	 */
	void insertChildUI(final int index, final UIControl control, final int spring)
	{
		if (control == null) {
			return;
		}
		if (aControls.contains(control)) {
			System.err.println("Warning: Trying to add the same UIControl twice.");
		}
		aControls.add(MathUtils.mod(index, aControls.size() + 1), control);
		aSprings.put(control, spring);
		addNode(control);
		// Update parent
		control.aParent = this;
		recomputeAllBounds();
	}

	/**
	 * Insert a control as a child to this UIControl at a specific location. Defaults to a 0 spring.
	 * 
	 * @param index
	 *            The index at which to insert the {@link UIControl}. Negative values go from the end of the list, so an index
	 *            of -1 will insert the {@link UIControl} at the end (Python style)
	 * @param control
	 *            The control to add
	 * @return this, for chainability
	 */
	public UIControl insertUI(final int index, final UIControl control)
	{
		insertUI(index, control, 0);
		return this;
	}

	/**
	 * Insert a control as a child to this UIControl at a specific location. Overridden by container subclasses.
	 * 
	 * @param index
	 *            The index at which to insert the {@link UIControl}. Negative values go from the end of the list, so an index
	 *            of -1 will insert the {@link UIControl} at the end (Python style)
	 * @param control
	 *            The control to add
	 * @param spring
	 *            The spring value
	 * @return this, for chainability
	 */
	public UIControl insertUI(final int index, final UIControl control, final int spring)
	{
		insertChildUI(index, control, spring);
		return this;
	}

	/**
	 * @return Whether this control is enabled (responsive to input events) or not
	 */
	public boolean isEnabled()
	{
		return aIsEnabled;
	}

	/**
	 * Called whenever a keyboard press event occurs.
	 * 
	 * @param key
	 *            The {@link KeyboardKey} being pressed
	 * @return Whether the event had any impact or not. If it did, it is generally a good idea to stop propagating the event.
	 */
	public boolean onKeyPress(final KeyboardKey key)
	{
		final UIInputListener focused = getRootUI().aFocusedElement;
		if (focused != null && !equals(focused)) {
			if (focused.onKeyPress(this, key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Called whenever a keyboard release event occurs.
	 * 
	 * @param key
	 *            The {@link KeyboardKey} being released
	 * @return Whether the event had any impact or not. If it did, it is generally a good idea to stop propagating the event.
	 */
	public boolean onKeyRelease(final KeyboardKey key)
	{
		final UIInputListener focused = getRootUI().aFocusedElement;
		if (focused != null && !equals(focused)) {
			if (focused.onKeyRelease(this, key)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Called whenever a mouse movement event occurs.
	 * 
	 * @param point
	 *            The location of the mouse, in parent-control-based (relative) coordinates
	 * @return Whether the event had any impact or not. If it did, it is generally a good idea to stop propagating the event.
	 */
	public boolean onMouseMove(final Vector2f point)
	{
		if (!inBounds(point)) {
			closeTooltip();
			if (aHoverSelectable) {
				setSelected(false);
			}
			return false; // Out of bounds
		}
		if (aHoverSelectable) {
			setSelected(true);
		}
		final Vector2f newPoint = new Vector2f(point.x - aComputedBounds.x, point.y - aComputedBounds.y);
		// see if the control wants to do something about this loading
		if (aHasTooltip && aTooltipTimer == null) {
			// warn the control
			toolTipLoading();
			if (aTooltip != null) {
				// tooltip is present and not shown, load it
				aTooltipTimer = new FrameTimer(new Runnable()
				{
					@Override
					public void run()
					{
						if (aTooltip != null) {
							aTooltip.show();
						}
					}
				}, sTooltipTimer, 1).start();
			}
		}
		for (final UIControl c : getChildrenUIs()) {
			if (c.onMouseMove(newPoint)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Called whenever a mouse down-scrolling event occurs
	 * 
	 * @param delta
	 *            A measurement of how strong the scrolling was
	 * @param position
	 *            The location of the mouse, in parent-control-based (relative) coordinates
	 * @return Whether the event had any impact or not. If it did, it is generally a good idea to stop propagating the event.
	 */
	public boolean onMouseWheelDown(final float delta, final Vector2f position)
	{
		if (!inBounds(position)) {
			return false; // Out of bounds
		}
		final Vector2f newPoint = new Vector2f(position.x - aComputedBounds.x, position.y - aComputedBounds.y);
		for (final UIControl control : getChildrenUIs()) {
			if (control.onMouseWheelDown(delta, newPoint)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Called whenever a mouse up-scrolling event occurs
	 * 
	 * @param delta
	 *            A measurement of how strong the scrolling was
	 * @param position
	 *            The location of the mouse, in parent-control-based (relative) coordinates
	 * @return Whether the event had any impact or not. If it did, it is generally a good idea to stop propagating the event.
	 */
	public boolean onMouseWheelUp(final float delta, final Vector2f position)
	{
		if (!inBounds(position)) {
			return false; // Out of bounds
		}
		final Vector2f newPoint = new Vector2f(position.x - aComputedBounds.x, position.y - aComputedBounds.y);
		for (final UIControl control : getChildrenUIs()) {
			if (control.onMouseWheelUp(delta, newPoint)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if the mouse is still within the element's bounds. If it is not, then will hide the selection background, if it was
	 * there in the first place.
	 */
	private void pollMouse()
	{
		if (aHoverSelectable && !getAbsoluteComputedBounds().contains(EverVoidClient.sCursorPosition)) {
			setSelected(false); // Will stop the timer
		}
	}

	/**
	 * Recomputes all dimension of everything in this UI tree, even if this UIControl isn't the root.
	 */
	protected void recomputeAllBounds()
	{
		final UIControl root = getRootUI();
		if (root.aComputedBounds != null) {
			root.setBounds(root.aComputedBounds);
		}
	}

	/**
	 * Add a {@link ClickObserver} to this UIControl's list of {@link ClickObserver}s.
	 * 
	 * @param observer
	 *            The {@link ClickObserver} to add
	 */
	public void registerClickObserver(final ClickObserver observer)
	{
		aClickObservers.add(observer);
	}

	/**
	 * Set the bounds that this control must fit into. Will compute the layout of this UIControl and all its children.
	 * 
	 * @param bounds
	 *            The bounds to fit into
	 */
	public void setBounds(final Bounds bounds)
	{
		aComputedBounds = bounds;
		setControlOffset(bounds.x, bounds.y);
		int availWidth = bounds.width;
		int availHeight = bounds.height;
		int totalSprings = 0;
		final Map<UIControl, Dimension> minimumSizes = new HashMap<UIControl, Dimension>();
		for (final UIControl c : getChildrenUIs()) {
			final Dimension d = c.getDesiredSize();
			minimumSizes.put(c, d);
			availWidth -= d.width;
			availHeight -= d.height;
			totalSprings += aSprings.get(c);
		}
		float springSize = availWidth / Math.max(1, totalSprings);
		final List<UIControl> controls = new ArrayList<UIControl>(getChildrenUIs());
		if (aDirection.equals(BoxDirection.VERTICAL)) {
			springSize = availHeight / Math.max(1, totalSprings);
			// If this is vertical, we want the first control to be at the top, so reverse the
			Collections.reverse(controls);
		}
		int currentX = 0;
		int currentY = 0;
		for (final UIControl c : controls) {
			final Dimension d = minimumSizes.get(c);
			if (aDirection.equals(BoxDirection.HORIZONTAL)) {
				final int cWidth = (int) (d.width + aSprings.get(c) * springSize);
				c.setBounds(new Bounds(currentX, currentY, cWidth, bounds.height));
				currentX += cWidth;
			}
			else {
				final int cHeight = (int) (d.height + aSprings.get(c) * springSize);
				c.setBounds(new Bounds(currentX, currentY, bounds.width, cHeight));
				currentY += cHeight;
			}
		}
		if (aTooltip != null) {
			aTooltip.parentBoundsChanged();
		}
		if (aSelectNode != null) {
			aSelectNode.parentBoundsChanged();
		}
	}

	/**
	 * Translates this UIControl by a certain amount. Subclasses requiring special positioning may override this
	 * 
	 * @param x
	 *            The x offset
	 * @param y
	 *            The y offset
	 */
	protected void setControlOffset(final float x, final float y)
	{
		aOffset.translate(x, y);
	}

	/**
	 * Change the default color of text elements added to this UIControl. Useful when wishing to add a lot of text elements to
	 * this UIControl.
	 * 
	 * @param color
	 *            The new default text color
	 * @return this, for chainability
	 */
	public UIControl setDefaultTextColor(final ColorRGBA color)
	{
		if (color != null) {
			aDefaultColor = color;
		}
		return this;
	}

	/**
	 * Override the desired dimension of this UIControl with a new {@link Dimension}. When computing the desired size of this
	 * UIControl, the largest between the minimum dimension and the desired dimension will be used.
	 * 
	 * @param desired
	 *            The new desired dimesnion
	 */
	public void setDesiredDimension(final Dimension desired)
	{
		aMinimumDimension = desired.clone();
	}

	/**
	 * Set the enabled-ness of this UIControl to true or false. Equivalent to calling .enable() or .disable().
	 * 
	 * @param enabled
	 *            Whether this UIControl should be enabled (true) or disabled (false)
	 */
	public void setEnabled(final boolean enabled)
	{
		if (enabled) {
			enable();
		}
		else {
			disable();
		}
	}

	/**
	 * Set the {@link UIInputListener} currently possessing the keyboard focus to a new {@link UIInputListener}
	 * 
	 * @param focused
	 *            The {@link UIInputListener} that should have focus
	 */
	protected void setFocusedNode(final UIInputListener focused)
	{
		getRootUI().aFocusedElement = focused;
	}

	/**
	 * Set whether this UIControl should have a selection effect when hovered.
	 * 
	 * @param hoverable
	 *            Whether this UIControl should have a selection effect when hovered or not
	 * @return This, for chainability
	 */
	public UIControl setHoverSelectable(final boolean hoverable)
	{
		aHoverSelectable = hoverable;
		if (aHoverSelectable) {
			aHoverSelectTimer = new FrameTimer(new Runnable()
			{
				@Override
				public void run()
				{
					pollMouse();
				}
			}, 0.2f);
		}
		else if (aHoverSelectTimer != null) {
			aHoverSelectTimer.stop();
			aHoverSelectTimer = null;
		}
		setSelected(getAbsoluteComputedBounds() != null && getAbsoluteComputedBounds().contains(EverVoidClient.sCursorPosition));
		return this;
	}

	/**
	 * Set whether this UIControl should have the selection effect background or not
	 * 
	 * @param selected
	 *            whether this UIControl should have the selection effect background or not
	 * @return This, for chainability
	 */
	public UIControl setSelected(final boolean selected)
	{
		aSelected = selected;
		if (aSelected && aSelectNode == null) {
			aSelectNode = new UIHoverSelect(this);
			aSelectNode.smoothAppear(0.2f);
		}
		else if (!aSelected && aSelectNode != null) {
			aSelectNode.smoothDisappear(0.2f);
			aSelectNode = null;
		}
		if (aHoverSelectable) {
			if (aSelected) {
				aHoverSelectTimer.start();
			}
			else {
				aHoverSelectTimer.stop();
			}
		}
		return this;
	}

	/**
	 * Set the tooltip of this UIControl; null if no tooltip should be shown.
	 * 
	 * @param content
	 *            The tooltip's text content
	 * @return this, for chainability
	 */
	public UIControl setTooltip(final String content)
	{
		if (content == null) {
			return setTooltip((UIControl) null);
		}
		return setTooltip(new StaticTextControl(content, aDefaultColor));
	}

	/**
	 * Sets the tooltip that should be shown when hovering this control; null if no tooltip should be shown.
	 * 
	 * @param control
	 *            The tooltip, or null to disable tooltips
	 * @return this, for chainability
	 */
	public UIControl setTooltip(final UIControl control)
	{
		aTooltip = control == null ? null : new UITooltip(control, this);
		return this;
	}

	/**
	 * Called when the tooltip is about to be shown. Subclasses should override this if they desire to generate the tooltip
	 * right at that moment.
	 */
	protected void toolTipLoading()
	{
		// Overridden by subclasses
	}

	/**
	 * @return A Shortened string representing this control.
	 */
	public String toShortString()
	{
		return getClass().getSimpleName() + " - " + aComputedBounds + " with desired " + getDesiredSize() + " ("
				+ aDirection.toString().toLowerCase() + ")";
	}

	@Override
	public String toString()
	{
		return toString("");
	}

	/**
	 * Returns a fancy string representing this control. Includes all children.
	 * 
	 * @param prefix
	 *            The prefix to use before new lines
	 * @return The fancy string
	 */
	public String toString(final String prefix)
	{
		String str = toShortString();
		if (getChildrenUIs().isEmpty()) {
			return str;
		}
		str += " {\n";
		for (final UIControl c : getChildrenUIs()) {
			str += prefix + "\tSpring " + aSprings.get(c) + ": " + c.toString(prefix + "\t") + "\n";
		}
		return str + prefix + "}";
	}
}
