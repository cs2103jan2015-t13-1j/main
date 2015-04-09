package resources;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

public class ResourceUtil {

	public static File makeTemporaryFromResource(String resourcePath) throws IOException {
		File temp = File.createTempFile(resourcePath, ".tmp");
		Files.copy(ResourceUtil.class.getResourceAsStream(resourcePath), temp.toPath());
		return temp;
	}
	
}
