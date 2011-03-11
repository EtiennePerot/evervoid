package com.evervoid.client.graphics.materials;

import java.io.File;

import com.evervoid.client.graphics.GraphicManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;

class BaseMaterial extends Material
{
	public BaseMaterial(final String name)
	{
		super(GraphicManager.getAssetManager(), "mat" + File.separator + name + File.separator + name + ".j3md");
		setTransparent(true);
		getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
	}
}
