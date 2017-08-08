package data;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SearchHitTest {
	private SearchHit h1;
	private SearchHit h2;
	private SearchHit h3;
	private List<SearchHit> hits;
	
	@Before
	public void setUp() throws Exception {
		hits = new ArrayList<SearchHit>(3);
		
		h1 = new SearchHit();
		h1.setScore(0.0);
		h1.setTitle("hit 1");
		hits.add(h1);
		
		h2 = new SearchHit();
		h2.setScore(1.0);
		h2.setTitle("hit 2");
		hits.add(h2);
		
		h3 = new SearchHit();
		h3.setScore(2.0);
		h3.setTitle("hit 3");
		hits.add(h3);
	}


	@Test
	public void sortingIsDecreasing() {
		Collections.sort(hits, new SearchHitComparator());
		
		assertTrue(hits.get(0).equals(h3));
		assertTrue(hits.get(1).equals(h2));
		assertTrue(hits.get(2).equals(h1));

	}

}
