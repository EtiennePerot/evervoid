package com.evervoid.utils;

public class Pair<K, V>
{
	private final K aKey;
	private V aValue;

	public Pair(final K key, final V value)
	{
		aKey = key;
		aValue = value;
	}

	public K getKey()
	{
		return aKey;
	}

	public V getValue()
	{
		return aValue;
	}

	public boolean setValue(final V value)
	{
		if (value == null) {
			return false;
		}
		aValue = value;
		return true;
	}

	@Override
	public String toString()
	{
		return "(" + aKey.toString() + ", " + aValue.toString() + ")";
	}
}
