package com.evervoid.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class BiMap<T1, T2>
{
	private final Map<T1, T2> aMap1 = new HashMap<T1, T2>();
	private final Map<T2, T1> aMap2 = new HashMap<T2, T1>();

	public BiMap()
	{
	}

	public BiMap(final Map<T1, T2> pMap)
	{
		for (final T1 key : pMap.keySet()) {
			put(key, pMap.get(key));
		}
	}

	public void clear()
	{
		aMap1.clear();
		aMap2.clear();
	}

	public boolean contains(final Object obj)
	{
		return aMap1.containsKey(obj) || aMap2.containsKey(obj);
	}

	public T1 get1(final Object obj)
	{
		return aMap2.get(obj);
	}

	public T2 get2(final Object obj)
	{
		return aMap1.get(obj);
	}

	@SuppressWarnings("unchecked")
	public T1 getRandom1()
	{
		return (T1) MathUtils.getRandomElement(aMap1.keySet());
	}

	@SuppressWarnings("unchecked")
	public T2 getRandom2()
	{
		return (T2) MathUtils.getRandomElement(aMap2.keySet());
	}

	public boolean isEmpty()
	{
		return aMap1.isEmpty();
	}

	public Set<T1> keySet1()
	{
		return aMap1.keySet();
	}

	public Set<T2> keySet2()
	{
		return aMap2.keySet();
	}

	public void put(final T1 obj1, final T2 obj2)
	{
		aMap1.put(obj1, obj2);
		aMap2.put(obj2, obj1);
	}

	public void remove(final Object obj)
	{
		if (aMap1.containsKey(obj)) {
			aMap2.remove(aMap1.get(obj));
			aMap1.remove(obj);
		}
		else if (aMap2.containsKey(obj)) {
			aMap1.remove(aMap2.get(obj));
			aMap2.remove(obj);
		}
	}

	public int size()
	{
		return aMap1.size();
	}
}
