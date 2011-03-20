package com.evervoid.client.views.solar;

import java.util.HashSet;
import java.util.Set;

import com.evervoid.client.graphics.EasyMesh;
import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.Grid;
import com.evervoid.client.graphics.geometry.Rectangle;
import com.evervoid.client.graphics.materials.PlainColor;
import com.evervoid.state.geometry.GridLocation;
import com.evervoid.state.geometry.Point;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;

public class SolarGridHighlightLocations extends EverNode
{
	private static final float sHighlightLocationsZOffset = -0.5f;
	private static final ColorRGBA sValidLocationsColor = new ColorRGBA(0f, 0.3f, 0.05f, 0.5f);
	private final Grid aGrid;
	private PlainColor aMaterial;
	private final EasyMesh aMesh = new EasyMesh();
	private final Set<Point> aPoints = new HashSet<Point>();

	public SolarGridHighlightLocations(final Grid grid, final Iterable<GridLocation> locations)
	{
		aGrid = grid;
		for (final GridLocation loc : locations) {
			for (final Point p : loc.getPoints()) {
				addPoint(p);
			}
		}
		aMesh.apply();
		final Geometry geo = new Geometry("Grid Highlight", aMesh);
		aMaterial = new PlainColor(sValidLocationsColor);
		geo.setMaterial(aMaterial);
		attachChild(geo);
		getNewTransform().translate(0, 0, sHighlightLocationsZOffset);
		smoothAppear(0.25f);
	}

	public SolarGridHighlightLocations(final Grid grid, final Set<Point> points)
	{
		aGrid = grid;
		for (final Point p : points) {
			addPoint(p);
		}
		aMesh.apply();
	}

	private void addPoint(final Point p)
	{
		if (aPoints.add(p)) {
			// addNode(new PlainRectangle(new Vector2f(SolarGrid.sCellSize * p.x, SolarGrid.sCellSize * p.y),
			// SolarGrid.sCellSize, SolarGrid.sCellSize, sValidLocationsColor));
			final Rectangle rec = aGrid.getCellBounds(p);
			aMesh.connect(rec.getBottomLeft(), rec.getBottomRight(), rec.getTopLeft());
			aMesh.connect(rec.getTopLeft(), rec.getBottomRight(), rec.getTopRight());
		}
	}

	void fadeOut()
	{
		smoothDisappear(0.25f);
	}

	@Override
	public void setAlpha(final float alpha)
	{
		aMaterial.setAlpha(alpha);
	}
}
