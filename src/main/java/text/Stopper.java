package text;

import java.util.Set;

public abstract class Stopper {
	protected Set<String> stoplist;
	
	public boolean contains(String term) {
		return stoplist.contains(term);
	}
	
}
