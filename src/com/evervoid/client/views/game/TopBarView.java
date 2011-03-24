package com.evervoid.client.views.game;

import java.util.Map;
import java.util.TreeMap;

import com.evervoid.client.ui.BackgroundedUIControl;
import com.evervoid.client.ui.BorderedControl;
import com.evervoid.client.ui.ImageControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.ui.VerticalCenteredControl;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverUIView;
import com.evervoid.state.observers.PlayerObserver;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.ResourceAmount;
import com.jme3.math.ColorRGBA;

public class TopBarView extends EverUIView implements PlayerObserver
{
	private class ResourceDisplayControl extends UIControl
	{
		ResourceDisplayControl(final String resourceName, final int initialAmount)
		{
		}
	}

	private final Map<String, ResourceDisplayControl> aResourceDisplays = new TreeMap<String, ResourceDisplayControl>();

	protected TopBarView(final Player player)
	{
		super(new UIControl(BoxDirection.HORIZONTAL));
		final ImageControl left = new ImageControl("ui/topbar/left.png");
		final ImageControl right = new ImageControl("ui/topbar/right.png");
		final BackgroundedUIControl middle = new BackgroundedUIControl(BoxDirection.HORIZONTAL, "ui/topbar/middle.png");
		middle.addUI(new VerticalCenteredControl(new StaticTextControl("ohai", ColorRGBA.Blue)));
		addUI(new BorderedControl(left, middle, right), 1);
		final Bounds bounds = Bounds.getWholeScreenBounds();
		setBounds(new Bounds(bounds.x, bounds.y + bounds.height - left.getHeight(), bounds.width, left.getHeight()));
		player.registerObserver(this);
		final ResourceAmount pAmount = player.getResources();
		for (final String resName : pAmount.getNames()) {
			aResourceDisplays.put(resName, new ResourceDisplayControl(resName, pAmount.getValue(resName)));
		}
	}

	@Override
	public void playerDefeat(final Player player)
	{
		// Nothing
	}

	@Override
	public void playerDisconnect(final Player player)
	{
		// Nothing
	}

	@Override
	public void playerIncome(final Player player, final ResourceAmount amount)
	{
	}

	@Override
	public void playerVictory(final Player player)
	{
		// Nothing
	}
}
