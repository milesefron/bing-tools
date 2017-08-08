package main;

import java.util.Collections;
import java.util.List;

import bing.BingSearch;
import config.Parameters;
import config.ParametersTabImpl;
import data.SearchHit;
import data.SearchHitComparator;

/**
 * Demo class that runs a query against the Bing API.  The class retrieves the results as a
 * {@link java.util.List} containing one {@link data.SearchHit} per result.
 * 
 * @author Miles Efron
 *
 */
public class RunSearchToList {

	
	public static void main(String[] args) throws Exception {
		String pathToParams = args[0];
		ParametersTabImpl params = new ParametersTabImpl();
		params.setPathToParamSource(pathToParams);
		params.readParams();
		
		BingSearch search = new BingSearch(params.getParamValue(Parameters.PARAM_NAME_USER_KEY));
		search.setResultCount(Integer.parseInt(params.getParamValue(Parameters.PARAM_NAME_COUNT)));
		search.setOffset(Integer.parseInt(params.getParamValue(Parameters.PARAM_NAME_OFFSET)));
		List<SearchHit> hits = search.runQueryToSearchHits(params.getParamValue(Parameters.PARAM_NAME_QUERY_TO_RUN));
		
		// technically we don't need to sort these since they are given to us in order...this line
		// is here simply to show how clients can sort SearchHits if they get re-scored.
		Collections.sort(hits, new SearchHitComparator());
		System.out.println(hits);
	}
}
