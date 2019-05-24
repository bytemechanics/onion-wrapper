/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author E103880
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
