package com.evervoid.client.showroom;

import java.util.HashMap;
import java.util.Map;

import com.evervoid.client.ui.ClickObserver;
import com.evervoid.client.ui.ImageControl;
import com.evervoid.client.ui.RescalableControl;
import com.evervoid.client.ui.ScrollingControl;
import com.evervoid.client.ui.UIControl;
import com.evervoid.state.data.GameData;
import com.evervoid.state.data.PlanetData;
import com.evervoid.state.data.RaceData;
import com.evervoid.state.data.ShipData;
import com.evervoid.utils.Pair;
import com.evervoid.utils.namedtree.NamedNode;

/**
 * Panel containing all clickable ships in the showroom menu
 */
class ShowRoomPanel extends ScrollingControl implements ClickObserver
{
	/**
	 * The last clicked node (second click sets to null.
	 */
	private NamedNode<UIControl> aActiveNode;
	/**
	 * The data used to load up sprites and rows.
	 */
	private final GameData aData;
	/**
	 * The root node containing the entire tree of possible sprites.
	 */
	private final NamedNode<UIControl> aRootNode;
	/**
	 * Maps clickable controls to the ({@link RaceData}, {@link ShipData}) pair to load when they are clicked
	 */
	private final Map<UIControl, Pair<RaceData, ShipData>> aShipControls = new HashMap<UIControl, Pair<RaceData, ShipData>>();
	/**
	 * Reference to the parent {@link ShowRoomView}
	 */
	private final ShowRoomView aView;

	/**
	 * Constructor; just needs the {@link GameData} and the parent view
	 * 
	 * @param view
	 *            The parent {@link ShowRoomView}
	 * @param data
	 *            The {@link GameData} to use
	 */
	public ShowRoomPanel(final ShowRoomView view, final GameData data)
	{
		super(600, 400);
		aData = data;
		aView = view;
		setAutomaticSpacer(6);
		aRootNode = new NamedNode<UIControl>(this);
		aActiveNode = aRootNode;
		buildTree();
		showTopRows();
	}

	/**
	 * Builds the planet rows.
	 * 
	 * @param pPlanetNode
	 *            The parent node for planets
	 */
	private void buildPlanets(final NamedNode<UIControl> pPlanetNode)
	{
		for (final String planetType : aData.getPlanetTypes()) {
			final PlanetData planet = aData.getPlanetData(planetType);
			final UIControl planetRow = new UIControl(BoxDirection.HORIZONTAL);
			planetRow.addSpacer(10, 1);
			planetRow.addString(planet.getTitle());
			planetRow.registerClickObserver(this);
			pPlanetNode.addChild(planet.getTitle(), planetRow);
		}
	}

	/**
	 * Builds the ship rows
	 * 
	 * @param pShipsNode
	 *            The node to which ship lists will be added.
	 */
	private void buildShips(final NamedNode<UIControl> pShipsNode)
	{
		for (final String raceType : aData.getRaceTypes()) {
			final RaceData race = aData.getRaceData(raceType);
			final UIControl raceRow = new UIControl(BoxDirection.VERTICAL);
			final UIControl title = new UIControl(BoxDirection.HORIZONTAL);
			title.addSpacer(8, 1);
			title.addUI(new ImageControl(race.getRaceIcon("small")));
			title.addSpacer(4, 1);
			title.addString(race.getTitle(), BoxDirection.VERTICAL);
			raceRow.addUI(title);
			raceRow.registerClickObserver(this);
			final NamedNode<UIControl> raceNode = pShipsNode.addChild(race.getTitle(), raceRow);
			for (final String shipType : race.getShipTypes()) {
				final ShipData ship = race.getShipData(shipType);
				final UIControl shipRow = new UIControl(BoxDirection.HORIZONTAL);
				shipRow.addSpacer(32, 1); // Indent slightly
				shipRow.addUI(new RescalableControl(ship.getBaseSprite()).setEnforcedDimension(32, 32));
				shipRow.addSpacer(8, 1);
				shipRow.addString(ship.getTitle(), BoxDirection.VERTICAL);
				shipRow.setHoverSelectable(true);
				aShipControls.put(shipRow, new Pair<RaceData, ShipData>(race, ship));
				shipRow.registerClickObserver(this);
				raceNode.addChild(shipType, shipRow);
			}
			addSpacer(1, 8);
		}
	}

	/**
	 * Creates the tree of possible UI controls.
	 */
	private void buildTree()
	{
		// ships
		final UIControl ships = new UIControl(BoxDirection.VERTICAL);
		ships.addSpacer(4, 1);
		ships.addString("Ships");
		ships.registerClickObserver(this);
		buildShips(aRootNode.addChild("ships", ships));
		// planets
		final UIControl planets = new UIControl(BoxDirection.VERTICAL);
		planets.addSpacer(4, 1);
		planets.addString("Planets");
		planets.registerClickObserver(this);
		buildPlanets(aRootNode.addChild("planets", planets));
	}

	/**
	 * Displays the direct children of the top node in the tree.
	 */
	private void showTopRows()
	{
		for (final UIControl control : aRootNode.getChildrenValues()) {
			addUI(control);
		}
	}

	/**
	 * Shows or hides all direct children of the given node.
	 * 
	 * @param pRoot
	 *            The node from which to get the children.
	 */
	private void toggleChildren(final NamedNode<UIControl> pRoot)
	{
		final UIControl row = pRoot.getValue();
		for (final UIControl control : pRoot.getChildrenValues()) {
			if (row.hasChild(control)) {
				row.deleteChildUI(control);
			}
			else {
				row.addUI(control);
			}
		}
	}

	@Override
	public boolean uiClicked(final UIControl clicked)
	{
		final NamedNode<UIControl> node = aRootNode.findNode(clicked);
		if (!node.isLeaf() && !node.isHead()) {
			if (aActiveNode == null) {
				toggleChildren(node);
				aActiveNode = node;
			}
			else if (aActiveNode.equals(node)) {
				toggleChildren(aActiveNode);
				aActiveNode = null;
			}
			else if (aActiveNode.equals(node.getParent())) {
				aActiveNode = node;
				toggleChildren(node);
			}
			else {
				toggleChildren(aActiveNode);
				aActiveNode = node;
				toggleChildren(node);
			}
		}
		else if (aRootNode.getChild("ships").childrenContain(clicked)) {
			final Pair<RaceData, ShipData> clickedShip = aShipControls.get(clicked);
			aView.openPlayground(clickedShip.getKey(), clickedShip.getValue());
		}
		else {
			return false;
		}
		return true;
	}
}
