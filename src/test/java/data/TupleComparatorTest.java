package data;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TupleComparatorTest {

	/**
	 * Make sure that the {@link data.TupleComparator} gives a reverse sorting
	 * on {@link data.Tuple} values.
	 */
	@Test
	public void sortOrderIsReverse() {
		Tuple greater = new Tuple("f1", 2.0);
		Tuple less    = new Tuple("f2", 1.0);
		TupleComparator comparator = new TupleComparator();
		
		assertEquals(-1, comparator.compare(greater, less));
		assertEquals(1,  comparator.compare(less, greater));
		assertEquals(0,  comparator.compare(less, less));
	}

}
