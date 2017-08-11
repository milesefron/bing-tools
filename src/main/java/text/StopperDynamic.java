package text;

import java.util.HashSet;

/** A {@link text.Stopper} that allows the client to add or remove words to/from the stoplist.
 * 
 * @author Miles Efron
 *
 */

public class StopperDynamic extends Stopper {
	
	public StopperDynamic() {
		stoplist = new HashSet<String>();
	}
	
	public void addStopword(String stopword) {
		stoplist.add(stopword);
	}
	public void removeStopword(String stopword) {
		stoplist.remove(stopword);
	}
	
}
