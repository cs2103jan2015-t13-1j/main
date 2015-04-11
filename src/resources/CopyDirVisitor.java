package resources;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

//@author A0098824N
/**Adapted from http://codingjunkie.net/java-7-copy-move/ **/

public class CopyDirVisitor extends SimpleFileVisitor<Path>
{
	private final Path sourcePath, destPath;
	private final CopyOption copyOption;

	public CopyDirVisitor(Path sourcePath, Path destPath, CopyOption copyOption)
	{
		this.sourcePath = sourcePath;
		this.destPath = destPath;
		this.copyOption = copyOption;
	}

	@Override
	public FileVisitResult preVisitDirectory(Path sourceDir, BasicFileAttributes attrs) throws IOException
	{
		Path targetPath = destPath.resolve(sourcePath.relativize(sourceDir));

		if(!Files.exists(targetPath)) {
			File tempDir = new File(targetPath.toString());
			tempDir.mkdirs();
		}
		return FileVisitResult.CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Path sourceFile, BasicFileAttributes attrs) throws IOException
	{
		Files.copy(sourceFile, destPath.resolve(sourcePath.relativize(sourceFile)), copyOption);
		return FileVisitResult.CONTINUE;
	}
}