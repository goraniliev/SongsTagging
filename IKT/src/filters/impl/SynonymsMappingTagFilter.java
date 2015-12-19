package filters.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import filters.FilterFactory;
import utility.Tag;

public class SynonymsMappingTagFilter extends AbstractTagFilter {

	Map<String, Set<String>> finalTagsAndSynonyms;
	
	public SynonymsMappingTagFilter() {
		super();
		finalTagsAndSynonyms = FilterFactory.getFinalTagsAndSynonyms();
	}
	
	@Override
	public Tag[] filter(Tag[] tags) {
		Set<Tag> newTags = new HashSet<Tag>();
		for(int i=0;i<tags.length;++i){
			Set<String> baseTags = finalTagsAndSynonyms.get(tags[i].getName());
			for(String baseTag : baseTags){
				newTags.add(new Tag(baseTag, null, null));
			}
		}
		return newTags.toArray(new Tag[newTags.size()]);
	}

}
