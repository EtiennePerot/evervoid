package com.evervoid.client.views;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.Sizeable;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.UIConnector;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.state.Dimension;
import com.jme3.math.Vector2f;

public class BottomBarView extends EverView implements Sizeable
{
	private static final int sBottomBarWidth = 768;
	private final float aBarHeight;
	private final Sprite aLeftBottomCorner;
	private final Transform aLeftBottomCornerTransform;
	private final UIConnector aLeftBottomHorizontal;
	private final Sprite aLeftMiddleBottomCorner;
	private final Transform aLeftMiddleBottomCornerTransform;
	private final Sprite aLeftMiddleTopCorner;
	private final Transform aLeftMiddleTopCornerTransform;
	private final UIConnector aLeftMiddleVertical;
	private final Sprite aLeftTopCorner;
	private final Transform aLeftTopCornerTransform;
	private final UIConnector aLeftTopHorizontal;
	private final UIConnector aLeftVertical;
	private final UIConnector aMiddleBottomHorizontal;
	private final UIConnector aMiddleTopHorizontal;
	private final Sprite aRightBottom;
	private final Transform aRightBottomTransform;
	private final Sprite aRightTop;
	private final Transform aRightTopTransform;
	private final Transform aScreenOffset;
	private final float aSidePanelWidth;

	protected BottomBarView()
	{
		aScreenOffset = getNewTransform();
		aLeftBottomCorner = new Sprite("ui/bottombar/left_corner_bottom.png").bottomLeftAsOrigin();
		aLeftBottomCornerTransform = aLeftBottomCorner.getNewTransform();
		addNode(aLeftBottomCorner);
		aLeftTopCorner = new Sprite("ui/bottombar/left_corner_top.png").bottomLeftAsOrigin();
		aLeftTopCornerTransform = aLeftTopCorner.getNewTransform();
		addNode(aLeftTopCorner);
		aLeftVertical = new UIConnector("ui/bottombar/vertical_left.png", true);
		addNode(aLeftVertical);
		aLeftTopHorizontal = new UIConnector("ui/bottombar/horizontal_top.png", false);
		addNode(aLeftTopHorizontal);
		aLeftBottomHorizontal = new UIConnector("ui/bottombar/horizontal_bottom.png", false);
		addNode(aLeftBottomHorizontal);
		aLeftMiddleBottomCorner = new Sprite("ui/bottombar/middle_left_corner_bottom.png").bottomLeftAsOrigin();
		aLeftMiddleBottomCornerTransform = aLeftMiddleBottomCorner.getNewTransform();
		addNode(aLeftMiddleBottomCorner);
		aLeftMiddleTopCorner = new Sprite("ui/bottombar/middle_left_corner_top.png").bottomLeftAsOrigin();
		aLeftMiddleTopCornerTransform = aLeftMiddleTopCorner.getNewTransform();
		addNode(aLeftMiddleTopCorner);
		aLeftMiddleVertical = new UIConnector("ui/bottombar/vertical_middle_left.png", true);
		addNode(aLeftMiddleVertical);
		aMiddleTopHorizontal = new UIConnector("ui/bottombar/horizontal_top.png", false);
		addNode(aMiddleTopHorizontal);
		aMiddleBottomHorizontal = new UIConnector("ui/bottombar/horizontal_bottom.png", false);
		addNode(aMiddleBottomHorizontal);
		aRightBottom = new Sprite("ui/bottombar/right_corner_bottom.png").bottomLeftAsOrigin();
		aRightBottomTransform = aRightBottom.getNewTransform();
		addNode(aRightBottom);
		aRightTop = new Sprite("ui/bottombar/right_corner_top.png").bottomLeftAsOrigin();
		aRightTopTransform = aRightTop.getNewTransform();
		addNode(aRightTop);
		aBarHeight = aRightTop.getHeight() + aRightBottom.getHeight();
		aSidePanelWidth = aRightTop.getWidth();
		resolutionChanged();
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
		return sBottomBarWidth;
	}

	@Override
	public void resolutionChanged()
	{
		super.resolutionChanged();
		/*
		 * setBounds(new Bounds(0, EverVoidClient.getWindowDimension().height - getHeight(),
		 * EverVoidClient.getWindowDimension().width, getHeight()));
		 */
		final Dimension windowDimension = EverVoidClient.getWindowDimension();
		// FIXME: Don't use fixed Z
		aScreenOffset.translate(windowDimension.width / 2 - sBottomBarWidth / 2, 0, 100);
		final float leftWidth = aLeftBottomCorner.getWidth();
		final float bottomHeight = aLeftBottomCorner.getHeight();
		final float topHeight = aLeftTopCorner.getHeight();
		final float middleHeight = aBarHeight - bottomHeight - topHeight;
		final float leftMiddleOffset = aSidePanelWidth - aLeftMiddleBottomCorner.getWidth();
		aLeftBottomCornerTransform.translate(0, 0);
		aLeftTopCornerTransform.translate(0, aBarHeight - topHeight);
		aLeftVertical.setOffset(0, bottomHeight).setLength(middleHeight);
		aLeftBottomHorizontal.setOffset(leftWidth, 0).setLength(leftMiddleOffset - leftWidth);
		aLeftTopHorizontal.setOffset(leftWidth, aBarHeight - aLeftTopHorizontal.getHeight()).setLength(
				leftMiddleOffset - leftWidth);
		aLeftMiddleBottomCornerTransform.translate(leftMiddleOffset, 0);
		aLeftMiddleVertical.setOffset(
				leftMiddleOffset - aLeftMiddleVertical.getWidth() / 2 + aLeftMiddleTopCorner.getWidth() / 2, bottomHeight)
				.setLength(middleHeight);
		aLeftMiddleTopCornerTransform.translate(leftMiddleOffset, aBarHeight - topHeight);
		final float middleWidth = sBottomBarWidth - aSidePanelWidth * 2;
		aMiddleBottomHorizontal.setOffset(aSidePanelWidth, 0).setLength(middleWidth);
		aMiddleTopHorizontal.setOffset(aSidePanelWidth, aBarHeight - aMiddleTopHorizontal.getHeight()).setLength(middleWidth);
		final float rightOffset = sBottomBarWidth - aRightTop.getWidth();
		aRightTopTransform.translate(rightOffset, aBarHeight - aRightTop.getHeight());
		aRightBottomTransform.translate(rightOffset, 0);
	}
}
