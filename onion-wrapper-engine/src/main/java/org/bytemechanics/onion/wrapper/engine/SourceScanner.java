package org.bytemechanics.onion.wrapper.engine;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bytemechanics.logger.FluentLogger;

/**
 *
 * @author afarre
 */
public class SourceScanner{

	private static final String JAVA_EXTENSION=".java";
	private static final List<String> DEFAULT_SCAN=Stream.of("").collect(Collectors.toList());
	
	
	protected static Stream<Path> packagesToScan(final String... _packagesToScan){
		
		return Optional.ofNullable(_packagesToScan)
						.map(Arrays::asList)
						.map(list -> list.stream()
											.filter(Objects::nonNull)
											.collect(Collectors.toList()))
						.filter(list -> !list.isEmpty())
						.orElse(DEFAULT_SCAN)
							.stream()
								.filter(Objects::nonNull)
								.map(packageToScan -> packageToScan.replaceAll("\\.","/"))
								.map(Paths::get);
	}
	protected static Stream<Path> subFolders(final Path _path){
		
		try {
			return Files.list(_path)
						.filter(Files::exists)
						.filter(Files::isReadable)
						.filter(Files::isDirectory);
		} catch (NullPointerException e) {
			throw new UncheckedIOException(new IOException("Null path"));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	protected static Stream<Path> files(final Path _path){
		
		try {
			return Files.list(_path)
						.filter(Files::exists)
						.filter(Files::isReadable)
						.filter(Files::isRegularFile);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
	protected static boolean isJavaFile(final Path _path){
		return _path.endsWith(JAVA_EXTENSION);
	}
	
	public static Stream<Path> scan(final FluentLogger logger,final Path _sourceFolder,final String... _packages) {
		
		logger.info("scanning-folder::{}::packages::{}",_sourceFolder,_packages);
		return packagesToScan(_packages)
						.filter(Files::exists)
						.map(_sourceFolder::resolve)
						.flatMap(SourceScanner::subFolders)
						.flatMap(SourceScanner::files)
						.filter(SourceScanner::isJavaFile);
	}
}
