package stream;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.red5.server.api.IConnection;
import org.red5.server.api.Red5;
import org.red5.server.api.stream.IBroadcastStream;
import org.red5.server.stream.ClientBroadcastStream;

/**
 * <code>StreamManager</code> provides services for recording the broadcast
 * stream.
 */
public class StreamManager {

	private static final Logger logger = Logger.getLogger(StreamManager.class
			.getName());

	// Application components
	private Application app;

	/**
	 * Start recording the publishing stream for the specified
	 * <code>IConnection</code>.
	 */
	public void recordShow() {
		IConnection conn = Red5.getConnectionLocal();
		logger.finer("Recording show for: " + conn.getScope().getContextPath());
		String fileName = String.valueOf(System.currentTimeMillis());
		String streamName = conn.getStringAttribute("stream");
		// Get a reference to the current broadcast stream.
		IBroadcastStream stream = app.getBroadcastStream(conn.getScope(),
				streamName);
		try {
			// Save the stream to disk.
			stream.saveAs(streamName, false);
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Error while saving stream: " + streamName, e);
		}
	}

	/**
	 * Stops recording the publishing stream for the specified
	 * <code>IConnection</code>.
	 */
	public void stopRecordingShow() {
		IConnection conn = Red5.getConnectionLocal();
		logger.finer("Stop recording show for: "
				+ conn.getScope().getContextPath());
		// Get a reference to the current broadcast stream.
		String streamName = conn.getStringAttribute("stream");
		ClientBroadcastStream stream = (ClientBroadcastStream) app
				.getBroadcastStream(conn.getScope(), streamName);
		// Stop recording.
		String filename = stream.getSaveFilename();
		conn.setAttribute("filename", filename);
		stream.stopRecording();
		logger.info("Saved as " + filename);
	}

	private File getFile(String filename) {
		String webAppRoot = System.getProperty("red5.webapp.root");
		StringBuilder b = new StringBuilder(webAppRoot);
		b.append('/');
		b.append(app.getName());
		b.append('/');
		b.append(filename);
		File file = new File(b.toString());
		return file;
	}

	public void publishRecording() {
		IConnection conn = Red5.getConnectionLocal();
		String filename = (String) conn.getAttribute("filename");
		logger.info("publish recording show for: "
				+ conn.getScope().getContextPath() + " " + filename);
		try {
			String client = conn.getStringAttribute("client");
			String title = conn.getStringAttribute("title");
			String page = conn.getStringAttribute("page");
			String category = conn.getStringAttribute("category");
			String description = conn.getStringAttribute("description");
			String key = conn.getStringAttribute("key");
			// String id = Publisher.publish(getFile(filename), title, category,
			// description);
			// new AppClient().published(client, title, page, category,
			// description, id, key);
			new AppClient().upload(getFile(filename), client, title, page,
					category, description, key);
			logger.info("published recording show for: "
					+ conn.getScope().getContextPath() + " " + filename);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error while publishing: " + filename, e);
		}
	}

	public void deleteRecording() {
		IConnection conn = Red5.getConnectionLocal();
		String filename = (String) conn.getAttribute("filename");
		logger.info("delete recording show for: "
				+ conn.getScope().getContextPath() + " " + filename);
		File file = getFile(filename);
		file.delete();
	}

	/* ----- Spring injected dependencies ----- */

	public void setApplication(Application app) {
		this.app = app;
	}
}
