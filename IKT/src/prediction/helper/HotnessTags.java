package prediction.helper;

public class HotnessTags {
	private Double hotness;
	private Integer numberOfTags;
	
	public HotnessTags(Double hotness) {
		this.hotness = hotness;
		numberOfTags = 1;
	}
	
	public Double getHotness() {
		return hotness;
	}
	public Integer getNumberOfTags() {
		return numberOfTags;
	}
	
	public void addTag() {
		numberOfTags++;
	}
}
