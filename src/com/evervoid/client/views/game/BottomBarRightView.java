package com.evervoid.client.views.game;

import com.evervoid.client.graphics.geometry.FrameTimer;
import com.evervoid.client.ui.ButtonListener;
import com.evervoid.client.ui.CenteredControl;
import com.evervoid.client.ui.ShapedButtonControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverView;
import com.evervoid.client.views.game.GameView.PerspectiveType;
import com.evervoid.client.views.game.turn.TurnListener;
import com.evervoid.client.views.game.turn.TurnSynchronizer;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class BottomBarRightView extends EverView implements ButtonListener, TurnListener
{
	private static final Vector2f sButtonCommitOffset = new Vector2f(74, 4);
	private static final Vector2f sButtonGalaxyOffset = sButtonCommitOffset.clone().addLocal(-26, 64);
	private static final Vector2f sButtonPauseOffset = sButtonCommitOffset.clone().addLocal(16, 92);
	private static final Vector2f sButtonResearchOffset = sButtonCommitOffset.clone().addLocal(106, 4);
	private static final ColorRGBA sTimerDisplayColor = new ColorRGBA(0.9f, 0.9f, 1f, 1f);
	private static final ColorRGBA sTimerDisplayColorUrgent = new ColorRGBA(0.95f, 0.55f, 0.5f, 1f);
	private static final Bounds sTimerDisplayOffset = new Bounds(28, 46, 84, 58).add(sButtonCommitOffset);
	private final ShapedButtonControl aButtonCommit;
	private final ShapedButtonControl aButtonGalaxy;
	private final ShapedButtonControl aButtonPause;
	private final ShapedButtonControl aButtonResearch;
	private final StaticTextControl aTimerDisplay;
	private final UIControl aTimerDisplayContainer;
	private boolean aTimerDisplayEnabled = true;
	private int aTurnSecondsLeft = -1;
	private int aTurnTime = -1;
	private final FrameTimer aTurnTimer;

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
		aTimerDisplay = new StaticTextControl("", sTimerDisplayColor);
		aTimerDisplay.setKeepBoundsOnChange(false);
		aTimerDisplayContainer = new UIControl(BoxDirection.HORIZONTAL);
		aTimerDisplayContainer.addUI(new CenteredControl(aTimerDisplay), 1);
		addNode(aTimerDisplayContainer);
		aTurnTimer = new FrameTimer(new Runnable()
		{
			@Override
			public void run()
			{
				tickTimer();
			}
		}, 1);
		GameView.registerTurnListener(this);
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
		aTimerDisplayContainer.setBounds(sTimerDisplayOffset.add(bounds.x, bounds.y));
	}

	public void setTimer(final int seconds)
	{
		aTurnTime = seconds;
		aTurnSecondsLeft = aTurnTime;
		updateTimer();
		aTurnTimer.start();
	}

	public void stopTimer()
	{
		aTimerDisplayEnabled = true;
		aTurnSecondsLeft = 0;
		aTurnTimer.stop();
	}

	private void tickTimer()
	{
		if (aTurnSecondsLeft > 0) {
			aTurnSecondsLeft--;
			updateTimer();
		}
	}

	@Override
	public void turnPlayedback()
	{
		aTimerDisplayEnabled = true;
		updateTimer();
	}

	@Override
	public void turnReceived(final TurnSynchronizer synchronizer)
	{
		aTimerDisplay.setText("Turn OK.");
		aTimerDisplay.setColor(sTimerDisplayColor);
		aTurnSecondsLeft = aTurnTime;
		aTurnTimer.start();
	}

	@Override
	public void turnSent()
	{
		aTurnTimer.stop();
		aTimerDisplayEnabled = false;
		aTimerDisplay.setText("Turn sent.");
		aTimerDisplay.setColor(sTimerDisplayColor);
	}

	private void updateTimer()
	{
		if (!aTimerDisplayEnabled) {
			return;
		}
		final int minutes = aTurnSecondsLeft / 60;
		String leftSeconds = String.valueOf(aTurnSecondsLeft - minutes * 60);
		if (leftSeconds.length() == 1) {
			leftSeconds = "0" + leftSeconds;
		}
		aTimerDisplay.setText(String.valueOf(minutes) + ":" + leftSeconds);
		if (aTurnSecondsLeft < 10) {
			aTimerDisplay.setColor(sTimerDisplayColorUrgent);
		}
		else {
			aTimerDisplay.setColor(sTimerDisplayColor);
		}
		if (aTurnSecondsLeft <= 0) {
			// Force commit
			GameView.commitTurn();
		}
	}
}
