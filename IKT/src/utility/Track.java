package utility;

public class Track {

	private String name;
	private Integer listeners;
	private Integer playcount;
	private Artist artist;
	private Wiki wiki;

	public Track(String name, Integer listeners, Integer playcount, Artist artist, Wiki wiki) {
		this.name = name;
		this.listeners = listeners;
		this.playcount = playcount;
		this.artist = artist;
		this.wiki = wiki;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Artist getArtist() {
		return artist;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

	public Wiki getWiki() {
		return wiki;
	}

	public void setWiki(Wiki wiki) {
		this.wiki = wiki;
	}

}