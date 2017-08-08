package data;

import java.util.Comparator;

public class SearchHitComparator implements Comparator<SearchHit> {

	public int compare(SearchHit x, SearchHit y) {
		if(x.getScore() > y.getScore())
			return -1;
		if(x.getScore() < y.getScore())
			return 1;
		return 0;
	}
}
