package main;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import bing.BingSearch;
import data.FeatureVector;

public class RunTextUtils {

			
	public static void main(String[] args) throws Exception {
		String path = args[0];

		File dir = new File(path);
		File[] files = dir.listFiles();

		FeatureVector vector = new FeatureVector();

		for(File file : files) {

			if(! file.toString().endsWith("json"))
				continue;
			
			System.err.println("parsing " + file);
			
			Scanner in = new Scanner(new FileInputStream(file));
			String json = in.nextLine();
			in.close();

			
			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(json);
			JSONObject temp = (JSONObject) jsonObject.get(BingSearch.API_JSON_KEY_1);
			JSONArray jsonArray = (JSONArray) temp.get(BingSearch.API_JSON_KEY_2);
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> it = jsonArray.iterator();
			while(it.hasNext()) {
				jsonObject = it.next();
				vector.addText((String)jsonObject.get(BingSearch.RESULTS_FIELD_NAME));
				vector.addText((String)jsonObject.get(BingSearch.RESULTS_FIELD_URL));
				vector.addText((String)jsonObject.get(BingSearch.RESULTS_FIELD_URL));
			}
		}
		
		vector.clip(20);
		System.out.println(vector);
	}
}
