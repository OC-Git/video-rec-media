package stream;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

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

	private JsonNode post(String uri, Map<String, String> params)
			throws Exception {
		try {
			log.info(base + uri);
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(
					CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

			HttpPost httppost = new HttpPost(base + uri);

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
					params.size());
			for (Entry<String, String> entry : params.entrySet()) {
				nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			int statusCode = response.getStatusLine().getStatusCode();
			log.info(response.getStatusLine().toString());
			InputStream content = response.getEntity().getContent();
			JsonNode node = new ObjectMapper().readValue(content,
					JsonNode.class);
			httpclient.getConnectionManager().shutdown();
			return node;
		} catch (Exception e) {
			log.log(Level.SEVERE, base + uri + ": " + e.getMessage());
			throw e;
		}
	}

	public JsonNode create(String client, String title, String page,
			String category, String description, String key,
			String publishedId, String filename) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("title", title);
		params.put("page", page);
		params.put("category", category);
		params.put("description", description);
		params.put("key", key);
		params.put("publishedId", publishedId);
		params.put("filename", filename);
		return post("/" + client + "/create", params);
	}

	public void update(int id, String client, String title, String page,
			String category, String description, String key,
			String publishedId, String filename) throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("title", title);
		params.put("page", page);
		params.put("category", category);
		params.put("description", description);
		params.put("key", key);
		params.put("publishedId", publishedId);
		params.put("filename", filename);
		post("/" + client + "/update/" + id, params);
	}
}
