package org.bytemechanics.onion.wrapper.engine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author afarre
 */
public class SourceScanner{

	private static final String JAVA_EXTENSION=".java";
	private static final List<String> DEFAULT_SCAN=Stream.of("").collect(Collectors.toList());
	
	
	protected static boolean notEmtpyList(final List _list){
		return !_list.isEmpty();
	}
	protected static Stream<String> packagesToScan(final String... _packagesToScan){
		return Optional.ofNullable(_packagesToScan)
						.map(Arrays::asList)
						.filter(SourceScanner::notEmtpyList)
						.orElse(DEFAULT_SCAN)
							.stream();
	}
	protected static Stream<Path> subFolders(final Path _path){
		
		try {
			return Files.list(_path)
						.filter(Files::exists)
						.filter(Files::isReadable)
						.filter(Files::isDirectory);
		} catch (IOException ex) {
			return Stream.of(_path);
		}
	}
	protected static Stream<Path> files(final Path _path){
		
		try {
			return Files.list(_path)
						.filter(Files::exists)
						.filter(Files::isReadable)
						.filter(Files::isRegularFile);
		} catch (IOException ex) {
			return Stream.of(_path);
		}
	}
	protected static boolean isJavaFile(final Path _path){
		return _path.endsWith(JAVA_EXTENSION);
	}
	
	public static Stream<Path> scan(final Path _sourceFolder,final String... _packages) {
		return packagesToScan(_packages)
						.map(_sourceFolder::resolve)
						.flatMap(SourceScanner::subFolders)
						.flatMap(SourceScanner::files)
						.filter(SourceScanner::isJavaFile);
	}
}
