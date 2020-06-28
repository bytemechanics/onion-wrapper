/*
 * Copyright 2020 Byte Mechanics.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bytemechanics.onion.wrapper.engine;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * @author afarre
 */
public class SourceScannerTest {

	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> SourceScannerTest >>>> setupSpec");
	}

	@BeforeEach
    void beforeEachTest(final TestInfo testInfo) {
        System.out.println(">>>>> "+this.getClass().getSimpleName()+" >>>> "+testInfo.getTestMethod().map(Method::getName).orElse("Unkown")+""+testInfo.getTags().toString()+" >>>> "+testInfo.getDisplayName());
	}
	
	
	@Test
	@DisplayName("Return list of the given packages")
	public void packagesToScan() {
		final String[] packagesToScan=new String[]{"a","b"};
		Assertions.assertEquals(Stream.of(packagesToScan)
												.map(Paths::get)
												.collect(Collectors.toSet())
								,SourceScanner.packagesToScan(packagesToScan)
													.collect(Collectors.toSet()));
	}
	@Test
	@DisplayName("Return list of the given packages or an empty list if null")
	public void packagesToScan_empty() {
		Assertions.assertEquals(Stream.of("")
												.map(Paths::get)
												.collect(Collectors.toSet())
								,SourceScanner.packagesToScan()
													.collect(Collectors.toSet()));
	}
	@Test
	@DisplayName("Return list of the given packages or an empty list if null")
	public void packagesToScan_null() {
		Assertions.assertEquals(Stream.of("")
												.map(Paths::get)
												.collect(Collectors.toSet())
								,SourceScanner.packagesToScan((String[])null)
													.collect(Collectors.toSet()));
	}
	@Test
	@DisplayName("Return list of the given packages or an empty list if null")
	public void packagesToScan_null_path() {
		Assertions.assertEquals(Stream.of("")
												.map(Paths::get)
												.collect(Collectors.toSet())
								,SourceScanner.packagesToScan((String)null)
													.collect(Collectors.toSet()));
	}

	static Stream<Arguments> subfoldersDatapack() {
	    return Stream.of(
			Arguments.of("src",Stream.of("test","main")
															.collect(Collectors.toSet())),
			Arguments.of("src/test/java",Stream.of("org")
																	.collect(Collectors.toSet())),
			Arguments.of("src/test/resources",Stream.empty()
																		.collect(Collectors.toSet()))
		);
	}
	@ParameterizedTest(name = "subFolders({0}) should return {1}")
	@MethodSource("subfoldersDatapack")
	public void subFolders(final String _path,final Set<String> _subfolders) {
		Path main=Paths.get(_path);
		Assertions.assertEquals(_subfolders.stream()
													.map(main::resolve)
													.collect(Collectors.toSet())
								,SourceScanner.subFolders(main)
													.collect(Collectors.toSet()));
	}
	@Test
	@DisplayName("List subFolders from null should raise UncheckedIOException")
	public void subFolders_fromNull() {
		Assertions.assertThrows(UncheckedIOException.class, () -> SourceScanner.subFolders(null));
	}	
}
