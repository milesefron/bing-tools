package data;

import java.util.Comparator;

public class TupleComparator implements Comparator<Tuple> {

	public int compare(Tuple x, Tuple y) {
		if(x.getValue() > y.getValue())
			return -1;
		if(x.getValue() < y.getValue())
			return 1;
		return 0;
	}


	
}
