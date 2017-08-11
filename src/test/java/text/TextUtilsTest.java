package text;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TextUtilsTest {
	public static final String TEST_TEXT = "This		is a    test.  It is 	 only a little test.";
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void minWordLengthEnforcementToggles() {
		TextUtils.enforceMinWordLength = false;
		List<String> cleaned = TextUtils.clean(TEST_TEXT, null);
		assertEquals(cleaned.size(), 10);
		
		TextUtils.enforceMinWordLength = true;
		cleaned = TextUtils.clean(TEST_TEXT, null);
		assertEquals(cleaned.size(), 5);
	}
	
	@Test
	public void tokenizeHandlesWhitespace() {
		TextUtils.enforceMinWordLength = false;
		List<String> tokens = TextUtils.clean(TEST_TEXT, null);
		assertEquals(10, tokens.size());
	}

}
