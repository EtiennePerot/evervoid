package client.graphics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import client.EverNode;
import client.graphics.geometry.Transform;

public class MultiSprite extends EverNode
{
	private static float sGlobalDepth = 0f;
	private float aDepth;
	private final Set<EverNode> aSprites = new HashSet<EverNode>();
	private final Map<Sprite, Transform> aTranslations = new HashMap<Sprite, Transform>();

	public MultiSprite()
	{
		super();
		aDepth = MultiSprite.sGlobalDepth;
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
		MultiSprite.sGlobalDepth += 0.0001f;
		aDepth += 0.0001f;
		addNode(s);
		return s;
	}
}
