package stream;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class TicketManager {

	private static final Logger log = Logger.getLogger(TicketManager.class
			.getName());

	static class Ticket {
		int countVideo = 0;
		int countScreen = 0;
		int max = 1;
		boolean publish;
		private final String key;

		public Ticket(String key, boolean publish) {
			this.key = key;
			this.publish = publish;
		}

		public boolean alloc(boolean publish, String name) {
			if (publish != this.publish)
				return false;
			if (name.endsWith("-screen"))
				return allocScreen();
			else
				return allocVideo();
		}

		private boolean allocVideo() {
			log.info("alloc video: " + this);
			if (countVideo >= max)
				return false;
			countVideo++;
			return true;
		}

		private boolean allocScreen() {
			log.info("alloc screen: " + this);
			if (countScreen >= max)
				return false;
			countScreen++;
			return true;
		}

		public boolean free(String name) {
			if (name.endsWith("-screen"))
				freeScreen();
			else
				freeVideo();
			return countScreen == 0 && countVideo == 0;
		}

		private void freeVideo() {
			log.info("free video: " + this);
			if (countVideo == 0)
				log.warning("Releasing unused ticket/video: " + this);
			else
				countVideo--;
		}

		private void freeScreen() {
			log.info("free screen: " + this);
			if (countScreen == 0)
				log.warning("Releasing unused ticket/screen: " + this);
			else
				countScreen--;
		}

		@Override
		public String toString() {
			StringBuilder b = new StringBuilder("Ticket[");
			b.append(key);
			if (publish)
				b.append(", publish, ");
			else
				b.append(", playback, ");
			b.append(countVideo);
			b.append(", ");
			b.append(countScreen);
			b.append("]");
			return b.toString();
		}
	}

	private static TicketManager _instance;

	public static TicketManager get() {
		if (_instance == null) {
			synchronized (TicketManager.class) {
				if (_instance == null) {
					_instance = new TicketManager();
				}
			}
		}
		return _instance;
	}

	public TicketManager() {
		client = new Client66();
	}

	private Client66 client;
	Map<String, Ticket> tickets = new HashMap<String, Ticket>();

	public boolean allocateForPlayback(String name, String key) {
		Ticket ticket = tickets.get(key);
		if (ticket != null)
			return ticket.alloc(false, name);
		if (!client.checkPlaybackTicket(name, key))
			return false;
		ticket = new Ticket(key, false);
		tickets.put(key, ticket);
		return ticket.alloc(false, name);
	}

	public boolean allocateForPublish(String name, String key) {
		Ticket ticket = tickets.get(key);
		if (ticket != null)
			return ticket.alloc(true, name);
		if (!client.checkPublishTicket(name, key))
			return false;
		ticket = new Ticket(key, true);
		tickets.put(key, ticket);
		return ticket.alloc(true, name);
	}

	public void releaseTicket(String key, String name) {
		Ticket ticket = tickets.get(key);
		if (ticket == null) {
			log.warning("Releasing unknown ticket: " + ticket);
			return;
		}

		if (ticket.free(name)) {
			log.warning("Removing ticket: " + ticket);
			tickets.remove(key);
		}
	}
}
