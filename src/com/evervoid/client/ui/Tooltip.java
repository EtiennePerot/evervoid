package com.evervoid.client.ui;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.EverVoidClient.NodeType;
import com.evervoid.client.graphics.geometry.FrameTimer;
import com.evervoid.client.views.Bounds;
import com.evervoid.state.geometry.Dimension;
import com.evervoid.utils.MathUtils;
import com.jme3.math.ColorRGBA;

public class Tooltip extends WrapperControl
{
	private static final float sAppearDuration = 0.35f;
	private static final float sTooltipYOffset = 4;
	private final Bounds aParentBounds;
	private final FrameTimer aTimer;
	private final UIControl aTooltipParent;

	// For safekeeping:
	// Tooltip gradient goes from #3d3d3d (top) to #222222 (bottom)
	public Tooltip(final String label, final UIControl parent)
	{
		super(new UIControl(BoxDirection.HORIZONTAL), BoxDirection.VERTICAL);
		aTooltipParent = parent;
		addChildUI(new BorderedControl("ui/tooltip/topcorner.png", "ui/tooltip/top.png", "ui/tooltip/topcorner.png"));
		final BackgroundedUIControl bg = new BackgroundedUIControl(BoxDirection.HORIZONTAL, "ui/tooltip/middle.png");
		bg.addChildUI(aContained, 1);
		addChildUI(new BorderedControl(new ImageControl("ui/tooltip/middle.png", true), bg, new ImageControl(
				"ui/tooltip/middle.png", true)), 1);
		addChildUI(new BorderedControl("ui/tooltip/bottomcorner.png", "ui/tooltip/bottom.png", "ui/tooltip/bottomcorner.png"));
		addUI(new CenteredControl(new StaticTextControl(label, new ColorRGBA(0.7f, 0.7f, 0.75f, 1f), "squarehead", 16)), 1);
		aParentBounds = aTooltipParent.getAbsoluteComputedBounds();
		final Dimension dim = getMinimumSize();
		final Bounds wholeScreen = Bounds.getWholeScreenBounds();
		final float posX = MathUtils
				.clampFloat(0, aParentBounds.x + aParentBounds.width / 2 - dim.width / 2, wholeScreen.width);
		float posY = aParentBounds.y;
		if (aParentBounds.y > dim.height) {
			posY -= sTooltipYOffset + dim.height;
		}
		else {
			posY += sTooltipYOffset + aParentBounds.width;
		}
		setBounds(new Bounds(posX, posY, dim.width, dim.height));
		aTimer = new FrameTimer(new Runnable()
		{
			@Override
			public void run()
			{
				pollMouse();
			}
		}, 0.2f).start();
	}

	public void close()
	{
		aTimer.stop();
		smoothDisappear(sAppearDuration);
	}

	private void pollMouse()
	{
		if (!aParentBounds.contains(EverVoidClient.sCursorPosition.x, EverVoidClient.sCursorPosition.y)) {
			aTooltipParent.closeTooltip();
		}
	}

	public void show()
	{
		EverVoidClient.addRootNode(NodeType.TWODIMENSION, this);
		smoothAppear(sAppearDuration);
	}
}
