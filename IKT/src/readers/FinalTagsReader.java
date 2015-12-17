package readers;

import utility.Tag;

public class FinalTagsReader {
	private Tag[] finalTags;
	private static FinalTagsReader instance = new FinalTagsReader();
	
	private FinalTagsReader() {
		
	}
	
	public static FinalTagsReader getInstance() {
		return instance;
	}
	
	public Tag[] getTags() {
		return finalTags;
	}
}
