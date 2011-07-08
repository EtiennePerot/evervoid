package com.evervoid.client.graphics.materials;

import java.io.File;

import com.evervoid.client.graphics.GraphicManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;

/**
 * Abstract base material class from which all specific materials are derived. Always assumes to be transparent, which may cause
 * a slight performance loss, but almost none of the game's graphics is completely opaque, thus this makes the code simpler.
 */
public abstract class BaseMaterial extends Material
{
	/**
	 * Constructor; requires the name of the material (.j3md) file to load
	 * 
	 * @param name
	 *            The name of the material file to load. The .j3md file loaded is assumed to be /mat/{name}/{name}.j3md.
	 */
	public BaseMaterial(final String name)
	{
		super(GraphicManager.getAssetManager(), "mat" + File.separator + name + File.separator + name + ".j3md");
		setTransparent(true);
		getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
	}
}
