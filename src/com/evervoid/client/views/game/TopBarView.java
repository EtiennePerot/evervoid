package com.evervoid.client.views.game;

import java.util.Map;
import java.util.TreeMap;

import com.evervoid.client.graphics.GraphicsUtils;
import com.evervoid.client.ui.BackgroundedUIControl;
import com.evervoid.client.ui.BorderedControl;
import com.evervoid.client.ui.ImageControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.ui.VerticalCenteredControl;
import com.evervoid.client.views.Bounds;
import com.evervoid.client.views.EverUIView;
import com.evervoid.state.data.ResourceData;
import com.evervoid.state.observers.PlayerObserver;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.ResourceAmount;
import com.jme3.math.ColorRGBA;

public class TopBarView extends EverUIView implements PlayerObserver
{
	private class ResourceDisplayControl extends UIControl
	{
		private final StaticTextControl aAmount;

		ResourceDisplayControl(final ResourceData data, final String initialAmount)
		{
			super(BoxDirection.HORIZONTAL);
			addUI(new VerticalCenteredControl(new ImageControl(data.getIcon())));
			aAmount = new StaticTextControl(initialAmount, ColorRGBA.White, "squarehead", 24);
			aAmount.setKeepBoundsOnChange(false);
			addSpacer(sSpacerWidth / 2, 1);
			addUI(new VerticalCenteredControl(aAmount));
			// hacky, but needs to be done in order to warn UIControl that this contains a Tooltip
			enableTooltip();
		}

		@Override
		protected void toolTipLoading()
		{
			final UIControl proj = new UIControl(BoxDirection.VERTICAL);
			proj.addString("Projected Income", ColorRGBA.White);
			final ResourceAmount projectedResources = GameView.getLocalPlayer().getCurrentIncome();
			for (final String resource : projectedResources.getNames()) {
				final UIControl row = new UIControl(BoxDirection.HORIZONTAL);
				row.addString("  " + resource);
				row.addFlexSpacer(1);
				row.addString("  " + projectedResources.getFormattedValue(resource));
				proj.addUI(row);
			}
			setTooltip(proj);
		}

		void update(final String amount)
		{
			aAmount.setText(amount);
		}
	}

	private static final int sSpacerWidth = 16;
	private final Map<String, ResourceDisplayControl> aResourceDisplays = new TreeMap<String, ResourceDisplayControl>();

	protected TopBarView(final Player player)
	{
		super(new UIControl(BoxDirection.HORIZONTAL));
		final ImageControl left = new ImageControl("ui/topbar/left.png");
		final ImageControl right = new ImageControl("ui/topbar/right.png");
		final BackgroundedUIControl middle = new BackgroundedUIControl(BoxDirection.HORIZONTAL, "ui/topbar/middle.png");
		middle.addUI(new VerticalCenteredControl(new ImageControl(player.getRaceData().getRaceIcon("small_black"))));
		middle.addSpacer(sSpacerWidth, 1);
		middle.addString(player.getNickname(), GraphicsUtils.getColorRGBA(player.getColor()), BoxDirection.VERTICAL);
		middle.addFlexSpacer(1);
		final ResourceAmount pAmount = player.getResources();
		for (final String resName : pAmount.getNames()) {
			final ResourceDisplayControl display = new ResourceDisplayControl(player.getState().getResourceData(resName),
					pAmount.getFormattedValue(resName));
			aResourceDisplays.put(resName, display);
			middle.addUI(new VerticalCenteredControl(display));
			middle.addSpacer(sSpacerWidth, 1);
		}
		addUI(new BorderedControl(left, middle, right), 1);
		final Bounds bounds = Bounds.getWholeScreenBounds();
		setBounds(new Bounds(bounds.x, bounds.y + bounds.height - left.getHeight(), bounds.width, left.getHeight()));
		setCatchKeyEvents(false);
		player.registerObserver(this);
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
		final ResourceAmount playerTotal = player.getResources();
		for (final String resName : playerTotal.getNames()) {
			if (aResourceDisplays.containsKey(resName)) {
				aResourceDisplays.get(resName).update(playerTotal.getFormattedValue(resName));
			}
		}
	}

	@Override
	public void playerVictory(final Player player)
	{
		// Nothing
	}
}
