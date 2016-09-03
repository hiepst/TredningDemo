package com.hiep.common.util;

/**
 * Copyright 2002-2004 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Miscellaneous string utility methods. Mainly for internal use within the
 * framework; consider Jakarta's Commons Lang for a more comprehensive suite of
 * string utilities.
 *
 * <p>
 * This class delivers some simple functionality that should really be provided
 * by the core Java String and StringBuffer classes, such as the ability to
 * replace all occurrences of a given substring in a target string. It also
 * provides easy-to-use methods to convert between delimited strings, such as
 * CSV strings, and collections and arrays.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Keith Donald
 *
 * @since 16 April 2001
 * @see org.apache.commons.lang.StringUtils
 */
public abstract class StringUtils {

	public static final String DEFAULT_EMAIL_VALIDATION_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

	public static final String DEFAULT_PHONE_NUMBER_VALIDATION_PATTERN = "^.*[0-9].*$";

	public static final char NO_ESCAPE_CHAR = (char) 0;

	private static final Pattern XML_ENTITIES_PATTERN = Pattern.compile("\\&[\\w#\\d]+;");

	private static final Map<Character, String> NAMED_XML_ENTITIES = new HashMap<Character, String>();

	private static final Map<String, Character> NAMED_XML_ENTITIES_REVERSE = new HashMap<String, Character>();

	private static ExceptionListener defaultDecoderExceptionListener = null;

	public static final int BINARY_GROUPING = 4;

	static {
		NAMED_XML_ENTITIES.put('"', "&quot;");
		NAMED_XML_ENTITIES.put('&', "&amp;");
		NAMED_XML_ENTITIES.put('<', "&lt;");
		NAMED_XML_ENTITIES.put('>', "&gt;");
		NAMED_XML_ENTITIES.put('\'', "&#39;");
		for (Character ch : NAMED_XML_ENTITIES.keySet()) {
			NAMED_XML_ENTITIES_REVERSE.put(NAMED_XML_ENTITIES.get(ch), ch);
		}
	}

	private static final Collator COLLATOR = Collator.getInstance();

	/**
	 * An empty string.
	 */
	public static final String EMPTY_STRING = "";

	/**
	 * A new line character.
	 */
	public static final String NEW_LINE = System.getProperty("line.separator");

	/**
	 * File separator.
	 */
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");

	/**
	 * The String used to represent an ellipsis.
	 */
	public static final String ELLIPSIS = "...";

	private static final NumberFormat GIS_COORDINATE_FORMAT = DecimalFormat.getInstance();

	static {
		GIS_COORDINATE_FORMAT.setMaximumFractionDigits(6);
		GIS_COORDINATE_FORMAT.setRoundingMode(RoundingMode.HALF_UP);
	}

	// Default line length
	private static final int DEFAULT_LINE_LENGTH = 125;

	// public static boolean hasLength(String str) {
	// Use the spring library SpringUtils.hasLength() method

	/**
	 * Check if a String has text. More specifically, returns <code>true</code>
	 * if the string not <code>null<code>, it's <code>length is > 0</code>, and
	 * it has at least one non-whitespace character.
	 * <p>
	 * 
	 * <pre>
	 * StringUtils.hasText(null) = false
	 * StringUtils.hasText("") = false
	 * StringUtils.hasText(" ") = false
	 * StringUtils.hasText("12345") = true
	 * StringUtils.hasText(" 12345 ") = true
	 * </pre>
	 * 
	 * @param str
	 *            the String to check, may be null
	 * @return <code>true</code> if the String is not null, length > 0, and not
	 *         whitespace only
	 */
	public static boolean hasText(String str) {
		int strLen;

		if (str == null || (strLen = str.length()) == 0) {
			return false;
		}

		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Determine if a string contains a specified pattern.
	 * 
	 * Pattern a simple wild card string (the default) optionally embedding
	 * standard *nix glob wildcards asterisk (*) and question mark (?) where
	 * astrix matches zero or more characters and question mark matches exactly
	 * one character.
	 * 
	 * @param string
	 * @param pattern
	 */
	public static boolean isWildMatch(String string, String pattern) {
		/*
		 * if (pattern.length()==0) return string.length()==0; else { char
		 * c=pattern.charAt(0); if (c=='*') { if
		 * (isWildMatch(string,pattern.substring(1))) return true; } if
		 * (string.length()!=0) { if (c=='*') { return
		 * isWildMatch(string.substring(1),pattern); } if (c=='?' ||
		 * c==string.charAt(0)) return
		 * isWildMatch(string.substring(1),pattern.substring(1)); } return
		 * false; }
		 */
		// prevent a infinity loop for searching
		// String newPattern = pattern.replaceAll( "\\*", ".*" ).replaceAll(
		// "\\?", "." );
		if (pattern.length() == 0) {
			return string.length() == 0;
		}

		// Replace all regular expression special characters with escaped
		// characters
		StringBuffer outString = new StringBuffer();

		// Loop through characters and replace multi-char wild-card ("*") with
		// regular expression multi-char wild-card ("*.") and single char
		// wild-card ("?") with regular expression single char wild-card (".").
		// Also escape special regular expression characters.
		char ch;
		int len = pattern.length();
		for (int i = 0; i < len; i++) {

			ch = pattern.charAt(i);

			if (ch == '*') {
				outString.append(".*");
				continue;
			}

			if (ch == '?') {
				outString.append(".");
				continue;
			}

			if (ch == '[' || ch == '\\' || ch == '^' || ch == '$' || ch == '.' || ch == '|' || ch == '?' || ch == '*'
					|| ch == '+' || ch == '{' || ch == '}' || ch == '(' || ch == ')') {
				outString.append("\\");
			}
			outString.append(ch);

		}
		String newPattern = outString.toString();
		Pattern p = Pattern.compile(newPattern);
		Matcher m = p.matcher(string);
		return m.matches();
	}

	/**
	 * Determine if a string contains a specified pattern.
	 * <p>
	 * Pattern a simple wild card string (the default) optionally embedding
	 * standard *nix glob wildcards asterisk (*) and question mark (?) where
	 * astrix matches zero or more characters and question mark matches exactly
	 * one character.
	 * <p>
	 * If supportRegex is set to true, then the pattern can support regular
	 * expressions by prefixing the pattern with two left brackets "{{" (similar
	 * to the <code>MatchesName</code> registry name selector)
	 * 
	 * @param string
	 * @param pattern
	 * @param supportRegex
	 * @return
	 */
	public static boolean isWildMatch(String string, String pattern, boolean supportRegex) {

		if (!supportRegex) {
			return isWildMatch(string, pattern);
		}

		// Special regular expression notation allows user to specify a regular
		// expression
		// instead of glob (? and *) expression by beginning pattern with two
		// brackets: "{{".
		if (pattern.length() > 2 && pattern.charAt(0) == '{' && pattern.charAt(1) == '{') {
			String regularExpression = pattern.substring(2);
			Pattern patternObj = Pattern.compile(regularExpression);
			return patternObj.matcher(string).matches();
		} else {
			return isWildMatch(string, pattern);
		}

	}

	// public static int countOccurrencesOf(String s, String sub) {
	// Use the spring library SpringUtils.countOccurrencesOf() method

	/**
	 * Replace all occurences of a substring within a string with another
	 * string.
	 *
	 * @param inString
	 *            String to examine
	 * @param oldPattern
	 *            String to replace
	 * @param newPattern
	 *            String to insert
	 * @return a String with the replacements
	 */
	public static String replace(String inString, String oldPattern, String newPattern) {
		if (inString == null) {
			return null;
		}
		if (oldPattern == null || newPattern == null) {
			return inString;
		}

		StringBuffer sbuf = new StringBuffer();
		// output StringBuffer we'll build up
		int pos = 0; // Our position in the old string
		int index = inString.indexOf(oldPattern);
		// the index of an occurrence we've found, or -1
		int patLen = oldPattern.length();
		while (index >= 0) {
			sbuf.append(inString.substring(pos, index));
			sbuf.append(newPattern);
			pos = index + patLen;
			index = inString.indexOf(oldPattern, pos);
		}
		sbuf.append(inString.substring(pos));

		// remember to append any characters to the right of a match
		return sbuf.toString();
	}

	/**
	 * Take a String which is a delimited list and convert it to a String array.
	 * 
	 * @param s
	 *            String
	 * @param delim
	 *            delim (this will not be returned)
	 * @param escapeChar
	 *            character to use for escaping the delimiter (or the escapeChar
	 *            itself). Zero = no escapeChar
	 * @return an array of the tokens in the list
	 */
	public static String[] delimitedListToStringArray(String s, String delim, char escapeChar) {
		return delimitedListToStringArray(s, delim, escapeChar, false);
	}

	/**
	 * Take a String which is a delimited list and convert it to a String array.
	 * 
	 * @param s
	 *            String
	 * @param delim
	 *            delim (this will not be returned)
	 * @param escapeChar
	 *            character to use for escaping the delimiter (or the escapeChar
	 *            itself). Zero = no escapeChar
	 * @param treatEmptyTokenAsNull
	 *            If false, empty tokens (nothing between delimiter) will be
	 *            returned as an empty string, if true, empty tokens will be
	 *            returned as null.
	 * @return an array of the tokens in the list
	 */
	public static String[] delimitedListToStringArray(String s, String delim, char escapeChar,
			boolean treatEmptyTokenAsNull) {
		return delimitedListToStringArray(s, delim, escapeChar, treatEmptyTokenAsNull, true);
	}

	/**
	 * Take a String which is a delimited list and convert it to a String array.
	 * 
	 * @param s
	 *            String
	 * @param delim
	 *            delim (this will not be returned)
	 * @param escapeChar
	 *            character to use for escaping the delimiter (or the escapeChar
	 *            itself). Zero = no escapeChar
	 * @param treatEmptyTokenAsNull
	 *            If false, empty tokens (nothing between delimiter) will be
	 *            returned as an empty string, if true, empty tokens will be
	 *            returned as null.
	 * @param trim
	 *            If true, leading and trailing whitespace will be removed from
	 *            each string element
	 * @return an array of the tokens in the list
	 */
	public static String[] delimitedListToStringArray(String s, String delim, char escapeChar,
			boolean treatEmptyTokenAsNull, boolean trim) {

		if (s == null) {
			return new String[0];
		}

		if (delim == null || delim.isEmpty()) {
			return new String[] { s };
		}

		if (!hasText(s)) {
			return new String[0];
		}

		List<String> tokens = new ArrayList<String>();

		StringBuilder buf = new StringBuilder(s.length());

		int pos = 0;
		int delimPos = 0;
		boolean done = false;
		boolean escape = false;
		char ch;
		String token;

		while (!done) {

			// if a delimiter is found, step over it
			while (delimPos < delim.length() && (pos + delimPos) < s.length()
					&& s.charAt(pos + delimPos) == delim.charAt(delimPos)) {
				delimPos++;
			}

			// If a delimiter was found
			if (delimPos >= delim.length()) {

				if (escape) {
					buf.append(delim);
					pos += delimPos;
					escape = false;
				} else {
					token = buf.toString();
					if (trim) {
						token = token.trim();
					}
					if (!hasText(token)) {
						if (treatEmptyTokenAsNull) {
							tokens.add(null);
						}
					} else {
						String tokenToAdd = buf.toString();
						if (trim) {
							tokenToAdd = tokenToAdd.trim();
						}
						tokens.add(tokenToAdd);
					}
					buf.delete(0, buf.length());
					pos += delimPos;
				}

				delimPos = 0;

			} else {

				delimPos = 0;

				ch = s.charAt(pos);
				if (ch == escapeChar) {
					if (escape) {
						buf.append(escapeChar);
						escape = false;
					} else {
						escape = true;
					}
				} else {
					buf.append(ch);
				}

				pos++;

			}

			if (pos >= s.length()) {
				done = true;
			}

		}

		token = buf.toString();
		if (trim) {
			token = token.trim();
		}
		if (!hasText(token)) {
			if (treatEmptyTokenAsNull) {
				tokens.add(null);
			}
		} else {
			String tokenToAdd = buf.toString();
			if (trim) {
				tokenToAdd = tokenToAdd.trim();
			}
			tokens.add(tokenToAdd);
		}

		return (String[]) tokens.toArray(new String[tokens.size()]);

	}

	// public static String[] commaDelimitedListToStringArray(String s) {
	// Use the spring library SpringUtils.commaDelimitedListToStringArray()
	// method

	// public static Set commaDelimitedListToSet(String s) {
	// Use the spring library SpringUtils.collectionToDelimitedString() method

	/**
	 * Convenience method to return a String array as a delimited (e.g. CSV)
	 * String. E.g. useful for toString() implementations.
	 * 
	 * @param arr
	 *            array to display. Elements may be of any type (toString will
	 *            be called on each element).
	 * @param delim
	 *            delimiter to use (probably a ,)
	 */
	public static String arrayToDelimitedString(Object[] arr, String delim) {
		return arrayToDelimitedString(arr, delim, NO_ESCAPE_CHAR, "null");
	}

	public static String arrayToDelimitedString(Object[] arr, String delim, char escapeChar, String nullOrEmptyValue) {

		if (arr == null || arr.length == 0) {
			return nullOrEmptyValue;
		} else {

			String escapeString = null;
			String escapedEscapeString = null;
			String escapedDelim = null;

			if (escapeChar != NO_ESCAPE_CHAR) {
				escapeString = Character.toString(escapeChar);
				escapedEscapeString = escapeString + escapeString;
				escapedDelim = escapeString + delim;
			}

			StringBuffer sb = new StringBuffer();
			String s;
			for (int i = 0; i < arr.length; i++) {
				if (i > 0) {
					sb.append(delim);
				}
				if (arr[i] instanceof Enum) {
					s = ((Enum<?>) arr[i]).name();
				} else {
					s = arr[i] == null ? null : arr[i].toString();
				}
				if (s != null && escapeString != null) {
					s = replace(s, escapeString, escapedEscapeString);
					s = replace(s, delim, escapedDelim);
				}
				if (s != null) {
					sb.append(s);
				} else if (nullOrEmptyValue != null) {
					sb.append(nullOrEmptyValue);
				}
			}

			return sb.toString();

		}
	}

	// public static String collectionToDelimitedString(Collection c, String
	// delim, String prefix, String suffix) {
	// Use the spring library SpringUtils.collectionToDelimitedString() method

	// public static String collectionToDelimitedString(Collection c, String
	// delim) {
	// Use the spring library SpringUtils.collectionToDelimitedString() method

	/**
	 * Convenience method to return a String array as a CSV String. E.g. useful
	 * for toString() implementations.
	 * 
	 * @param arr
	 *            array to display. Elements may be of any type (toString will
	 *            be called on each element).
	 */
	public static String arrayToCommaDelimitedString(Object[] arr) {
		return arrayToDelimitedString(arr, ",");
	}

	// public static String collectionToCommaDelimitedString(Collection c) {
	// Use the spring library SpringUtils.collectionToCommaDelimitedString()
	// method

	// public static String[] addStringToArray(String[] arr, String s) {
	// Use the spring library SpringUtils.addStringToArray() method

	// public static String unqualify(String qualifiedName) {
	// Use the spring library SpringUtils.unqualify() method

	// public static String unqualify(String qualifiedName, char separator) {
	// Use the spring library SpringUtils.unqualify() method

	// public static String capitalize(String str) {
	// Use the spring library SpringUtils.capitalize() method

	// public static String uncapitalize(String str) {
	// Use the spring library SpringUtils.uncapitalize() method

	// public static String cleanPath(String path) {
	// Use the spring library SpringUtils.cleanPath() method

	// public static boolean pathEquals(String path1, String path2) {
	// Use the spring library SpringUtils.pathEquals() method

	/**
	 * Capitalizes the first character in any string if the character can be
	 * capitalized. Useful in determining class names from variables with the
	 * same name.
	 * 
	 * @param in
	 *            - string whose first character is to be capitalized.
	 * @return string with first character uncapitalized
	 */
	public static String capitalizeFirstChar(String in) {
		StringBuffer sb = new StringBuffer();

		sb.append(Character.toUpperCase(in.charAt(0)));
		sb.append(in.substring(1));

		return sb.toString();
	}

	/**
	 * Uncapitalizes the first character in any string if the character can be
	 * uncapitalized. Useful in determining property names from classes with the
	 * same name.
	 * 
	 * @param in
	 *            - string whose first character is to be uncapitalized.
	 * @return string with first character capitalized
	 */
	public static String uncapitalizeFirstChar(String in) {
		StringBuffer sb = new StringBuffer();

		sb.append(Character.toLowerCase(in.charAt(0)));
		sb.append(in.substring(1));

		return sb.toString();
	}

	/**
	 * Convenience function used to trim user input that returns null if the
	 * input is null.
	 *
	 * @param toTrim
	 *            the string to be trimmed.
	 * @return null if toTrim is null or toTrim.trim() if it is not null.
	 */
	public static String trim(String toTrim) {
		return (toTrim != null ? toTrim.trim() : null);
	}

	/**
	 * Breaks a long String into multiple lines of maximum line length
	 * DEFAULT_LINE_LENGTH.
	 *
	 * @param string
	 *            the String to break into multiple lines.
	 */
	public static String breakIntoLines(String string) {
		return breakIntoLines(string, DEFAULT_LINE_LENGTH);
	}

	/**
	 * Breaks a long String into multiple lines of maximum line length
	 * <i>maxLineLength</i>.
	 * <p/>
	 * If the String is less than or equal to maximum line length, the given
	 * string is returned unchanged. If the String is greater than
	 * maxLineLength, the String is broken into lines on the last whitespace
	 * character that is less than maxLineLength.
	 *
	 * @param string
	 *            the String to break into multiple lines.
	 * @param maxLineLength
	 *            the maximum length of any line returned by this method.
	 */
	public static String breakIntoLines(String string, int maxLineLength) {

		Assert.isTrue(maxLineLength > 0, "The maximum line length must be " + "greater than zero.");

		if (string == null || string.length() == 0) {
			return string;
		}

		if (string.length() <= maxLineLength) {
			return string;
		}

		// The StringBuffer built with multiple lines
		StringBuffer newString = new StringBuffer();

		// The String remaining to be processed.
		String remainingString = string;
		while (remainingString.length() > maxLineLength) {

			int lineIndex = maxLineLength - 1;

			// If the character at the line length is
			char curChar = remainingString.charAt(lineIndex);
			while (!Character.isWhitespace(curChar) && lineIndex > 0) {
				lineIndex = lineIndex - 1;
				curChar = remainingString.charAt(lineIndex);
			}

			// If there was no whitespace in the remainingString up to the
			// max line length, we will just break the "word" at the max
			// line length mark
			if (lineIndex == 0) {
				lineIndex = maxLineLength;
			}

			String newLine = remainingString.substring(0, lineIndex).trim();
			newString.append(newLine);
			newString.append(NEW_LINE);
			remainingString = remainingString.substring(lineIndex + 1, remainingString.length());
		}

		// Add any remaining string to the newString
		if (remainingString.length() > 0) {
			newString.append(remainingString);
		}

		return newString.toString();
	}

	/**
	 * Converts camel back notation to underscore notation.
	 * <p>
	 * For example: converts "oneTwoThree" to "ONE_TWO_THREE" input can be
	 * first-letter capitalized or not.
	 *
	 * @param camelNotatedString
	 *            The camel notation string.
	 * @param upperCaseUnderscoring
	 *            true - output will be all caps (e.g. "ONE_TWO_THREE") false -
	 *            output will be all lower case (e.g. "one_two_three").
	 *
	 * @return The converted underscore notation.
	 */
	public static String camelToUnderscoreNotation(String camelNotatedString, boolean upperCaseUnderscoring) {

		int strLen;
		if (camelNotatedString == null || (strLen = camelNotatedString.length()) == 0) {
			return camelNotatedString;
		}

		StringBuffer buf = new StringBuffer(strLen * 2);

		char ch;
		for (int i = 0; i < strLen; i++) {

			ch = camelNotatedString.charAt(i);

			if (i != 0 && Character.isUpperCase(ch)) {
				buf.append('_');
			}

			if (upperCaseUnderscoring) {
				buf.append(Character.toUpperCase(ch));
			} else {
				buf.append(Character.toLowerCase(ch));
			}

		}

		return buf.toString();

	}

	/**
	 * Converts underscore notation to camel back notation.
	 * <p>
	 * For example: converts "ONE_TWO_THREE" to "oneTwoThree"
	 *
	 * @param underscoreNotatedString
	 *            The underscore notation string.
	 * @param capitalizeFirstCharacter
	 *            true - capitalize the first character (e.g. "OneTwoThree")
	 *            false - lower-case-italize the first character (e.g.
	 *            "oneTwoThree").
	 *
	 * @return The converted underscore notation.
	 */
	public static String underscoreToCamelNotation(String underscoreNotatedString, boolean capitalizeFirstCharacter) {

		int strLen;
		if (underscoreNotatedString == null || (strLen = underscoreNotatedString.length()) == 0) {
			return underscoreNotatedString;
		}

		StringBuffer buf = new StringBuffer(strLen);

		char ch;
		boolean nextIsUpper = capitalizeFirstCharacter;
		for (int i = 0; i < strLen; i++) {

			ch = underscoreNotatedString.charAt(i);

			if (ch == '_') {
				nextIsUpper = true;
			} else {
				if (nextIsUpper) {
					buf.append(Character.toUpperCase(ch));
					nextIsUpper = false;
				} else {
					buf.append(Character.toLowerCase(ch));
				}
			}

		}

		return buf.toString();

	}

	/**
	 * Replaces white space with underscores
	 * <p>
	 * For example: converts "One Two Three" to "One_Two_Three" Leading and
	 * trailing white space will be trimmed.
	 *
	 * @param spacedString
	 *            String with spaces
	 *
	 * @return The converted underscore notation.
	 */
	public static String spaceToUnderscoreNotation(String spacedString) {

		if (!hasText(spacedString)) {
			return null;
		}

		return spacedString.trim().replaceAll("\\s+", "_");

	}

	/**
	 * Makes a String from the given strings by concatinating them together.
	 * <p>
	 * This method has some overhead over making your own StringBuilder in line
	 * with your code and, therefore, may be a little slower (about three times
	 * as slow - see StringBuilderTest.main test). However, this is still far
	 * quicker than appending String together and provides a more readable
	 * format<br>
	 * Example:<br>
	 * <code>StringUtils.makeString( "Hello ", "world", "." );</code><br>
	 * is easier to read than<br>
	 * <code>
	 * StringBuilder s = new StringBuilder();<br>
	 * s.append( "Hello " );<br>
	 * s.append( "world" );<br>
	 * s.append( "." );<br>
	 * </code>
	 *
	 * @param strings
	 * @return
	 */
	public static String makeString(String... strings) {

		if (strings == null) {
			return null;
		}

		if (strings.length == 0) {
			return EMPTY_STRING;
		}

		StringBuilder stringBuilder = new StringBuilder();
		for (String string : strings) {
			stringBuilder.append(string);
		}

		return stringBuilder.toString();

	}

	public static void appendToStringBuilder(StringBuilder stringBuilder, String... strings) {

		if (strings == null) {
			return;
		}

		if (strings.length == 0) {
			return;
		}

		for (String string : strings) {
			stringBuilder.append(string);
		}

	}

	/**
	 * Converts a property name or other camel notation key into human readable
	 * form.<br>
	 * - First letter is capitalized<br>
	 * - Each new capital letter found has a space inserted before it and is
	 * converted to upper-case with the assumption that this is a new word.<br>
	 * - Underscores are replaced with spaces.<br>
	 * - Other non-alphanumeric characters are left as delimiters between words
	 * (i.e. no space is inserted to deliniate words). - Number sequences are
	 * separated into their own words.<br>
	 * - Acronyms are treated as their own word and left capitalized
	 * <p>
	 * Example:<br>
	 * <code>createDisplayString( "myPropertyNAME^Number365" );</code><br>
	 * Will result in:<br>
	 * <code>"My Property NAME^Number 365"</code> <br>
	 *
	 * @param propertyName
	 * @return
	 */
	public static String createDisplayString(String propertyName) {

		// Start with this mode
		final int START = 0;
		// A word with only letters all lower-case except the first
		final int WORD1 = 1;
		// Another word with only letters all lower-case except the first
		// WORD1 and WORD2 are alternated so as to detect a transition
		// from one word to another delimited by a capital letter
		final int WORD2 = 2;
		// An acronym (All caps)
		final int ACRONYM = 3;
		// A number (only digits)
		final int NUMBER = 4;
		// Space, tab, etc or underscore which gets converted to a space
		final int WHITESPACE = 5;

		StringBuffer buf = new StringBuffer();

		int mode = START;
		int length = propertyName.length();

		// Step through each letter
		for (int i = 0; i < length; ++i) {

			// Detect what the new mode should be.
			int newMode = -1;

			char currentChar = propertyName.charAt(i);

			// Don't try and alter a resource with Chinese, Japanese, or Korean
			// characters.
			if ((Character.UnicodeBlock.of(currentChar) == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)) {
				return propertyName;
			}

			// Convert underscore to a space
			if (currentChar == '_') {
				currentChar = ' ';
			}

			// Get the next character after the current if any
			char nextChar = 0;
			if (i + 1 < length) {
				nextChar = propertyName.charAt(i + 1);
			}

			// If this is a digit, new mode is NUMBER
			if (Character.isDigit(currentChar)) {
				newMode = NUMBER;
			}
			// If this is whitespace (underscore already converted to
			// whitespace)
			// or other non alphanumeric character (e.g. ^)
			else if (!Character.isLetterOrDigit(currentChar)) {
				newMode = WHITESPACE;
			}
			// If this letter is upper case - may designate a new word
			// or a new acronym or a continuation of an acronym
			else if (Character.isUpperCase(currentChar)) {
				// If next character is a capital (acronym requires two capital
				// letters in a row) or if we were already
				// processing an acronym (and next character is not lower case),
				// then we will continue to be an acronym
				// Note, if we were already processing an acronym, but come
				// accross an upper-case
				// letter followed by a lower case letter, then we must assume
				// this upper-case letter
				// is the start of the next word. E.g. "BARTTrain" should be
				// "BART Train"
				if (Character.isUpperCase(nextChar) || (mode == ACRONYM && !Character.isLowerCase(nextChar))) {
					newMode = ACRONYM;
				}
				// Else, this is the start of a new word, toggle to or from
				// WORD1/WORD2
				else {
					newMode = mode == WORD1 ? WORD2 : WORD1;
				}
			}
			// Else this is a lower case letter, new mode has to be
			// either a continuation of the previous word - in which case mode
			// will stay the same
			// or transitioning from any other non-word mode, in which case
			// WORD1 mode will be chosen
			else {
				newMode = mode == WORD2 ? WORD2 : WORD1;
			}

			// If transitioning between modes (but not to whitespace)
			if (newMode != WHITESPACE && mode != newMode) {
				// If not previously whitespace and not the start of the string
				// then insert a whitespace (i.e. transitioning from
				// one word type to another)
				if (mode != WHITESPACE && mode != START) {
					buf.append(' ');
				}
				// If transitioning from one normal word to another, capitalize
				// the first letter
				if (newMode == WORD1 || newMode == WORD2) {
					currentChar = Character.toUpperCase(currentChar);
				}
			}

			buf.append(currentChar);

			mode = newMode;

		}

		return buf.toString();

	}

	/**
	 * Returns the common prefix shared by the two given strings. For example,
	 * "AAAAB" and "AAAAC" would return "AAAA".
	 * <p>
	 * If the strings are completely different (no shared prefix), then null is
	 * returned (as opposed to an empty string).
	 * <p>
	 * The removeTrailingSymbols parameter will remove any trailing non-alpha
	 * and non-digit characters from the prefix. For example: "AAAA-B" and
	 * "AAAA-C" will return "AAAA" if removeTrailingSymbols is true and "AAAA-"
	 * if false.
	 *
	 * @param string1
	 * @param string2
	 * @param removeTrailingSymbols
	 * @return
	 */
	public static String getCommonPrefix(String string1, String string2, boolean removeTrailingSymbols) {

		if (string1 == null || string2 == null) {
			return null;
		}

		if (!hasText(string1) || !hasText(string2)) {
			return null;
		}

		int count = Math.min(string1.length(), string2.length());

		StringBuilder str = new StringBuilder();
		for (int i = 0; i < count; i++) {

			if (string1.charAt(i) == string2.charAt(i)) {
				str.append(string1.charAt(i));
			} else {
				break;
			}

		}

		if (removeTrailingSymbols) {
			char ch;
			int lastIndex;
			while (str.length() > 0) {

				lastIndex = str.length() - 1;

				ch = str.charAt(lastIndex);

				if (!Character.isLetterOrDigit(ch)) {
					str.deleteCharAt(lastIndex);
				} else {
					break;
				}

			}
		}

		return str.length() == 0 ? null : str.toString();

	}

	/**
	 * Pads (or trims) the given string to the given length with spaces.
	 *
	 * @param str
	 * @param length
	 * @param rightAlign
	 * @return
	 */
	public static String pad(String str, int length, boolean rightAlign) {

		if (str == null) {
			str = EMPTY_STRING;
		}

		if (str.length() > length) {
			return str.substring(0, length);
		}

		StringBuilder strBuilder = new StringBuilder();

		if (!rightAlign) {
			strBuilder.append(str);
		}

		for (int i = str.length(); i < length; i++) {
			strBuilder.append(" ");
		}

		if (rightAlign) {
			strBuilder.append(str);
		}

		return strBuilder.toString();

	}

	/**
	 * Removes leading and trailing white space and replaces any inner white
	 * space sequences with a single space (e.g. two or more spaces, tabs, etc
	 * will be replaced with a single space).
	 * 
	 * @param inputString
	 * @return The a copy of the input string with extra white space removed.
	 */
	public static String removeExtraWhiteSpace(String inputString) {

		return inputString.trim().replaceAll("\\s+", " ");

	}

	/**
	 * Convert given Number to a nice string that is rounded to a reasonable
	 * number of digits. Useful when it is not necessary to have a precision
	 * greater than 3-5 digits for the given number for display in a GUI.
	 * <p>
	 * This is particularly useful for point values that have excessive digit
	 * precision need to be displayed in a GUI, event message, alarm message,
	 * etc.
	 */
	public static String numberToRoundedString(Number number) {

		NumberFormat formatter = NumberFormat.getNumberInstance();
		formatter.setMaximumFractionDigits(Integer.MAX_VALUE);

		// Round doubles to a reasonable number of decimal places.
		if (number instanceof Double) {

			double d = number.doubleValue();
			// If greater than 10,000 then round to integer.
			if (d > 10000d) {
				return formatter.format((double) Math.round(d));
			}

			// Else, less than 10,000. Convert to BigDecimal so we can
			// round to a specified precision.
			else {
				// Precision is 4 significant digits unless value is
				// greater than or equal to 1000 which will be 5 digits.
				// changed precision always 5 digits.
				int precision = 5;
				/*
				 * if( d >= 1000d ) { precision = 5; }
				 */
				BigDecimal bigDecimal = new BigDecimal(d, new MathContext(precision, RoundingMode.HALF_UP));
				return formatter.format(bigDecimal.doubleValue());
			}

		} else if (number instanceof Float) {

			float f = number.floatValue();

			// If greater than 10,000 then round to integer.
			if (f > 10000f) {
				return formatter.format((double) Math.round(f));
			}

			// Else, less than 10,000. Convert to BigDecimal so we can
			// round to a specified precision.
			else {
				// Precision is 4 significant digits unless value is
				// greater than or equal to 1000 which will be 5 digits.
				// changed precision always 5 digits.
				int precision = 5;
				/*
				 * if( f >= 1000f ) { precision = 5; }
				 */
				BigDecimal bigDecimal = new BigDecimal(f, new MathContext(precision, RoundingMode.HALF_UP));
				return formatter.format(bigDecimal.doubleValue());
			}

		}

		// If integer, long, short, convert to long and format as a Long
		return formatter.format(number.longValue());

	}

	/**
	 * Parses a string to an integer using an internationalized formatter.
	 * 
	 * @param string
	 *            The string to parse
	 * @param defaultValue
	 *            A default value to use if the string cannot be parsed
	 * @return The parsed value
	 */
	public static int stringToInteger(String string, int defaultValue) {

		if (!hasText(string)) {
			return defaultValue;
		}

		NumberFormat formatter = DecimalFormat.getIntegerInstance();
		formatter.setMaximumFractionDigits(0);

		try {
			return formatter.parse(string).intValue();
		} catch (ParseException e) {
			return defaultValue;
		}

	}

	/**
	 * Converts a collection to a CSV string.
	 * <p>
	 * Mostly for logging purposes, as nulls and empty collections are treated
	 * and represented in the string.
	 * <p>
	 * A null collection returns "NullCollection"<br>
	 * An empty collection returns "EmptyCollection"<br>
	 * Any null values are represented by "Null"<br>
	 * 
	 * @param stringCollection
	 *            The string collection to convert
	 * @return A string representing the collection via comma separated values.
	 */
	public static String collectionToCsvString(Collection<?> stringCollection) {
		return collectionToCsvString(stringCollection, false);
	}

	/**
	 * Converts a collection to a CSV string.
	 * <p>
	 * Mostly for logging purposes, as nulls and empty collections are treated
	 * and represented in the string.
	 * <p>
	 * if returnEmptyOrNullCollectionsAsNull is false, then the following
	 * Strings are returned: <br>
	 * A null collection returns "NullCollection"<br>
	 * An empty collection returns "EmptyCollection"<br>
	 * Any null values are represented by "Null"<br>
	 * 
	 * @param stringCollection
	 *            The string collection to convert
	 * @param returnEmptyOrNullCollectionsAsNull
	 *            If the given collection is null or empty, return null string.
	 *            Also, null values are returned with no value between commas.
	 * @return A string representing the collection via comma separated values.
	 */
	public static String collectionToCsvString(Collection<?> stringCollection,
			boolean returnEmptyOrNullCollectionsAsNull) {

		if (stringCollection == null) {
			return returnEmptyOrNullCollectionsAsNull ? null : "NullCollection";
		}

		if (stringCollection.size() == 0) {
			return returnEmptyOrNullCollectionsAsNull ? null : "EmptyCollection";
		}

		StringBuffer sb = new StringBuffer();
		boolean first = true;
		for (Object o : stringCollection) {
			if (!first) {
				sb.append(',');
			}
			first = false;
			if (o == null) {
				if (returnEmptyOrNullCollectionsAsNull) {
					sb.append("Null");
				}
			} else {
				sb.append(o.toString());
			}
		}

		return sb.toString();

	}

	/**
	 * Reverses the collection->csv process. May mishandle strings with commas
	 * embedded
	 * 
	 * @param stringCollection
	 *            The string collection to convert
	 * @param returnEmptyOrNullCollectionsAsNull
	 *            If the given collection is null or empty, return null string.
	 *            Also, null values are returned with no value between commas.
	 * @return A string representing the collection via comma separated values.
	 */
	public static Collection<String> csvStringToCollection(String csvString) {
		Collection<String> collection = new ArrayList<String>();

		if (csvString == null || csvString.equals("NullCollection") || csvString.equals("EmptyCollection")) {
			return collection;
		}

		StringTokenizer tok = new StringTokenizer(csvString, ",");
		while (tok.hasMoreTokens()) {
			String s = tok.nextToken();
			// if s.equals( "Null" ) - ignore
			collection.add(s);
		}

		return collection;

	}

	public static String clipStringToSize(String string, int maxLength) {
		if (string == null) {
			return null;
		}

		if (string.length() > maxLength) {
			if (maxLength >= ELLIPSIS.length()) {
				return string.substring(0, maxLength - ELLIPSIS.length()) + ELLIPSIS;
			} else {
				return ELLIPSIS.substring(0, maxLength);
			}
		}

		return string;
	}

	/**
	 * Converts a string with possible invalid XML characters (&<>'") to a valid
	 * XML string value with escaped entities (e.g. " is converted to: &quot;).
	 * Only those characters that are "predeclared" entities in the XML spec are
	 * converted - any other characters should be valid in XML as long as the
	 * file used UTF-8 encoding (unless we need to one day support UTF-16 or
	 * higher?).
	 * 
	 * @param fromString
	 *            The Java string to convert to XML
	 * @return A valid XML string with characters escapced as XML entities.
	 */
	public static String toXmlString(String fromString) {

		StringBuilder str = new StringBuilder();
		char ch;
		String replace;
		for (int i = 0; i < fromString.length(); i++) {
			ch = fromString.charAt(i);
			replace = NAMED_XML_ENTITIES.get(Character.valueOf(ch));
			if (replace == null) {
				str.append(ch);
			} else {
				str.append(replace);
			}
		}

		return str.toString();

	}

	/**
	 * Converts an escaped (with entities) XML string to a Java string.
	 * 
	 * @param fromString
	 *            The XML string to convert
	 * @return The escaped XML string.
	 */
	public static String fromXmlString(String fromString) {

		Matcher matcher = XML_ENTITIES_PATTERN.matcher(fromString);

		StringBuilder str = new StringBuilder();

		int index = 0;
		String entity;
		Character ch;
		while (index < fromString.length() && matcher.find(index)) {

			if (index < matcher.start()) {
				str.append(fromString.substring(index, matcher.start()));
			}
			entity = fromString.substring(matcher.start(), matcher.end());

			ch = NAMED_XML_ENTITIES_REVERSE.get(entity);
			if (ch == null) {
				// Unknown entity - add it back to string as-is
				str.append(entity);
			} else {
				str.append(ch);
			}

			index = matcher.end();

		}

		if (index < fromString.length()) {
			str.append(fromString.substring(index));
		}

		return str.toString();

	}

	public static String toHexString(byte forByte) {

		String pseudo[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };

		StringBuffer out = new StringBuffer(2);

		byte ch = (byte) (forByte & 0xF0);
		ch = (byte) (ch >>> 4);
		ch = (byte) (ch & 0x0F);

		out.append(pseudo[(int) ch]);

		ch = (byte) (forByte & 0x0F);

		out.append(pseudo[(int) ch]);

		return out.toString();

	}

	public static void setDefaultDecoderExceptionListener(ExceptionListener defaultDecoderExceptionListener) {
		StringUtils.defaultDecoderExceptionListener = defaultDecoderExceptionListener;
	}

	/**
	 * Deserializes an XML encoding of an object back into the original object
	 * by using the java.beans.XMLDecoder class.
	 *
	 * @param string
	 *            an XML encoding of an object.
	 * @return The original object.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getFromXml(String xmlString) {
		return (T) getFromXml(xmlString, null);
	}

	/**
	 * Deserializes an XML encoding of an object back into the original object
	 * by using the java.beans.XMLDecoder class.
	 *
	 * @param string
	 *            an XML encoding of an object.
	 * @param exceptionListener
	 *            an optional exception listener to handle exceptions during
	 *            loading
	 * @return The original object.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getFromXml(String xmlString, ExceptionListener exceptionListener) {

		ByteArrayInputStream byteArrayIS = new ByteArrayInputStream(xmlString.getBytes());
		XMLDecoder decoder = new XMLDecoder(byteArrayIS);
		if (exceptionListener != null) {
			decoder.setExceptionListener(exceptionListener);
		} else if (defaultDecoderExceptionListener != null) {
			decoder.setExceptionListener(defaultDecoderExceptionListener);
		}
		Object object = decoder.readObject();
		decoder.close();
		return (T) object;

	}

	/**
	 * Serializes an object into an XML string using the java.beans.XMLEncoder
	 * class.
	 *
	 * @param dc
	 *            the object to serialize.
	 * @return an XML encoded string representing the object.
	 */
	public static <T> String getAsXml(T object) {

		ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
		XMLEncoder encoder = new XMLEncoder(byteArrayOS);

		encoder.writeObject(object);

		encoder.close();

		return (byteArrayOS.toString());

	}

	public static int nullSafeCompare(String string1, String string2, boolean nullIsBigger) {

		if (string1 == null) {
			if (string2 == null) {
				return 0;
			}
			return nullIsBigger ? 1 : -1;
		}
		if (string2 == null) {
			return nullIsBigger ? -1 : 1;
		}
		return string1.compareTo(string2);
	}

	public static int compareI18nCaseInsensitive(String string1, String string2, boolean nullIsBigger) {

		if (string1 == null) {
			if (string2 == null) {
				return 0;
			}
			return nullIsBigger ? 1 : -1;
		}
		if (string2 == null) {
			return nullIsBigger ? -1 : 1;
		}
		return COLLATOR.compare(string1, string2);

	}

	public static Comparator<String> getI18nCaseInsensitiveComparator(boolean nullIsBigger) {
		return new I18nComparator(nullIsBigger);
	}

	private static final class I18nComparator implements Comparator<String> {

		private boolean nullIsBigger = false;

		public I18nComparator(boolean nullIsBigger) {
			super();
			this.nullIsBigger = nullIsBigger;
		}

		@Override
		public int compare(String string1, String string2) {
			return compareI18nCaseInsensitive(string1, string2, nullIsBigger);
		}

	}

	public static String mapToDelimitedString(Map<String, String> map, String delim, char escapeChar,
			String nullOrEmptyValue) {

		if (map == null) {
			return null;
		}

		if (map.isEmpty()) {
			return "";
		}

		Object[] keyValuesArray = new String[map.size() * 2];

		int index = 0;
		for (Map.Entry<String, String> entry : map.entrySet()) {
			keyValuesArray[index] = entry.getKey();
			keyValuesArray[index + 1] = entry.getValue();
			index += 2;
		}

		return arrayToDelimitedString(keyValuesArray, delim, escapeChar, nullOrEmptyValue);

	}

	public static Map<String, String> delimitedStringToMap(String delimitedString, String delim, char escapeChar,
			boolean treatEmptyTokenAsNull) {

		if (delimitedString == null) {
			return null;
		}

		if (!StringUtils.hasText(delimitedString)) {
			return Collections.emptyMap();
		}

		String[] keyValuesArray = delimitedListToStringArray(delimitedString, delim, escapeChar, treatEmptyTokenAsNull);

		HashMap<String, String> map = new HashMap<String, String>();

		for (int i = 0; i < keyValuesArray.length; i += 2) {
			map.put(keyValuesArray[i], keyValuesArray[i + 1]);
		}

		return map;

	}

	public static String getBinaryString(List<Boolean> bits) {

		String binaryString = "";

		if (bits == null || bits.isEmpty()) {
			return null;
		}

		int numBits = bits.size();

		// Calculate how many groups are needed and how many stand alone 0s and
		// 1s
		int numberOfGroups = numBits / BINARY_GROUPING;
		int standAlone = numBits % BINARY_GROUPING;

		int bitIndex = 0;

		// First create the standalone 0s and 1s -- if any
		if (standAlone > 0) {
			for (int index = 0; index < standAlone; index++) {

				Boolean bit = bits.get(bitIndex);
				if (bit) {
					binaryString += 1;
				} else {
					binaryString += 0;
				}
				bitIndex++;
			}
			binaryString += " ";
		}

		// Now create the button for each group
		for (int groupIndex = 0; groupIndex < numberOfGroups; groupIndex++) {

			if (groupIndex > 0) {
				binaryString += " ";
			}
			for (int index = 0; index < BINARY_GROUPING; index++) {

				Boolean bit = bits.get(bitIndex);
				if (bit) {
					binaryString += 1;
				} else {
					binaryString += 0;
				}
				bitIndex++;
			}
		}

		return binaryString;
	}

	/**
	 * Format the given GIS coordinate such that it is rounded to the system
	 * preferred number of digits.
	 * 
	 * @param gisCoordinate
	 *            The GIS coordinate (either latitude or longitude)
	 * @return The coordinate as a string
	 */
	public static String formatGisCoordinate(double gisCoordinate) {
		return GIS_COORDINATE_FORMAT.format(gisCoordinate);
	}

	/**
	 * Get the length of a MessageFormat template if all of its parameters where
	 * empty strings.
	 * <p>
	 * For example, "Test {1}Template{0}" would return 13
	 * 
	 * @param template
	 *            The template
	 * @return The length
	 */
	public static int getEmptyMessageTemplateLength(String template) {

		if (!hasText(template)) {
			return 0;
		}

		int count = 0;
		int len = template.length();
		boolean inSingleQuote = false;
		boolean inBrace = false;
		boolean atLeastOneSingleQuotedCharacter = false;
		for (int i = 0; i < len; i++) {
			char ch = template.charAt(i);
			if (ch == '\'') {
				if (inSingleQuote) {
					// Close quote
					inSingleQuote = false;
					if (!atLeastOneSingleQuotedCharacter) {
						// two single quotes in a row with nothing in between -
						// this counts as
						// an escaped single quote.
						count++;
					}
					// Clear it for next time
					atLeastOneSingleQuotedCharacter = false;
				} else {
					// Start quote string
					inSingleQuote = true;
				}
			} else if (inSingleQuote) {
				// Flag to indicate this is a quoted string and not an escaped
				// single quote (two in a row)
				atLeastOneSingleQuotedCharacter = true;
				// Count all characters in single quote (includes braces)
				count++;
			} else if (ch == '{') {
				// Start of format field (left brace is not quoted if we got
				// here)
				inBrace = true;
			} else if (inBrace && ch == '}') {
				// Close brace and do not increment count.
				// Note, a right brace that is not preceeded by left brace is
				// assumed to be escaped and counts
				inBrace = false;
			} else if (!inBrace) {
				// Count all characters outside of brace block
				count++;
			}

		}

		return count;

	}

}
