package readers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import utility.Tag;

public class FinalTagsReader {
	private Tag[] finalTags;
	private static FinalTagsReader instance = new FinalTagsReader();
	
	private FinalTagsReader() {
		finalTags = new Tag[0];
		read();
	}
	
	private void read() {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("finalTags.txt"), "utf-8"));
			List<Tag> tags = new ArrayList<Tag>();
			String line;
			while ((line = reader.readLine()) != null) {
				tags.add(new Tag(line, null, null));
			}
			finalTags = tags.toArray(new Tag[tags.size()]);
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
	
	public Tag[] getTags() {
		return finalTags;
	}
}
