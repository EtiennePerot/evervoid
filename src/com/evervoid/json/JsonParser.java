package com.evervoid.json;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses Json strings and returns Json objects. Regular usage: Json results = new JsonParser(jsonString).parse();
 */
public class JsonParser
{
	/**
	 * Matches a boolean
	 */
	private static Pattern sBooleanPattern = Regex.get("^true|false", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
	/**
	 * Matches float numbers
	 */
	private static Pattern sFloatPattern = Regex.get("^\\d+\\.\\d+");
	/**
	 * Matches integers
	 */
	private static Pattern sIntPattern = Regex.get("^\\d+");
	/**
	 * Matches the key part of a key -> value mapping ("key": value)
	 */
	private static Pattern sObjectKeyPattern = Regex.get("^(\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*\")\\s*:\\s*");
	/**
	 * Matches a double-quoted string
	 */
	private static Pattern sStringDoublePattern = Regex.get("^\"[^\"\\\\]*(?:\\\\.[^\"\\\\]*)*\"");
	/**
	 * Matches a single-quoted string
	 */
	private static Pattern sStringSinglePattern = Regex.get("^'[^'\\\\]*(?:\\\\.[^'\\\\]*)*'");

	/**
	 * Takes a double-quoted, escaped string and returns a plain string
	 * 
	 * @param s
	 *            The sanitized string
	 * @return The plain string
	 */
	public static String plainString(final String s)
	{
		return s.substring(1, s.length() - 1).replace("\\\\", "\\").replace("\\\"", "\"").replace("\r", "")
				.replace("\\n", "\n");
	}

	/**
	 * Takes a plain string and returns a double-quoted, escaped string
	 * 
	 * @param s
	 *            The string to sanitize
	 * @return The sanitized string
	 */
	public static String sanitizeString(final String s)
	{
		return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\r", "").replace("\n", "\\n") + "\"";
	}

	private final String aRawString;

	/**
	 * Creates a new Json parser
	 * 
	 * @param jsonString
	 *            The string that this parser is meant to parse
	 */
	JsonParser(final String jsonString)
	{
		aRawString = jsonString.trim();
	}

	/**
	 * Parses the string provided in the constructor
	 * 
	 * @return The Json object representing the string
	 */
	Json parse()
	{
		return parseString(aRawString).getJson();
	}

	/**
	 * Parses a string partially
	 * 
	 * @param str
	 *            The string to parse
	 * @return A parsing result object containing the parsed Json node and the length of the parsed portion of the String
	 */
	private JsonParsingResult parseString(final String str)
	{
		final String trimmed = str.trim();
		// Try float:
		final Matcher floatMatcher = sFloatPattern.matcher(trimmed);
		if (floatMatcher.find()) {
			return new JsonParsingResult(new Json(Float.valueOf(floatMatcher.group())), floatMatcher.group());
		}
		// Try int:
		final Matcher intMatcher = sIntPattern.matcher(trimmed);
		if (intMatcher.find()) {
			return new JsonParsingResult(new Json(Integer.valueOf(intMatcher.group())), intMatcher.group());
		}
		// Try boolean:
		final Matcher booleanMatcher = sBooleanPattern.matcher(trimmed);
		if (booleanMatcher.find()) {
			return new JsonParsingResult(new Json(Boolean.valueOf(booleanMatcher.group().toLowerCase())),
					booleanMatcher.group());
		}
		// Try string:
		final Matcher stringDoubleMatcher = sStringDoublePattern.matcher(trimmed);
		if (stringDoubleMatcher.find()) {
			return new JsonParsingResult(new Json(plainString(stringDoubleMatcher.group())), stringDoubleMatcher.group());
		}
		final Matcher stringSingleMatcher = sStringSinglePattern.matcher(trimmed);
		if (stringSingleMatcher.find()) {
			return new JsonParsingResult(new Json(plainString(stringSingleMatcher.group())), stringSingleMatcher.group());
		}
		// Try list:
		if (trimmed.startsWith("[")) {
			final int initialLength = trimmed.length();
			String list = trimmed.substring(1).trim();
			final List<Json> results = new ArrayList<Json>();
			while (!list.startsWith("]")) {
				final JsonParsingResult result = parseString(list);
				results.add(result.getJson());
				list = list.substring(result.getOffset()).trim();
				if (list.startsWith(",")) {
					list = list.substring(1).trim();
				}
			}
			return new JsonParsingResult(new Json(results), initialLength - list.length() + 1);
		}
		// Try object:
		if (trimmed.startsWith("{")) {
			final int initialLength = trimmed.length();
			String dict = trimmed.substring(1).trim();
			final Json node = new Json();
			while (!dict.startsWith("}")) {
				final Matcher keyMatch = sObjectKeyPattern.matcher(dict);
				if (keyMatch.find()) {
					dict = dict.substring(keyMatch.group().length());
					final JsonParsingResult result = parseString(dict);
					node.setAttribute(plainString(keyMatch.group(1)), result.getJson());
					dict = dict.substring(result.getOffset()).trim();
					if (dict.startsWith(",")) {
						dict = dict.substring(1).trim();
					}
				}
			}
			return new JsonParsingResult(node, initialLength - dict.length() + 1);
		}
		// If all fails, return a blank node
		return new JsonParsingResult(new Json(), str);
	}
}
