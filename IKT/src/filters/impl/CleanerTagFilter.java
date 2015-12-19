package filters.impl;

import java.util.Map;

import filters.FilterFactory;
import filters.TagFilter;
import utility.Tag;

public class CleanerTagFilter extends AbstractTagFilter {
	Map<String, String> finalTags;
	

	public CleanerTagFilter() {
		super();
		finalTags = FilterFactory.getFinalTagsAndSynonyms();
	}

	public CleanerTagFilter(TagFilter filter) {
		super(filter);
	}

	@Override
	public Tag[] filter(Tag[] tags) {
		return tags;
	}
	
	
}
