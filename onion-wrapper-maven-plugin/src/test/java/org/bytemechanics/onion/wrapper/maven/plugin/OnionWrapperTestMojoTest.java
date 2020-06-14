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
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.Verifications;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.bytemechanics.onion.wrapper.maven.plugin.enums.Scope;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 *
 * @author afarre
 */
public class OnionWrapperTestMojoTest {
	
	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> OnionWrapperTestMojoTest >>>> setupSpec");
	}

	@BeforeEach
    void beforeEachTest(final TestInfo testInfo) {
        System.out.println(">>>>> "+this.getClass().getSimpleName()+" >>>> "+testInfo.getTestMethod().map(Method::getName).orElse("Unkown")+""+testInfo.getTags().toString()+" >>>> "+testInfo.getDisplayName());
	}
	
	@Tested
	@Mocked
	OnionWrapperTestMojo instance;

	@Test
	@DisplayName("Execute should delegate to generateSources with SRC Scope")
	public void execute() throws MojoExecutionException, MojoFailureException {
		
		new Expectations() {{
			instance.generateSources(Scope.TEST);
		}};
		instance.execute();
		new Verifications() {{
			instance.generateSources(Scope.TEST); times=1;
		}};
	}

	@Test
	@DisplayName("Execute should with exception should raise mojoFailureException")
	public void executeFailure() throws MojoExecutionException, MojoFailureException {
		
		new Expectations() {{
			instance.generateSources(Scope.TEST); result=new RuntimeException("errorot");
		}};
		Assertions.assertThrows(MojoFailureException.class
								, () -> instance.execute()
								,"Can not generate test source wrappers");
	}
}