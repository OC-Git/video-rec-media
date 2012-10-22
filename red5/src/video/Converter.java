package video;

import java.io.File;
import java.io.IOException;

public class Converter {

	public static File convert(File input) throws IOException,
			InterruptedException {
		File output = new File(changeExtension(input, "flv"));
		String cmd = ffmpeg(input, output);
		Process process = Runtime.getRuntime().exec(cmd);
		process.waitFor();
		return output;
		// System.out.println(cmd);
		// return input;
	}

	private static String ffmpeg(File input, File output) {
		StringBuilder b = new StringBuilder();
		b.append("ffmpeg");
		b.append(" -i ");
		b.append(input.getAbsolutePath());
		b.append(" -r 20 -ar 44100 -ac 1 -b 300k ");
		b.append(output.getAbsolutePath());
		return b.toString();
	}

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
}
