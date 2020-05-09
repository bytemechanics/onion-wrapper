package org.bytemechanics.onion.wrapper.engine.beans;

import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import java.lang.annotation.Annotation;
import java.util.Optional;

/**
 *
 * @author afarre
 */
public class JavaSourceClass {

	private final Optional<PackageDeclaration> internalPackage;	
	private final ClassOrInterfaceDeclaration internal;	

	public JavaSourceClass(final Optional<PackageDeclaration> _internalPackage,final ClassOrInterfaceDeclaration _internal) {
		this.internalPackage=_internalPackage;
		this.internal=_internal;
	}

	public String getName(){
		return this.internal.getNameAsString();
	}
	public String getPackage(){
		return this.internalPackage
								.map(PackageDeclaration::toString)
								.orElse(null);
	}
	
	public boolean isAnnotationPresent(final Class<? extends Annotation> _annotationClass){
		return this.internal.isAnnotationPresent(_annotationClass);
	}
	public boolean isClass(){
		return !this.internal.isInterface();
	}

	@Override
	public String toString() {
		return this.internal.toString();
	}
}
