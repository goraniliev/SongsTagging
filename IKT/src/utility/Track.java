package utility;

public class Track {

	private String name;
	private Tags toptags;
	private Integer listeners;
	private Integer playcount;
	private Wiki wiki;

	public Track(String name, Tags toptags, Integer listeners, Integer playcount, Wiki wiki) {
		this.name = name;
		this.toptags = toptags;
		this.listeners = listeners;
		this.playcount = playcount;
		this.wiki = wiki;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Tags getToptags() {
		return toptags;
	}

	public void setToptags(Tags toptags) {
		this.toptags = toptags;
	}

	public Integer getListeners() {
		return listeners;
	}

	public void setListeners(Integer listeners) {
		this.listeners = listeners;
	}

	public Integer getPlaycount() {
		return playcount;
	}

	public void setPlaycount(Integer playcount) {
		this.playcount = playcount;
	}

	public Wiki getWiki() {
		return wiki;
	}

	public void setWiki(Wiki wiki) {
		this.wiki = wiki;
	}

}