package filters.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import filters.FilterFactory;
import filters.TagFilter;
import utility.Tag;

public class KeywordTagFilter extends AbstractTagFilter {
	public KeywordTagFilter() {
		super();
	}
	
	public KeywordTagFilter(TagFilter chainFilter) {
		super(chainFilter);
	}

	@Override
	public Tag[] filter(Tag[] tags) {
		Tag[] finalTags = FilterFactory.getFinalTags();
		/*Tag[] finalTags = new Tag[1];
		finalTags[0] = new Tag("pop", null, null);*/
		Set<Tag> newTags = new HashSet<Tag>();
		for (Tag tag : tags) {
			for (Tag finalTag : finalTags) {
				if (tag.getName().contains(finalTag.getName())) {
					newTags.add(tag);
				}
			}
		}
		return newTags.toArray(new Tag[newTags.size()]);
	}
}