package demo;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Scanner;

import bing.JsonToSearchHits;
import data.FeatureVector;
import data.SearchHit;
import text.Stopper;
import text.StopperDummy;
import text.StopperFile;

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
public class SerpsToFeatureVector {
	public static final String FILE_EXTENSION = "json";

	private static final int VECTOR_MAX_SIZE = 20;

	/**
	 * 
	 * @param args args[0] should contain a resolveable path to a *directory* containing one or
	 * more JSON files returned by the Bing search API. args[1] handles our stopper.  If nothing
	 * is specified in args[1], we use no stopper.  If specified, args[1] should give the path to
	 * a file containing 1 stopword per line.
	 * @throws Exception This may be encountered if the supplied directory is bogus.  It will NOT be thrown if
	 * the program finds a wrongly-encoded search result file (problems with JSON encoding simply lead to a file
	 * getting skipped).
	 */
	public static void main(String[] args) throws Exception {
		// housekeeping...find our list of files to process
		String pathToData = args[0];
		File dir = new File(pathToData);
		File[] files = dir.listFiles();

		// check if the user has specified a stopper
		String pathToStopper = null;
		if(args.length > 1)
			pathToStopper = args[1];
		
		Stopper stopper = null;
		if(pathToStopper != null)
			stopper = new StopperFile(pathToStopper);
		else 
			stopper = new StopperDummy();

		// setup a FeatureVector to store all our result text.
		FeatureVector vector = new FeatureVector();
		vector.setStopper(stopper);

		// iterate over our files, grabbing the text from each one and adding it to our FeatureVector.
		for(File file : files) {

			Scanner in = new Scanner(new FileInputStream(file));
			String json = in.nextLine();
			in.close();

			List<SearchHit> hits = null;

			try {
				hits = JsonToSearchHits.toSearchHits(json);
				System.err.println("parsed file: " + file);
				for(SearchHit hit : hits) {
					vector.addText(hit.getSnippet());
					vector.addText(hit.getTitle());
					vector.addText(hit.getUrl());
				}
			} catch(Exception e) {
				System.err.println("skipped file: " + file);
			}
		}

		vector.clip(SerpsToFeatureVector.VECTOR_MAX_SIZE);
		System.out.println(vector);
	}
}
