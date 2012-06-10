package stream;

import java.util.logging.Logger;

import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.Red5;
import org.red5.server.api.stream.IStreamPlaybackSecurity;

public class StreamPlaybackSecurityHandler implements IStreamPlaybackSecurity {

	private static final Logger log = Logger
			.getLogger(StreamPlaybackSecurityHandler.class.getName());

	@Override
	public boolean isPlaybackAllowed(IScope scope, String name, int start,
			int length, boolean flushPlaylist) {
		IConnection connection = Red5.getConnectionLocal();
		String ticket = connection.getStringAttribute("ticket");
		connection.setAttribute("stream-name", name);
		boolean ok = TicketManager.get().allocateForPlayback(name, ticket);
		if (ok)
			log.info("66and33 ticket playback access granted for " + ticket);
		else
			log.info("66and33 ticket playback access denied for " + ticket);
		return ok;
	}

}
