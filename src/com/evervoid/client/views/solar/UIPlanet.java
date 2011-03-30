package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.GraphicsUtils;
import com.evervoid.client.graphics.ShadedSprite;
import com.evervoid.client.ui.HorizontalCenteredControl;
import com.evervoid.client.ui.ImageControl;
import com.evervoid.client.ui.RescalableControl;
import com.evervoid.client.ui.StaticTextControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.client.ui.UIControl.BoxDirection;
import com.evervoid.client.ui.VerticalCenteredControl;
import com.evervoid.state.building.Building;
import com.evervoid.state.data.ResourceData;
import com.evervoid.state.data.ShipData;
import com.evervoid.state.data.SpriteData;
import com.evervoid.state.observers.PlanetObserver;
import com.evervoid.state.player.Player;
import com.evervoid.state.player.ResourceAmount;
import com.evervoid.state.prop.Planet;
import com.evervoid.state.prop.Ship;
import com.evervoid.utils.Pair;
import com.jme3.math.ColorRGBA;

public class UIPlanet extends UIShadedProp implements PlanetObserver
{
	private final Planet aPlanet;

	public UIPlanet(final SolarGrid grid, final Planet planet)
	{
		super(grid, planet.getLocation(), planet);
		aPlanet = planet;
		buildProp();
		aPlanet.registerObserver(this);
	}

	@Override
	public void buildingConstructed(final Building building, final int progress)
	{
		// TODO Auto-generated method stub
	}

	@Override
	protected UIControl buildPanelUI()
	{
		final UIControl root = new UIControl(BoxDirection.HORIZONTAL);
		final UIControl base = new UIControl(BoxDirection.VERTICAL);
		base.addUI(new RescalableControl(getPlanetSprite()), 1);
		base.addUI(new HorizontalCenteredControl(new StaticTextControl(aPlanet.getData().getTitle(), ColorRGBA.White)));
		base.addUI(new HorizontalCenteredControl(new StaticTextControl("Owned by " + aPlanet.getPlayer().getNickname(),
				GraphicsUtils.getColorRGBA(aPlanet.getPlayer().getColor()))));
		final UIControl stats = new UIControl(BoxDirection.VERTICAL);
		stats.addUI(new StaticTextControl("Resources:", ColorRGBA.White));
		final ResourceAmount amount = aPlanet.getResourceRate();
		for (final String resName : amount.getNames()) {
			if (amount.getValue(resName) <= 0) {
				continue;
			}
			final ResourceData data = aPlanet.getState().getResourceData(resName);
			final UIControl row = new UIControl(BoxDirection.HORIZONTAL);
			row.addUI(new VerticalCenteredControl(new ImageControl(data.getIcon())));
			row.addSpacer(4, 1);
			row.addUI(new VerticalCenteredControl(new StaticTextControl(data.getTitle() + ": " + amount.getValue(resName),
					ColorRGBA.White)));
			stats.addUI(row);
		}
		stats.addFlexSpacer(1);
		final Pair<ShipData, Integer> shipProgress = aPlanet.getShipPorgress();
		if (shipProgress == null) {
			stats.addUI(new VerticalCenteredControl(new StaticTextControl("Not buidling a ship", ColorRGBA.Red)));
		}
		else {
			stats.addUI(new VerticalCenteredControl(new StaticTextControl("Building ship " + shipProgress.getKey().getTitle()
					+ ", " + shipProgress.getValue() + " turns left ", ColorRGBA.Red)));
		}
		stats.addFlexSpacer(1);
		final UIControl abilities = new UIControl(BoxDirection.VERTICAL);
		root.addUI(base);
		root.addFlexSpacer(1);
		root.addUI(stats);
		root.addFlexSpacer(1);
		root.addUI(abilities);
		return root;
	}

	@Override
	protected void buildSprite()
	{
		final ShadedSprite shade = new ShadedSprite(getPlanetSprite());
		addSprite(shade);
		setShade(shade);
	}

	@Override
	public void captured(final Player player)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void delFromGrid()
	{
		aPlanet.deregisterObserver(this);
	}

	@Override
	protected void finishedMoving()
	{
		// TODO Auto-generated method stub
	}

	private SpriteData getPlanetSprite()
	{
		return aPlanet.getData().getBaseSprite();
	}

	@Override
	boolean isSelectable()
	{
		return true;
	}

	@Override
	public void shipConstructed(final Ship ship, final int progress)
	{
		// TODO Auto-generated method stub
	}
}
