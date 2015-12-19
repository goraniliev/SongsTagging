package filters;

import java.util.Map;

import filters.impl.CleanerTagFilter;
import filters.impl.KeywordTagFilter;
import readers.FinalTagsAndSynonymsReader;

public class FilterFactory {
	private static FinalTagsAndSynonymsReader reader = FinalTagsAndSynonymsReader.getInstance();
	private static TagFilter filter = new KeywordTagFilter(new CleanerTagFilter());
	
	private FilterFactory(){
		
	}
	
	public static TagFilter getTagFilter() {
		return filter;
	}
	
	public static String[] getFinalTags() {
		return reader.getTags();
	}
	
	public static Map<String, String> getFinalTagsAndSynonyms(){
		return reader.getTagsAndSynonyms();
	}
}
