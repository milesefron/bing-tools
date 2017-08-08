package config;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class ParametersTabImplTest {
	public static final String KEY_VALUE = "key";
	public static final String QUERY_VALUE = "an example query";
	
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	


	@Test
	public void allParamsAreRead() throws Exception {
		String outFileName = "temp.txt";
		final File outFile = tempFolder.newFile(outFileName);
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		writer.write(Parameters.PARAM_NAME_USER_KEY + ParametersTabImpl.DELIMITER + ParametersTabImplTest.KEY_VALUE + System.lineSeparator());
		writer.flush();
		writer.write(Parameters.PARAM_NAME_QUERY_TO_RUN + ParametersTabImpl.DELIMITER + ParametersTabImplTest.QUERY_VALUE);
		writer.flush();
		writer.close();
		
		ParametersTabImpl params = new ParametersTabImpl();
		params.setPathToParamSource(outFile.getAbsolutePath());
		params.readParams();
		
		assertEquals(2, params.parameterCount());
	}

	
	
	@Test(expected = Exception.class)
	public void illFormedLinesAreRejected() throws Exception {
		String outFileName = "temp.txt";
		final File outFile = tempFolder.newFile(outFileName);
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		writer.write(Parameters.PARAM_NAME_USER_KEY + ParametersTabImpl.DELIMITER + ParametersTabImplTest.KEY_VALUE + System.lineSeparator());
		writer.flush();
		writer.write(Parameters.PARAM_NAME_QUERY_TO_RUN + ParametersTabImpl.DELIMITER + ParametersTabImplTest.QUERY_VALUE + System.lineSeparator());
		writer.flush();
		writer.write("bad line");
		writer.flush();
		writer.close();
		
		ParametersTabImpl params = new ParametersTabImpl();
		params.setPathToParamSource(outFile.getAbsolutePath());
		params.readParams();
		
	}
	
	
}
