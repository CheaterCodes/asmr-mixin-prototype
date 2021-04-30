package org.quiltmc.asmr.compiler;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ErrorType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.IntersectionType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.NullType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.TypeVisitor;
import javax.lang.model.type.UnionType;
import javax.lang.model.type.WildcardType;

public class TypeUtils {
    private static DescriptorVisitor descriptor = new DescriptorVisitor();

    private TypeUtils() {}

    // Examples:
    // Lorg/quiltmc/amsr/compiler/TypeUtils;
    // Lorg/quiltmc/amsr/compiler/TypeUtils$BinaryVisitor;
    // (Ljavax/lang/model/type/TypeMirror;)Ljava/lang/String;
    public static String getDescriptor(TypeMirror type) {
        return type.accept(descriptor, null);
    }

    private static class DescriptorVisitor implements TypeVisitor<String, Void> {
        @Override
        public String visit(TypeMirror type) {
            return visit(type, null);
        }

        @Override
        public String visit(TypeMirror type, Void v) {
            throw new RuntimeException("Unsupported Type: " + type);
        }

        // Array
        @Override
        public String visitArray(ArrayType type, Void v) {
            return "[" + type.getComponentType().accept(this, v);
        }

        // Interface, Class
        @Override
        public String visitDeclared(DeclaredType type, Void v) {
            return "L" + ElementUtils.getBinaryName(type.asElement()) + ";";
        }

        // Partial Interface, Partial Class
        @Override
        public String visitError(ErrorType type, Void v) {
            return visitDeclared(type, v);
        }

        // Method, Constructor, Initializer
        @Override
        public String visitExecutable(ExecutableType type, Void v) {
            String name = "(";
            for (TypeMirror parameter : type.getParameterTypes()) {
                name += parameter.accept(this, v);
            }
            name += ")" + type.getReturnType().accept(this, v);

            return name;
        }

        @Override
        public String visitIntersection(IntersectionType type, Void v) {
            throw new RuntimeException("Unsupported Type: " + type);
        }

        @Override
        public String visitNoType(NoType type, Void v) {
            if (type.getKind() == TypeKind.VOID) {
                return "V";
            }

            throw new RuntimeException("Unsupported Type: " + type);
        }

        @Override
        public String visitNull(NullType type, Void v) {
            throw new RuntimeException("Unsupported Type: " + type);
        }

        @Override
        public String visitPrimitive(PrimitiveType type, Void v) {
            switch (type.toString()) {
                case "boolean":
                    return "Z";
                case "byte":
                    return "B";
                case "short":
                    return "S";
                case "int":
                    return "I";
                case "long":
                    return "J";
                case "char":
                    return "C";
                case "float":
                    return "F";
                case "double":
                    return "D";
                default:
                    throw new RuntimeException("Unsupported Type: " + type);
            }
        }

        @Override
        public String visitTypeVariable(TypeVariable type, Void v) {
            throw new RuntimeException("Unsupported Type: " + type);
        }

        @Override
        public String visitUnion(UnionType type, Void v) {
            throw new RuntimeException("Unsupported Type: " + type);
        }

        @Override
        public String visitUnknown(TypeMirror type, Void v) {
            throw new RuntimeException("Unsupported Type: " + type);
        }

        @Override
        public String visitWildcard(WildcardType type, Void v) {
            throw new RuntimeException("Unsupported Type: " + type);
        }

    }
}
