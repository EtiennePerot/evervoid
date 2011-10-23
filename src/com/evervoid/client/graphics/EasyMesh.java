package com.evervoid.client.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	 * Bounds that the mesh will be considered to have when computing texture coordinates, if a world unit is considered to
	 * correspond to a texture instance.
	 */
	private static final float[] sUnitTextureBounds = { 0, 0, 1, 1 };
	/**
	 * Maps vertices to their color info. Is not completely populated; only acts as override when calling .apply()
	 */
	private final Map<Integer, ColorRGBA> aColorCoords = new HashMap<Integer, ColorRGBA>();
	/**
	 * List of connections between vertices
	 */
	private final List<Integer> aConnections = new ArrayList<Integer>();
	/**
	 * Maps every vertex index to the index of the first triangle in which it appears. Used for normal computation in case
	 * normals have not been set.
	 */
	private final Map<Integer, Integer> aFirstTriangle = new HashMap<Integer, Integer>();
	/**
	 * Whether to consider the mesh to be laid out on the (X, Y) plane or not
	 */
	private boolean aFlatYAxis = true;
	/**
	 * Maps vertices to their normal vector. Is not completely populated; only acts as override when calling .apply()
	 */
	private final Map<Integer, Vector3f> aNormals = new HashMap<Integer, Vector3f>();
	/**
	 * Maps vertices to their texture coordinates. Is not completely populated; only acts as override when calling .apply()
	 */
	private final Map<Integer, Vector2f> aTextureCoords = new HashMap<Integer, Vector2f>();
	/**
	 * Whether to consider a world unit to be a single instance of the texture. Useful for textures that should be repeated
	 * every unit.
	 */
	private boolean aUnitTextureCoordinates = false;
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
	 * Default color for vertices without a manually-assigned color
	 */
	private final ColorRGBA defaultVertexColor = new ColorRGBA(1, 1, 1, 1);

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
		for (int i = 0; i < aVertexNumber; i++) {
			final ColorRGBA chosenColor = aColorCoords.containsKey(i) ? aColorCoords.get(i) : defaultVertexColor;
			final int bufIndex = i * 4;
			colorCoords[bufIndex] = chosenColor.r;
			colorCoords[bufIndex + 1] = chosenColor.g;
			colorCoords[bufIndex + 2] = chosenColor.b;
			colorCoords[bufIndex + 3] = chosenColor.a;
		}
		// Build normals
		final float[] normals = new float[aVertexNumber * 3];
		for (int i = 0; i < aVertexNumber; i++) {
			final Vector3f normal;
			if (aNormals.containsKey(i)) {
				normal = aNormals.get(i);
			}
			else if (aFirstTriangle.containsKey(i)) {
				final int firstTriangleIndex = aFirstTriangle.get(i);
				final Vector3f v1 = aVertexList.get(aConnections.get(firstTriangleIndex * 3));
				final Vector3f v2 = aVertexList.get(aConnections.get(firstTriangleIndex * 3 + 1));
				final Vector3f v3 = aVertexList.get(aConnections.get(firstTriangleIndex * 3 + 2));
				normal = v2.subtract(v1).cross(v3.subtract(v1)).normalizeLocal();
			}
			else {
				// This vertex is used in no triangles! Just fill with 0, 0, 0.
				normal = new Vector3f(0, 0, 0);
			}
			final int bufIndex = i * 3;
			normals[bufIndex] = normal.x;
			normals[bufIndex + 1] = normal.y;
			normals[bufIndex + 2] = normal.z;
		}
		// Build texture coordinates
		final float[] bounds = getBounds(vertices, aFlatYAxis);
		final Vector2f[] texCoord = new Vector2f[aVertexNumber];
		for (int i = 0; i < aVertexNumber; i++) {
			if (aTextureCoords.containsKey(i)) {
				texCoord[i] = aTextureCoords.get(i);
			}
			else if (aUnitTextureCoordinates) {
				texCoord[i] = getRelativeVector2f(aVertexList.get(i), sUnitTextureBounds, aFlatYAxis);
			}
			else {
				texCoord[i] = getRelativeVector2f(aVertexList.get(i), bounds, aFlatYAxis);
			}
		}
		// Build index list
		final int connections = aConnections.size();
		final int[] indexes = new int[connections];
		for (int i = 0; i < connections; i++) {
			indexes[i] = aConnections.get(i);
		}
		// Apply to Mesh object
		clearBuffer(Type.Color);
		setBuffer(Type.Color, 4, BufferUtils.createFloatBuffer(colorCoords));
		clearBuffer(Type.Normal);
		setBuffer(Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
		clearBuffer(Type.Position);
		setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
		clearBuffer(Type.TexCoord);
		setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
		clearBuffer(Type.Index);
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
		final int triangleIndex = aConnections.size() / 3;
		if (!aFirstTriangle.containsKey(a)) {
			aFirstTriangle.put(a, triangleIndex);
		}
		if (!aFirstTriangle.containsKey(b)) {
			aFirstTriangle.put(b, triangleIndex);
		}
		if (!aFirstTriangle.containsKey(c)) {
			aFirstTriangle.put(c, triangleIndex);
		}
		aConnections.add(a);
		aConnections.add(b);
		aConnections.add(c);
	}

	/**
	 * Connect 3 vectors to make a triangle in the mesh. Vectors are 2-dimensional. They will be assumed to have y = 0 or z = 0,
	 * depending on whether the Y axis has been set to be flat or not.
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
		connect(new Vector3f(a.x, aFlatYAxis ? a.y : 0, aFlatYAxis ? 0 : a.y), new Vector3f(b.x, aFlatYAxis ? b.y : 0,
				aFlatYAxis ? 0 : b.y), new Vector3f(c.x, aFlatYAxis ? c.y : 0, aFlatYAxis ? 0 : c.y));
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
	 * Connect 3 vectors in a certain direction only.
	 * 
	 * @param a
	 *            Vector 1
	 * @param b
	 *            Vector 2
	 * @param c
	 *            Vector 3
	 * @param clockwise
	 *            True to connect the vertices clockwise, false to connect them anticlockwise
	 */
	public void connectDirection(final Integer a, final Integer b, final Integer c, final boolean clockwise)
	{
		final Vector3f v1 = aVertexList.get(a);
		final Vector3f v2 = aVertexList.get(b);
		final Vector3f v3 = aVertexList.get(c);
		final Vector2f a1 = new Vector2f(v1.x, aFlatYAxis ? v1.y : v1.z);
		final Vector2f a2 = new Vector2f(v2.x, aFlatYAxis ? v2.y : v2.z);
		final Vector2f a3 = new Vector2f(v3.x, aFlatYAxis ? v3.y : v3.z);
		final Vector3f cross = a2.subtract(a1).cross(a2.subtract(a3));
		if ((cross.z > 0 && clockwise) || (cross.z < 0 && !clockwise)) {
			connect(a, b, c);
		}
		else {
			connect(a, c, b);
		}
	}

	/**
	 * Connect 3 vectors in a certain direction only.
	 * 
	 * @param a
	 *            Vector 1
	 * @param b
	 *            Vector 2
	 * @param c
	 *            Vector 3
	 * @param clockwise
	 *            True to connect the vertices clockwise, false to connect them anticlockwise
	 */
	public void connectDirection(final Vector3f a, final Vector3f b, final Vector3f c, final boolean clockwise)
	{
		connectDirection(getVertex(a), getVertex(b), getVertex(c), clockwise);
	}

	/**
	 * Connect 3 vectors to make two triangles (one for each triangle face) in the mesh.
	 * 
	 * @param a
	 *            Index of vector 1
	 * @param b
	 *            Index of vector 2
	 * @param c
	 *            Index of vector 3
	 */
	public void doubleConnect(final Integer a, final Integer b, final Integer c)
	{
		connect(a, b, c);
		connect(a, c, b);
	}

	/**
	 * Connect 3 vectors to make two triangles (one for each triangle face) in the mesh. Vectors are 2-dimensional, and assumed
	 * to have z = 0.
	 * 
	 * @param a
	 *            Vector 1
	 * @param b
	 *            Vector 2
	 * @param c
	 *            Vector 3
	 */
	public void doubleConnect(final Vector2f a, final Vector2f b, final Vector2f c)
	{
		connect(a, b, c);
		connect(a, c, b);
	}

	/**
	 * Connect 3 vectors to make two triangles (one for each triangle face) in the mesh.
	 * 
	 * @param a
	 *            Vector 1
	 * @param b
	 *            Vector 2
	 * @param c
	 *            Vector 3
	 */
	public void doubleConnect(final Vector3f a, final Vector3f b, final Vector3f c)
	{
		connect(a, b, c);
		connect(a, c, b);
	}

	/**
	 * @param coords
	 *            Set of vertices to get the bounds for
	 * @param useYAxis
	 *            Whether to use the Y axis from the vector or not (then, will use the Z axis)
	 * @return The bounds of the rectangle containing all the points associated with the vectors passed
	 */
	private float[] getBounds(final Vector3f[] coords, final boolean useYAxis)
	{
		float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE, maxX = -Float.MAX_VALUE, maxY = -Float.MAX_VALUE;
		for (final Vector3f v : coords) {
			minX = Math.min(minX, v.x);
			minY = Math.min(minY, useYAxis ? v.y : v.z);
			maxX = Math.max(maxX, v.x);
			maxY = Math.max(maxY, useYAxis ? v.y : v.z);
		}
		final float[] ret = { minX, minY, maxX - minX, maxY - minY };
		return ret;
	}

	/**
	 * Given a vector and a 4-bounds rectangle, gives the relative offset of the vector within the rectangle (e.g. a vector
	 * corresponding to the top-right corner of the rectangle would have relative offset (1, 1)).
	 * 
	 * @param absolute
	 *            The vector
	 * @param referential
	 *            The rectangle
	 * @param useYAxis
	 *            Whether to use the Y axis from the vector or not (then, will use the Z axis)
	 * @return The relative offset
	 */
	private Vector2f getRelativeVector2f(final Vector3f absolute, final float[] referential, final boolean useYAxis)
	{
		return new Vector2f((absolute.x - referential[0]) / referential[2],
				((useYAxis ? absolute.y : absolute.z) - referential[1]) / referential[3]);
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
	 * Set the default color for vertices without a manually-assigned color. Make sure to call .apply() for changes to take
	 * effect.
	 * 
	 * @param defaultColor
	 *            The default color for vertices without a manually-assigned color.
	 */
	public void setDefaultVertexColor(final ColorRGBA defaultColor)
	{
		defaultVertexColor.set(defaultColor);
	}

	/**
	 * Sets whether the Y axis should be considered a flat axis or not. Affects the way texture coordinates are computed.
	 * 
	 * @param isYFlat
	 *            Whether the Y axis should be considered flat or not.
	 */
	public void setFlatYAxis(final boolean isYFlat)
	{
		aFlatYAxis = isYFlat;
	}

	/**
	 * Assigns a manual normal vector to a given vertex. Make sure to call .apply() for changes to take effect.
	 * 
	 * @param vertex
	 *            The index of the vertex at which to modify the normal vector
	 * @param normal
	 *            The normal vector at the given vertex
	 */
	public void setNormal(final Integer vertex, final Vector3f normal)
	{
		aNormals.put(vertex, normal.normalize());
	}

	/**
	 * Assigns a manual normal vector to a given vertex. Make sure to call .apply() for changes to take effect.
	 * 
	 * @param vertex
	 *            The vertex at which to modify the normal vector
	 * @param normal
	 *            The normal vector at the given vertex
	 */
	public void setNormal(final Vector3f vertex, final Vector3f normal)
	{
		setNormal(getVertex(vertex), normal);
	}

	/**
	 * Sets whether the texture coordinates should be computed so that one world unit corresponds to an instance of the texture.
	 * 
	 * @param repeat
	 *            True if it should correspond to an instance of the texture, false if the texture instance should correspond to
	 *            the whole object.
	 */
	public void setRepeatTextureOnUnit(final boolean repeat)
	{
		aUnitTextureCoordinates = repeat;
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
