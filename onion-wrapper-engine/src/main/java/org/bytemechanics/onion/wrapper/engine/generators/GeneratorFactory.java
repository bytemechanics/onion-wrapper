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

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import java.util.Optional;
import org.bytemechanics.commons.string.SimpleFormat;
import org.bytemechanics.onion.wrapper.generator.Generator;
import org.bytemechanics.onion.wrapper.generator.WrapperGenerator;

/**
 * @author afarre
 */
public class GeneratorFactory {

	private final String name;
	private final Class<? extends WrapperGenerator> clazz;
	
	public GeneratorFactory(final Class<? extends WrapperGenerator> _clazz){
		this.clazz=_clazz;
		this.name=Optional.ofNullable(this.clazz)
							.map(cl -> cl.getDeclaredAnnotation(Generator.class))
							.map(Generator::name)
							.orElseThrow(UnannotatedGeneratorException.from(_clazz,Generator.class));
	}

	public String getName() {
		return name;
	}
	public Class<? extends WrapperGenerator> getGeneratorClass() {
		return clazz;
	}
	
	public WrapperGenerator getGenerator() {
		try {
			return getGeneratorClass()
						.getConstructor()
							.newInstance();
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new UninstantiableGeneratorException(getGeneratorClass(),e);
		}
	}

	@Override
	public int hashCode() {
		
		int hash = 3;
		hash = 37 * hash + Objects.hashCode(this.name);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final GeneratorFactory other = (GeneratorFactory) obj;

		return Objects.equals(this.name, other.name);
	}

	@Override
	public String toString() {
		return SimpleFormat.format("Generator[name: {}, class: {}]",this.name,this.clazz);
	}
}
