package com.evervoid.client.ui;

import com.evervoid.client.views.Bounds;
import com.evervoid.state.geometry.Dimension;

public interface Resizeable
{
	public Dimension getMinimumSize();

	public void setBounds(Bounds bounds);

	public String toString(String prefix);
}
