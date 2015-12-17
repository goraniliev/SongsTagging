package utility;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Wiki {
	
	private String published;
	private String summary;
	private String content;
	
	private static final Map<String, Integer> monthIdx;
	
	static {
		monthIdx = new HashMap<>();
		monthIdx.put("Jan", 0);
		monthIdx.put("Feb", 1);
		monthIdx.put("Mar", 2);
		monthIdx.put("Apr", 3);
		monthIdx.put("May", 4);
		monthIdx.put("Jun", 5);
		monthIdx.put("Jul", 6);
		monthIdx.put("Aug", 7);
		monthIdx.put("Sep", 8);
		monthIdx.put("Oct", 9);
		monthIdx.put("Nov", 10);
		monthIdx.put("Dec", 11);
	}
	
	public Wiki(String published, String summary, String content) {
		this.published = published;
		this.summary = summary;
		this.content = content;
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
		this.published = published;
	}

	@SuppressWarnings("deprecation")
	public Date getPublishedDate() {
		String[] parts = this.published.split(" ");
		int day = Integer.parseInt(parts[0]);
		int month = Wiki.monthIdx.get(parts[1]);
		int year = Integer.parseInt(parts[2].substring(0, parts[2].length() - 1)) - 1900;
		return new Date(year, month, day);
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
