package main;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import bing.BingSearch;
import data.FeatureVector;

/** 
 * This demo program reads in the results of Bing searches that have been stored in one or more
 * text files, each containing a JSON object obtained from one call to the Bing API.  The JSON follows the
 * structure of search results returned by the Bing API (v5.0).
 * The program reads in the Bing SERPs and creates a single FeatureVector
 * from the results (text from each hit's url, title and snippet are included in the vector).  The 
 * FeatureVector is truncated and then pretty-printed to STDOUT.
 * 
 * N.B. If the user supplies a directory containing files that are NOT proper, JSON-encoded SERP objects,
 * these files will be ignored during vector construction. 
 *  
 * @author Miles Efron
 *
 */
public class RunSerpToFeatureVector {
	public static final String FILE_EXTENSION = "json";
	
	private static final int VECTOR_MAX_SIZE = 20;
	
	/**
	 * 
	 * @param args args[0] should contain a resolveable path to a *directory* containing one or
	 * more JSON files returned by the Bing search API.
	 * @throws Exception This may be encountered if the supplied directory is bogus.  It will NOT be thrown if
	 * the program finds a wrongly-encoded search result file (problems with JSON encoding simply lead to a file
	 * getting skipped).
	 */
	public static void main(String[] args) throws Exception {
		String path = args[0];

		File dir = new File(path);
		File[] files = dir.listFiles();

		FeatureVector vector = new FeatureVector();

		for(File file : files) {

			Scanner in = new Scanner(new FileInputStream(file));
			String json = in.nextLine();
			in.close();

			// throw an exception and then move along if we encounter a file that isn't a JSON-encoded SERP object.
			try {
				JSONParser parser = new JSONParser();
				JSONObject jsonObject = (JSONObject) parser.parse(json);
				System.err.println("parsed " + file);
				JSONObject temp = (JSONObject) jsonObject.get(BingSearch.API_JSON_KEY_1);
				JSONArray jsonArray = (JSONArray) temp.get(BingSearch.API_JSON_KEY_2);
				@SuppressWarnings("unchecked")
				Iterator<JSONObject> it = jsonArray.iterator();
				while(it.hasNext()) {
					jsonObject = it.next();
					vector.addText((String)jsonObject.get(BingSearch.RESULTS_FIELD_NAME));
					vector.addText((String)jsonObject.get(BingSearch.RESULTS_FIELD_URL));
					vector.addText((String)jsonObject.get(BingSearch.RESULTS_FIELD_URL));
				}
			} catch(Exception e) {
				System.err.println("skipped non-JSON file " + file);
			}
		}

		vector.clip(RunSerpToFeatureVector.VECTOR_MAX_SIZE);
		System.out.println(vector);
	}
}
