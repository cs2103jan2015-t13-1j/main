package resources;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import organizer.gui.MainApp;
import sun.misc.Launcher;
public class ResourceUtil {

	private final static String TEMP_FOLDER_NAME =  "c:\\temp\\";

	//@author A0113627L
	public static File makeTemporaryFromResource(String resourcePath) throws IOException {
		File temp = File.createTempFile(resourcePath, ".tmp");
		Files.copy(ResourceUtil.class.getResourceAsStream(resourcePath), temp.toPath());
		return temp;
	}

	//@author A0098824N
	public static File makeTemporaryFromResourceFolder(String resourcePath) throws IOException, URISyntaxException {
		String path = MainApp.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		path = URLDecoder.decode(path, "UTF-8");
		System.out.println(path);
		
		File sourceFile = new File(path.concat(resourcePath));
		File destinationFile = new File(TEMP_FOLDER_NAME);
		Path sourcePath = sourceFile.toPath();
		Path destinationPath = destinationFile.toPath();
	    
		Files.walkFileTree(sourcePath, new CopyDirVisitor(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING));
		deleteDirOnExit(destinationFile);
		return destinationFile;
	}
	

	public static void deleteDirOnExit(File tempResourceDir) {
		tempResourceDir.deleteOnExit();
		File[] files = tempResourceDir.listFiles();
		
		for(int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()){
				deleteDirOnExit(files[i]);
			} else {
				files[i].deleteOnExit();
			}
		}
	}

}
