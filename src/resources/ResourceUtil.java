package resources;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import organizer.gui.MainApp;

public class ResourceUtil {

	private final static String TEMP_FOLDER_NAME =  "c:/temp/";
	private final static String FOLDER_TO_COPY = "resources/help_manual/";

	//@author A0098824N
	public static File makeTemporaryFromResourceFolder(String resourcePath) throws IOException, URISyntaxException {
		String sourcePath = MainApp.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		sourcePath = URLDecoder.decode(sourcePath, "UTF-8");
		File destinationFile;

		if(sourcePath.endsWith(".jar")) {
			String destPath = sourcePath.substring(0, sourcePath.lastIndexOf("/"));
			System.out.println(destPath);
			destinationFile = getResourceFromJarFile(sourcePath, TEMP_FOLDER_NAME);
		} else {
			File sourceFile = new File(sourcePath.concat(resourcePath));
			destinationFile = new File(TEMP_FOLDER_NAME);
			Path sourceFilePath = sourceFile.toPath();
			Path destinationFilePath = destinationFile.toPath();
			Files.walkFileTree(sourceFilePath, new CopyDirVisitor(sourceFilePath, destinationFilePath, StandardCopyOption.REPLACE_EXISTING));
		}
		
		deleteDirOnExit(destinationFile);
		return destinationFile;
	}

	public static File getResourceFromJarFile(String jarSource, String destDir) throws IOException {
		JarFile jarFile = new JarFile(jarSource);
		Enumeration<JarEntry> enumEntries = jarFile.entries();
		List<JarEntry> resEntries = new ArrayList<JarEntry>();
		File destFile = new File("");

		while (enumEntries.hasMoreElements()) {
			JarEntry file = (java.util.jar.JarEntry) enumEntries.nextElement();

			// if its a directory, create it
			if (file.getName().startsWith(FOLDER_TO_COPY)) { 
				destFile = new File(destDir + java.io.File.separator + file.getName());
				if(file.isDirectory() && !destFile.exists()) {
					destFile.mkdirs();
				} else {
					resEntries.add(file);
				}
			}	
		}

		writeNonDirFiles(destDir, jarFile, resEntries);
		jarFile.close();
		
		destFile = new File(destDir+java.io.File.separator+FOLDER_TO_COPY);

		return destFile;
	}

	private static void writeNonDirFiles(String destDir, JarFile jarFile, List<JarEntry> resEntries) throws IOException,FileNotFoundException {
		File destFile;
		for(int i = 0; i < resEntries.size(); i++) {
			destFile = new File(destDir + java.io.File.separator + resEntries.get(i).getName());
			if(!destFile.exists()) {
				InputStream is = null;
				FileOutputStream fos = null;
				is = jarFile.getInputStream(resEntries.get(i)); // get the input stream
				fos = new java.io.FileOutputStream(destFile);
				readWriteFiles(is, fos);
			}
		}
	}

	private static void readWriteFiles(InputStream is, FileOutputStream fos) throws IOException {
		// write contents of 'is' to 'fos'
		while (is.available() > 0) {  
			byte[] buffer = new byte[4096];
			int n = 0;
			while (-1 != (n = is.read(buffer))) {
				fos.write(buffer, 0, n);
			}
		}
		fos.close();
		is.close();
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
