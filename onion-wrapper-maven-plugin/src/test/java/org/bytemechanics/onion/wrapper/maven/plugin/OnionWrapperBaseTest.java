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
package org.bytemechanics.onion.wrapper.maven.plugin;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Build;
import org.apache.maven.project.MavenProject;
import org.bytemechanics.onion.wrapper.maven.plugin.enums.Scope;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.impl.ArtifactResolver;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 *
 * @author afarre
 */
public class OnionWrapperBaseTest {
	
	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> OnionWrapperBaseTest >>>> setupSpec");
	}

	@Injectable
	ArtifactResolver artifactResolver;
	@Mocked 
	@Injectable
	MavenSession session; 
	@Mocked 
	@Injectable
	MavenProject project; 

	@Injectable
	String generatedSourceFolder;
	@Injectable
	String[] packageScan;

	@BeforeEach
    void beforeEachTest(final TestInfo testInfo) {
        System.out.println(">>>>> "+this.getClass().getSimpleName()+" >>>> "+testInfo.getTestMethod().map(Method::getName).orElse("Unkown")+""+testInfo.getTags().toString()+" >>>> "+testInfo.getDisplayName());
		this.generatedSourceFolder="onion-wrapper";
		this.packageScan=new String[]{"my-pacakge","my-pacakge.my-subpackage"};
		this.artifactResolver=new ArtifactResolver() {
									@Override
									public ArtifactResult resolveArtifact(RepositorySystemSession rss, ArtifactRequest ar) throws ArtifactResolutionException {
										throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
									}

									@Override
									public List<ArtifactResult> resolveArtifacts(RepositorySystemSession rss, Collection<? extends ArtifactRequest> clctn) throws ArtifactResolutionException {
										throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
									}
								};
	}

	@Tested
	@Mocked
	OnionWrapperMojo instance;

	@Test
	@DisplayName("getCharset() with configured enconding")
	public void getCharset(){
		
		Properties properties=new Properties();
		properties.setProperty("project.build.sourceEncoding", "ISO-8859-1");
		
		new Expectations() {{
			project.getProperties(); result=properties;
		}};
		
		Assertions.assertEquals(Charset.forName("ISO-8859-1"),instance.getCharset());
	}
	@Test
	@DisplayName("getCharset() not configured returns the default encoding")
	public void getCharsetNotConfigured(){
		
		new Expectations() {{
			project.getProperties(); result=new Properties();
		}};
		
		Assertions.assertEquals(Charset.defaultCharset(),instance.getCharset());
	}

	@Test
	@DisplayName("getSourceFolder() returns the maven source folder")
	public void getSourceFolder(final @Mocked Build _build){
		
		final String EXPECTED="my-folder";
		
		new Expectations() {{
			project.getBuild(); result=_build;
			_build.getSourceDirectory(); result=EXPECTED;
		}};
		
		Assertions.assertEquals(Paths.get(EXPECTED),instance.getSourceFolder());
	}

	@ParameterizedTest(name = "buildSourceDestiny({0}) should return the expected generated folder inside the corresponding folder")
	@EnumSource(Scope.class)
	public void buildSourceDestiny(final Scope _scope,final @Mocked Build _build){
		
		final String BASEDIR="my-base-dir";
		final Path EXPECTED=Paths.get(BASEDIR,_scope.getFolder(),"onion-wrapper");
		
		new Expectations() {{
			project.getBuild(); result=_build;
			_build.getDirectory(); result=BASEDIR;
		}};
		
		Assertions.assertEquals(EXPECTED,instance.buildSourceDestiny(_scope));
	}

	@ParameterizedTest(name = "getSourceDestiny({0}) should return the expected generated folder inside the corresponding folder and register the folder")
	@EnumSource(Scope.class)
	public void getSourceDestiny(final Scope _scope,final @Mocked Build _build){
		
		final Path EXPECTED=Paths.get("my-base-dir",_scope.getFolder(),"onion-wrapper");
		
		new Expectations() {{
			instance.buildSourceDestiny(_scope); result=EXPECTED; times=1;
		}};
		
		Assertions.assertEquals(EXPECTED,instance.getSourceDestiny(_scope));
		
		new Verifications() {{
			project.addCompileSourceRoot(EXPECTED.toString()); times=(Scope.SRC==_scope)? 1 : 0;
			project.addTestCompileSourceRoot(EXPECTED.toString()); times=(Scope.TEST==_scope)? 1 : 0;
		}};
	}
}
