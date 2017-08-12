package demo;

import java.io.File;
import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import bing.JsonToSearchHits;
import data.FeatureVector;
import data.SearchHit;
import ir.RelevanceModel;
import text.Stopper;
import text.StopperBasic;

/**
 * Reads the result of a single web search (i.e. one SERP) and uses the results to populate
 * a list of {@link data.SearchHit} objects, where they would be easy to manipulate.
 * 
 * @author Miles Efron
 *
 */
public class SerpToRelevanceModel {


	/**
	 * 
	 * @param args args[0] should contain a resolveable path to a file containing
	 * a single JSON file returned by the a single call to the Bing search API.
	 * @throws Exception This may be encountered if the supplied file is bogus.  
	 */
	public static void main(String[] args) throws Exception {
		// find the file containing one SERP encoded in JSON
		File inFile = new File(args[0]);

		// an object to hold our results
		List<SearchHit> hits = new LinkedList<SearchHit>();

		// read in the SERP
		Scanner in = new Scanner(new FileInputStream(inFile));
		String json = in.nextLine();
		in.close();

		// convert the JSON to a List of SearchHits.
		Stopper stopper = new StopperBasic();
		hits = JsonToSearchHits.toSearchHits(json, stopper);

		
        RelevanceModel rmObject = new RelevanceModel();
        rmObject.setExponentialRateParam(1.0);
        FeatureVector rm1 = rmObject.getModel(hits);
        rm1.clip(50);
        System.out.println(rm1);


	}
}
