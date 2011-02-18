package com.evervoid.state.prop;

public interface PropContainer
{
	/**
	 * Checks whether a Prop is in this container or not
	 * 
	 * @param p
	 *            The Prop to search for
	 * @return True if the Prop is in this container, false otherwise
	 */
	public boolean containsProp(final Prop p);

	/**
	 * Tells the container that the Prop is leaving it
	 * 
	 * @param prop
	 *            The Prop leaving the container
	 * @return Whether insertion was successful or not
	 */
	public boolean delProp(Prop prop);

	/**
	 * @return An Iterable over all the Props in this container
	 */
	public Iterable<Prop> getProps();

	/**
	 * Register the Prop in the container
	 * 
	 * @param prop
	 *            The Prop to register
	 * @return If insertion was successful or not
	 */
	public boolean addProp(Prop prop);
}
