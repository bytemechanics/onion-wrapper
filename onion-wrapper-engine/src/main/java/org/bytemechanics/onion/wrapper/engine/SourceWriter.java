package org.bytemechanics.onion.wrapper.engine;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import org.bytemechanics.onion.wrapper.engine.beans.JavaSourceClass;

/**
 *
 * @author E103880
 */
public class SourceWriter {

	protected static Path getPackageFolder (final Path _generateSourceFolder,final String _package){
		return Optional.ofNullable(_package)
						.map(packageName -> packageName.replace('.','/'))
						.map(_generateSourceFolder::resolve)
						.orElse(_generateSourceFolder);
	}
	protected static Path getFile(final Path _path,final String _package,final String _fileName){
		try {
			final Path packageFolder=getPackageFolder(_path, _package);
			Files.createDirectories(packageFolder);
			return packageFolder.resolve(_fileName);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public static void writeClass (final Path _generateSourceFolder,final JavaSourceClass _sourceClass,final Charset _charset){
		
		final Path filePath=getFile(_generateSourceFolder, _sourceClass.getPackage(),_sourceClass.getName());
								
		try(Writer writer=Files.newBufferedWriter(filePath,_charset,StandardOpenOption.WRITE,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING)){
			writer.write(_sourceClass.toString());
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
}
