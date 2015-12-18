package filters.impl;

import java.util.HashSet;
import java.util.Set;

import filters.FilterFactory;
import filters.TagFilter;
import utility.Tag;

public class KeywordTagFilter extends AbstractTagFilter {
	Set<String> finalTags;
	
	public KeywordTagFilter() {	
		super();
		finalTags = new HashSet<String>();
		Tag[] tags = FilterFactory.getFinalTags();
		for (Tag tag : tags) {
			finalTags.add(tag.getName());
		}
	}
	
	public KeywordTagFilter(TagFilter chainFilter) {
		super(chainFilter);
		finalTags = new HashSet<String>();
		Tag[] tags = FilterFactory.getFinalTags();
		for (Tag tag : tags) {
			finalTags.add(tag.getName());
		}
	}

	@Override
	public Tag[] filter(Tag[] tags) {
		Set<Tag> newTags = new HashSet<Tag>();
		for (Tag tag : tags) {
			if (checkTag(tag)){
				newTags.add(tag);
			}
		}
		return newTags.toArray(new Tag[newTags.size()]);
	}
	
	private boolean checkTag(Tag tag) {
		String[] words = tag.getName().toLowerCase().split("\\W+");
		for (String word : words) {
			if (finalTags.contains(word)) {
				return true;
			}
		}
		return false;
	}
}