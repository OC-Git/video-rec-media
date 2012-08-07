package stream;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.Red5;
import org.red5.server.api.scope.IScope;
import org.red5.server.api.stream.IBroadcastStream;

public class Application extends ApplicationAdapter {

	private static final Logger log = Logger.getLogger(Application.class
			.getName());

	@Override
	public boolean appStart(IScope app) {
		super.appStart(app);
		log.info("videorec appStart");
		return true;
	}

	@Override
	public boolean appConnect(IConnection conn, Object[] params) {
		if (params.length != 7) {
			log.info("videorec appConnect illegal param count");
			rejectClient("Illegal param count");
			return false;
		}
		conn.setAttribute("stream", params[0]);
		conn.setAttribute("client", params[1]);
		conn.setAttribute("page", params[2]);
		conn.setAttribute("title", params[3]);
		conn.setAttribute("category", params[4]);
		conn.setAttribute("description", params[5]);
		conn.setAttribute("key", params[6]);
		log.info("videorec appConnect: " + params[0] + " " + params[1] + " "
				+ params[2] + " " + params[3] + " " + params[4] + " "
				+ params[5] + " " + params[6]);
		return super.appConnect(conn, params);
	}

	/** {@inheritDoc} */
	@Override
	public void appDisconnect(IConnection conn) {
		log.info("videorec appDisconnect");
		super.appDisconnect(conn);
	}

	@Override
	public void streamPublishStart(IBroadcastStream stream) {
		super.streamPublishStart(stream);
		try {
			log.info("videorec start saving");
			// Das ist ein ClientBroadcastStream
			String name = stream.getPublishedName();
			stream.saveAs(name, false);
			String filename = stream.getSaveFilename();
			IConnection conn = Red5.getConnectionLocal();
			conn.setAttribute("filename", filename);
		} catch (Exception e) {
			log.log(Level.SEVERE, "on stream record", e);
		}
	}
}
