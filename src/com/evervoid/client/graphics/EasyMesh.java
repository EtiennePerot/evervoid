package com.evervoid.client.graphics;

import java.util.ArrayList;
import java.util.List;

import com.evervoid.client.graphics.geometry.MathUtils;
import com.evervoid.client.graphics.geometry.Rectangle;
import com.evervoid.state.BiMap;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

public class EasyMesh extends Mesh
{
	private final List<Integer> aConnections = new ArrayList<Integer>();
	private final BiMap<Vector3f, Integer> aVertexMap = new BiMap<Vector3f, Integer>();
	private int aVertexNumber = 0;

	protected void apply()
	{
		// Build vertices
		final Vector3f[] vertices = new Vector3f[aVertexNumber];
		for (int i = 0; i < aVertexNumber; i++) {
			vertices[i] = aVertexMap.get1(i);
		}
		// Build texture coords
		final Rectangle bounds = MathUtils.getBounds(vertices);
		final Vector2f[] texCoord = new Vector2f[aVertexNumber];
		for (int i = 0; i < aVertexNumber; i++) {
			texCoord[i] = MathUtils.getRelativeVector2f(aVertexMap.get1(i), bounds);
		}
		// Build index list
		final int connections = aConnections.size();
		final int[] indexes = new int[connections];
		for (int i = 0; i < connections; i++) {
			indexes[i] = aConnections.get(i);
		}
		// Apply to Mesh object
		setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
		setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
		setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indexes));
	}

	protected void connect(final Vector2f a, final Vector2f b, final Vector2f c)
	{
		connect(new Vector3f(a.x, a.y, 0), new Vector3f(b.x, b.y, 0), new Vector3f(c.x, c.y, 0));
	}

	protected void connect(final Vector3f a, final Vector3f b, final Vector3f c)
	{
		aConnections.add(getVertex(a));
		aConnections.add(getVertex(b));
		aConnections.add(getVertex(c));
	}

	private Integer getVertex(final Vector3f vertex)
	{
		if (!aVertexMap.contains(vertex)) {
			aVertexMap.put(vertex, aVertexNumber);
			aVertexNumber++;
		}
		return aVertexMap.get2(vertex);
	}
}
