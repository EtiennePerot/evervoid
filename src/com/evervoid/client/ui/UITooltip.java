package com.evervoid.client.ui;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.EverVoidClient.NodeType;
import com.evervoid.client.graphics.geometry.FrameTimer;
import com.evervoid.client.views.Bounds;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.utils.MathUtils;
import com.jme3.math.ColorRGBA;

/**
 * A UITooltip displays a tooltip text when its corresponding {@link UIControl} is hovered after a certain amount of time
 */
public class UITooltip extends WrapperControl
{
	/**
	 * Duration that the user has to keep hovering before the tooltip appears, in seconds
	 */
	private static final float sAppearDuration = 0.35f;
	/**
	 * Vertical offset away from the {@link UIControl} that the tooltip must maintain
	 */
	private static final float sTooltipYOffset = 4;
	/**
	 * The parent {@link UIControl}'s absolute {@link Bounds}.
	 */
	private Bounds aParentBounds;
	/**
	 * The {@link FrameTimer} used to determine when to appear
	 */
	private final FrameTimer aTimer;
	/**
	 * The parent {@link UIControl} that this tooltip corresponds to
	 */
	private final UIControl aTooltipParent;

	/**
	 * Constructor
	 * 
	 * @param label
	 *            The text to display in the tooltip
	 * @param parent
	 *            The {@link UIControl} that this {@link UITooltip} belongs to
	 */
	public UITooltip(final String label, final UIControl parent)
	{
		this(new StaticTextControl(label, new ColorRGBA(0.7f, 0.7f, 0.75f, 1f), "bitvoid", 15), parent);
	}

	/**
	 * Constructor
	 * 
	 * @param contents
	 *            The tooltip's contents; can be any {@link UIControl} containing anything, but if the only thing necessary is
	 *            text, it is recommended to use the other, simpler constructor
	 * @param parent
	 *            The {@link UIControl} that this {@link UITooltip} belongs to
	 */
	public UITooltip(final UIControl contents, final UIControl parent)
	{
		super(new UIControl(BoxDirection.HORIZONTAL), BoxDirection.VERTICAL);
		aTooltipParent = parent;
		addChildUI(new BorderedControl("ui/tooltip/topcorner.png", "ui/tooltip/top.png", "ui/tooltip/topcorner.png"));
		final BackgroundedUIControl bg = new BackgroundedUIControl(BoxDirection.HORIZONTAL, "ui/tooltip/middle.png");
		bg.addChildUI(aContained, 1);
		addChildUI(new BorderedControl(new ImageControl("ui/tooltip/middle.png", true), bg, new ImageControl(
				"ui/tooltip/middle.png", true)), 1);
		addChildUI(new BorderedControl("ui/tooltip/bottomcorner.png", "ui/tooltip/bottom.png", "ui/tooltip/bottomcorner.png"));
		addUI(new CenteredControl(contents), 1);
		// make sure to call after adding the UI (so you get the correct min size)
		if (aTooltipParent.getAbsoluteComputedBounds() != null) {
			// parent already has bound, conform to them
			parentBoundsChanged();
		}
		aTimer = new FrameTimer(new Runnable()
		{
			@Override
			public void run()
			{
				pollMouse();
			}
		}, 0.2f).start();
	}

	/**
	 * Close the tooltip now.
	 */
	public void close()
	{
		aTimer.stop();
		smoothDisappear(sAppearDuration);
	}

	/**
	 * Called by the parent {@link UIControl} whenever its bounds change.
	 */
	void parentBoundsChanged()
	{
		aParentBounds = aTooltipParent.getAbsoluteComputedBounds();
		final Dimension dim = getMinimumSize();
		final Bounds wholeScreen = Bounds.getWholeScreenBounds();
		final float posX = MathUtils.clampFloat(5, aParentBounds.x + aParentBounds.width / 2 - dim.width / 2, wholeScreen.width
				- dim.width - 5);
		float posY = aParentBounds.y;
		if (aParentBounds.y > dim.height) {
			posY -= sTooltipYOffset + dim.height;
		}
		else {
			posY += sTooltipYOffset + aParentBounds.height;
		}
		setBounds(new Bounds(posX, posY, dim.width, dim.height));
		getNewTransform().translate(0, 0, 1000000);
	}

	/**
	 * Poll the mouse position to check if the mouse is still hovering the {@link UIControl}.
	 */
	private void pollMouse()
	{
		if (!aParentBounds.contains(EverVoidClient.sCursorPosition)) {
			aTooltipParent.closeTooltip();
		}
	}

	/**
	 * Show the tooltip now.
	 */
	public void show()
	{
		EverVoidClient.addRootNode(NodeType.TWODIMENSION, this);
		smoothAppear(sAppearDuration);
	}
}
