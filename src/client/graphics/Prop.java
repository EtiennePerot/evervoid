package client.graphics;

import java.awt.Point;

public abstract class Prop extends GridNode
{
	protected MultiSprite aSprite = new MultiSprite();

	public Prop(final Grid grid, final Point location)
	{
		super(grid, location);
		buildSprite();
		addNode(aSprite);
	}

	protected Sprite addSprite(final String image)
	{
		return aSprite.addSprite(image);
	}

	protected Sprite addSprite(final String image, final float x, final float y)
	{
		return aSprite.addSprite(image, x, y);
	}

	protected abstract void buildSprite();
}
