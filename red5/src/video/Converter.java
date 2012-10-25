package video;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Converter {
	private static final String CONVERTED_EXTENSION = "flv";
	private static final Logger log = Logger.getLogger(Converter.class
			.getName());

	public static File convert(File input) throws IOException,
			InterruptedException {
		File script = getFile("/../convertVideo.sh");
		if (!script.canExecute()) {
			log.info("Converter script not available: " + script);
			return input;
		}
		File output = new File(changeExtension(input, CONVERTED_EXTENSION));
		String[] cmd = new String[3];
		cmd[0] = script.getAbsolutePath();
		cmd[1] = input.getAbsolutePath();
		cmd[2] = output.getAbsolutePath();
		log.info("Calling script " + script);
		Process process = Runtime.getRuntime().exec(cmd);
		process.waitFor();
		log.info("Script ready " + script);
		return output;
	}

	private static File getFile(String name) {
		String webAppRoot = System.getProperty("red5.webapp.root");
		return new File(webAppRoot + name);
	}

	// private static String ffmpeg(File input, File output) {
	// StringBuilder b = new StringBuilder();
	// b.append("ffmpeg");
	// b.append(" -i ");
	// b.append(input.getAbsolutePath());
	// b.append(" -r 20 -ar 44100 -ac 1 -b 300k ");
	// b.append(output.getAbsolutePath());
	// return b.toString();
	// }

	static String changeExtension(File file, String newExtension) {
		String originalName = file.getAbsolutePath();
		int lastDot = originalName.lastIndexOf(".");
		if (lastDot != -1) {
			return originalName.substring(0, lastDot + 1) + newExtension;
		} else {
			return originalName + newExtension;
		}
	}

	static String getExtension(File file) {
		String originalName = file.getAbsolutePath();
		int lastDot = originalName.lastIndexOf(".");
		if (lastDot != -1) {
			return originalName.substring(lastDot + 1);
		} else {
			return "";
		}
	}

	public static boolean requiresConversion(File original) {
		return !CONVERTED_EXTENSION.equalsIgnoreCase(getExtension(original));
	}
}
