package org.quiltmc.asmr.compiler;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ElementVisitor;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;

public class ElementUtils {
    private static BinaryVisitor binary = new BinaryVisitor();

    private ElementUtils() {}

    // Examples:
    // org/quiltmc/amsr/compiler/TypeUtils
    // org/quiltmc/amsr/compiler/TypeUtils$BinaryVisitor
    // getBinaryName
    // binary
    public static String getBinaryName(Element element) {
        return element.accept(binary, null);
    }

    private static class BinaryVisitor implements ElementVisitor<String, Void> {
        @Override
        public String visit(Element element) {
            return visit(element, null);
        }

        @Override
        public String visit(Element element, Void v) {
            throw new RuntimeException("Unsupported Element: " + element);
        }

        @Override
        public String visitExecutable(ExecutableElement element, Void v) {
            return element.getSimpleName().toString();
        }

        @Override
        public String visitPackage(PackageElement element, Void v) {
            return element.toString().replace('.', '/');
        }

        @Override
        public String visitType(TypeElement element, Void v) {
            Element enclosing = element.getEnclosingElement();
            if (enclosing.getKind().isClass() || enclosing.getKind().isInterface()) {
                return visitType((TypeElement) enclosing, v) + "$" + element.getSimpleName();
            }
            if (enclosing.getKind() == ElementKind.PACKAGE) {
                return visitPackage((PackageElement) enclosing, v) + '/' + element.getSimpleName();
            }
            throw new RuntimeException("Unsupported Element: " + enclosing);
        }

        @Override
        public String visitTypeParameter(TypeParameterElement element, Void v) {
            throw new RuntimeException("Unsupported Element: " + element);
        }

        @Override
        public String visitUnknown(Element element, Void v) {
            throw new RuntimeException("Unsupported Element: " + element);
        }

        @Override
        public String visitVariable(VariableElement element, Void v) {
            return element.getSimpleName().toString();
        }

    }
}
