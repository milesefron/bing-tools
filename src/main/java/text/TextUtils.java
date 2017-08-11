package text;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities for manipulating text.
 * 
 * @author Miles Efron
 *
 */
public class TextUtils {
	/**
	 * regex used to remove punctuation from text
	 */
	private static final String TEXT_CLEANING_REGEX = "[®:\\(\\)\\-\\—\\/\\.,!\\?'\"]";
	
	/**
	 * regex used to remove extra whitespace from text
	 */
	private static final String WHITESPACE_REGEX   = "\\s";
	
	/**
	 * character string denoting what separates individual tokens (i.e. a single space)
	 */
	private static final String TOKEN_SEPARATOR = " ";
	
	/**
	 * if we are enforcing minimum word length, all words shorter than this will be ignored
	 */
	private static final int MIN_WORD_LENGTH = 3;
	
	
	
	private static Pattern cleaningRegex = Pattern.compile(TextUtils.TEXT_CLEANING_REGEX);
	private static Pattern whitespaceRegex = Pattern.compile(TextUtils.WHITESPACE_REGEX);
	
	private static boolean enforceMinWordLength = true;
	
	
	/**
	 * One-stop shopping for cleaning and tokenizing a string.  Specifically, this method
	 * performs the following operations on a supplied string:
	 * <ol>
	 *  <li> case-folding</li>
	 *  <li> stoplisting (using {@link text.StopperBasic})</li>
	 *  <li> removing punctuation and extra whitespace (see {@link text.TextUtils#TEXT_CLEANING_REGEX} and
	 *        {@link text.TextUtils#WHITESPACE_REGEX} for exactly what is removed)</li>
	 * </ol>
	 * @param input A String to clean
	 * @param stopper An object of type {@link text.Stopper}.  If stopper==null, no stoplisting is applied.
	 * @return cleaned version of the input.
	 */
	public static List<String> clean(String input, Stopper stopper) {		
		String lc = input.toLowerCase();
		Matcher matcher = cleaningRegex.matcher(lc);
		lc = matcher.replaceAll(TextUtils.TOKEN_SEPARATOR);
		List<String> tokens = tokenize(lc);
		if(TextUtils.enforceMinWordLength)
			tokens = TextUtils.enforceWordLength(tokens);
		if(stopper != null)
			tokens = TextUtils.stoplist(tokens, stopper);
		return tokens;
	}
	
	/**
	 * Should we insist that all stored words are at least {@link TextUtils#MIN_WORD_LENGTH} characters long?
	 * @param enforceWordLength true leads us to ignore any words that are not at least {@link text.TextUtils#MIN_WORD_LENGTH} chars.
	 */
	public void setEnforceWordLength(boolean enforceWordLength) {
		TextUtils.enforceMinWordLength = enforceWordLength;
	}
	
	
	/**
	 * Splits a string on whitespace.
	 * @param input A string to tokenize
	 * @return A List of the derived tokens.
	 */
	private static List<String> tokenize(String input) {
		String[] toks = whitespaceRegex.split(input);
		return Arrays.asList(toks);
	}
	
	private static List<String> stoplist(Collection<String> input, Stopper stopper) {		
		List<String> stopped = new LinkedList<String>();
		for(String term : input)
			if(!stopper.contains(term))
				stopped.add(term);
		return stopped;
	}
	
	private static List<String> enforceWordLength(List<String> input) {
		List<String> stopped = new LinkedList<String>();
		for(String term : input)
			if(term.length() >= MIN_WORD_LENGTH)
				stopped.add(term);
		return stopped;
	}
}
