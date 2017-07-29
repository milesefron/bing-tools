package text;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StopperBasic implements Stopper {
	Set<String> terms = new HashSet<String>(
			Arrays.asList("a", "am", "an", "and", "another", "as",
					"be", "been", 
					"can", "could",
					"did", "do",
					"get", "getting", "go", "got", "gotten",
					"had", "have", "has", "http", "https",
					"i", "in", "is", "isn", "it", "its",
					"no", "nor", "not",
					"of", "or",
					"that", "these", "this", "those",
					"www", "wikipedia"));

	public boolean contains(String term) {
		return terms.contains(term);
	}
}
