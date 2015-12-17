package crawler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import utility.Tag;
import utility.Tags;
import utility.TopTags;
import utility.Track;
import utility.TrackWrapper;

import com.google.gson.Gson;

import filters.FilterFactory;

public class TrackInfoCrawler {

	public static void main(String[] args) throws IllegalStateException, IOException {
		
		String artist = "Bon Jovi";
		String songName = "It's My Life";
		allSongInformations(artist, songName);
	}
	
	public static void allSongInformations(String artist, String songName) 
			throws ClientProtocolException, IOException {
		// For song info crawling
		String query = "http://ws.audioscrobbler.com/2.0/?method=track.getInfo"
				+ "&artist=" 
				+ URLEncoder.encode(artist, "utf-8") 
				+ "&track="
				+ URLEncoder.encode(songName, "utf-8")
				+ "&api_key=dd499d6c71a646deb54725b96bf4baa8&format=json";
		
		Track track = getSongInfo(query);
		if (track != null) {
			if (track.getListeners() != null) {
				System.out.println("Listeners: " + track.getListeners());
				System.out.println("=====================");	
			}
			
			if (track.getPlaycount() != null) {
				System.out.println("Play count: " + track.getPlaycount());
				System.out.println("=====================");	
			}
			
			if (track.getWiki() != null) {
				System.out.println("Published on: " + track.getWiki().getPublishedDate());	
				System.out.println("=====================");
			}	
		}
		
		// For tags crawling
		query = "http://ws.audioscrobbler.com/2.0/?method=track.getToptags"
				+ "&artist=" 
				+ URLEncoder.encode(artist, "utf-8") 
				+ "&track=" 
				+ URLEncoder.encode(songName, "utf-8")
				+ "&api_key=dd499d6c71a646deb54725b96bf4baa8&format=json";
		
		Tags tags = getSongTags(query);
		if (tags != null && tags.getTag() != null) { 
			Tag[] filteredTags = FilterFactory.getTagFilter().filterTags(tags.getTag());
			System.out.println("Tags:");
			for (Tag tag : filteredTags) {
				System.out.println(tag);
			}
		}
	}

	public static Track getSongInfo(String query) throws IllegalStateException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpGet getMethod = new HttpGet(query);
		HttpResponse response = client.execute(getMethod);

		if (response.getStatusLine().getStatusCode() != 200) {
			return null;
		}

		InputStream is = response.getEntity().getContent();
		if (is == null) {
			return null;
		}

		TrackWrapper track = new Gson().fromJson(new InputStreamReader(is), TrackWrapper.class);
		if (track == null) {
			return null;
		}

		return track.getTrack();
	}
	

	public static Tags getSongTags(String query) throws ClientProtocolException, IOException {
		HttpClient client = new DefaultHttpClient();
		HttpGet getMethod = new HttpGet(query);
		HttpResponse response = client.execute(getMethod);

		if (response.getStatusLine().getStatusCode() != 200) {
			return null;
		}

		InputStream is = response.getEntity().getContent();
		if (is == null) {
			return null;
		}
		
		TopTags topTags = new Gson().fromJson(new InputStreamReader(is), TopTags.class);
		if (topTags == null) {
			return null;
		}
		
		return topTags.getToptags();
	}
}