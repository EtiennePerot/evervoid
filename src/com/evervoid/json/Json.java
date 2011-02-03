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
		aList = new ArrayList<Json>();
		aList.addAll(list);
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

	public int getInt()
	{
		return aInt;
	}

	public String getString()
	{
		return aString;
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
