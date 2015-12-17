package utility;

import java.util.List;
import java.util.Date;

public class TrackAllInfo {
	
	private String trackName;
	private String artistName;
	private Integer numListeners;
	private Integer playCount;
	private Date publishedDate;
	private List<String> tags;
	
	public TrackAllInfo() {
		
	}
	
	public TrackAllInfo(String trackName, String artistName,
			Integer numListeners, Integer playCount, Date publishedDate,
			List<String> tags) {
		
		this.trackName = trackName;
		this.artistName = artistName;
		this.numListeners = numListeners;
		this.playCount = playCount;
		this.publishedDate = publishedDate;
		this.tags = tags;
	}

	public String getTrackName() {
		return trackName;
	}

	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public Integer getNumListeners() {
		return numListeners;
	}

	public void setNumListeners(Integer numListeners) {
		this.numListeners = numListeners;
	}

	public Integer getPlayCount() {
		return playCount;
	}

	public void setPlayCount(Integer playCount) {
		this.playCount = playCount;
	}

	public Date getPublishedDate() {
		return publishedDate;
	}

	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Artist name: " + this.artistName);
		sb.append("\n");
		sb.append("==============");
		sb.append("\n");
		sb.append("Track name: " + this.trackName);
		sb.append("\n");
		sb.append("==============");
		sb.append("\n");
		sb.append("Number of listeners: " + this.numListeners);
		sb.append("\n");
		sb.append("==============");
		sb.append("\n");
		sb.append("Play count: " + this.playCount);
		sb.append("\n");
		sb.append("==============");
		sb.append("\n");
		sb.append("Published date: " + this.publishedDate);
		sb.append("\n");
		sb.append("==============");
		sb.append("\n");
		sb.append("Tags:");
		sb.append("\n");
		for (String tag : this.tags) {
			sb.append(tag);
			sb.append("\n");
		}
		return sb.toString();
	}
}
