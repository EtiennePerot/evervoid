package com.evervoid.state;

public interface EVContainer<E>
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
	Iterable<E> elemIterator();

	/**
	 * Removes the element from the container.
	 * 
	 * @param e
	 *            The element to remove;
	 */
	boolean removeElem(final E e);
}