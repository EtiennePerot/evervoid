package client.graphics;

import com.jme3.material.Material;
import com.jme3.scene.Node;

public class Graphic extends Node
{
	private Material aMaterial;

	public Material getMaterial()
	{
		return aMaterial;
	}

	@Override
	public void setMaterial(final Material m)
	{
		super.setMaterial(m);
		aMaterial = m;
	}
}
