package filters.impl;

import filters.TagFilter;
import utility.Tag;

public class TrimTagFilter extends AbstractTagFilter {
	
	

	public TrimTagFilter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TrimTagFilter(TagFilter filter) {
		super(filter);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Tag[] filter(Tag[] tags) {
		for (int i = 0; i < tags.length; ++i) {
			trim(tags[i]);
		}

		return tags;
	}

	private void trim(Tag tag) {
		StringBuilder sb = new StringBuilder();
		String tagName = tag.getName().toLowerCase();
		String[] parts = tagName.split("\\W+");
		for (int i = 0; i < parts.length; ++i) {
			sb.append(trim(parts[i]));
			sb.append(" ");
		}

		tag.setName(sb.toString());
	}

	private static String trim(String tag) {
		int count = 0;
		char lastChar = tag.charAt(0);
		boolean flag = true;
		StringBuilder sb = new StringBuilder();
		sb.append(tag.charAt(0));
		
		for (int i = 1; i < tag.length(); ++i) {
			if (tag.charAt(i) == lastChar) {
				count++;
			} else {
				lastChar = tag.charAt(i);
				count = 0;
				flag = true;
			}
			
			if(count <= 1){
				sb.append(lastChar);
			}
			else if(flag){
				sb.deleteCharAt(sb.length()-1);
				flag = false;
			}
		}
		
		return sb.toString();
	}
	
}
