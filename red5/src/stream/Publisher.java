package stream;

import java.io.File;
import java.net.URL;
import java.util.logging.Logger;

import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaDescription;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaTitle;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YouTubeNamespace;

public class Publisher {

	private static final Logger logger = Logger.getLogger(Publisher.class
			.getName());

	public static void main(String[] args) {
		File file = new File("/Users/viola/Desktop/1339404332723.flv");
		try {
			publish(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void publish(File file) throws Exception {
		logger.info("Publishing to Youtube: " + file.getAbsolutePath());
		String clientID = "lean-video-recording";
		String developer_key = "AI39si642bz59kjhdF7sZOLDlCL2BjQT8_c9mtdBLRhvv8CB4KP8TApMj7Q94AOYhIBS5jfLENomZ0fuOywEKzrBk3Aqw2bdDQ";
		YouTubeService service = new YouTubeService(clientID, developer_key);
		service.setUserCredentials("joerg.viola@gmail.com",
				"yDYf8PfWczURuKUPiuliUjPjZXO3uLIVP");

		VideoEntry newEntry = new VideoEntry();

		YouTubeMediaGroup mg = newEntry.getOrCreateMediaGroup();
		mg.setTitle(new MediaTitle());
		mg.getTitle().setPlainTextContent("Another Recorded Video");
		mg.addCategory(new MediaCategory(YouTubeNamespace.CATEGORY_SCHEME,
				"Autos"));
		mg.setKeywords(new MediaKeywords());
		mg.getKeywords().addKeyword("recording");
		mg.setDescription(new MediaDescription());
		mg.getDescription().setPlainTextContent("My description");
		mg.setPrivate(false);
		mg.addCategory(new MediaCategory(YouTubeNamespace.DEVELOPER_TAG_SCHEME,
				"recording"));

		// newEntry.setGeoCoordinates(new GeoRssWhere(37.0, -122.0));
		// alternatively, one could specify just a descriptive string
		// newEntry.setLocation("Mountain View, CA");

		MediaFileSource ms = new MediaFileSource(file, "video/quicktime");
		newEntry.setMediaSource(ms);

		String uploadUrl = "http://uploads.gdata.youtube.com/feeds/api/users/default/uploads";

		VideoEntry createdEntry = service.insert(new URL(uploadUrl), newEntry);
	}

}
