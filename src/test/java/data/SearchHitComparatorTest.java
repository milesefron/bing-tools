package data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SearchHitComparatorTest {


	@Test
	public void HigherScoresAreFirst() {
		SearchHit greater = new SearchHit();
		greater.setScore(1.0);
		SearchHit less    = new SearchHit();
		less.setScore(0.5);
		
		SearchHitComparator comparator = new SearchHitComparator();
		
		assertEquals(-1, comparator.compare(greater, less));
		assertEquals(1,  comparator.compare(less, greater));
		assertEquals(0,  comparator.compare(less, less));
	}

	@Test
	public void unititializedScoresAreOk() {
		SearchHit greater = new SearchHit();
		SearchHit less    = new SearchHit();
		
		SearchHitComparator comparator = new SearchHitComparator();
		
		assertEquals(0, comparator.compare(greater, less));
	}
}
