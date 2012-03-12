package com.evervoid.state.action;

import static com.evervoid.state.action.Action.deserializeAction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.evervoid.json.Json;
import com.evervoid.json.Jsonable;
import com.evervoid.state.EVGameState;
import com.evervoid.utils.LoggerUtils;

/**
 * Represents a game turn. Holds a list of actions.
 */
public class Turn implements Jsonable, Iterable<Action>
{
    /**
     * The list of actions played during this turn
     */
    private final List<Action> aActions;

    /**
     * Default constructor, creates an empty turn.
     */
    public Turn()
    {
        aActions = new ArrayList<Action>();
    }

    /**
     * JSON constructor; rebuilds a Turn instance from a Json object
     * 
     * @param j
     *            The Json representation of a Turn
     * @param state
     *            The state on which this turn will be executed.
     */
    public Turn(final Json j, final EVGameState state)
    {
        aActions = new ArrayList<Action>();
        for (final Json action : j.getListAttribute("turns")) {
            try {
                aActions.add(deserializeAction(state, action));
            } catch (final IllegalEVActionException e) {
                LoggerUtils.severe("Got an illegal action serialization while constructing turn");
            } catch (final Exception e) {
                LoggerUtils.severe("Trying to instantiate an Action that does not exists. Maybe Server version out of sync with Client?");

                e.printStackTrace();
            }
        }
    }

    /**
     * Adds an action to the list of actions during this turn
     * 
     * @param action
     *            The action to add
     */
    public void addAction(final Action action)
    {
        aActions.add(action);
    }

    /**
     * Adds all the given actions to the action list in the order presented by their iterator.
     * 
     * @param actions
     *            The actions to add to the turn.
     * @return This Turn object, with the new actions added, for chainability.
     */
    public Turn addActions(final Collection<Action> actions)
    {
        for (final Action a : actions) {
            addAction(a);
        }
        return this;
    }

    /**
     * Adds all the actions in the parameter turn to the list of actions in this Turn.
     * 
     * @param turn
     *            This Turn with the new actions, for chainability.
     */
    public void addTurn(final Turn turn)
    {
        if (turn == null) {
            return;
        }
        for (final Action a : turn.getActions()) {
            addAction(a);
        }
    }

    @Override
    public Turn clone()
    {
        final Turn newTurn = new Turn();
        newTurn.aActions.addAll(aActions);
        return newTurn;
    }

    /**
     * Removes the given action from the turn if it is contained.
     * 
     * @param action
     *            The action to remove.
     */
    public void delAction(final Action action)
    {
        aActions.remove(action);
    }

    /**
     * Removes all the actions in the parameter turn from this one.
     * 
     * @param turn
     *            The Turn from which to pick the actions to remove.
     * @return This modified turn.
     */
    public Turn delActions(final Turn turn)
    {
        for (final Action a : turn.getActions()) {
            delAction(a);
        }
        return this;
    }

    /**
     * @return The list of Actions this Turn contains.
     */
    public List<Action> getActions()
    {
        return aActions;
    }

    /**
     * @param classTypes
     *            The types of the Actions to get.
     * @return A list of all actions in this turn that are instances of at least one of the passed classes.
     */
    public List<Action> getActionsOfType(final Class<?>... classTypes)
    {
        final List<Action> actions = new ArrayList<Action>(aActions.size() / 2 + 1);
        for (final Action act : aActions) {
            for (final Class<?> c : classTypes) {
                if (c.isInstance(act)) {
                    actions.add(act);
                }
            }
        }
        return actions;
    }

    @Override
    public Iterator<Action> iterator()
    {
        return aActions.iterator();
    }

    /**
     * Moves the Action from wherever it is in the list to the back of the list. If the action is not in the list
     * currently, it
     * is simply added to the back.
     * 
     * @param action
     *            The action to re-enqueue.
     */
    public void reEnqueueAction(final Action action)
    {
        delAction(action);
        addAction(action);
    }

    /**
     * Re-enqueues each Action in the parameter list, the queueing order is determined by the iterator of the list.
     * 
     * @param list
     *            The list of actions to re-enqueue.
     */
    public void reEnqueueActions(final List<Action> list)
    {
        for (final Action a : list) {
            reEnqueueAction(a);
        }
    }

    @Override
    public Json toJson()
    {
        final Json j = new Json();
        j.setListAttribute("turns", aActions);
        return j;
    }

    @Override
    public String toString()
    {
        String s = "";
        final String sep = "\n~ THEN ~\n";
        for (final Action a : aActions) {
            s += a + sep;
        }
        return s.substring(0, Math.max(0, s.length() - sep.length()));
    }
}
