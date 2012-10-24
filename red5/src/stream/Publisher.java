package stream;

import java.io.File;
import java.net.URL;
import java.util.logging.Level;
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

	public static String publish(File file, String title, String category,
			String description, String accessToken) {
		logger.info("Publishing to Youtube: " + file.getAbsolutePath());
		String clientID = "lean-video-recording";
		String developer_key = "AI39si642bz59kjhdF7sZOLDlCL2BjQT8_c9mtdBLRhvv8CB4KP8TApMj7Q94AOYhIBS5jfLENomZ0fuOywEKzrBk3Aqw2bdDQ";
		YouTubeService service = new YouTubeService(clientID, developer_key);
		service.setAuthSubToken(accessToken);

		VideoEntry newEntry = new VideoEntry();

		YouTubeMediaGroup mg = newEntry.getOrCreateMediaGroup();
		mg.setTitle(new MediaTitle());
		mg.getTitle().setPlainTextContent(conv(title));
		mg.addCategory(new MediaCategory(YouTubeNamespace.CATEGORY_SCHEME,
				"People"));
		mg.setKeywords(new MediaKeywords());
		mg.getKeywords().addKeyword(conv(category));
		mg.setDescription(new MediaDescription());
		mg.getDescription().setPlainTextContent(conv(description));
		mg.setPrivate(false);
		mg.addCategory(new MediaCategory(YouTubeNamespace.DEVELOPER_TAG_SCHEME,
				category));

		// newEntry.setGeoCoordinates(new GeoRssWhere(37.0, -122.0));
		// alternatively, one could specify just a descriptive string
		// newEntry.setLocation("Mountain View, CA");

		MediaFileSource ms = new MediaFileSource(file, "video/quicktime");
		newEntry.setMediaSource(ms);

		String uploadUrl = "http://uploads.gdata.youtube.com/feeds/api/users/default/uploads";

		try {
			VideoEntry createdEntry = service.insert(new URL(uploadUrl),
					newEntry);
			// tag:youtube.com,2008:video:9yCGrfG9vRk
			String id = createdEntry.getId();
			return id.substring(id.lastIndexOf(':') + 1);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "On publish", e);
			return "";
		}
	}

	private static String conv(String s) {
		if (s == null || s.length() == 0)
			return "Video";
		return s.replaceAll("ä", "ae").replaceAll("Ä", "Ae")
				.replaceAll("ö", "oe").replaceAll("Ö", "Oe")
				.replaceAll("ü", "ue").replaceAll("U", "Ue")
				.replaceAll("ß", "ss").replaceAll(">", ")")
				.replaceAll("<", "(");
	}

}
