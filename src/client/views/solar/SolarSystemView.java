package client.views.solar;

import java.awt.Rectangle;
import java.util.Map;

import client.GameView;
import client.Translation;
import client.everVoidClient;
import client.graphics.FrameUpdate;
import client.graphics.GUIUtils;
import client.graphics.Grid.HoverMode;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;

public class SolarSystemView extends GameView
{
	private final static float aGridScrollBorder = 0.2f;
	private final static float aGridScrollSpeed = 1024f;
	private final SolarSystemGrid aGrid;
	private final Translation aGridOffset;
	private Rectangle aGridScrollRegion = new Rectangle(0, 0, everVoidClient.sScreenWidth, everVoidClient.sScreenHeight);
	private final Vector2f aGridTranslation = new Vector2f();

	public SolarSystemView()
	{
		super();
		aGrid = new SolarSystemGrid();
		addNode(aGrid);
		aGrid.setHandleHover(HoverMode.ON);
		aGrid.setHoverColor(new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f));
		aGridOffset = aGrid.getNewTranslation();
		aGridOffset.setMinimumConstraint(aGridScrollRegion.width - aGrid.getTotalWidth(), aGridScrollRegion.height
				- aGrid.getTotalHeight());
		aGridOffset.setMaximumConstraint(2, 2);
	}

	@Override
	public void frame(final FrameUpdate f)
	{
		aGridOffset.move(aGridTranslation.mult(f.getTPF()));
		// Hovered square
		aGrid.handleOver(f.getMousePosition().add(aGridOffset.get2f().negate()));
	}

	@Override
	public void onMouseMove(final String name, final float isPressed, final float tpf, final Vector2f position)
	{
		// Recompute grid scrolling speed
		aGridTranslation.set(0, 0);
		for (final Map.Entry<GUIUtils.Border, Float> e : GUIUtils.isInBorder(position, aGridScrollRegion,
				SolarSystemView.aGridScrollBorder).entrySet())
		{
			aGridTranslation.addLocal(-e.getKey().getXDirection() * e.getValue() * SolarSystemView.aGridScrollSpeed, -e
					.getKey().getYDirection() * e.getValue() * SolarSystemView.aGridScrollSpeed);
		}
	}

	@Override
	public void resolutionChanged()
	{
		aGridScrollRegion = new Rectangle(0, 0, everVoidClient.sScreenWidth, everVoidClient.sScreenHeight);
	}
}
