package client.graphics;

import client.everNode;

import com.jme3.material.Material;

public class Graphic extends everNode
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
