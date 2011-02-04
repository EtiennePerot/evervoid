package com.evervoid.json;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Manager for all compiled regular expressions. Used in order to avoid recompiling the same regular expression twice.
 */
class Regex
{
	private static final Map<Regex, Pattern> sRegexPatterns = new HashMap<Regex, Pattern>();

	/**
	 * Get a compiled regular expression based on a pattern
	 * 
	 * @param regex
	 *            The string pattern of the regular expression
	 * @return The compiled regular expression
	 */
	static Pattern get(final String regex)
	{
		return get(regex, 0);
	}

	/**
	 * Get a compiled regular expression based on a pattern
	 * 
	 * @param regex
	 *            The string pattern of the regular expression
	 * @param flags
	 *            The flags to use for this regular expression
	 * @return The compiled regular expression
	 */
	static Pattern get(final String regex, final int flags)
	{
		final Regex r = new Regex(regex, flags);
		if (!sRegexPatterns.containsKey(r)) {
			sRegexPatterns.put(r, Pattern.compile(r.aRegex, r.aFlags));
		}
		return sRegexPatterns.get(r);
	}

	private final int aFlags;
	private final String aRegex;

	private Regex(final String regex, final int flags)
	{
		aRegex = regex;
		aFlags = flags;
	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	@Override
	public String toString()
	{
		return "Regex[" + aFlags + "/" + aRegex + "]";
	}
}
