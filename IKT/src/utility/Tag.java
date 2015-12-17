package utility;

public class Tag {

	private String name;
	private Integer count;
	private String url;

	public Tag(String name, Integer count, String url) {
		this.name = name;
		this.count = count;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return this.count + " " + this.name;
	}

}
