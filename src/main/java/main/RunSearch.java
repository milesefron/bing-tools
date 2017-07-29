package main;

import bing.BingSearch;
import config.ParametersTabImpl;
import config.Parameters;

public class RunSearch {

	
	public static void main(String[] args) throws Exception {
		String pathToParams = args[0];
		ParametersTabImpl params = new ParametersTabImpl();
		params.setPathToParamSource(pathToParams);
		params.readParams();
		
		BingSearch search = new BingSearch(params.getParamValue(Parameters.PARAM_NAME_USER_KEY));
		search.setQueryString(params.getParamValue(Parameters.PARAM_NAME_QUERY_TO_RUN));
		search.runSearch();
		
	}
}
