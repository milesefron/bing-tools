package bing;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Scanner;

import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import data.SearchHit;

public class JsonToSearchHitsTest {
	/**
	 * A path to a well-formed Bing API search results serialized as a JSON file.
	 */
	public static final String PATH_TO_JSON_FILE = "data/topics/ra/ra.results.rheumatoid-arthritis-treatment.json";
	
	public static final int HIT_COUNT_IN_FILE = 10;
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void validJsonParsesCorrectly() throws FileNotFoundException {
		Scanner in = new Scanner(new FileInputStream(new File(JsonToSearchHitsTest.PATH_TO_JSON_FILE)));
		String json = in.nextLine();
		in.close();

		try {
			List<SearchHit> hits = JsonToSearchHits.toSearchHits(json);
			assertEquals(JsonToSearchHitsTest.HIT_COUNT_IN_FILE, hits.size());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test(expected = ParseException.class)
	public void invalidJsonThrowsParseException() throws UnsupportedEncodingException, ParseException {
		String bogusJson = "this is not JSON.";
		JsonToSearchHits.toSearchHits(bogusJson);
		
	}

}
