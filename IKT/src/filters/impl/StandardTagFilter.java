package filters.impl;

import filters.TagFilter;
import utility.Tag;

public class StandardTagFilter implements TagFilter {

	@Override
	public Tag[] filterTags(Tag[] tags) {
		return tags;
	}
	
}
