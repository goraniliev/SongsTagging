package prediction.helper;

public class HotnessTags {
	private Integer numberOfTags;
	
	public HotnessTags() {
		numberOfTags = 1;
	}

	public Integer getNumberOfTags() {
		return numberOfTags;
	}
	
	public void addTag() {
		numberOfTags++;
	}
}
