package main;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class RunText {

	private static String userSubscriptionID = "ed22f6aad64f42a8a35ce94567b768ab";

	public static void main(String[] args) throws Exception {
		String path = "data/topics/naltrexone/";

		File dir = new File(path);
		File[] files = dir.listFiles();

		for(File file : files) {

			Scanner in = new Scanner(new FileInputStream(file));
			String json = in.nextLine();
			in.close();

			JSONParser parser = new JSONParser();
			JSONObject jsonObject = (JSONObject) parser.parse(json);
			JSONObject temp = (JSONObject) jsonObject.get("webPages");
			JSONArray jsonArray = (JSONArray) temp.get("value");
			Iterator<JSONObject> it = jsonArray.iterator();
			while(it.hasNext()) {
				jsonObject = it.next();
				System.out.println(jsonObject.get("name"));
				System.out.println(jsonObject.get("displayUrl"));
				System.out.println(jsonObject.get("snippet") + "\n");
			}
		}
	}
}
