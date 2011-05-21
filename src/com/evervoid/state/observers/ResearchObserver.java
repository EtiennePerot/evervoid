package com.evervoid.state.observers;

import com.evervoid.state.player.Research;

/**
 * ResearchObserver is a template for Objects wishing to observe {@link Research}. Researches will broadcast to all their
 * observer when appropriate. The first parameter of broadcasting methods will be the broadcasting Research, in order to allow
 * Objects to observer multiple Researches.
 */
public interface ResearchObserver
{
}
