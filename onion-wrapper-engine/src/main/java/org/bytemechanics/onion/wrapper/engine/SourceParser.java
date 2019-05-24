package org.bytemechanics.onion.wrapper.engine;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;
import org.bytemechanics.onion.wrapper.engine.beans.JavaSourceClass;

/**
 *
 * @author E103880
 */
public class SourceParser {

	protected static CompilationUnit extractCompilationUnit(final Path _file){
		try {
			return StaticJavaParser.parse(_file);
		} catch (IOException ex) {
			return null;
		}
	}
	protected static Stream<JavaSourceClass> extractSourceClasses(final CompilationUnit _compilationUnit) {
		return _compilationUnit.findAll(ClassOrInterfaceDeclaration.class)
								.stream()
									.filter(Objects::nonNull)
									.map(classOrInterface -> new JavaSourceClass(_compilationUnit.getPackageDeclaration(), classOrInterface));
	}

	public static Stream<JavaSourceClass> parse(final Path _sourceFile){
		return Stream.of(_sourceFile)
						.map(SourceParser::extractCompilationUnit)
						.flatMap(SourceParser::extractSourceClasses);
	}
}
