package data;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FeatureVectorTest {
	private String text = "This is a TEST.  It is only a test.  Ignore 'everything' else. xz.";
	private FeatureVector vector;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void constructorInitializesVector() {
		vector = new FeatureVector();
		assertEquals(vector.getFeatureCount(), 0);
		assertEquals(vector.getFeatures().size(), 0);
		vector.addFeature("feature1");
		assertEquals(vector.getFeatureCount(), 1);
	}

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
		assertEquals((int)vector.getValue(feature1), 2);
	}
	
	@Test
	public void addTextRemovesPunctuation() {
		vector = new FeatureVector();
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

	}
}
