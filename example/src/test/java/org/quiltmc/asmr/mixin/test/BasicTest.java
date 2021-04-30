package org.quiltmc.asmr.mixin.test;

import java.io.IOException;
import java.nio.file.*;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.ClassReader;
import org.quiltmc.asmr.mixin.example.BasicMixin$Tansformer;
import org.quiltmc.asmr.processor.AsmrPlatform;
import org.quiltmc.asmr.processor.AsmrProcessor;
import org.quiltmc.asmr.processor.tree.member.AsmrClassNode;

public class BasicTest {
    @Test
    public void test() {
        AsmrProcessor processor = new AsmrProcessor(new TestAsmrPlatform());
        processor.addTransformer(BasicMixin$Tansformer.class);
        processor.addJar(Paths.get("build/libs/example.jar"), null);
        processor.process();
        AsmrClassNode clazz = processor.findClassImmediately("org/quiltmc/asmr/mixin/example/BasicClass");
        System.out.println("Interfaces:");
        clazz.interfaces().forEach(node -> {
            System.out.println(node.value());
        });
        System.out.println("Methods:");
        clazz.methods().forEach(node -> {
            System.out.println(node.name().value());
        });
    }

    private static class TestAsmrPlatform implements AsmrPlatform {
        @Override
        public byte[] getClassBytecode(String clazz) throws ClassNotFoundException {
            try {
                System.out.println("Read class " + clazz);
                ClassReader reader = new ClassReader(clazz);
                System.out.println("Read class done!");
                throw new RuntimeException();
                //return reader.b;
            }
            catch (IOException e) {
                throw new ClassNotFoundException("Error loading class " + clazz);
            }
        }

    }
}
