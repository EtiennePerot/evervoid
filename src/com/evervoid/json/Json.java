package com.evervoid.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Json implements Iterable<Json>
{
	public enum JsonType
	{
		FLOAT, INTEGER, LIST, OBJECT, STRING;
	}

	public static String sanitizeString(final String s)
	{
		return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "\\n") + "\"";
	}

	private float aFloat = 0f;
	private int aInt = 0;
	private List<Json> aList = null;
	private Map<String, Json> aObject = null;
	private String aString = "";
	private final JsonType aType;

	public Json()
	{
		aType = JsonType.OBJECT;
		aObject = new HashMap<String, Json>();
	}

	public Json(final Collection<Json> list)
	{
		aType = JsonType.LIST;
		aList = new ArrayList<Json>(list);
	}

	public Json(final double number)
	{
		this((float) number);
	}

	public Json(final float number)
	{
		aType = JsonType.FLOAT;
		aFloat = number;
	}

	public Json(final int integer)
	{
		aType = JsonType.INTEGER;
		aInt = integer;
	}

	public Json(final String str)
	{
		aType = JsonType.STRING;
		aString = str;
	}

	public Json getAttribute(final String attribute)
	{
		return aObject.get(attribute);
	}

	public float getFloat()
	{
		return aFloat;
	}

	public float getFloatAttribute(final String attribute)
	{
		return getAttribute(attribute).getFloat();
	}

	public int getInt()
	{
		return aInt;
	}

	public int getIntAttribute(final String attribute)
	{
		return getAttribute(attribute).getInt();
	}

	public Iterable<Json> getListAttribute(final String attribute)
	{
		return getAttribute(attribute);
	}

	public String getString()
	{
		return aString;
	}

	public String getStringAttribute(final String attribute)
	{
		return getAttribute(attribute).getString();
	}

	public JsonType getType()
	{
		return aType;
	}

	@Override
	public Iterator<Json> iterator()
	{
		if (aType.equals(JsonType.LIST)) {
			return aList.iterator();
		}
		return null;
	}

	public Json setAttribute(final String key, final Json element)
	{
		aObject.put(key, element);
		return this;
	}

	public Json setAttribute(final String key, final Jsonable element)
	{
		return setAttribute(key, element.toJson());
	}

	public Json setFloatAttribute(final String key, final float element)
	{
		return setAttribute(key, new Json(element));
	}

	public Json setIntAttribute(final String key, final int element)
	{
		return setAttribute(key, new Json(element));
	}

	public Json setListAttribute(final String key, final Collection<Json> elements)
	{
		return setAttribute(key, new Json(elements));
	}

	public Json setStringAttribute(final String key, final String element)
	{
		return setAttribute(key, new Json(element));
	}

	@Override
	public String toString()
	{
		switch (aType) {
			case INTEGER:
				return String.valueOf(aInt);
			case FLOAT:
				return String.valueOf(aFloat);
			case STRING:
				return sanitizeString(aString);
			case LIST:
				String str = "[";
				for (final Json j : aList) {
					str += j.toString() + ",";
				}
				return str.substring(0, str.length() - 1) + "]";
			case OBJECT:
				String obj = "{";
				for (final String key : aObject.keySet()) {
					obj += sanitizeString(key) + ":" + sanitizeString(aObject.get(key).toString()) + ",";
				}
				return obj.substring(0, obj.length() - 1) + "}";
		}
		return "{}";
	}
}
