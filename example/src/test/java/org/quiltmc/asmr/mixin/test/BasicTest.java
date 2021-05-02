package org.quiltmc.asmr.mixin.test;

import java.nio.file.*;

import org.junit.jupiter.api.Test;
import org.quiltmc.asmr.mixin.example.BasicMixin$Tansformer;
import org.quiltmc.asmr.processor.AsmrClassWriter;
import org.quiltmc.asmr.processor.AsmrProcessor;
import org.quiltmc.asmr.processor.tree.member.AsmrClassNode;

public class BasicTest {
    @Test
    public void test() throws Exception {
        AsmrProcessor processor = new AsmrProcessor(null);
        processor.addTransformer(BasicMixin$Tansformer.class);
        processor.addJar(Paths.get("build/libs/example.jar"), null);
        processor.process();

        Path outPath = Paths.get("build", "generated", "classes", "mixin", "test");
        Files.createDirectories(outPath);
        for (String className : processor.getModifiedClassNames()) {
            AsmrClassWriter writer = new AsmrClassWriter(processor);
            AsmrClassNode clazz = processor.findClassImmediately(className);
            clazz.accept(writer);
            String packageName = className.substring(0, className.lastIndexOf('/'));
            String simpleName = className.substring(className.lastIndexOf('/') + 1);
            Path packagePath = outPath.resolve(Paths.get(packageName));
            Files.createDirectories(packagePath);
            Path filePath = packagePath.resolve(Paths.get(simpleName + ".class"));
            Files.deleteIfExists(filePath);
            Files.createFile(filePath);
            Files.newOutputStream(filePath).write(writer.toByteArray());
        }
    }
}
