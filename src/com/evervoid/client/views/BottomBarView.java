package com.evervoid.client.views;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.state.Dimension;

public class BottomBarView extends EverView
{
	private static final int sBottomBarHeight = 200;
	private static final int sBottomBarWidth = 768;
	private final Sprite aLeftBottomCorner;
	private final Transform aLeftBottomCornerTransform;
	private final Sprite aLeftMiddleBottomCorner;
	private final Transform aLeftMiddleBottomCornerTransform;
	private final Sprite aLeftMiddleTopCorner;
	private final Transform aLeftMiddleTopCornerTransform;
	private final Sprite aLeftMiddleVertical;
	private final Transform aLeftMiddleVerticalTransform;
	private final Sprite aLeftTopCorner;
	private final Transform aLeftTopCornerTransform;
	private final Sprite aLeftVertical;
	private final Transform aLeftVerticalTransform;
	private final Transform aScreenOffset;

	protected BottomBarView()
	{
		aScreenOffset = getNewTransform();
		aLeftBottomCorner = new Sprite("ui/bottombar/left_corner_bottom.png").bottomLeftAsOrigin();
		aLeftBottomCornerTransform = aLeftBottomCorner.getNewTransform();
		addNode(aLeftBottomCorner);
		aLeftTopCorner = new Sprite("ui/bottombar/left_corner_top.png").bottomLeftAsOrigin();
		aLeftTopCornerTransform = aLeftTopCorner.getNewTransform();
		addNode(aLeftTopCorner);
		aLeftVertical = new Sprite("ui/bottombar/vertical_left.png").bottomLeftAsOrigin();
		aLeftVerticalTransform = aLeftVertical.getNewTransform();
		addNode(aLeftVertical);
		aLeftMiddleBottomCorner = new Sprite("ui/bottombar/middle_left_corner_bottom.png").bottomLeftAsOrigin();
		aLeftMiddleBottomCornerTransform = aLeftMiddleBottomCorner.getNewTransform();
		addNode(aLeftMiddleBottomCorner);
		aLeftMiddleTopCorner = new Sprite("ui/bottombar/middle_left_corner_top.png").bottomLeftAsOrigin();
		aLeftMiddleTopCornerTransform = aLeftMiddleTopCorner.getNewTransform();
		addNode(aLeftMiddleTopCorner);
		aLeftMiddleVertical = new Sprite("ui/bottombar/vertical_middle_left.png").bottomLeftAsOrigin();
		aLeftMiddleVerticalTransform = aLeftMiddleVertical.getNewTransform();
		addNode(aLeftMiddleVertical);
		resolutionChanged();
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
		aScreenOffset.translate(windowDimension.width / 2 - sBottomBarWidth / 2, 0);
		final float bottomHeight = aLeftBottomCorner.getHeight();
		final float topHeight = aLeftTopCorner.getHeight();
		final float middleHeight = sBottomBarHeight - bottomHeight - topHeight;
		aLeftTopCornerTransform.translate(0, sBottomBarHeight - topHeight);
		aLeftVerticalTransform.translate(0, bottomHeight).setScale(1f, middleHeight / aLeftVertical.getHeight(), 1f);
	}
}
