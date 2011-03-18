package com.evervoid.client;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

public enum KeyboardKey
{
	A, APOSTROPHE, B, BACKSLASH, BACKSPACE, BACKTICK, C, COMMA, D, DIRECTION_DOWN, DIRECTION_LEFT, DIRECTION_RIGHT, DIRECTION_UP, DOWN, E, EIGHT, EIGHT_K, ENTER, EQUALS, ESCAPE, F, FIVE, FIVE_K, FOUR, FOUR_K, G, H, I, J, K, L, LEFT, LEFTBRACKET, LSHIFT, M, MINUS, MINUS_K, N, NINE, NINE_K, O, ONE, ONE_K, P, PERIOD, PERIOD_K, PLUS_K, Q, R, RIGHT, RIGHTBRACKET, RSHIFT, S, SEMICOLON, SEVEN, SEVEN_K, SIX, SIX_K, SLASH, SLASH_K, SPACE, STAR_K, T, THREE, THREE_K, TWO, TWO_K, U, UP, V, W, X, Y, Z, ZERO, ZERO_K;
	/**
	 * Get a Key enum value by its string value
	 * 
	 * @param mapping
	 *            The string of the mapping
	 * @return The Key enum value, or null if not found
	 */
	public static KeyboardKey fromMapping(final String mapping)
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
		for (final KeyboardKey k : values()) {
			manager.addListener(listener, k.getMapping());
			manager.addMapping(k.getMapping(), k.getKeyTrigger());
		}
	}

	private KeyboardKey()
	{
	}

	/**
	 * Returns the letter that this key corresponds to
	 * 
	 * @param uppercase
	 *            True to return the letter in uppercase
	 * @return The letter
	 */
	public String getCharacter(final boolean uppercase)
	{
		switch (this) {
			// Blacklist non-letters
			case DIRECTION_DOWN:
			case DIRECTION_LEFT:
			case DIRECTION_UP:
			case DIRECTION_RIGHT:
			case RSHIFT:
			case LSHIFT:
			case BACKSPACE:
			case ESCAPE:
			case ENTER:
			case LEFT:
			case RIGHT:
			case UP:
			case DOWN:
				return "";
			case SPACE:
				return " ";
				// Special cases
			case ZERO:
				if (uppercase) {
					return ")";
				}
			case ZERO_K:
				return "0";
			case ONE:
				if (uppercase) {
					return "!";
				}
			case ONE_K:
				return "1";
			case TWO:
				if (uppercase) {
					return "@";
				}
			case TWO_K:
				return "2";
			case THREE:
				if (uppercase) {
					return "#";
				}
			case THREE_K:
				return "3";
			case FOUR:
				if (uppercase) {
					return "$";
				}
			case FOUR_K:
				return "4";
			case FIVE:
				if (uppercase) {
					return "%";
				}
			case FIVE_K:
				return "5";
			case SIX:
				if (uppercase) {
					return "^";
				}
			case SIX_K:
				return "6";
			case SEVEN:
				if (uppercase) {
					return "&";
				}
			case SEVEN_K:
				return "7";
			case EIGHT:
				if (uppercase) {
					return "*";
				}
			case EIGHT_K:
				return "8";
			case NINE:
				if (uppercase) {
					return "(";
				}
			case NINE_K:
				return "9";
			case APOSTROPHE:
				if (uppercase) {
					return "\"";
				}
				return "'";
			case BACKTICK:
				if (uppercase) {
					return "~";
				}
				return "`";
			case COMMA:
				if (uppercase) {
					return "<";
				}
				return ",";
			case PERIOD:
				if (uppercase) {
					return ">";
				}
			case PERIOD_K:
				return ".";
			case SLASH:
				if (uppercase) {
					return "?";
				}
				return "/";
			case BACKSLASH:
				if (uppercase) {
					return "|";
				}
				return "\\";
			case MINUS:
				if (uppercase) {
					return "_";
				}
				return "-";
			case EQUALS:
				if (uppercase) {
					return "+";
				}
				return "=";
			case LEFTBRACKET:
				if (uppercase) {
					return "{";
				}
				return "[";
			case RIGHTBRACKET:
				if (uppercase) {
					return "}";
				}
				return "]";
			case SEMICOLON:
				if (uppercase) {
					return ":";
				}
				return ";";
			case SLASH_K:
				return "/";
			case STAR_K:
				return "*";
			case MINUS_K:
				return "-";
			case PLUS_K:
				return "+";
		}
		if (uppercase) {
			return toString().toUpperCase();
		}
		return toString().toLowerCase();
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
			case LSHIFT:
				return KeyInput.KEY_LSHIFT;
			case RSHIFT:
				return KeyInput.KEY_RSHIFT;
			case BACKSPACE:
				return KeyInput.KEY_BACK;
			case SPACE:
				return KeyInput.KEY_SPACE;
			case ENTER:
				return KeyInput.KEY_RETURN;
			case ZERO:
				return KeyInput.KEY_0;
			case ZERO_K:
				return KeyInput.KEY_NUMPAD0;
			case ONE:
				return KeyInput.KEY_1;
			case ONE_K:
				return KeyInput.KEY_NUMPAD1;
			case TWO:
				return KeyInput.KEY_2;
			case TWO_K:
				return KeyInput.KEY_NUMPAD2;
			case THREE:
				return KeyInput.KEY_3;
			case THREE_K:
				return KeyInput.KEY_NUMPAD3;
			case FOUR:
				return KeyInput.KEY_4;
			case FOUR_K:
				return KeyInput.KEY_NUMPAD4;
			case FIVE:
				return KeyInput.KEY_5;
			case FIVE_K:
				return KeyInput.KEY_NUMPAD5;
			case SIX:
				return KeyInput.KEY_6;
			case SIX_K:
				return KeyInput.KEY_NUMPAD6;
			case SEVEN:
				return KeyInput.KEY_7;
			case SEVEN_K:
				return KeyInput.KEY_NUMPAD7;
			case EIGHT:
				return KeyInput.KEY_8;
			case EIGHT_K:
				return KeyInput.KEY_NUMPAD8;
			case NINE:
				return KeyInput.KEY_9;
			case NINE_K:
				return KeyInput.KEY_NUMPAD9;
			case APOSTROPHE:
				return KeyInput.KEY_APOSTROPHE;
			case BACKTICK:
				return KeyInput.KEY_GRAVE;
			case COMMA:
				return KeyInput.KEY_COMMA;
			case PERIOD:
				return KeyInput.KEY_PERIOD;
			case PERIOD_K:
				return KeyInput.KEY_NUMPADCOMMA;
			case SLASH:
				return KeyInput.KEY_SLASH;
			case BACKSLASH:
				return KeyInput.KEY_BACKSLASH;
			case MINUS:
				return KeyInput.KEY_MINUS;
			case EQUALS:
				return KeyInput.KEY_EQUALS;
			case LEFTBRACKET:
				return KeyInput.KEY_LBRACKET;
			case RIGHTBRACKET:
				return KeyInput.KEY_RBRACKET;
			case SEMICOLON:
				return KeyInput.KEY_SEMICOLON;
			case SLASH_K:
				return KeyInput.KEY_DIVIDE;
			case STAR_K:
				return KeyInput.KEY_MULTIPLY;
			case MINUS_K:
				return KeyInput.KEY_SUBTRACT;
			case PLUS_K:
				return KeyInput.KEY_ADD;
			case ESCAPE:
				return KeyInput.KEY_ESCAPE;
			case LEFT:
				return KeyInput.KEY_LEFT;
			case RIGHT:
				return KeyInput.KEY_RIGHT;
			case UP:
				return KeyInput.KEY_UP;
			case DOWN:
				return KeyInput.KEY_DOWN;
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
	 * @return The letter that this key corresponds to, in lowercase
	 */
	public String getLetter()
	{
		return getCharacter(false);
	}

	/**
	 * @return The name used for the jMonkey InputManager mapping
	 */
	public String getMapping()
	{
		return toString();
	}

	/**
	 * @return Whether this key represents Shift or not
	 */
	public boolean isShift()
	{
		return equals(KeyboardKey.LSHIFT) || equals(KeyboardKey.RSHIFT);
	}
}