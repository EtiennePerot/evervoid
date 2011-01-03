package client.graphics.materials;

import java.io.File;

import client.graphics.GraphicManager;

import com.jme3.material.Material;

public class BaseMaterial extends Material
{
	public BaseMaterial(final String name)
	{
		super(GraphicManager.getAssetManager(), "mat" + File.separator + name + ".j3md");
	}
}