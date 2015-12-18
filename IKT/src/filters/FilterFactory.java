package filters;

import java.util.Map;

import filters.impl.KeywordTagFilter;
import readers.FinalTagsAndSynonymsReader;

public class FilterFactory {
	private static FinalTagsAndSynonymsReader reader = FinalTagsAndSynonymsReader.getInstance();
	
	public static TagFilter getTagFilter() {
		return new KeywordTagFilter();
	}
	
	public static String[] getFinalTags() {
		return reader.getTags();
	}
	
	public static Map<String, String> getFinalTagsAndSynonyms(){
		return reader.getTagsAndSynonyms();
	}
}
