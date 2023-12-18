import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class VideoReader {
	static String[][] Sources = new String[][] {
		{"kata1", "/Users/josejuanhernandez/Downloads/40975-Kata 1. URL del video-329421"},
		{"kata2", "/Users/josejuanhernandez/Downloads/40975-Kata 2. URL del video-329422"},
		{"kata3", "/Users/josejuanhernandez/Downloads/40975-Kata 3. URL del video-329423"},
		{"kata4", "/Users/josejuanhernandez/Downloads/40975-Kata 4. URL del video-329424"},
		{"kata5", "/Users/josejuanhernandez/Downloads/40975-Kata 5. URL del video-329425"},
		{"kata6", "/Users/josejuanhernandez/Downloads/40975-Kata 6. URL del video-329426"},
		{"kata7", "/Users/josejuanhernandez/Downloads/40975-Kata 7. URL del video-329427"}
	};
	static Path Target = Path.of("triples.md");
	static String Separator = "# " + (LocalDate.now().toString() + "\n\n");

	public static void main(String[] args) throws IOException {
		Set<String> set = Target.toFile().exists() ? new HashSet<>(Files.readAllLines(Target)) : Set.of();
		Files.write(Target, Separator.getBytes(), CREATE, APPEND);
		for (String[] source : Sources) {
			File root = new File(source[1]);
			if (!root.exists()) continue;
			File[] files = root.listFiles();
			for (File file : files) {
				if (!file.isDirectory()) continue;
				String name = file.getName().split("_")[0];
				String url = extractUrl(contentOf(file));
				String triple = "* " + name + " [" + source[0] + "](" + url + ")";
				if (set.contains(triple)) continue;
				Files.write(Target, (triple + "\n\n").getBytes(), APPEND);
			}
		}
	}

	private static String extractUrl(String data) {
		try {
			int i = data.indexOf("http");
			int j = Math.min(x(data.indexOf("<", i)), x(data.indexOf("\"", i)));
			return data.substring(i, j).replace("&amp;", "&");
		}
		catch (Exception e) {
			return "Video mal subido";
		}
	}

	private static int x(int i) {
		return i < 0 ? Integer.MAX_VALUE : i;
	}

	private static String contentOf(File file) throws IOException {
		return Files.readAllLines(new File(file, "onlinetext.html").toPath()).get(0);
	}
}
