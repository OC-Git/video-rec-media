package stream;

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
		String streamName = String.valueOf(System.currentTimeMillis());
		// Get a reference to the current broadcast stream.
		IBroadcastStream stream = app.getBroadcastStream(conn.getScope(),
				"blabla");
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
		ClientBroadcastStream stream = (ClientBroadcastStream) app
				.getBroadcastStream(conn.getScope(), "blabla");
		// Stop recording.
		String filename = stream.getSaveFilename();
		conn.setAttribute("filename", filename);
		stream.stopRecording();
		logger.info("Saved as " + filename);
	}

	public void publishRecording() {
		IConnection conn = Red5.getConnectionLocal();
		ClientBroadcastStream stream = (ClientBroadcastStream) app
				.getBroadcastStream(conn.getScope(), "blabla");
		String filename = (String) conn.getAttribute("filename");
		logger.info("publish recording show for: "
				+ conn.getScope().getContextPath() + " " + filename);
	}

	public void deleteRecording() {
		IConnection conn = Red5.getConnectionLocal();
		ClientBroadcastStream stream = (ClientBroadcastStream) app
				.getBroadcastStream(conn.getScope(), "blabla");
		String filename = (String) conn.getAttribute("filename");
		logger.info("delete recording show for: "
				+ conn.getScope().getContextPath() + " " + filename);
	}

	/* ----- Spring injected dependencies ----- */

	public void setApplication(Application app) {
		this.app = app;
	}
}
