package com.evervoid.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A Json node.
 */
public class Json implements Iterable<Json>, Jsonable
{
	/**
	 * A Json node can have multiple types: Object (string -> Json node mapping), List (Multiple Json nodes), String, Int, and
	 * Float.
	 */
	public enum JsonType
	{
		FLOAT, INTEGER, LIST, OBJECT, STRING;
	}

	/**
	 * Parse a Json String and return a Json object
	 * 
	 * @param jsonString
	 *            The Json strin to parse
	 * @return The parsed representation
	 */
	public static Json fromString(final String jsonString)
	{
		return new JsonParser(jsonString).parse();
	}

	/**
	 * This node's float value, if it is a float
	 */
	private float aFloat = 0f;
	/**
	 * This node's int value, it is is an int
	 */
	private int aInt = 0;
	/**
	 * This node's list of Json nodes, if it is a list
	 */
	private List<Json> aList = null;
	/**
	 * This node's map of String -> Json nodes, if it is an object
	 */
	private Map<String, Json> aObject = null;
	/**
	 * This node's String value, if it is a String
	 */
	private String aString = "";
	/**
	 * This node's type
	 */
	private final JsonType aType;

	/**
	 * Creates a new Json node of type Object.
	 */
	public Json()
	{
		aType = JsonType.OBJECT;
		aObject = new HashMap<String, Json>();
	}

	/**
	 * Creates a new Json node of type List.
	 * 
	 * @param list
	 *            The list of Json nodes to contain
	 */
	public Json(final Collection<? extends Jsonable> list)
	{
		aType = JsonType.LIST;
		aList = new ArrayList<Json>(list.size());
		for (final Jsonable j : list) {
			aList.add(j.toJson());
		}
	}

	/**
	 * Creates a new Json node of type Float.
	 * 
	 * @param number
	 *            The float value of the node
	 */
	public Json(final double number)
	{
		this((float) number);
	}

	/**
	 * Creates a new Json node of type Float.
	 * 
	 * @param number
	 *            The float value of the node
	 */
	public Json(final float number)
	{
		aType = JsonType.FLOAT;
		aFloat = number;
	}

	/**
	 * Creates a new Json node of type Integer.
	 * 
	 * @param integer
	 *            The int value of the node
	 */
	public Json(final int integer)
	{
		aType = JsonType.INTEGER;
		aInt = integer;
	}

	/**
	 * Creates a new Json node of type String
	 * 
	 * @param str
	 *            The String value of the node
	 */
	public Json(final String str)
	{
		aType = JsonType.STRING;
		aString = str;
	}

	/**
	 * Retrieve an attribute in an Object node. Example: On {"a": "b"}, getAttribute("a") returns Json("b").
	 * 
	 * @param attribute
	 *            The name of the attribute
	 * @return The Json node representing the attribute
	 */
	public Json getAttribute(final String attribute)
	{
		return aObject.get(attribute);
	}

	/**
	 * Returns an Iterable over the attributes of an Object node. Always sorted in order to guarantee consistent output. This
	 * way, comparing two Json nodes is as easy as comparing their string representation.
	 * 
	 * @return An Iterable over the list of attributes of this Object node
	 */
	public Iterable<String> getAttributes()
	{
		final List<String> keys = new ArrayList<String>(aObject.keySet());
		Collections.sort(keys);
		return keys;
	}

	/**
	 * @return The float value of this node
	 */
	public float getFloat()
	{
		return aFloat;
	}

	/**
	 * Retrieve a Float attribute in an Object node. Equivalent to getAttribute(attribute).getFloat()
	 * 
	 * @param attribute
	 *            The name of the attribute
	 * @return The Float value stored at the specified attribute
	 */
	public float getFloatAttribute(final String attribute)
	{
		return getAttribute(attribute).getFloat();
	}

	/**
	 * @return The int value of this node
	 */
	public int getInt()
	{
		return aInt;
	}

	/**
	 * Retrieve an Integer attribute in an Object node. Equivalent to getAttribute(attribute).getInt()
	 * 
	 * @param attribute
	 *            The name of the attribute
	 * @return The Integer value stored at the specified attribute
	 */
	public int getIntAttribute(final String attribute)
	{
		return getAttribute(attribute).getInt();
	}

	/**
	 * Retrieve a List attribute in an Object node. Equivalent to iterating on getAttribute(attribute)
	 * 
	 * @param attribute
	 *            The name of the attribute
	 * @return An iterable object corresponding to the List stored at the specified attribute
	 */
	public Iterable<Json> getListAttribute(final String attribute)
	{
		return getAttribute(attribute);
	}

	/**
	 * @return The string value of this node
	 */
	public String getString()
	{
		return aString;
	}

	/**
	 * Retrieve a String attribute in an Object node. Equivalent to getAttribute(attribute).getString()
	 * 
	 * @param attribute
	 *            The name of the attribute
	 * @return The String value stored at the specified attribute
	 */
	public String getStringAttribute(final String attribute)
	{
		return getAttribute(attribute).getString();
	}

	/**
	 * @return The type of this node
	 */
	public JsonType getType()
	{
		return aType;
	}

	/**
	 * Iterates over the Json nodes contained in a List node.
	 */
	@Override
	public Iterator<Json> iterator()
	{
		if (aType.equals(JsonType.LIST)) {
			return aList.iterator();
		}
		return null;
	}

	/**
	 * Set an attribute in an Object node
	 * 
	 * @param key
	 *            The name of the attribute
	 * @param element
	 *            A Json or Jsonable object (will be serialized to Json automatically)
	 * @return This (for chainability)
	 */
	public Json setAttribute(final String key, final Jsonable element)
	{
		aObject.put(key, element.toJson());
		return this;
	}

	/**
	 * Set an attribute in an Object node to a Float value
	 * 
	 * @param key
	 *            The name of the attribute
	 * @param element
	 *            The float value that the attribute should have
	 * @return This (for chainability)
	 */
	public Json setFloatAttribute(final String key, final float element)
	{
		return setAttribute(key, new Json(element));
	}

	/**
	 * Set an attribute in an Object node to an Integer value
	 * 
	 * @param key
	 *            The name of the attribute
	 * @param element
	 *            The integer value that the attribute should have
	 * @return This (for chainability)
	 */
	public Json setIntAttribute(final String key, final int element)
	{
		return setAttribute(key, new Json(element));
	}

	/**
	 * Set an attribute in an Object node to a List of Json nodes
	 * 
	 * @param key
	 *            The name of the attribute
	 * @param element
	 *            The list of nodes that the attribute should contain
	 * @return This (for chainability)
	 */
	public Json setListAttribute(final String key, final Collection<? extends Jsonable> elements)
	{
		return setAttribute(key, new Json(elements));
	}

	/**
	 * Set an attribute in an Object node to a String value
	 * 
	 * @param key
	 *            The name of the attribute
	 * @param element
	 *            The string value that the attribute should have
	 * @return This (for chainability)
	 */
	public Json setStringAttribute(final String key, final String element)
	{
		return setAttribute(key, new Json(element));
	}

	/**
	 * Json objects themselves implement Jsonable. This has no effects but makes method signatures much lighter, to avoid
	 * overloading methods with both Json and Jsonable arguments.
	 * 
	 * @return This
	 */
	@Override
	public Json toJson()
	{
		return this; // That was hard
	}

	/**
	 * Serialize this Json object to a human-readable Json string
	 * 
	 * @return A pretty Json string
	 */
	public String toPrettyString()
	{
		return toPrettyString("");
	}

	/**
	 * Serialize this Json object to a human-readable Json string
	 * 
	 * @param prefix
	 *            Prefix that will be prepended to each line
	 * @return A pretty Json string
	 */
	public String toPrettyString(final String prefix)
	{
		switch (aType) {
			case INTEGER:
			case FLOAT:
			case STRING:
				// Same as regular string representation
				return toString();
			case LIST:
				String str = "[";
				for (final Json j : aList) {
					str += "\n" + prefix + "\t" + j.toPrettyString(prefix + "\t") + ",";
				}
				return str.substring(0, str.length() - 1) + "\n" + prefix + "]";
			case OBJECT:
				String obj = "{";
				for (final String key : getAttributes()) {
					obj += "\n" + prefix + "\t" + JsonParser.sanitizeString(key) + ": "
							+ aObject.get(key).toPrettyString(prefix + "\t") + ",";
				}
				return obj.substring(0, obj.length() - 1) + "\n" + prefix + "}";
		}
		return "{}";
	}

	/**
	 * Serialize this Json object to a Json string.
	 * 
	 * @return A Json string.
	 */
	@Override
	public String toString()
	{
		switch (aType) {
			case INTEGER:
				return String.valueOf(aInt);
			case FLOAT:
				return String.valueOf(aFloat);
			case STRING:
				return JsonParser.sanitizeString(aString);
			case LIST:
				String str = "[";
				for (final Json j : aList) {
					str += j.toString() + ",";
				}
				return str.substring(0, str.length() - 1) + "]";
			case OBJECT:
				String obj = "{";
				for (final String key : getAttributes()) {
					obj += JsonParser.sanitizeString(key) + ":" + aObject.get(key) + ",";
				}
				return obj.substring(0, obj.length() - 1) + "}";
		}
		return "{}";
	}
}
