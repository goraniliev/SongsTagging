package prediction.helper;

import java.util.LinkedList;
import java.util.List;

public class HotnessListTags {
	private double hotness;
	private List<Integer> tags;
	
	public HotnessListTags(double hotness) {
		this.hotness = hotness;
		tags = new LinkedList<Integer>();
	}

	public double getHotness() {
		return hotness;
	}

	public List<Integer> getTags() {
		return tags;
	}
	
	public void addTag(Integer tag) {
		tags.add(tag);
	}
}
