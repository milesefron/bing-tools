package text;

import java.util.HashSet;

/**
 * Implements a stopper that never removes anything
 * @author Miles Efron
 *
 */
public class StopperDummy extends Stopper {


	public StopperDummy() {
		stoplist = new HashSet<String>();
	}
}
