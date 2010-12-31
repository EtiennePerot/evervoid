package client;

import java.util.ArrayList;
import java.util.List;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class everNode extends Node
{
	List<everNode> aSubnodes = new ArrayList<everNode>();
	List<Translation> aTranslations = new ArrayList<Translation>();

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

	public void frame(final float tpf)
	{
		// Do nothing; overridden by subclasses
	}

	public Translation getNewTranslation()
	{
		final Translation t = new Translation(this);
		aTranslations.add(t);
		return t;
	}

	public void recurse(final float tpf)
	{
		for (final everNode e : aSubnodes)
		{
			e.recurse(tpf);
		}
		frame(tpf);
	}
}
