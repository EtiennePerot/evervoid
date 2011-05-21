package com.evervoid.state.observers;

import com.evervoid.state.EVGameState;

/**
 * StateObserver is a template for Objects wishing to observer an {@link EVGameState}. ECGameStates will broadcast to all its
 * observer when appropriate. The first parameter of broadcasting methods will be the State currentlyl broadcasting, in order to
 * allow Objects to observer multiple States at once.
 */
public interface StateObserver
{
}
