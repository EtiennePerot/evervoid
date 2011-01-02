package client.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import client.EverNode;
import client.graphics.geometry.Transform;

public class MultiSprite extends EverNode
{
	private float aDepth = 0f;
	private final List<Sprite> aSprites = new ArrayList<Sprite>();
	private final Map<Sprite, Transform> aTranslations = new HashMap<Sprite, Transform>();

	public MultiSprite()
	{
		super();
	}

	public MultiSprite(final String image)
	{
		this();
		addSprite(image);
	}

	public Sprite addSprite(final String image)
	{
		return addSprite(image, 0, 0);
	}

	public Sprite addSprite(final String image, final float x, final float y)
	{
		final Sprite s = new Sprite(image);
		aSprites.add(s);
		final Transform t = s.getNewTransform();
		aTranslations.put(s, t);
		t.translate(x, y, aDepth);
		aDepth += 0.0001f;
		addNode(s);
		return s;
	}
}
