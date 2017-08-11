package demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import bing.JsonToSearchHits;
import data.Csv;
import data.FeatureVector;
import data.SearchHit;
import text.Stopper;
import text.StopperBasic;

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
public class SerpsToCsvDemo {
	public static final String FILE_EXTENSION = "json";
		
	/**
	 * 
	 * @param args args[0] should contain a resolveable path to a *directory* containing one or
	 * more JSON files returned by the Bing search API.  If args[1] is not specified, output goes
	 * to STDOUT.  If args[1] <em>is</em> specified, output is directed to the filename specified by
	 * args[1].
	 * @throws Exception This may be encountered if the supplied directory is bogus.  It will NOT be thrown if
	 * the program finds a wrongly-encoded search result file (problems with JSON encoding simply lead to a file
	 * getting skipped).
	 */
	public static void main(String[] args) throws Exception {
		String pathToInputData = args[0];		
		File dir = new File(pathToInputData);
		File[] files = dir.listFiles();

		Stopper stopper = new StopperBasic();
		
		String pathToOutFile = null;
		if(args.length > 1)
			pathToOutFile = args[1];
		
		List<FeatureVector> vectors = new LinkedList<FeatureVector>();
		
		
		BufferedWriter writer;
		if(pathToOutFile != null)
			writer = new BufferedWriter(new FileWriter(pathToOutFile));
		else writer = new BufferedWriter(new OutputStreamWriter(System.out));
		
		for(File file : files) {

			Scanner in = new Scanner(new FileInputStream(file));
			String json = in.nextLine();
			in.close();

			// throw an exception and then move along if we encounter a file that isn't a JSON-encoded SERP object.
			try {
				List<SearchHit> hits = JsonToSearchHits.toSearchHits(json);
				System.err.println("parsed " + file);
				for(SearchHit hit : hits) {
					FeatureVector vector = new FeatureVector();
					vector.setStopper(stopper);
					
					vector.addText(hit.getUrl());
					vector.addText(hit.getTitle());
					vector.addText(hit.getSnippet());
					
					vector.setTitle(hit.getUrl());
					
					vectors.add(vector);
				}
			} catch(Exception e) {
				System.err.println("skipped non-JSON file " + file);
			}
		}

		Csv.writeCsv(vectors, writer);
		
		
	}
}
