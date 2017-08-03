package text;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtils {
	private static final String TEXT_CLEANING_REGEX = "[®:\\(\\)\\-\\—\\/\\.,!\\?'\"]";
	private static final String WHITESPACE_REGEX   = "\\s";
	private static final String TOKEN_SEPARATOR = " ";
	
	private static final int MIN_WORD_LENGTH = 2;
	private static final Stopper STOPPER = new StopperBasic();
	
	private static Pattern cleaningRegex = Pattern.compile(TextUtils.TEXT_CLEANING_REGEX);
	private static Pattern whitespaceRegex = Pattern.compile(TextUtils.WHITESPACE_REGEX);
	
	/**
	 * One-stop shopping for cleaning and tokenizing a string.
	 * @param input A String to clean
	 * @return cleaned version of the input.
	 */
	public static List<String> clean(String input) {		
		String lc = input.toLowerCase();
		Matcher matcher = cleaningRegex.matcher(lc);
		lc = matcher.replaceAll(TextUtils.TOKEN_SEPARATOR);
		List<String> tokens = tokenize(lc);
		tokens = TextUtils.enforceWordLength(tokens);
		tokens = TextUtils.stoplist(tokens);
		return tokens;
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
	
	private static List<String> stoplist(Collection<String> input) {
		List<String> stopped = new LinkedList<String>();
		for(String term : input)
			if(!STOPPER.contains(term))
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
