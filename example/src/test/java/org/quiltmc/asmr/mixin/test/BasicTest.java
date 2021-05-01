package org.quiltmc.asmr.mixin.test;

import java.nio.file.*;

import org.junit.jupiter.api.Test;
import org.quiltmc.asmr.mixin.example.BasicMixin$Tansformer;
import org.quiltmc.asmr.processor.AsmrPlatform;
import org.quiltmc.asmr.processor.AsmrProcessor;
import org.quiltmc.asmr.processor.tree.AsmrTreeUtil;
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
        System.out.println(AsmrTreeUtil.dump(clazz));
    }

    private static class TestAsmrPlatform implements AsmrPlatform {
        @Override
        public byte[] getClassBytecode(String clazz) throws ClassNotFoundException {
            throw new ClassNotFoundException();
        }
    }

}
