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

import java.util.stream.Stream;
import org.bytemechanics.logger.FluentLogger;
import org.bytemechanics.onion.wrapper.engine.generators.internal.ClasspathScannerService;
import org.bytemechanics.onion.wrapper.engine.generators.internal.ClasspathScannerServiceImpl;
import org.bytemechanics.onion.wrapper.generator.Generator;
import org.bytemechanics.onion.wrapper.generator.WrapperGenerator;

/**
 *
 * @author afarre
 */
public class GeneratorScannerServiceImpl implements GeneratorScannerService{
	
	private final FluentLogger logger;
	private final String[] packageFilters;
	private final ClasspathScannerService scannerService;

	
	public GeneratorScannerServiceImpl(final String[] _packageFilters){
		this(FluentLogger.of(GeneratorScannerServiceImpl.class)
				,new ClasspathScannerServiceImpl(_packageFilters)
				,_packageFilters);
	}
	public GeneratorScannerServiceImpl(final FluentLogger _logger,final ClasspathScannerService _scannerService,final String[] _packageFilters){
		this.logger=_logger;
		this.scannerService=_scannerService;
		this.packageFilters=_packageFilters;
	}

	
	protected boolean isValidGenerator(final Class _class){
		
		boolean reply;
		
		reply=WrapperGenerator.class.isAssignableFrom(_class);
		if(!reply){
			logger.warning("WARNING: Annotated generator {} does not implement {} > Will be discarded.",_class.getCanonicalName(),WrapperGenerator.class.getCanonicalName());
		}
				
		return reply;
	}
	@SuppressWarnings("unchecked")
	protected Class<? extends WrapperGenerator> castToWrapper(final Class _class){
		return (Class<? extends WrapperGenerator>)_class;
	}
	

	@Override
	public Stream<GeneratorFactory> streamGenerators() {
		return this.scannerService.getAnnotatedClasses(Generator.class)
									.stream()
										.filter(this::isValidGenerator)
										.map(this::castToWrapper)
										.map(GeneratorFactory::new);
	}
}
