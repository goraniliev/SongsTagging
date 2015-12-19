package readers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import stemming.Stemmer;

public class FinalTagsAndSynonymsReader {
	
	private Map<String, Set<String>> finalTagsAndSynonyms;
	private static FinalTagsAndSynonymsReader instance = new FinalTagsAndSynonymsReader();
	
	private FinalTagsAndSynonymsReader(){
		finalTagsAndSynonyms = new HashMap<String, Set<String>>();
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
				String word = Stemmer.stem(st.nextToken());
				finalTagsAndSynonyms.put(word, addBaseTag(word, word));
				while(st.hasMoreTokens()){
					String synonym = Stemmer.stem(st.nextToken());
					finalTagsAndSynonyms.put(synonym, addBaseTag(word, synonym));
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
	
	private Set<String> addBaseTag(String baseTag, String synonym){
		Set<String> baseTags = finalTagsAndSynonyms.get(synonym);
		if(baseTags == null){
			baseTags = new HashSet<String>();
		}
		baseTags.add(baseTag);
		return baseTags;
	}

	public String[] getTags() {
		return finalTagsAndSynonyms.keySet().toArray(new String[finalTagsAndSynonyms.size()]);
	}

	public Map<String, Set<String>> getTagsAndSynonyms() {
		return finalTagsAndSynonyms;
	}
	
}
