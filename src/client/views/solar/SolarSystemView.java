package client.views.solar;

import java.awt.Rectangle;
import java.util.Map;

import client.GameView;
import client.Translation;
import client.everVoidClient;
import client.graphics.GUIUtils;

public class SolarSystemView extends GameView
{
	private final static float aGridScrollBorder = 0.2f;
	private final static float aGridScrollSpeed = 1024f;
	private final SolarSystemGrid aGrid;
	private final Translation aGridOffset;
	private final Rectangle aGridScrollRegion = new Rectangle(0, 0, everVoidClient.sScreenWidth,
			everVoidClient.sScreenHeight);

	public SolarSystemView()
	{
		super();
		aGrid = new SolarSystemGrid();
		addNode(aGrid);
		aGridOffset = aGrid.getNewTranslation();
		aGridOffset.setMinimumConstraint(aGridScrollRegion.width - aGrid.getTotalWidth(),
				aGridScrollRegion.height - aGrid.getTotalHeight());
		aGridOffset.setMaximumConstraint(2, 2);
	}

	@Override
	public void frame(final float tpf)
	{
		for (final Map.Entry<GUIUtils.Border, Float> e : GUIUtils.isInBorder(everVoidClient.sCursorPosition,
				aGridScrollRegion, SolarSystemView.aGridScrollBorder).entrySet())
		{
			aGridOffset.move(-e.getKey().getXDirection() * e.getValue() * tpf * SolarSystemView.aGridScrollSpeed, -e
					.getKey().getYDirection() * e.getValue() * tpf * SolarSystemView.aGridScrollSpeed);
		}
	}
}
