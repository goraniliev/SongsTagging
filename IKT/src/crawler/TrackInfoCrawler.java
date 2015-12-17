package crawler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import utility.Tag;
import utility.Track;
import utility.TrackWrapper;

import com.google.gson.Gson;

public class TrackInfoCrawler {

	public static void main(String[] args) throws IllegalStateException, IOException {
		
		String artist = "Bon Jovi";
		String songName = "It's My Life";
		String query = "http://ws.audioscrobbler.com/2.0/?method=track.getInfo"
				+ "&artist=" 
				+ URLEncoder.encode(artist, "utf-8") 
				+ "&track="
				+ URLEncoder.encode(songName, "utf-8")
				+ "&api_key=dd499d6c71a646deb54725b96bf4baa8&format=json";
		
		Track track = getTrackInfo(query);
		
		if (track != null) {
			if (track.getToptags() != null) {
				System.out.println("Tags:");
				for (Tag tag : track.getToptags().getTag()) {
					System.out.println(tag.getName());
				}
				System.out.println("=====================");
			}
			
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
			}	
		}
	}

	public static Track getTrackInfo(String query) throws IllegalStateException, IOException {
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
}
