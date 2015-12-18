package filters;

import filters.impl.KeywordTagFilter;
import readers.FinalTagsReader;
import utility.Tag;

public class FilterFactory {
	private static FinalTagsReader reader = FinalTagsReader.getInstance();
	
	public static TagFilter getTagFilter() {
		return new KeywordTagFilter();
	}
	
	public static Tag[] getFinalTags() {
		return reader.getTags();
	}
}
