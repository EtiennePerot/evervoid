package com.evervoid.utils;

import com.evervoid.json.Jsonable;

/**
 * This interface simply wraps some common container functionality for ease of use within everVoid. Since the container must be
 * Jsonable, the elements themselves must be too.
 * 
 * @param <E>
 *            The type of the Objects to be contained
 */
public interface EVContainer<E extends Jsonable> extends Jsonable
{
	/**
	 * Adds the element e to the container.
	 * 
	 * @param e
	 *            The element to add.
	 * @return true if the element was successfully added.
	 */
	boolean addElem(final E e);

	/**
	 * Finds out if the given element is in the container.
	 * 
	 * @param e
	 *            The element to search for.
	 * @return true if the element is in the container.
	 */
	boolean containsElem(E e);

	/**
	 * Returns an iterable over all the objects of Type T stored in the EverVoidContainer.
	 * 
	 * @return The iterable
	 */
	Iterable<? extends E> elemIterator();

	/**
	 * @return The container ID.
	 */
	int getID();

	/**
	 * Removes the element from the container.
	 * 
	 * @param e
	 *            The element to remove;
	 * @return whether the operation was successful
	 */
	boolean removeElem(final E e);
}
