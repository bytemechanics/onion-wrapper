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

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.bytemechanics.onion.wrapper.engine.SourceParser;
import org.bytemechanics.onion.wrapper.engine.SourceScanner;
import org.bytemechanics.onion.wrapper.engine.SourceWriter;
import org.bytemechanics.onion.wrapper.engine.WrapperEngine;
import org.sonatype.aether.impl.ArtifactResolver;

/**
 *
 * @author afarre
 */
@Mojo(name="onion-wrapper",defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class OnionWrapperMojo extends AbstractMojo {

	private static final List<String> DEFAULT_SCAN=Stream.of("").collect(Collectors.toList());
	
	/**
	 * Artifact resolver, needed to download source jars for inclusion in classpath.
	 */
	@Component
	protected ArtifactResolver artifactResolver;
	
	/**
	 * Maven session
	 */
	@Parameter(defaultValue = "${session}", readonly = true, required = true)
	protected MavenSession session;

	/**
	 * Maven project
	 */
	@Parameter(defaultValue = "${project}", readonly = true, required = true)
	protected MavenProject project;
	
	/**
	 * Define the copies to do
	 * @see CopyDefinition
	 */
	@Parameter(required = false)
	protected String[] packageScan;
	
	
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		final Charset sourceEncoding=Charset.forName(this.project.getProperties().getProperty("project.build.sourceEncoding"));
		final Path sourceFolder=Paths.get(project.getBuild().getSourceDirectory());
		final Path sourceDestiny=Paths.get(this.project.getBuild().getDirectory())
										.resolve("generated-sources")
										.resolve("onion-wrapper");
		this.project.addCompileSourceRoot(sourceDestiny.toString());
		
		SourceScanner.scan(sourceFolder, this.packageScan)
						.flatMap(SourceParser::parse)
						.filter(WrapperEngine::wrapperRequested)
						.filter(WrapperEngine::isWrappable)
						.map(WrapperEngine::generateWrapper)
						.forEach(wrapper -> SourceWriter.writeClass(sourceDestiny, wrapper, sourceEncoding));
	}
}
