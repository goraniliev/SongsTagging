package utility;

public class Artist {

	private String name;
	private String mbid;
	private String url;
	
	public Artist(String name, String mbid, String url) {
		this.name = name;
		this.mbid = mbid;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMbid() {
		return mbid;
	}

	public void setMbid(String mbid) {
		this.mbid = mbid;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
