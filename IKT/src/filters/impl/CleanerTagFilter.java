package filters.impl;

import java.util.ArrayList;
import java.util.List;
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
		List<Tag> newTags = new ArrayList<Tag>();
		for (Tag tag: tags) {
			if (checkTag(tag)) {
				newTags.add(tag);
			}
		}
		return newTags.toArray(new Tag[newTags.size()]);
	}
	
	private boolean checkTag(Tag tag) {
		if (stopTags.contains(tag.getName()))
			return false;
		String[] words = tag.getName().split("\\W+");
		for (String word : words) {
			if (stopTags.contains(word))
				return false;
		}
		return true;
	}
}
