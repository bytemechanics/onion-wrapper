package org.bytemechanics.onion.wrapper.engine;

import java.util.Map;
import java.util.stream.Collectors;
import org.bytemechanics.logger.FluentLogger;
import org.bytemechanics.onion.wrapper.Wrap;
import org.bytemechanics.onion.wrapper.Wrappers;
import org.bytemechanics.onion.wrapper.engine.beans.JavaSourceClass;
import org.bytemechanics.onion.wrapper.engine.generators.GeneratorFactory;
import org.bytemechanics.onion.wrapper.engine.generators.GeneratorScannerService;
import org.bytemechanics.onion.wrapper.engine.generators.GeneratorScannerServiceImpl;

/**
 *
 * @author afarre
 */
public class WrapperEngine {

	private final FluentLogger logger;
	private final String[] packageFilters;
	private final GeneratorScannerService scannerService;
	private final Map<String,GeneratorFactory> generators;
	
	public WrapperEngine(final String[] _packageFilters){
		this(FluentLogger.of(WrapperEngine.class)
								.prefixed("Engine > ")
				,new GeneratorScannerServiceImpl(_packageFilters)
				,_packageFilters);
	}
	public WrapperEngine(final FluentLogger _logger,final GeneratorScannerService _scannerService,final String[] _packageFilters){
		this.logger=_logger;
		this.scannerService=_scannerService;
		this.packageFilters=_packageFilters;
		logger.info("Scanning generators at: {}",(Object)this.packageFilters);
		this.generators=_scannerService.streamGenerators()
										.peek(generatorFactory -> logger.info("\tFound {}",generatorFactory))
										.collect(Collectors.toMap(GeneratorFactory::getName,generatorFactory -> generatorFactory));
	}
	
	public static boolean wrapperRequested(final JavaSourceClass _sourceClass){
		return _sourceClass.isAnnotationPresent(Wrap.class) || _sourceClass.isAnnotationPresent(Wrappers.class);
	}
	public static boolean isWrappable(final JavaSourceClass _sourceClass){
		return _sourceClass.isClass();
	}	
	public static JavaSourceClass generateWrapper(final JavaSourceClass _sourceClass){
		return null;
	}	
}
