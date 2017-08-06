package config;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract class that defines behavior for handling parameterization
 * of runnable methods in this API.  Extending classes must implement
 * the {@link #readParams()} method, which handles different kinds of
 * data stores (files, URLs, etc.) and various formatting (tab-delimited,
 * JSON, XML, etc.).
 * 
 * @author Miles Efron
 *
 */
public abstract class Parameters {
	public static final String PARAM_NAME_USER_KEY = "key";
	public static final String PARAM_NAME_QUERY_TO_RUN = "query";
	
	protected Map<String,String> params;
	protected String pathToParamSource;
	
	/**
	 * @param pathToParamSource A resolvable path to a data store containing parameter specifications
	 */
	public void setPathToParamSource(String pathToParamSource) {
		this.pathToParamSource = pathToParamSource;
	}
	
	/**
	 * @param paramName The identifier of a parameter whose value this method fetches
	 * @return the value associated with this parameter name.  
	 * @throws Exception Throws an exception if client requests a non-existent parameter.
	 */
	public String getParamValue(String paramName) throws Exception {
		if(params == null || !params.containsKey(paramName))
			throw new Exception("NO PARAMETER FOUND WITH NAME: " + paramName);
		return params.get(paramName);
	}
	
	/**
	 * 
	 * @param paramName Name of the parameter we are setting
	 * @param paramValue Value of the parameter we are setting
	 */
	public void setParamValue(String paramName, String paramValue) {
		if(params == null)
			params = new HashMap<String,String>();
		
		params.put(paramName, paramValue);
	}
	
	public int parameterCount() {
		if(params == null)
			return 0;
		return params.size();
	}
	
	/**
	 * Reads parameters from an externally stored resource (e.g. a formatted file)
	 * @exception Exception The precise exception will depend on the implementation of the extending class.
	 */
	abstract public void readParams() throws Exception;
	
}
