package org.quiltmc.asmr.mixin.processor;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import org.quiltmc.asmr.compiler.ElementUtils;
import org.quiltmc.asmr.compiler.TypeUtils;
import org.quiltmc.asmr.mixin.Mixin;

@SupportedAnnotationTypes("org.quiltmc.asmr.mixin.Mixin")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MixinProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element mixin : roundEnv.getElementsAnnotatedWith(Mixin.class)) {
            Set<String> targets = new HashSet<>();

            if (!(mixin instanceof TypeElement)) {
                throw new RuntimeException("Mixin annotation must be on class or interface");
            }

            for (AnnotationMirror annotation : mixin.getAnnotationMirrors()) {
                if (Mixin.class.getTypeName().equals(annotation.getAnnotationType().toString())) {
                    for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotation.getElementValues().entrySet()) {
                        if (entry.getKey().getSimpleName().toString().equals("value")) {
                            for (DeclaredType type : entry.getValue().accept(new ClassNameExtractor(), processingEnv)) {
                                targets.add(ElementUtils.getBinaryName(type.asElement()));
                            }
                        }
                    }
                }
            }

            for (String target : mixin.getAnnotation(Mixin.class).target()) {
                targets.add(target.replace('.', '/'));
            }

            generateMixin((TypeElement) mixin, targets);
        }

        return true;
    }

    private void generateMixin(TypeElement mixin, Set<String> targets) {
        String mixinName = processingEnv.getElementUtils().getBinaryName(mixin).toString();
        int split = mixinName.lastIndexOf('.');
        if (split == -1) {
            throw new RuntimeException("Mixin needs to be inside a package");
        }
        String packageName = mixinName.substring(0, split);
        String transformerName = mixinName.substring(split + 1) + "$Tansformer";

        List<String> bodyLines = new ArrayList<>();
        for (String target : targets) {
            bodyLines.addAll(generateTransformations(mixin, target));
        }
        String body = bodyLines.stream().reduce("", (a, b) -> a.concat("\n        " + b));

        try {
            FileObject template = processingEnv.getFiler().getResource(StandardLocation.ANNOTATION_PROCESSOR_PATH,
                    "__packageName__", "__transformerName__.java");

            String transformer = template.getCharContent(false).toString().replace("__packageName__", packageName)
                    .replace("__transformerName__", transformerName).replace("__body__", body);

            JavaFileObject file = processingEnv.getFiler().createSourceFile(packageName + '.' + transformerName, mixin);
            try (Writer writer = file.openWriter()) {
                writer.write(transformer);
            }
        } catch (IOException err) {
            throw new RuntimeException("Error writing Transformer", err);
        }
    }

    private List<String> generateTransformations(TypeElement mixin, String target) {
        List<String> lines = new ArrayList<>();

        for (TypeMirror type : mixin.getInterfaces()) {
            lines.add("implementInterface(processor, \"" + target + "\", \""
                    + ElementUtils.getBinaryName(((DeclaredType) type).asElement()) + "\");");
        }

        for (Element child : mixin.getEnclosedElements()) {
            if (child.getKind() == ElementKind.METHOD) {
                lines.add("copyMethod(processor, \"" + target + "\", \"" + ElementUtils.getBinaryName(mixin) + "\", \""
                        + ElementUtils.getBinaryName(child) + TypeUtils.getDescriptor(child.asType()) + "\");");
            }
        }

        return lines;
    }
}

