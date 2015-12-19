package filters.impl;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import filters.FilterFactory;
import filters.TagFilter;
import utility.Tag;

public class ExtractTagFilter extends AbstractTagFilter {
	
	

	public ExtractTagFilter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ExtractTagFilter(TagFilter filter) {
		super(filter);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Tag[] filter(Tag[] tags) {
		Set<Tag> newTags = new HashSet<Tag>();
		Map<String, Set<String>> synonymsMap = FilterFactory.getFinalTagsAndSynonyms();
		for(int i=0;i<tags.length;++i){
			String[] parts = tags[i].getName().split("\\W++");
			for(int j=0;j<parts.length;++j){
				if(synonymsMap.containsKey(parts[j])){
					newTags.add(new Tag(parts[j], null, null));
				}
			}
		}
		return newTags.toArray(new Tag[newTags.size()]);
	}

}
