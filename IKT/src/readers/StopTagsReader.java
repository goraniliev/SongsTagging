package readers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import stemming.Stemmer;
import utility.Tag;

public class StopTagsReader {
	private static StopTagsReader instance = new StopTagsReader();
	private Set<String> tags;
	
	private StopTagsReader() {
		tags = new HashSet<String>();
		read();
	}
	
	private void read() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("stopTags.txt"), "utf-8"));
			String line;
			while ((line = reader.readLine()) != null) {
				tags.add(line);
			}
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Set<String> getTags() {
		return tags;
	}
	
	public static StopTagsReader getInstance() {
		return instance;
	}
}
