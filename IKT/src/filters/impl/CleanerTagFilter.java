package filters.impl;

import java.util.Map;
import java.util.Set;

import filters.FilterFactory;
import filters.TagFilter;
import utility.Tag;

public class CleanerTagFilter extends AbstractTagFilter {
	Set<String> stopTags;
	

	public CleanerTagFilter() {
		super();
		stopTags = FilterFactory.getStopTags();
	}

	public CleanerTagFilter(TagFilter filter) {
		super(filter);
		stopTags = FilterFactory.getStopTags();
	}

	@Override
	public Tag[] filter(Tag[] tags) {
		return tags;
	}
}
