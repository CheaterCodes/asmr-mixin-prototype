package org.quiltmc.asmr.mixin.processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.type.UnionType;
import javax.lang.model.type.WildcardType;

public class CanonicalNameVisitor implements TypeVisitor<String, ProcessingEnvironment> {
    @Override
    public String visit(TypeMirror type) {
        return visit(type, null);
    }

    @Override
    public String visit(TypeMirror type, ProcessingEnvironment env) {
        throw new RuntimeException("Exception visiting generic");
    }

    @Override
    public String visitArray(ArrayType type, ProcessingEnvironment env) {
        throw new RuntimeException("Exception visiting array");
    }

    @Override
    public String visitDeclared(DeclaredType type, ProcessingEnvironment env) {
        String packageName = getPackage(type.asElement()).toString().replace('.', '/');
        String className = type.toString().substring(packageName.length() + 1).replace('.', '$');
        return "L" + packageName + "/" + className + ";";
    }

    @Override
    public String visitError(ErrorType type, ProcessingEnvironment env) {
        throw new RuntimeException("Exception visiting error");
    }

    @Override
    public String visitExecutable(ExecutableType type, ProcessingEnvironment env) {
        String name = "(";
        for (TypeMirror param : type.getParameterTypes()) {
            name += param.accept(this, env);
        }
        name += ")";
        name += type.getReturnType().accept(this, env);
        return name;
    }

    @Override
    public String visitIntersection(IntersectionType type, ProcessingEnvironment env) {
        throw new RuntimeException("Exception visiting inter");
    }

    @Override
    public String visitNoType(NoType type, ProcessingEnvironment env) {
        return "V";
    }

    @Override
    public String visitNull(NullType type, ProcessingEnvironment env) {
        throw new RuntimeException("Exception visiting null");
    }

    @Override
    public String visitPrimitive(PrimitiveType type, ProcessingEnvironment env) {
		switch (type.toString()) {
			case "void":
				return "V";
			case "byte":
				return "B";
			case "char":
				return "C";
			case "double":
				return "D";
			case "float":
				return "F";
			case "int":
				return "I";
			case "long":
				return "J";
			case "short":
				return "S";
			case "boolean":
				return "Z";
			default:
                throw new IllegalArgumentException("Unknown primitive type: " + type.toString());
		}
    }

    @Override
    public String visitTypeVariable(TypeVariable type, ProcessingEnvironment env) {
        throw new RuntimeException("Exception visiting typevar");
    }

    @Override
    public String visitUnion(UnionType type, ProcessingEnvironment env) {
        throw new RuntimeException("Exception visiting union");
    }

    @Override
    public String visitUnknown(TypeMirror type, ProcessingEnvironment env) {
        throw new RuntimeException("Exception visiting unknown");
    }

    @Override
    public String visitWildcard(WildcardType type, ProcessingEnvironment env) {
        throw new RuntimeException("Exception visiting wildcard");
    }

    private static PackageElement getPackage(Element element) {
        while (element.getKind() != ElementKind.PACKAGE) {
            element = element.getEnclosingElement();
        }
        return (PackageElement) element;
    }
}
