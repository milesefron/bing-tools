package text;

import java.util.Arrays;
import java.util.HashSet;

public class StopperBasic extends Stopper {


	public StopperBasic() {
		stoplist = new HashSet<String>(
				Arrays.asList(
						"a", "about", "also", "am", "an", "and", "another", "are", "as", "at", 
						"be", "been", 
						"can", "com", "could",
						"did", "do",
						"en", 
						"for", "from",
						"get", "getting", "go", "got", "gotten",
						"had", "have", "has", "how", "html", "http", "https",
						"i", "if", "in", "is", "isn", "it", "its",
						"no", "nor", "not",
						"of", "on", "one", "or", "org", 
						"pdf",
						"that", "the", "them", "they", "these", "their", "this", "those", "to",
						"when", "where", "with", "without", "www", "wikipedia",
						"you", "your", "yours"));
	}
}
