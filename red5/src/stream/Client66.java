package stream;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client66 {
	private static final Logger log = Logger
			.getLogger(Client66.class.getName());
	private static final int OK = 200;

	private String base;

	public Client66() {
		base = System.getenv("URL66");
		if (base == null)
			base = "http://localhost:9000";
	}

	public boolean checkPlaybackTicket(String name, String ticket) {
		name = getStream(name);
		log.info("Checking playback ticket: " + name + ", " + ticket);
		return OK == call("/seminar/" + name + "/attend/" + ticket);
	}

	public boolean checkPublishTicket(String name, String ticket) {
		name = getStream(name);
		log.info("Checking publish ticket: " + name + ", " + ticket);
		return OK == call("/seminar/" + name + "/show/" + ticket);
	}

	private String getStream(String stream) {
		if (stream.endsWith("-screen"))
			stream = stream.substring(0, stream.lastIndexOf("-"));
		return stream;
	}

	private int call(String uri) {
		try {
			log.info(base + uri);
			URL u = new URL(base + uri);
			HttpURLConnection connection = (HttpURLConnection) u
					.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");

			connection.connect();
			connection.getInputStream();
			int responseCode = connection.getResponseCode();
			connection.disconnect();
			return responseCode;
		} catch (Exception e) {
			log.log(Level.SEVERE, base + uri + ": " + e.getMessage());
			return 0;
		}
	}
}
