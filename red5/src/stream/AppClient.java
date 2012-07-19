package stream;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;

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

	private int post(String uri, Map<String, String> params, File file) {
		try {
			log.info(base + uri);
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(
					CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

			HttpPost httppost = new HttpPost(base + uri);

			MultipartEntity mpEntity = new MultipartEntity();
			if (file != null) {
				ContentBody cbFile = new FileBody(file, "video/flv");
				mpEntity.addPart("file", cbFile);
			}
			Charset utf8 = Charset.forName("UTF-8");
			for (Entry<String, String> entry : params.entrySet()) {
				mpEntity.addPart(entry.getKey(),
						new StringBody(entry.getValue(), utf8));
			}

			httppost.setEntity(mpEntity);
			HttpResponse response = httpclient.execute(httppost);
			int statusCode = response.getStatusLine().getStatusCode();
			httpclient.getConnectionManager().shutdown();
			return statusCode;
		} catch (Exception e) {
			log.log(Level.SEVERE, base + uri + ": " + e.getMessage());
			return 0;
		}
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
		int result = post("/" + client + "/published", params, null);
		if (result != OK)
			log.log(Level.SEVERE, "published call status: " + result);
	}

	public void upload(File file, String client, String title, String page,
			String category, String description, String key) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("title", title);
		params.put("page", page);
		params.put("category", category);
		params.put("description", description);
		params.put("publishedId", "");
		params.put("key", key);
		int result = post("/" + client + "/upload", params, file);
		if (result != OK)
			log.log(Level.SEVERE, "published call status: " + result);
	}
}
