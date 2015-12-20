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
		if (chainFilter != null)
			return chainFilter.filterTags(tagFilter(tags));
		return tagFilter(tags);
	}
	
	synchronized
	public Tag[] tagFilter(Tag tags[]) {
		return filter(tags);
	}
	
	public abstract Tag[] filter(Tag[] tags);
}
