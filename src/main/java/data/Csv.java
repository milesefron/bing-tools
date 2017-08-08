package data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Offers support for writing and reading CSV files
 * 
 * @author Miles Efron
 *
 */
public class Csv {
	public static final String SEPARATOR = ",";
	public static final Pattern SEPARATOR_REGEX = Pattern.compile(SEPARATOR);
	
	/**
	 * Prints (to STDOUT) a comma-separated version of the term-document matrix specified in the textVectors
	 * argument.
	 * @param textVectors each member of this list is a vector-representation of one document
	 * @param writer provides access to the stream where we'll send our output (e.g. STDOUT or a file)
	 * @throws IOException File/filesystem glitches
	 */
	public static void writeCsv(List<FeatureVector> textVectors, BufferedWriter writer) throws IOException {
		String[] columns = Csv.getColumns(textVectors);
		
		// first, print out our column names 
		
		// starting with a column to hold document names/URLs.
		writer.write("URL" + Csv.SEPARATOR);
		writer.flush();
		// now the vocabulary
		for(int i=0; i<columns.length-1; i++) {
			writer.write(columns[i] + Csv.SEPARATOR);
			writer.flush();
		}
		writer.write(columns[columns.length-1] + System.lineSeparator());
		writer.flush();
		// done with columns
		
		// iterate over our individual texts (i.e. rows)
		for(FeatureVector v : textVectors) {
			String title = v.getTitle();			
			writer.write(title + Csv.SEPARATOR);
			for(int i=0; i<columns.length-1; i++) 
				writer.write(v.getValue(columns[i]) + Csv.SEPARATOR);
			writer.flush();
			writer.write(v.getValue(columns[columns.length-1]) + System.lineSeparator());
			writer.flush();
		}
		
	}
	
	public static List<FeatureVector> readFromFile(File input) throws Exception {
		List<FeatureVector> vectors = new LinkedList<FeatureVector>();
		@SuppressWarnings("resource")
		Scanner in = new Scanner(new FileInputStream(input));

		// read in the first row of the CSV, which contains our column names (i.e. the indexing vocabulary).
		// N.B. The first element of this listing is URL, a column header for each document's title/URL.
		String[] vocab = Csv.SEPARATOR_REGEX.split(in.nextLine());
		
		//System.out.println(Arrays.toString(vocab));
		while(in.hasNextLine()) {
			String[] fields = Csv.SEPARATOR_REGEX.split(in.nextLine());
			//System.out.println(Arrays.toString(fields));
			if(fields.length != vocab.length)
				throw new Exception("Dimension mismatch.  Found a row with an incompatible length.");
			FeatureVector vector = new FeatureVector();
			vector.setTitle(fields[0]);
			// 1-indexing to avoid treating the row name/URL as a feature
			for(int i=1; i<fields.length; i++) {
				Double value = new Double(fields[i]);
				// no need to store 0's
				if(value == 0.0)
					continue;
				vector.setFeatureValue(vocab[i], value);
			}
			vectors.add(vector);
		}
		in.close();
		return vectors;
	}
	
	private static String[] getColumns(List<FeatureVector> textVectors) {
		Set<String> vocab = new HashSet<String>();
		for(FeatureVector v : textVectors)
			vocab.addAll(v.getFeatures());
		
		String[] columns = new String[vocab.size()];
		
		int i=0;
		for(String term : vocab) 
			columns[i++] = term;
		
		return columns;
	}
	
}
