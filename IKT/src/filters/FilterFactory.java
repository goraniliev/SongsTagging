package filters;

import java.util.Map;
import java.util.Set;

import filters.impl.CleanerTagFilter;
import filters.impl.ExtractTagFilter;
import filters.impl.KeywordTagFilter;
import filters.impl.StemmingTagFilter;
import filters.impl.SynonymsMappingTagFilter;
import filters.impl.TrimTagFilter;
import readers.FinalTagsAndSynonymsReader;
import readers.StopTagsReader;

public class FilterFactory {
	private static FinalTagsAndSynonymsReader reader = FinalTagsAndSynonymsReader.getInstance();
	private static StopTagsReader stopTagsReader = StopTagsReader.getInstance();
	private static TagFilter filter = new TrimTagFilter(new CleanerTagFilter(new StemmingTagFilter(new KeywordTagFilter(new ExtractTagFilter(new SynonymsMappingTagFilter())))));
	
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
