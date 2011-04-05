package com.evervoid.client.views.game;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.Sizeable;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.ui.ImageControl;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverView;
import com.jme3.math.Vector2f;

public class BottomBarView extends EverView implements Sizeable
{
	private static final int[] sBottomBarWidth = { 800, 1024, 1280, 1366, 1440, 1680, 1920 };
	private ImageControl aBackground = null;
	private final float aBarHeight;
	private int aBarWidth = 0;
	private float aBarXOffset = 0f;
	private final ImageControl aLeftBottomCorner;
	private final ImageControl aLeftBottomHorizontal;
	private final ImageControl aLeftMiddleBottomCorner;
	private final ImageControl aLeftMiddleTopCorner;
	private final ImageControl aLeftMiddleVertical;
	private final ImageControl aLeftTopCorner;
	private final ImageControl aLeftTopHorizontal;
	private final ImageControl aLeftVertical;
	private final ImageControl aMiddleBottomHorizontal;
	private final ImageControl aMiddleTopHorizontal;
	private final ImageControl aRightBottom;
	private final ImageControl aRightTop;
	private final Transform aScreenOffset;
	private final float aSidePanelWidth;

	protected BottomBarView()
	{
		aScreenOffset = getNewTransform();
		aLeftBottomCorner = new ImageControl("ui/bottombar/left_corner_bottom.png");
		addNode(aLeftBottomCorner);
		aLeftTopCorner = new ImageControl("ui/bottombar/left_corner_top.png");
		addNode(aLeftTopCorner);
		aLeftVertical = new ImageControl("ui/bottombar/vertical_left.png", true);
		addNode(aLeftVertical);
		aLeftTopHorizontal = new ImageControl("ui/bottombar/horizontal_top.png", false);
		addNode(aLeftTopHorizontal);
		aLeftBottomHorizontal = new ImageControl("ui/bottombar/horizontal_bottom.png", false);
		addNode(aLeftBottomHorizontal);
		aLeftMiddleBottomCorner = new ImageControl("ui/bottombar/middle_left_corner_bottom.png");
		addNode(aLeftMiddleBottomCorner);
		aLeftMiddleTopCorner = new ImageControl("ui/bottombar/middle_left_corner_top.png");
		addNode(aLeftMiddleTopCorner);
		aLeftMiddleVertical = new ImageControl("ui/bottombar/vertical_middle_left.png", true);
		addNode(aLeftMiddleVertical);
		aMiddleTopHorizontal = new ImageControl("ui/bottombar/horizontal_top.png", false);
		addNode(aMiddleTopHorizontal);
		aMiddleBottomHorizontal = new ImageControl("ui/bottombar/horizontal_bottom.png", false);
		addNode(aMiddleBottomHorizontal);
		aRightBottom = new ImageControl("ui/bottombar/right_corner_bottom.png");
		addNode(aRightBottom);
		aRightTop = new ImageControl("ui/bottombar/right_corner_top.png");
		addNode(aRightTop);
		aBarHeight = aRightTop.getHeight() + aRightBottom.getHeight();
		aSidePanelWidth = aRightTop.getWidth();
		resolutionChanged();
	}

	private void computeBarWidth()
	{
		final int maxW = getBoundsWidth();
		for (int i = 0; i < sBottomBarWidth.length; i++) {
			if (sBottomBarWidth[i] > maxW) {
				aBarWidth = sBottomBarWidth[Math.max(0, i - 1)];
				return;
			}
		}
		aBarWidth = sBottomBarWidth[sBottomBarWidth.length - 1];
	}

	@Override
	public Vector2f getDimensions()
	{
		return new Vector2f(getWidth(), getHeight());
	}

	@Override
	public float getHeight()
	{
		return aBarHeight;
	}

	Bounds getLeftBounds()
	{
		final Vector2f bottomLeftDimension = aLeftBottomCorner.getDimensions();
		return new Bounds(aBarXOffset + bottomLeftDimension.x, bottomLeftDimension.y, aSidePanelWidth - bottomLeftDimension.x
				- aLeftMiddleBottomCorner.getWidth(), aBarHeight - bottomLeftDimension.y - aLeftTopCorner.getHeight());
	}

	Bounds getMiddleBounds()
	{
		final Bounds bottomBounds = getBounds();
		final float x = aBarXOffset + aSidePanelWidth;
		final float y = bottomBounds.y + aLeftBottomCorner.getHeight();
		final float width = aBarWidth - 2 * aSidePanelWidth;
		final float height = bottomBounds.height - aLeftBottomCorner.getHeight() - aLeftTopCorner.getHeight();
		return new Bounds(x, y, width, height);
	}

	Bounds getRightBounds()
	{
		final Bounds bottomBounds = getBounds();
		final float x = aBarXOffset + aBarWidth - aRightTop.getWidth();
		final float y = bottomBounds.y + aLeftBottomCorner.getHeight();
		final float width = aRightTop.getWidth();
		final float height = bottomBounds.height - aLeftBottomCorner.getHeight() - aLeftTopCorner.getHeight();
		return new Bounds(x, y, width, height);
	}

	/**
	 * @return A Z value to translate by in order to show up above this bar
	 */
	float getVisibleZ()
	{
		return 10001;
	}

	@Override
	public float getWidth()
	{
		return aBarWidth;
	}

	@Override
	public void resolutionChanged()
	{
		super.resolutionChanged();
		setBounds(new Bounds(0, 0, EverVoidClient.getWindowDimension().width, getHeight()));
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		computeBarWidth();
		aBarXOffset = bounds.x + bounds.width / 2 - aBarWidth / 2;
		aScreenOffset.translate(aBarXOffset, bounds.y, 10000);
		delNode(aBackground);
		aBackground = new ImageControl("ui/bottombar/bg_" + aBarWidth + "_z.png");
		addNode(aBackground);
		aBackground.getNewTransform().translate(0, 0, -50);
		final float leftWidth = aLeftBottomCorner.getWidth();
		final float bottomHeight = aLeftBottomCorner.getHeight();
		final float topHeight = aLeftTopCorner.getHeight();
		final float middleHeight = aBarHeight - bottomHeight - topHeight;
		final float leftMiddleOffset = aSidePanelWidth - aLeftMiddleBottomCorner.getWidth();
		aLeftBottomCorner.setOffset(0, 0);
		aLeftTopCorner.setOffset(0, aBarHeight - topHeight);
		aLeftVertical.setOffset(0, bottomHeight).setLength(middleHeight);
		aLeftBottomHorizontal.setOffset(leftWidth, 0).setLength(leftMiddleOffset - leftWidth);
		aLeftTopHorizontal.setOffset(leftWidth, aBarHeight - aLeftTopHorizontal.getHeight()).setLength(
				leftMiddleOffset - leftWidth);
		aLeftMiddleBottomCorner.setOffset(leftMiddleOffset, 0);
		aLeftMiddleVertical.setOffset(
				leftMiddleOffset - aLeftMiddleVertical.getWidth() / 2 + aLeftMiddleTopCorner.getWidth() / 2, bottomHeight)
				.setLength(middleHeight);
		aLeftMiddleTopCorner.setOffset(leftMiddleOffset, aBarHeight - topHeight);
		final float middleWidth = aBarWidth - aSidePanelWidth * 2;
		aMiddleBottomHorizontal.setOffset(aSidePanelWidth, 0).setLength(middleWidth);
		aMiddleTopHorizontal.setOffset(aSidePanelWidth, aBarHeight - aMiddleTopHorizontal.getHeight()).setLength(middleWidth);
		final float rightOffset = aBarWidth - aRightTop.getWidth();
		aRightTop.setOffset(rightOffset, aBarHeight - aRightTop.getHeight());
		aRightBottom.setOffset(rightOffset, 0);
	}
}
