package stream;

import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppClient {
	private static final Logger log = Logger.getLogger(AppClient.class
			.getName());
	private static final int OK = 200;

	private String base;

	public AppClient() {
		base = System.getenv("APPURL");
		if (base == null)
			base = "http://localhost:9000";
	}

	private int post(String uri, Map<String, String> params) {
		try {
			log.info(base + uri);
			URL u = new URL(base + uri);
			HttpURLConnection connection = (HttpURLConnection) u
					.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");

			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
			wr.writeBytes(getParamString(params));
			wr.flush();
			wr.close();

			connection.getInputStream();
			int responseCode = connection.getResponseCode();
			connection.disconnect();
			return responseCode;
		} catch (Exception e) {
			log.log(Level.SEVERE, base + uri + ": " + e.getMessage());
			return 0;
		}
	}

	private String getParamString(Map<String, String> params) {
		StringBuilder b = new StringBuilder();
		for (Entry<String, String> entry : params.entrySet()) {
			if (b.length() > 0)
				b.append("&");
			b.append(entry.getKey());
			b.append("=");
			try {
				b.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return b.toString();
	}

	public void published(String client, String title, String page,
			String category, String description, String id, String key) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("title", title);
		params.put("page", page);
		params.put("category", category);
		params.put("description", description);
		params.put("publishedId", id);
		params.put("key", key);
		int result = post("/" + client + "/published", params);
		if (result != OK)
			log.log(Level.SEVERE, "published call status: " + result);
	}
}
