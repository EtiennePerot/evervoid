package com.evervoid.utils;

/**
 * A simple wrapper Class containing a key-value pair.
 * 
 * @param <K>
 *            The Type which the key must extend
 * @param <V>
 *            The type which the value must extend
 */
public class Pair<K, V>
{
	/**
	 * The Key value
	 */
	private final K aKey;
	/**
	 * THe Value.
	 */
	private V aValue;

	/**
	 * Creates a Pair object with the given key/value combination.
	 * 
	 * @param key
	 *            The key of the pair.
	 * @param value
	 *            The value of the pair.
	 */
	public Pair(final K key, final V value)
	{
		aKey = key;
		aValue = value;
	}

	/**
	 * @return The Key
	 */
	public K getKey()
	{
		return aKey;
	}

	/**
	 * @return The Value
	 */
	public V getValue()
	{
		return aValue;
	}

	/**
	 * Changes the previous value to a new one, which cannot be null. If the element is null, the fuction breaks and returns
	 * false; The value to be set.
	 * 
	 * @param value
	 *            The value to set
	 * @return Whether the operation executed successfully.
	 */
	public boolean setValue(final V value)
	{
		if (value == null) {
			return false;
		}
		aValue = value;
		return true;
	}

	/**
	 * Format: "(key, value)"
	 * 
	 * @return The string representation of the Pair
	 */
	@Override
	public String toString()
	{
		return "(" + aKey + ", " + aValue + ")";
	}
}
