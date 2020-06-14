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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.bytemechanics.onion.wrapper.maven.plugin.enums.Scope;

/**
 * Test onion wrapper generator plugin Mojo
 * @author afarre
 */
@Mojo(name="onion-wrapper-test",defaultPhase = LifecyclePhase.GENERATE_TEST_SOURCES)
public class OnionWrapperTestMojo extends OnionWrapperBase {
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {

		try{
			generateSources(Scope.TEST);
		}catch(Exception e){
			throw new MojoFailureException("Can not generate test source wrappers", e);
		}
	}
}
