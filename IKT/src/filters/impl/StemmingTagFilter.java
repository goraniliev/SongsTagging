package filters.impl;

import filters.TagFilter;
import stemming.Stemmer;
import utility.Tag;

public class StemmingTagFilter extends AbstractTagFilter {
	

	public StemmingTagFilter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public StemmingTagFilter(TagFilter filter) {
		super(filter);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Tag[] filter(Tag[] tags) {
		for(int i=0;i<tags.length;++i){
			tags[i].setName(stemPhrase(tags[i]));
		}
		return tags;
	}

	private String stemPhrase(Tag tag) {
		StringBuilder sb = new StringBuilder();
		String[] words = tag.getName().toLowerCase().split("\\W+");
		for (String word : words) {
			sb.append(Stemmer.stem(word));
			sb.append(" ");
		}
		return sb.toString();
	}
}
