package stream;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.red5.server.adapter.ApplicationAdapter;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.stream.IBroadcastStream;
import org.red5.server.api.stream.ISubscriberStream;
import org.red5.server.stream.ClientBroadcastStream;

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
		if (params.length != 5) {
			log.info("videorec appConnect illegal param count");
			rejectClient("Illegal param count");
			return false;
		}
		conn.setAttribute("client", params[0]);
		conn.setAttribute("page", params[1]);
		conn.setAttribute("title", params[2]);
		conn.setAttribute("category", params[3]);
		conn.setAttribute("description", params[4]);
		log.info("videorec appConnect: " + params[0] + " " + params[1] + " "
				+ params[2] + " " + params[3] + " " + params[4]);
		return super.appConnect(conn, params);
	}

	/** {@inheritDoc} */
	@Override
	public void appDisconnect(IConnection conn) {
		log.info("videorec appDisconnect");
		super.appDisconnect(conn);
	}
}
