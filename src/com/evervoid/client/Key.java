package com.evervoid.client;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

public enum Key
{
	A, B, C, D, DIRECTION_DOWN, DIRECTION_LEFT, DIRECTION_RIGHT, DIRECTION_UP, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z;
	/**
	 * Get a Key enum value by its string value
	 * 
	 * @param mapping
	 *            The string of the mapping
	 * @return The Key enum value, or null if not found
	 */
	public static Key fromMapping(final String mapping)
	{
		try {
			return valueOf(mapping);
		}
		catch (final IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * Apply all mappings to the specified listener, using the specified InputManager.
	 * 
	 * @param manager
	 *            The input manager to use
	 * @param listener
	 *            The listener of all mappings
	 */
	public static void setMappings(final InputManager manager, final ActionListener listener)
	{
		for (final Key k : values()) {
			manager.addListener(listener, k.getMapping());
			manager.addMapping(k.getMapping(), k.getKeyTrigger());
		}
	}

	/**
	 * @return jMonkey keycode corresponding to the enum value, or null if not a valid key
	 */
	private Integer getJMonkeyKey()
	{
		// Yes, this is ugly, but this is a necessary evil in order to get around jMonkey's uglier KeyInput.
		switch (this) {
			case DIRECTION_DOWN:
				return KeyInput.KEY_DOWN;
			case DIRECTION_LEFT:
				return KeyInput.KEY_LEFT;
			case DIRECTION_RIGHT:
				return KeyInput.KEY_RIGHT;
			case DIRECTION_UP:
				return KeyInput.KEY_UP;
			case A:
				return KeyInput.KEY_A;
			case B:
				return KeyInput.KEY_B;
			case C:
				return KeyInput.KEY_C;
			case D:
				return KeyInput.KEY_D;
			case E:
				return KeyInput.KEY_E;
			case F:
				return KeyInput.KEY_F;
			case G:
				return KeyInput.KEY_G;
			case H:
				return KeyInput.KEY_H;
			case I:
				return KeyInput.KEY_I;
			case J:
				return KeyInput.KEY_J;
			case K:
				return KeyInput.KEY_K;
			case L:
				return KeyInput.KEY_L;
			case M:
				return KeyInput.KEY_M;
			case N:
				return KeyInput.KEY_N;
			case O:
				return KeyInput.KEY_O;
			case P:
				return KeyInput.KEY_P;
			case Q:
				return KeyInput.KEY_Q;
			case R:
				return KeyInput.KEY_R;
			case S:
				return KeyInput.KEY_S;
			case T:
				return KeyInput.KEY_T;
			case U:
				return KeyInput.KEY_U;
			case V:
				return KeyInput.KEY_V;
			case W:
				return KeyInput.KEY_W;
			case X:
				return KeyInput.KEY_X;
			case Y:
				return KeyInput.KEY_Y;
			case Z:
				return KeyInput.KEY_Z;
		}
		return null;
	}

	/**
	 * @return jMonkey KeyTriger corresponding to the enum value
	 */
	public KeyTrigger getKeyTrigger()
	{
		final Integer key = getJMonkeyKey();
		if (key == null) {
			return null;
		}
		return new KeyTrigger(key);
	}

	/**
	 * @return The name used for the jMonkey InputManager mapping
	 */
	public String getMapping()
	{
		return toString();
	}
}