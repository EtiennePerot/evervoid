package com.evervoid.client.views.solar;

import com.evervoid.client.graphics.EasyMesh;
import com.evervoid.client.graphics.EverNode;
import com.evervoid.client.graphics.geometry.AnimatedAlpha;
import com.evervoid.client.graphics.geometry.AnimatedScaling;
import com.evervoid.client.graphics.geometry.AnimatedTransform.DurationMode;
import com.evervoid.client.graphics.geometry.AnimatedTranslation;
import com.evervoid.client.graphics.materials.PlainColor;
import com.evervoid.state.geometry.GridLocation;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;

public class SolarGridSelection extends EverNode
{
	private final AnimatedAlpha aAlpha;
	private GridLocation aLocation;
	private final PlainColor aMaterial;
	private final AnimatedScaling aScale;
	private final AnimatedTranslation aTranslation;

	SolarGridSelection()
	{
		final EasyMesh mesh = new EasyMesh();
		mesh.connect(new Vector2f(0, 0), new Vector2f(SolarGrid.sCellSize, 0), new Vector2f(SolarGrid.sCellSize,
				SolarGrid.sCellSize));
		mesh.connect(new Vector2f(0, 0), new Vector2f(SolarGrid.sCellSize, SolarGrid.sCellSize), new Vector2f(0,
				SolarGrid.sCellSize));
		mesh.apply();
		final Geometry geo = new Geometry("GridCellsNode-" + hashCode(), mesh);
		aMaterial = new PlainColor(new ColorRGBA(0.5f, 0.5f, 0.5f, 0.5f));
		geo.setMaterial(aMaterial);
		attachChild(geo);
		aTranslation = getNewTranslationAnimation();
		aScale = getNewScalingAnimation();
		aAlpha = getNewAlphaAnimation();
		aTranslation.setDuration(0.075f).setDurationMode(DurationMode.CONTINUOUS).translate(0, 0, -20);
		aScale.setDuration(0.075f).setDurationMode(DurationMode.CONTINUOUS);
		aAlpha.setDuration(0.25f).setDurationMode(DurationMode.CONTINUOUS).setAlpha(0);
		aLocation = new GridLocation(0, 0);
	}

	void fadeIn()
	{
		aAlpha.setTargetAlpha(1).start();
	}

	void fadeOut()
	{
		aAlpha.setTargetAlpha(0).start();
	}

	void goTo(final GridLocation location)
	{
		if (aLocation.equals(location)) {
			return;
		}
		aLocation = (GridLocation) location.clone();
		aTranslation.smoothMoveTo(location.origin.x * SolarGrid.sCellSize, location.origin.y * SolarGrid.sCellSize).start();
		aScale.setTargetScale(location.dimension.width, location.dimension.height).start();
	}

	@Override
	public void setAlpha(final float alpha)
	{
		aMaterial.setAlpha(alpha);
	}
}
