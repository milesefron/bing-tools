package text;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import text.StopperDummy;
import text.StopperDynamic;

public class StopperTest {
	public static final String[] TEST_TEXT = {"this", "is", "a", "test"};

	@Test
	public void dummyDoesNothing() {
		StopperDummy stopper = new StopperDummy();
		
		for(String token : TEST_TEXT)
			assertFalse(stopper.contains(token));
	}
	
	@Test 
	public void dynamicAcceptsNewStopwords() {
		// initialize an empty stopper
		StopperDynamic stopper = new StopperDynamic();
		
		for(String token : TEST_TEXT)
			assertFalse(stopper.contains(token));
		
		for(String token : TEST_TEXT)
			stopper.addStopword(token);;
		
		for(String token : TEST_TEXT)
			assertTrue(stopper.contains(token));
		
		for(String token : TEST_TEXT) 
			stopper.removeStopword(token);
		
		for(String token : TEST_TEXT)
			assertFalse(stopper.contains(token));
			
	}

}
