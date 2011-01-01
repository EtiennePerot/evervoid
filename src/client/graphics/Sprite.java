package client.graphics;

import client.everNode;
import client.graphics.materials.BaseMaterial;

public class Sprite extends everNode
{
	private BaseMaterial aMaterial;

	public Sprite()
	{
	}

	public BaseMaterial getMaterial()
	{
		return aMaterial;
	}

	public void setMaterial(final BaseMaterial m)
	{
		super.setMaterial(m);
		aMaterial = m;
	}
}
