package com.evervoid.engine;

import java.util.Iterator;

public interface EverVoidContainer<E>
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
	 * returns an iterator over all the objects of Type T stored in the
	 * EverVoidContainer.
	 * 
	 * @return The iterator
	 */
	Iterator<E> getIterator();

	/**
	 * Removes the element from the container.
	 * 
	 * @param e
	 *            The element to remove;
	 */
	void removeElem(final E e);
}
