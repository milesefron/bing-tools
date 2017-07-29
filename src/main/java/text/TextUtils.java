package text;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TextUtils {
	private static final String TEXT_CLEANING_REGEX = "[\\.,!\\?'\"]";
	private static final String WHITESPACE_REGEX   = "\\s";
	
	private static final int MIN_WORD_LENGTH = 2;
	
	private static final Stopper STOPPER = new StopperBasic();
	
	
	public static String clean(String input) {
		String lc = input.toLowerCase();
		lc = lc.replaceAll(TEXT_CLEANING_REGEX, " ");
		return lc;
	}
	
	public static List<String> tokenize(String input) {
		String[] toks = input.split(TEXT_CLEANING_REGEX);
		return Arrays.asList(toks);
	}
	
	public static List<String> stoplist(Collection<String> input) {
		List<String> stopped = new LinkedList<String>();
		for(String term : input)
			if(term.length() >= MIN_WORD_LENGTH && !STOPPER.contains(term))
				stopped.add(term);
		return stopped;
	}
}
