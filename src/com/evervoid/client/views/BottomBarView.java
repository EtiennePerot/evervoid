package com.evervoid.client.views;

import com.evervoid.client.EverVoidClient;
import com.evervoid.client.graphics.Sprite;
import com.evervoid.client.graphics.geometry.Transform;
import com.evervoid.state.Dimension;

public class BottomBarView extends EverView
{
	private static final int sBottomBarHeight = 200;
	private static final int sBottomBarLeftMiddleOffset = 192;
	private static final int sBottomBarMiddleRightVerticalOffset = 14;
	private static final int sBottomBarWidth = 768;
	private final Sprite aLeftBottomCorner;
	private final Transform aLeftBottomCornerTransform;
	private final Sprite aLeftBottomHorizontal;
	private final Transform aLeftBottomHorizontalTransform;
	private final Sprite aLeftMiddleBottomCorner;
	private final Transform aLeftMiddleBottomCornerTransform;
	private final Sprite aLeftMiddleTopCorner;
	private final Transform aLeftMiddleTopCornerTransform;
	private final Sprite aLeftMiddleVertical;
	private final Transform aLeftMiddleVerticalTransform;
	private final Sprite aLeftTopCorner;
	private final Transform aLeftTopCornerTransform;
	private final Sprite aLeftTopHorizontal;
	private final Transform aLeftTopHorizontalTransform;
	private final Sprite aLeftVertical;
	private final Transform aLeftVerticalTransform;
	private final Sprite aMiddleBottomHorizontal;
	private final Transform aMiddleBottomHorizontalTransform;
	private final Sprite aMiddleRightVertical;
	private final Transform aMiddleRightVerticalTransform;
	private final Sprite aMiddleTopHorizontal;
	private final Transform aMiddleTopHorizontalTransform;
	private final Sprite aRightBottom;
	private final Transform aRightBottomTransform;
	private final Sprite aRightTop;
	private final Transform aRightTopTransform;
	private final Sprite aRightVertical;
	private final Transform aRightVerticalTransform;
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
		aLeftTopHorizontal = new Sprite("ui/bottombar/horizontal_top.png").bottomLeftAsOrigin();
		aLeftTopHorizontalTransform = aLeftTopHorizontal.getNewTransform();
		addNode(aLeftTopHorizontal);
		aLeftBottomHorizontal = new Sprite("ui/bottombar/horizontal_bottom.png").bottomLeftAsOrigin();
		aLeftBottomHorizontalTransform = aLeftBottomHorizontal.getNewTransform();
		addNode(aLeftBottomHorizontal);
		aLeftMiddleBottomCorner = new Sprite("ui/bottombar/middle_left_corner_bottom.png").bottomLeftAsOrigin();
		aLeftMiddleBottomCornerTransform = aLeftMiddleBottomCorner.getNewTransform();
		addNode(aLeftMiddleBottomCorner);
		aLeftMiddleTopCorner = new Sprite("ui/bottombar/middle_left_corner_top.png").bottomLeftAsOrigin();
		aLeftMiddleTopCornerTransform = aLeftMiddleTopCorner.getNewTransform();
		addNode(aLeftMiddleTopCorner);
		aLeftMiddleVertical = new Sprite("ui/bottombar/vertical_middle_left.png").bottomLeftAsOrigin();
		aLeftMiddleVerticalTransform = aLeftMiddleVertical.getNewTransform();
		addNode(aLeftMiddleVertical);
		aMiddleTopHorizontal = new Sprite("ui/bottombar/horizontal_top.png").bottomLeftAsOrigin();
		aMiddleTopHorizontalTransform = aMiddleTopHorizontal.getNewTransform();
		addNode(aMiddleTopHorizontal);
		aMiddleBottomHorizontal = new Sprite("ui/bottombar/horizontal_bottom.png").bottomLeftAsOrigin();
		aMiddleBottomHorizontalTransform = aMiddleBottomHorizontal.getNewTransform();
		addNode(aMiddleBottomHorizontal);
		aRightBottom = new Sprite("ui/bottombar/right_corner_bottom.png").bottomLeftAsOrigin();
		aRightBottomTransform = aRightBottom.getNewTransform();
		addNode(aRightBottom);
		aRightTop = new Sprite("ui/bottombar/right_corner_top.png").bottomLeftAsOrigin();
		aRightTopTransform = aRightTop.getNewTransform();
		addNode(aRightTop);
		aMiddleRightVertical = new Sprite("ui/bottombar/vertical_middle_right.png").bottomLeftAsOrigin();
		aMiddleRightVerticalTransform = aMiddleRightVertical.getNewTransform();
		addNode(aMiddleRightVertical);
		aRightVertical = new Sprite("ui/bottombar/vertical_right.png").bottomLeftAsOrigin();
		aRightVerticalTransform = aRightVertical.getNewTransform();
		addNode(aRightVertical);
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
		// FIXME: Don't use fixed Z
		aScreenOffset.translate(windowDimension.width / 2 - sBottomBarWidth / 2, 0, 100);
		final float leftWidth = aLeftBottomCorner.getWidth();
		final float bottomHeight = aLeftBottomCorner.getHeight();
		final float topHeight = aLeftTopCorner.getHeight();
		final float middleHeight = sBottomBarHeight - bottomHeight - topHeight;
		aLeftBottomCornerTransform.translate(0, 0);
		aLeftTopCornerTransform.translate(0, sBottomBarHeight - topHeight);
		aLeftVerticalTransform.translate(0, bottomHeight).setScale(1f, middleHeight / aLeftVertical.getHeight(), 1f);
		aLeftBottomHorizontalTransform.translate(leftWidth, 0).setScale(
				(sBottomBarLeftMiddleOffset - leftWidth) / aLeftTopHorizontal.getWidth(), 1f, 1f);
		aLeftTopHorizontalTransform.translate(leftWidth, sBottomBarHeight - aLeftTopHorizontal.getHeight()).setScale(
				(sBottomBarLeftMiddleOffset - leftWidth) / aLeftTopHorizontal.getWidth(), 1f, 1f);
		aLeftMiddleBottomCornerTransform.translate(sBottomBarLeftMiddleOffset, 0);
		aLeftMiddleVerticalTransform.translate(
				sBottomBarLeftMiddleOffset - aLeftMiddleVertical.getWidth() / 2 + aLeftMiddleTopCorner.getWidth() / 2,
				bottomHeight).setScale(1f, middleHeight / aLeftMiddleVertical.getHeight(), 1f);
		aLeftMiddleTopCornerTransform.translate(sBottomBarLeftMiddleOffset, sBottomBarHeight - topHeight);
		final float middleLeftOffset = sBottomBarLeftMiddleOffset + aLeftMiddleTopCorner.getWidth();
		final float middleWidth = sBottomBarWidth - middleLeftOffset - aRightTop.getWidth();
		aMiddleBottomHorizontalTransform.translate(middleLeftOffset, 0).setScale(
				middleWidth / aMiddleBottomHorizontal.getWidth(), 1f, 1f);
		aMiddleTopHorizontalTransform.translate(middleLeftOffset, sBottomBarHeight - aMiddleTopHorizontal.getHeight())
				.setScale(middleWidth / aMiddleTopHorizontal.getWidth(), 1f, 1f);
		final float rightOffset = sBottomBarWidth - aRightTop.getWidth();
		final float rightHeight = aRightBottom.getHeight();
		aRightTopTransform.translate(rightOffset, sBottomBarHeight - aRightTop.getHeight());
		aRightBottomTransform.translate(rightOffset, 0);
		aMiddleRightVerticalTransform.translate(rightOffset + sBottomBarMiddleRightVerticalOffset, rightHeight).setScale(1f,
				1.5f, 1f);
		aRightVerticalTransform.translate(sBottomBarWidth - aMiddleRightVertical.getWidth(),
				sBottomBarHeight - aRightTop.getHeight() - aRightVertical.getHeight() * 1.25).setScale(1.15f, 1.25f, 1f);
	}
}
