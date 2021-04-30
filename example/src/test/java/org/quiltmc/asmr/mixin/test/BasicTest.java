package org.quiltmc.asmr.mixin.test;

import java.io.IOException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.quiltmc.asmr.mixin.example.BasicMixin$Tansformer;
import org.quiltmc.asmr.processor.AsmrPlatform;
import org.quiltmc.asmr.processor.AsmrProcessor;

public class BasicTest {
    @Test
    public void test() {
        AsmrProcessor processor = new AsmrProcessor(new TestAsmrPlatform());
        processor.addTransformer(BasicMixin$Tansformer.class);
        processor.addJar(Path.of("build/libs/example.jar"), null);
        processor.process();
    }

    private static class TestAsmrPlatform implements AsmrPlatform {
        @Override
        public byte[] getClassBytecode(String clazz) throws ClassNotFoundException {
            try {
                System.out.println("Read class " + clazz);
                ClassReader reader = new ClassReader(clazz);
                System.out.println("Read class done!");
                return reader.b;
            }
            catch (IOException e) {
                throw new ClassNotFoundException("Error loading class " + clazz);
            }
        }

    }
}
