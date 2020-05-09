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
package org.bytemechanics.onion.wrapper.engine.generators.internal;

import java.lang.annotation.Annotation;
import java.util.Set;
import org.bytemechanics.logger.FluentLogger;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

/**
 *
 * @author afarre
 */
public class ClasspathScannerServiceImpl implements ClasspathScannerService {

	private final FluentLogger logger;
	private final Reflections reflections;
	
	public ClasspathScannerServiceImpl (final String[] _packageFilters){
		 this(FluentLogger.of(ClasspathScannerServiceImpl.class)
							.prefixed("service::extensions::scanner::")
				 ,new Reflections(new ConfigurationBuilder()
										.forPackages(_packageFilters)
										.setScanners(new TypeAnnotationsScanner())));
	}
	public ClasspathScannerServiceImpl (final FluentLogger _logger,final Reflections _reflections){
		 this.logger=_logger;
		 this.reflections=_reflections;
	}
	
	@Override
	public Set<Class<?>> getAnnotatedClasses(final Class<? extends Annotation> _annotation){
		
		final Set<Class<?>> reply;
		
		logger.debug("recover-classes::annotation::{}::begin", _annotation);
		reply=this.reflections.getTypesAnnotatedWith(_annotation);
		logger.trace("recover-classes::annotation::{}::end::{}", _annotation,reply);
		
		return reply;
	}
}
