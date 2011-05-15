package com.evervoid.client.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.evervoid.client.graphics.geometry.Rectangle;
import com.evervoid.utils.MathUtils;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

/**
 * Easy Mesh builder; feed it some vertices, call .apply(), and it'll do the rest of the magic for you. Automatically guesses
 * texture coordinates, though you can override this with setTextureCoordinates().
 */
public class EasyMesh extends Mesh
{
	/**
	 * Maps vertices to their color info. Is not completely populated; only acts as override when calling .apply()
	 */
	private final Map<Integer, ColorRGBA> aColorCoords = new HashMap<Integer, ColorRGBA>();
	/**
	 * List of connections between vertices
	 */
	private final List<Integer> aConnections = new ArrayList<Integer>();
	/**
	 * Maps vertices to their texture coordinates. Is not completely populated; only acts as override when calling .apply()
	 */
	private final Map<Integer, Vector2f> aTextureCoords = new HashMap<Integer, Vector2f>();
	/**
	 * Map from vertices to their index in the mesh float buffer. Needs to be inverted during the apply() process for fast
	 * lookup.
	 */
	private final List<Vector3f> aVertexList = new ArrayList<Vector3f>();
	/**
	 * Current vertex index
	 */
	private int aVertexNumber = 0;

	/**
	 * Set the Mesh's internal float buffers. Call this when done adding all vertices.
	 */
	public void apply()
	{
		// Build vertices
		final Vector3f[] vertices = new Vector3f[aVertexNumber];
		for (int i = 0; i < aVertexNumber; i++) {
			vertices[i] = aVertexList.get(i);
		}
		// Build color coordinates
		final float[] colorCoords = new float[aVertexNumber * 4];
		final ColorRGBA defaultColor = new ColorRGBA();
		for (int i = 0; i < aVertexNumber; i++) {
			final ColorRGBA chosenColor = aColorCoords.containsKey(i) ? aColorCoords.get(i) : defaultColor;
			final int bufIndex = i * 4;
			colorCoords[bufIndex] = chosenColor.r;
			colorCoords[bufIndex + 1] = chosenColor.g;
			colorCoords[bufIndex + 2] = chosenColor.b;
			colorCoords[bufIndex + 3] = chosenColor.a;
		}
		// Build texture coordinates
		final Rectangle bounds = MathUtils.getBounds(vertices);
		final Vector2f[] texCoord = new Vector2f[aVertexNumber];
		for (int i = 0; i < aVertexNumber; i++) {
			if (aTextureCoords.containsKey(i)) {
				texCoord[i] = aTextureCoords.get(i);
			}
			else {
				texCoord[i] = MathUtils.getRelativeVector2f(aVertexList.get(i), bounds);
			}
		}
		// Build index list
		final int connections = aConnections.size();
		final int[] indexes = new int[connections];
		for (int i = 0; i < connections; i++) {
			indexes[i] = aConnections.get(i);
		}
		// Apply to Mesh object
		setBuffer(Type.Color, 4, BufferUtils.createFloatBuffer(colorCoords));
		setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
		setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
		setBuffer(Type.Index, 1, BufferUtils.createIntBuffer(indexes));
		updateBound();
	}

	/**
	 * Connect 3 vectors to make a triangle in the mesh.
	 * 
	 * @param a
	 *            Index of vector 1
	 * @param b
	 *            Index of vector 2
	 * @param c
	 *            Index of vector 3
	 */
	public void connect(final Integer a, final Integer b, final Integer c)
	{
		aConnections.add(a);
		aConnections.add(b);
		aConnections.add(c);
	}

	/**
	 * Connect 3 vectors to make a triangle in the mesh. Vectors are 2-dimensional, and assumed to have z = 0.
	 * 
	 * @param a
	 *            Vector 1
	 * @param b
	 *            Vector 2
	 * @param c
	 *            Vector 3
	 */
	public void connect(final Vector2f a, final Vector2f b, final Vector2f c)
	{
		connect(new Vector3f(a.x, a.y, 0), new Vector3f(b.x, b.y, 0), new Vector3f(c.x, c.y, 0));
	}

	/**
	 * Connect 3 vectors to make a triangle in the mesh.
	 * 
	 * @param a
	 *            Vector 1
	 * @param b
	 *            Vector 2
	 * @param c
	 *            Vector 3
	 */
	public void connect(final Vector3f a, final Vector3f b, final Vector3f c)
	{
		connect(getVertex(a), getVertex(b), getVertex(c));
	}

	/**
	 * Get the index in the mesh of a given vertex. If the vertex is not in the mesh, add it.
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @param z
	 *            Z coordinate
	 * @return The index of the vertex in the mesh
	 */
	public Integer getVertex(final float x, final float y, final float z)
	{
		return getVertex(x, y, z, false);
	}

	/**
	 * Get the index in the mesh of a given vertex. If the vertex is not in the mesh, add it if forceNew is false.
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @param z
	 *            Z coordinate
	 * @param forceNew
	 *            If true, forces the vertex to be declared as a new vertex in the mesh even if there is already such a vertex
	 *            in the mesh.
	 * @return The index of the vertex in the mesh
	 */
	public Integer getVertex(final float x, final float y, final float z, final boolean forceNew)
	{
		return getVertex(new Vector3f(x, y, z), forceNew);
	}

	/**
	 * Get the index in the mesh of a given vertex. If the vertex is not in the mesh, add it.
	 * 
	 * @param vertex
	 *            The vertex to look up
	 * @return The index of the vertex in the mesh
	 */
	public Integer getVertex(final Vector3f vertex)
	{
		return getVertex(vertex, false);
	}

	/**
	 * Get the index in the mesh of a given vertex. If the vertex is not in the mesh, add it if forceNew is false.
	 * 
	 * @param vertex
	 *            The vertex to look up
	 * @param forceNew
	 *            If true, forces the vertex to be declared as a new vertex in the mesh even if there is already such a vertex
	 *            in the mesh.
	 * @return The index of the vertex in the mesh
	 */
	public Integer getVertex(final Vector3f vertex, final boolean forceNew)
	{
		if (forceNew || !aVertexList.contains(vertex)) {
			aVertexList.add(vertex);
			aVertexNumber++;
			return aVertexNumber - 1;
		}
		return aVertexList.indexOf(vertex);
	}

	/**
	 * @return The number of vertices in the mesh
	 */
	@Override
	public int getVertexCount()
	{
		return aVertexNumber;
	}

	/**
	 * Assigns manual color values to a given vertex. Make sure to call .apply() for changes to take effect.
	 * 
	 * @param vertex
	 *            The index of the vertex at which to modify the texture coordinates
	 * @param color
	 *            The color value at the given vertex.
	 */
	public void setColorValue(final Integer vertex, final ColorRGBA color)
	{
		aColorCoords.put(vertex, color);
	}

	/**
	 * Assigns manual color values to a given vertex. Make sure to call .apply() for changes to take effect.
	 * 
	 * @param vertex
	 *            The vertex at which to modify the texture coordinates
	 * @param color
	 *            The color value at the given vertex.
	 */
	public void setColorValue(final Vector3f vertex, final ColorRGBA color)
	{
		setColorValue(getVertex(vertex), color);
	}

	/**
	 * Assigns manual texture coordinates to a given vertex. Make sure to call .apply() for changes to take effect.
	 * 
	 * @param vertex
	 *            The index of the vertex at which to modify the texture coordinates
	 * @param texCoords
	 *            The texture coordinates at the given vertex.
	 */
	public void setTextureCoordinate(final Integer vertex, final Vector2f texCoords)
	{
		aTextureCoords.put(vertex, texCoords);
	}

	/**
	 * Assigns manual texture coordinates to a given vertex. Make sure to call .apply() for changes to take effect.
	 * 
	 * @param vertex
	 *            The vertex at which to modify the texture coordinates
	 * @param texCoords
	 *            The texture coordinates at the given vertex.
	 */
	public void setTextureCoordinate(final Vector3f vertex, final Vector2f texCoords)
	{
		setTextureCoordinate(getVertex(vertex), texCoords);
	}
}