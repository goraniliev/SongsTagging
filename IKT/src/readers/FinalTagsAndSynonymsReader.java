package readers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import utility.Tag;

public class FinalTagsAndSynonymsReader {
	
	private Map<String, String> finalTagsAndSynonyms;
	private static FinalTagsAndSynonymsReader instance = new FinalTagsAndSynonymsReader();
	
	private FinalTagsAndSynonymsReader(){
		finalTagsAndSynonyms = new HashMap<String, String>();
		read();
	}
	
	public static FinalTagsAndSynonymsReader getInstance(){
		return instance;
	}
	
	private void read(){
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("finalTags.txt"), "utf-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line);
				String word = st.nextToken();
				finalTagsAndSynonyms.put(word, word);
				while(st.hasMoreTokens()){
					finalTagsAndSynonyms.put(st.nextToken(), word);
				}
			}
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String[] getTags() {
		return finalTagsAndSynonyms.keySet().toArray(new String[finalTagsAndSynonyms.size()]);
	}

	public Map<String, String> getTagsAndSynonyms() {
		return finalTagsAndSynonyms;
	}
	
}
