/**
 * 
 */
package config;

import java.io.FileInputStream;
import java.util.Scanner;

/**
 * @author Miles Efron
 *
 */
public class ParametersTabImpl extends Parameters {
	public static final String DELIMITER = "\t";
	public static final int INDEX_OF_PARAM_NAMES = 0;
	public static final int INDEX_OF_PARAM_VALS  = 1;
	
	@Override
	public void readParams() throws Exception {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(new FileInputStream(pathToParamSource));
		while(in.hasNextLine()) {
			String line = in.nextLine();
			String[] fields = line.split(DELIMITER);
			if(fields.length > (INDEX_OF_PARAM_VALS + 1))
				throw new Exception ("Too many fields in parameter specification: " + line);
			setParamValue(fields[INDEX_OF_PARAM_NAMES], fields[INDEX_OF_PARAM_VALS]);
		}
		in.close();
	}

}
