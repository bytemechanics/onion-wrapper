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
import java.util.Optional;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.bytemechanics.onion.wrapper.engine.SourceParser;
import org.bytemechanics.onion.wrapper.engine.SourceScanner;
import org.bytemechanics.onion.wrapper.engine.SourceWriter;
import org.bytemechanics.onion.wrapper.engine.WrapperEngine;
import org.bytemechanics.onion.wrapper.maven.plugin.enums.Scope;
import org.eclipse.aether.impl.ArtifactResolver;

/**
 * Base plugin mojo for Onion wrapper generator
 * @author afarre
 */
public abstract class OnionWrapperBase extends AbstractMojo {
	
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
	 * Packages to sacan
	 */
	@Parameter(required = false)
	protected String[] packageScan;
	
	/**
	 * Generated source folder
	 */
	@Parameter(required = false,defaultValue = "onion-wrapper")
	protected String generatedSourceFolder;

	
	public OnionWrapperBase() {
	}
	public OnionWrapperBase(final ArtifactResolver _artifactResolver,final MavenSession _session,final MavenProject _project,final String[] _packageScan,final String _generatedSourceFolder) {
		this.artifactResolver = _artifactResolver;
		this.session = _session;
		this.project = _project;
		this.packageScan = _packageScan;
		this.generatedSourceFolder = _generatedSourceFolder;
	}

	
	protected Charset getCharset(){
		
		return Optional.ofNullable(this.project)
						.map(MavenProject::getProperties)
						.map(properties -> properties.getProperty("project.build.sourceEncoding"))
						.map(Charset::forName)
						.orElse(Charset.defaultCharset());
	}
	protected Path getSourceFolder(){
		
		return Paths.get(project.getBuild().getSourceDirectory());
				
	}
	protected Path buildSourceDestiny(final Scope _scope){
		
		return Paths.get(this.project.getBuild().getDirectory())
					.resolve(_scope.getFolder())
					.resolve(this.generatedSourceFolder);
				
	}
	protected Path getSourceDestiny(final Scope _scope){
		
		return Optional.of(_scope)
						.map(this::buildSourceDestiny)
						.map(path -> _scope.registerSourceFolder(project, path))
						.get();
				
	}
	
	public void generateSources(final Scope _scope) {
		
		SourceScanner.scan(getSourceFolder(), this.packageScan)
						.flatMap(SourceParser::parse)
						.filter(WrapperEngine::wrapperRequested)
						.filter(WrapperEngine::isWrappable)
						.map(WrapperEngine::generateWrapper)
						.forEach(wrapper -> SourceWriter.writeClass(getSourceDestiny(_scope), wrapper, getCharset()));
	}
}