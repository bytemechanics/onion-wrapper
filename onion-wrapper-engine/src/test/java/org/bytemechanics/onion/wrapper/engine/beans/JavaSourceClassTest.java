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
package org.bytemechanics.onion.wrapper.engine.beans;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.stream.Stream;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * @author afarre
 */
public class JavaSourceClassTest {

	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> JavaSourceClassTest >>>> setupSpec");
	}

	@BeforeEach
    void beforeEachTest(final TestInfo testInfo) {
        System.out.println(">>>>> "+this.getClass().getSimpleName()+" >>>> "+testInfo.getTestMethod().map(Method::getName).orElse("Unkown")+""+testInfo.getTags().toString()+" >>>> "+testInfo.getDisplayName());
	}
	
	
	@Test
	@DisplayName("getName() should return the name of the class")
	public void getName(final @Mocked PackageDeclaration _internalPackage,final @Mocked ClassOrInterfaceDeclaration _internal) {
		
		final String EXPECTED="my-name";
		JavaSourceClass instance=new JavaSourceClass(Optional.ofNullable(_internalPackage),_internal);
		
		new Expectations() {{
			_internal.getNameAsString(); result=EXPECTED;
		}};
		
		Assertions.assertEquals(EXPECTED
								,instance.getName());
	}
	
	@Test
	@DisplayName("getPackage() should return the package of the class")
	public void getPackage(final @Mocked PackageDeclaration _internalPackage,final @Mocked ClassOrInterfaceDeclaration _internal) {
		
		final String EXPECTED="my-package.package";
		JavaSourceClass instance=new JavaSourceClass(Optional.ofNullable(_internalPackage),_internal);
		
		new Expectations() {{
			_internalPackage.toString(); result=EXPECTED;
		}};
		
		Assertions.assertEquals(EXPECTED
								,instance.getPackage());
	}

	@Test
	@DisplayName("getPackage() from a root class should return null")
	public void getPackage_null(final @Mocked PackageDeclaration _internalPackage,final @Mocked ClassOrInterfaceDeclaration _internal) {
		
		JavaSourceClass instance=new JavaSourceClass(Optional.empty(),_internal);
		
		Assertions.assertNull(instance.getPackage());
	}

	@ParameterizedTest(name="isAnnotationPresent(_class) should return true if the given annotation class exist")
	@ValueSource(strings = {"org.bytemechanics.onion.wrapper.Wrap","org.bytemechanics.onion.wrapper.Wrappers"})
	@SuppressWarnings("unchecked")
	public void isAnnotationPresent(final String _annotation,final @Mocked PackageDeclaration _internalPackage,final @Mocked ClassOrInterfaceDeclaration _internal) throws ClassNotFoundException {
		
		Class<? extends Annotation> annotation=(Class<? extends Annotation>)Class.forName(_annotation);
		JavaSourceClass instance=new JavaSourceClass(Optional.empty(),_internal);
		
		new Expectations() {{
			_internal.isAnnotationPresent(annotation); result=(_annotation.equals("org.bytemechanics.onion.wrapper.Wrappers"))? false : true;
		}};
		
		if(_annotation.equals("org.bytemechanics.onion.wrapper.Wrappers")){
			Assertions.assertFalse(instance.isAnnotationPresent(annotation));
		}else{
			Assertions.assertTrue(instance.isAnnotationPresent(annotation));
		}
	}
	
	static Stream<Arguments> typeDatapack() {
	    return Stream.of(
			Arguments.of(true,true),
			Arguments.of(true,false),
			Arguments.of(false,false),
			Arguments.of(false,true)
		);
	}

	
	@ParameterizedTest(name="isClass() should return true is not interface ({0}) nor enum ({1})")
	@MethodSource("typeDatapack")
	public void isClass(final boolean _isInterface,final boolean _isEnum,final @Mocked PackageDeclaration _internalPackage,final @Mocked ClassOrInterfaceDeclaration _internal) throws ClassNotFoundException {
		
		JavaSourceClass instance=new JavaSourceClass(Optional.empty(),_internal);
		
		new Expectations() {{
			_internal.isInterface(); result=_isInterface; minTimes=0;
			_internal.isEnumDeclaration(); result=_isEnum; minTimes=0;
		}};
		
		Assertions.assertEquals((!_isInterface&&!_isEnum),instance.isClass());
	}
	@ParameterizedTest(name="isInterface() should return true is not interface ({0}) nor enum ({1})")
	@MethodSource("typeDatapack")
	public void isInterface(final boolean _isInterface,final boolean _isEnum,final @Mocked PackageDeclaration _internalPackage,final @Mocked ClassOrInterfaceDeclaration _internal) throws ClassNotFoundException {
		
		JavaSourceClass instance=new JavaSourceClass(Optional.empty(),_internal);
		
		new Expectations() {{
			_internal.isInterface(); result=_isInterface; minTimes=0;
			_internal.isEnumDeclaration(); result=_isEnum; minTimes=0;
		}};
		
		Assertions.assertEquals(_isInterface,instance.isInterface());
	}
	@ParameterizedTest(name="isEnum() should return true is not interface ({0}) nor enum ({1})")
	@MethodSource("typeDatapack")
	public void isEnum(final boolean _isInterface,final boolean _isEnum,final @Mocked PackageDeclaration _internalPackage,final @Mocked ClassOrInterfaceDeclaration _internal) throws ClassNotFoundException {
		
		JavaSourceClass instance=new JavaSourceClass(Optional.empty(),_internal);
		
		new Expectations() {{
			_internal.isInterface(); result=_isInterface; minTimes=0;
			_internal.isEnumDeclaration(); result=_isEnum; minTimes=0;
		}};
		
		Assertions.assertEquals(_isEnum,instance.isEnum());
	}

	@Test
	@DisplayName("toString() should return the same internal toString")
	public void toString(final @Mocked PackageDeclaration _internalPackage,final @Mocked ClassOrInterfaceDeclaration _internal) throws ClassNotFoundException {
		
		final String EXPECTED="my-toString";
		JavaSourceClass instance=new JavaSourceClass(Optional.empty(),_internal);
		
		new Expectations() {{
			_internal.toString(); result=EXPECTED;
		}};
		
		Assertions.assertEquals(EXPECTED,instance.toString());
	}
}
