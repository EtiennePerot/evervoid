package com.evervoid.client.ui;

import com.evervoid.state.geometry.Dimension;
import com.jme3.math.Vector2f;

public interface Resizeable
{
	public Dimension getMinimumSize();

	public void offsetBy(Vector2f offset);

	public void sizeTo(Dimension dimension);

	public String toString(String prefix);
}
