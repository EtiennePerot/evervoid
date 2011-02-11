package com.evervoid.client.views;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.Sizeable;
import com.evervoid.client.graphics.UIConnector;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.client.ui.PlainRectangle;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class BottomBarView extends EverView implements Sizeable
{
	private static final int[] sBottomBarWidth = { 1024, 1280, 1366, 1440, 1680, 1920 };
	private final float aBarHeight;
	private float aBarWidth = 0;
	private PlainRectangle aBlocker = null;
	private final UIConnector aLeftBottomCorner;
	private final UIConnector aLeftBottomHorizontal;
	private final UIConnector aLeftMiddleBottomCorner;
	private final UIConnector aLeftMiddleTopCorner;
	private final UIConnector aLeftMiddleVertical;
	private final UIConnector aLeftTopCorner;
	private final UIConnector aLeftTopHorizontal;
	private final UIConnector aLeftVertical;
	private final UIConnector aMiddleBottomHorizontal;
	private final UIConnector aMiddleTopHorizontal;
	private final UIConnector aRightBottom;
	private final UIConnector aRightTop;
	private final Transform aScreenOffset;
	private final float aSidePanelWidth;

	protected BottomBarView()
	{
		aScreenOffset = getNewTransform();
		aLeftBottomCorner = new UIConnector("ui/bottombar/left_corner_bottom.png");
		addNode(aLeftBottomCorner);
		aLeftTopCorner = new UIConnector("ui/bottombar/left_corner_top.png");
		addNode(aLeftTopCorner);
		aLeftVertical = new UIConnector("ui/bottombar/vertical_left.png", true);
		addNode(aLeftVertical);
		aLeftTopHorizontal = new UIConnector("ui/bottombar/horizontal_top.png", false);
		addNode(aLeftTopHorizontal);
		aLeftBottomHorizontal = new UIConnector("ui/bottombar/horizontal_bottom.png", false);
		addNode(aLeftBottomHorizontal);
		aLeftMiddleBottomCorner = new UIConnector("ui/bottombar/middle_left_corner_bottom.png");
		addNode(aLeftMiddleBottomCorner);
		aLeftMiddleTopCorner = new UIConnector("ui/bottombar/middle_left_corner_top.png");
		addNode(aLeftMiddleTopCorner);
		aLeftMiddleVertical = new UIConnector("ui/bottombar/vertical_middle_left.png", true);
		addNode(aLeftMiddleVertical);
		aMiddleTopHorizontal = new UIConnector("ui/bottombar/horizontal_top.png", false);
		addNode(aMiddleTopHorizontal);
		aMiddleBottomHorizontal = new UIConnector("ui/bottombar/horizontal_bottom.png", false);
		addNode(aMiddleBottomHorizontal);
		aRightBottom = new UIConnector("ui/bottombar/right_corner_bottom.png");
		addNode(aRightBottom);
		aRightTop = new UIConnector("ui/bottombar/right_corner_top.png");
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
	protected void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		computeBarWidth();
		// FIXME: Don't use fixed Z
		final float xOffset = bounds.x + bounds.width / 2 - aBarWidth / 2;
		aScreenOffset.translate(xOffset, bounds.y, 100);
		delNode(aBlocker);
		// aBlocker is a child of BottomBarView, thus the "origin" Vector3f is relative to the BottombarView itself
		aBlocker = new PlainRectangle(new Vector3f(-xOffset, -bounds.y, -1), bounds.width, bounds.height, ColorRGBA.Black);
		addNode(aBlocker);
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
