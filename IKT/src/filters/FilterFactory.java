package filters;

import java.util.Map;
import java.util.Set;

import filters.impl.CleanerTagFilter;
import filters.impl.KeywordTagFilter;
import readers.FinalTagsAndSynonymsReader;
import readers.StopTagsReader;

public class FilterFactory {
	private static FinalTagsAndSynonymsReader reader = FinalTagsAndSynonymsReader.getInstance();
	private static TagFilter filter = new KeywordTagFilter(new CleanerTagFilter());
	private static StopTagsReader stopTagsReader = StopTagsReader.getInstance();
	
	private FilterFactory(){
		
	}
	
	public static TagFilter getTagFilter() {
		return filter;
	}
	
	public static String[] getFinalTags() {
		return reader.getTags();
	}
	
	public static Set<String> getStopTags() {
		return stopTagsReader.getTags();
	}
	
	public static Map<String, Set<String>> getFinalTagsAndSynonyms(){
		return reader.getTagsAndSynonyms();
	}
}
