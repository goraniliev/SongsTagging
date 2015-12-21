package readers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;

import stemming.Stemmer;
import utility.Tag;

public class FinalTagsReader {
	private Map<String, Integer> finalTags;
	private static FinalTagsReader instance = new FinalTagsReader();
	
	private FinalTagsReader() {
		finalTags = new HashMap<String, Integer>();
		read();
	}
	
	private void read() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("finalTags.txt"), "utf-8"));
			String line;
			int i = 1;
			while ((line = reader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line);
				String word = Stemmer.stem(st.nextToken());
				finalTags.put(word, i);
				i++;
			}
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static FinalTagsReader getInstance() {
		return instance;
	}
	
	public Map<String, Integer> getTags() {
		return finalTags;
	}
	
	public static void main(String[] args) {
		FinalTagsReader reader = getInstance();
		Map<String, Integer> map = reader.getTags();
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			System.out.println(entry.getKey() + " " + entry.getValue());
		}
	}
}
