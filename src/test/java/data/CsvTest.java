package data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * 
 * @author Miles Efron
 *
 */
public class CsvTest {
	private List<FeatureVector> vectors;
	private String doc1Text = "aba xyz";
	private String doc2Text = "bab aca xyz";
	private String doc1Url = "url1";
	private String doc2Url = "url2";
	
	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();
	
	@Before
	public void setUp() throws Exception {
		vectors = new LinkedList<FeatureVector>();
		
		FeatureVector v1 = new FeatureVector();
		v1.addText(doc1Text);
		v1.setTitle(doc1Url);
		vectors.add(v1);
		
		FeatureVector v2 = new FeatureVector();
		v2.addText(doc2Text);
		v2.setTitle(doc2Url);
		vectors.add(v2);
	}


	/**
	 * Makes sure that the data written to a CSV is correct.  Specifically, this test compares the 
	 * original data and the serialized data, checking the following:
	 * <ol>
	 *  <li>Number of documents is the same.</li>
	 *  <li>Each serialized document contains the same entries as its original counterpart and the values for
	 *      these entries are equal.</li> 
	 * </ol>
	 * @throws Exception
	 */
	@Test
	public void dataIsAccurate() throws Exception {
		String outFileName = "temp.csv";
		final File outFile = tempFolder.newFile(outFileName);
		BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
		Csv.writeCsv(vectors, writer);
		
		List<FeatureVector> recoveredVectors = Csv.readFromFile(outFile);
		
		// make sure we have recovered the same number of vectors we wrote
		assertEquals(vectors.size(), recoveredVectors.size());
		
		// make sure each vector has an identical counterpart in our recovered vectors
		for(int i=0; i<vectors.size(); i++) {
			assertTrue(FeatureVector.equalData(vectors.get(i), recoveredVectors.get(i)));
		}
	}
	
	/**
	 * Assures that writing to a null file throws an exception
	 */
	@Test(expected = Exception.class)
	public void ioExceptionHandled() {
		File outFile = null;
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(outFile));
			Csv.writeCsv(vectors, writer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
