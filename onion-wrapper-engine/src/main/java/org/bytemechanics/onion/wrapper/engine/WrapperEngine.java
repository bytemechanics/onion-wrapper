package org.bytemechanics.onion.wrapper.engine;

import org.bytemechanics.onion.wrapper.annotations.Wrap;
import org.bytemechanics.onion.wrapper.engine.beans.JavaSourceClass;

/**
 *
 * @author E103880
 */
public class WrapperEngine {

	public static boolean wrapperRequested(final JavaSourceClass _sourceClass){
		return _sourceClass.isAnnotationPresent(Wrap.class);
	}
	public static boolean isWrappable(final JavaSourceClass _sourceClass){
		return _sourceClass.isClass();
	}	
	public static JavaSourceClass generateWrapper(final JavaSourceClass _sourceClass){
		return null;
	}	
}
