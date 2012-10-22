package video;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jackson.JsonNode;

import stream.AppClient;
import stream.Application;
import stream.Publisher;

public class Handler {
	private static final Logger log = Logger.getLogger(Handler.class.getName());

	public static void handle(File original, boolean convert, String client,
			String title, String page, String category, String description,
			String key) throws Exception {
		try {
			log.info("Received upload: client=" + client + ", title=" + title
					+ ", page=" + page + ", category=" + category + ", key="
					+ key);
			AppClient appClient = new AppClient();
			JsonNode node = appClient.create(client, title, page, category,
					description, key, "", "");
			int id = node.get("id").asInt();
			String token = node.get("token").asText();
			log.info("Reserved id: " + id + ", token=" + token);
			S3 s3 = new S3();
			File converted = original;
			if (convert) {
				String originalKey = client + "/" + id + ".original."
						+ Converter.getExtension(original);
				s3.upload(originalKey, original);
				log.info("Uploaded original to S3: " + originalKey);

				converted = Converter.convert(original);
				log.info("Converted file: " + converted);
			}
			String filename = "converted." + Converter.getExtension(converted);
			String convertedKey = client + "/" + id + "." + filename;
			s3.upload(convertedKey, converted);
			log.info("Uploaded converted to S3: " + convertedKey);
			String publishedId = "";
			if (token != null && token.length() > 0) {
				publishedId = Publisher.publish(converted, title, category,
						description, token);
				log.info("Published as: " + publishedId);
			}
			appClient.update(id, client, title, page, category, description,
					key, publishedId, filename);
			log.info("Updated in DB");
			// original.delete();
			// converted.delete();
		} catch (Exception e) {
			log.log(Level.SEVERE, "handling", e);
			throw e;
		}
	}
}
