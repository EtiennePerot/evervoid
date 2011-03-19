package com.evervoid.client.views.game;

import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.ShapedButtonControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverView;
import com.evervoid.client.views.game.GameView.PerspectiveType;
import com.jme3.math.Vector2f;

public class BottomBarRightView extends EverView implements ButtonListener
{
	private static final Vector2f sButtonCommitOffset = new Vector2f(74, 4);
	private static final Vector2f sButtonGalaxyOffset = sButtonCommitOffset.clone().addLocal(-26, 64);
	private static final Vector2f sButtonPauseOffset = sButtonCommitOffset.clone().addLocal(16, 92);
	private static final Vector2f sButtonResearchOffset = sButtonCommitOffset.clone().addLocal(106, 4);
	private final ShapedButtonControl aButtonCommit;
	private final ShapedButtonControl aButtonGalaxy;
	private final ShapedButtonControl aButtonPause;
	private final ShapedButtonControl aButtonResearch;

	public BottomBarRightView()
	{
		aButtonCommit = new ShapedButtonControl("ui/bottombar/rightbuttons/normal_bottom_left.png",
				"ui/bottombar/rightbuttons/hover_bottom_left.png");
		aButtonCommit.getNewTransform().translate(sButtonCommitOffset);
		aButtonCommit.addButtonListener(this);
		addNode(aButtonCommit);
		aButtonPause = new ShapedButtonControl("ui/bottombar/rightbuttons/normal_top_right.png",
				"ui/bottombar/rightbuttons/hover_top_right.png");
		aButtonPause.getNewTransform().translate(sButtonPauseOffset);
		aButtonPause.addButtonListener(this);
		addNode(aButtonPause);
		aButtonGalaxy = new ShapedButtonControl("ui/bottombar/rightbuttons/normal_top_left.png",
				"ui/bottombar/rightbuttons/hover_top_left.png");
		aButtonGalaxy.getNewTransform().translate(sButtonGalaxyOffset);
		aButtonGalaxy.addButtonListener(this);
		addNode(aButtonGalaxy);
		aButtonResearch = new ShapedButtonControl("ui/bottombar/rightbuttons/normal_bottom_right.png",
				"ui/bottombar/rightbuttons/hover_bottom_right.png");
		aButtonResearch.getNewTransform().translate(sButtonResearchOffset);
		aButtonResearch.addButtonListener(this);
		addNode(aButtonResearch);
	}

	@Override
	public void buttonClicked(final UIControl button)
	{
		if (button.equals(aButtonGalaxy)) {
			GameView.changePerspective(PerspectiveType.GALAXY);
		}
		else if (button.equals(aButtonCommit)) {
			GameView.commitTurn();
		}
	}

	@Override
	public boolean onLeftClick(final Vector2f position, final float tpf)
	{
		if (aButtonCommit.click(position.subtract(sButtonCommitOffset))) {
			return true;
		}
		if (aButtonPause.click(position.subtract(sButtonPauseOffset))) {
			return true;
		}
		if (aButtonGalaxy.click(position.subtract(sButtonGalaxyOffset))) {
			return true;
		}
		if (aButtonResearch.click(position.subtract(sButtonResearchOffset))) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onMouseMove(final Vector2f position, final float tpf)
	{
		if (aButtonCommit.onMouseMove(position.subtract(sButtonCommitOffset))) {
			return true;
		}
		if (aButtonPause.onMouseMove(position.subtract(sButtonPauseOffset))) {
			return true;
		}
		if (aButtonGalaxy.onMouseMove(position.subtract(sButtonGalaxyOffset))) {
			return true;
		}
		if (aButtonResearch.onMouseMove(position.subtract(sButtonResearchOffset))) {
			return true;
		}
		return false;
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		aButtonCommit.setBounds(bounds);
		aButtonPause.setBounds(bounds);
		aButtonGalaxy.setBounds(bounds);
		aButtonResearch.setBounds(bounds);
	}
}
