package stream;

import java.util.logging.Logger;

import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.stream.IStreamPublishSecurity;

public class StreamPublishSecurityHandler implements IStreamPublishSecurity {

	private static final Logger log = Logger
			.getLogger(StreamPublishSecurityHandler.class.getName());

	public StreamPublishSecurityHandler() {
	}

	@Override
	public boolean isPublishAllowed(IScope arg0, String name, String mode) {
		IConnection connection = Red5.getConnectionLocal();
		String ticket = connection.getStringAttribute("ticket");
		connection.setAttribute("stream-name", name);
		boolean ok = TicketManager.get().allocateForPublish(name, ticket);
		if (ok)
			log.info("66and33 ticket publish access granted for " + ticket);
		else
			log.info("66and33 ticket publish access denied for " + ticket);
		return ok;
	}
}
