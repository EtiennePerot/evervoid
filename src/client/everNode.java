package client;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import client.graphics.FrameUpdate;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class everNode extends Node
{
	Set<everNode> aSubnodes = new HashSet<everNode>();
	List<Translation> aTranslations = new ArrayList<Translation>();

	public everNode()
	{
		super();
		resolutionChanged();
	}

	public void addNode(final everNode node)
	{
		aSubnodes.add(node);
		attachChild(node);
	}

	public void computeTranslation()
	{
		final Vector3f finalOffset = new Vector3f(0, 0, 0);
		for (final Translation t : aTranslations)
		{
			finalOffset.addLocal(t.get());
		}
		setLocalTranslation(finalOffset);
	}

	public void delNode(final everNode node)
	{
		if (aSubnodes.contains(node))
		{
			aSubnodes.remove(node);
		}
		if (hasChild(node))
		{
			detachChild(node);
		}
	}

	public void frame(final FrameUpdate tpf)
	{
		// Do nothing; overridden by subclasses
	}

	public Translation getNewTranslation()
	{
		final Translation t = new Translation(this);
		aTranslations.add(t);
		return t;
	}

	public void recurse(final FrameUpdate f)
	{
		for (final everNode e : aSubnodes)
		{
			e.recurse(f);
		}
		frame(f);
	}

	public void resolutionChanged()
	{
		for (final everNode e : aSubnodes)
		{
			e.resolutionChanged();
		}
	}
}
