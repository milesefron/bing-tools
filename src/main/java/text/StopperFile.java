package text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Implements a stopper whose contents are specified in a text file with one stopword per line
 * 
 * @author Miles Efron
 *
 */
public class StopperFile extends Stopper {


	public StopperFile(String pathToStoplistFile) throws FileNotFoundException {
		stoplist = new HashSet<String>();
		Scanner in = new Scanner(new FileInputStream(new File(pathToStoplistFile)));
		while(in.hasNextLine())
			stoplist.add(in.nextLine());
		
		in.close();
	}
}
