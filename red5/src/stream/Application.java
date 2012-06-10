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

	/** {@inheritDoc} */
	@Override
	public void appDisconnect(IConnection conn) {
		log.info("videorec appDisconnect");
		super.appDisconnect(conn);
	}
}
