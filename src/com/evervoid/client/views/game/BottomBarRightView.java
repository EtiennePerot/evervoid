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
		aButtonCommit.addButtonListener(this);
		aButtonCommit.setTooltip("Commit turn");
		addNode(aButtonCommit);
		aButtonPause = new ShapedButtonControl("ui/bottombar/rightbuttons/normal_top_right.png",
				"ui/bottombar/rightbuttons/hover_top_right.png");
		aButtonPause.addButtonListener(this);
		aButtonPause.setTooltip("Pause menu");
		addNode(aButtonPause);
		aButtonGalaxy = new ShapedButtonControl("ui/bottombar/rightbuttons/normal_top_left.png",
				"ui/bottombar/rightbuttons/hover_top_left.png");
		aButtonGalaxy.addButtonListener(this);
		aButtonGalaxy.setTooltip("Galaxy");
		addNode(aButtonGalaxy);
		aButtonResearch = new ShapedButtonControl("ui/bottombar/rightbuttons/normal_bottom_right.png",
				"ui/bottombar/rightbuttons/hover_bottom_right.png");
		aButtonResearch.addButtonListener(this);
		aButtonResearch.setTooltip("Research");
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
		else if (button.equals(aButtonPause)) {
			GameView.pause();
		}
	}

	@Override
	public boolean onLeftClick(final Vector2f position, final float tpf)
	{
		if (aButtonCommit.click(position)) {
			return true;
		}
		if (aButtonPause.click(position)) {
			return true;
		}
		if (aButtonGalaxy.click(position)) {
			return true;
		}
		return aButtonResearch.click(position);
	}

	@Override
	public boolean onMouseMove(final Vector2f position, final float tpf)
	{
		if (aButtonCommit.onMouseMove(position)) {
			return true;
		}
		if (aButtonPause.onMouseMove(position)) {
			return true;
		}
		if (aButtonGalaxy.onMouseMove(position)) {
			return true;
		}
		return aButtonResearch.onMouseMove(position);
	}

	@Override
	public void setBounds(final Bounds bounds)
	{
		super.setBounds(bounds);
		aButtonCommit.setBounds(bounds.add(sButtonCommitOffset));
		aButtonPause.setBounds(bounds.add(sButtonPauseOffset));
		aButtonGalaxy.setBounds(bounds.add(sButtonGalaxyOffset));
		aButtonResearch.setBounds(bounds.add(sButtonResearchOffset));
	}
}
