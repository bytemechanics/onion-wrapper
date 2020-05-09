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
package org.bytemechanics.onion.wrapper.engine.generators;

import java.util.function.Supplier;
import org.bytemechanics.commons.string.SimpleFormat;

/**
 * @author afarre
 */
public class UnannotatedGeneratorException extends RuntimeException{
	
	private static final String MESSAGE="Generator {} should have annotation {} but is null or its name is empty";
	
	public UnannotatedGeneratorException(final Class _generator,final Class _expectedAnnotation){
		super(SimpleFormat.format(MESSAGE,_generator,_expectedAnnotation));
	}
	
	public static Supplier<UnannotatedGeneratorException> from(final Class _generator,final Class _expectedAnnotation){
		return () -> new UnannotatedGeneratorException(_generator, _expectedAnnotation);
	}
}
