package filters.impl;

import filters.TagFilter;
import utility.Tag;

public abstract class AbstractTagFilter implements TagFilter {
	private TagFilter chainFilter;
	
	public AbstractTagFilter() {
		// TODO Auto-generated constructor stub
	}
	
	public AbstractTagFilter(TagFilter filter) {
		this.chainFilter = filter;
	}

	@Override
	public Tag[] filterTags(Tag[] tags) {
		Tag[] newTags = filter(tags);
		if (chainFilter != null)
			return chainFilter.filterTags(newTags);
		return newTags;
	}
	
	public abstract Tag[] filter(Tag[] tags);
}
