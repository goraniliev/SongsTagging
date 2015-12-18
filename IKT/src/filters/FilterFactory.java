package filters;

import filters.impl.KeywordTagFilter;
import readers.FinalTagsAndSynonymsReader;
import readers.FinalTagsReader;
import utility.Tag;

public class FilterFactory {
	private static FinalTagsAndSynonymsReader reader = FinalTagsAndSynonymsReader.getInstance();
	
	public static TagFilter getTagFilter() {
		return new KeywordTagFilter();
	}
	
	public static String[] getFinalTags() {
		return reader.getTags();
	}
}
