package data;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import text.Stopper;
import text.StopperBasic;

public class FeatureVectorTest {
	private String text = "This is a TEST.  It is only a test.  Ignore 'everything' else. xz.";
	private FeatureVector vector;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Assures we never have a null object in a vector
	 */
	@Test
	public void constructorInitializesVector() {
		vector = new FeatureVector();
		assertEquals(vector.getFeatureCount(), 0);
		assertEquals(vector.getFeatures().size(), 0);
		vector.addFeature("feature1");
		assertEquals(vector.getFeatureCount(), 1);
	}

	/**
	 * Validates count incrementation
	 */
	@Test
	public void addFeatureIncrements() {
		String feature1 = "feature1";
		String feature2 = "feature2";

		vector = new FeatureVector();
		vector.addFeature(feature1);
		assertEquals((int)vector.getValue(feature1), 1);
		vector.addFeature(feature1);
		assertEquals((int)vector.getValue(feature1), 2);
		vector.addFeature(feature2);
		assertEquals((int)vector.getValue(feature2), 1);
	}
	
	@Test
	public void addTextInvokesCleaning() {
		vector = new FeatureVector();
		Stopper stopper = new StopperBasic();
		vector.setStopper(stopper);
		vector.addText(text);
		
		/**
		 * all punctuation should be removed
		 */
		assertEquals((int)vector.getValue("test."), 0); 
		
		/**
		 * case-folding happened and incrementing was proper
		 */
		assertEquals((int)vector.getValue("test"),  2);		
		
		/**
		 * stoplisting works
		 */
		assertEquals((int)vector.getValue("this"),  0);

		/**
		 * ignore short words
		 */
		assertEquals((int)vector.getValue("xz"), 0);
	}
	
	/**
	 * Assures that we can't increment a feature's count once we normalize the vector
	 */
	@Test(expected = Exception.class)
	public void doNotIncrementProbabilities() {
		vector = new FeatureVector();
		vector.addText(text);
		assertTrue(vector.getFeatureCount() > 0);
		
		vector.normalize();
		
		/**
		 * this should throw an exception
		 */
		vector.addFeature("feature");
	}
	
	/**
	 * Sanity checks for the {@link data.FeatureVector#cosine(FeatureVector, FeatureVector)} method
	 */
	@Test
	public void cosine01() {
		FeatureVector x = new FeatureVector();
		x.addText("axa ono ici");
		
		FeatureVector y = new FeatureVector();
		y.addText("zyz vwv utu");
		
		assertEquals(Math.round(FeatureVector.cosine(x, y)), 0);
		assertEquals(Math.round(FeatureVector.cosine(x, x)), 1);
	}
}
