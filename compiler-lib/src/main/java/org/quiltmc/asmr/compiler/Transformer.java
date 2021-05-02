package org.quiltmc.asmr.compiler;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class Transformer {
    private final String compilerName;
    private final ClassNode transformer;

    private final MethodNode read;
    private final MethodNode addRoundDependencies;

    public static class Builder {
        private String compilerName;
        private String transformerName;
        private List<Dependency> dependencies = new ArrayList<>();
        private List<String> phases = new ArrayList<>();

        public Transformer build() {
            return new Transformer(compilerName, transformerName, phases, dependencies);
        }

        public Builder name(String transformerName) {
            this.transformerName = transformerName;
            return this;
        }

        public Builder compiler(String compilerName) {
            this.compilerName = compilerName;
            return this;
        }

        public Builder afterRound(String otherTransformer) {
            dependencies.add(Dependency.afterRound(otherTransformer));
            return this;
        }

        public Builder beforeRound(String otherTransformer) {
            dependencies.add(Dependency.beforeRound(otherTransformer));
            return this;
        }

        public Builder afterWrite(String otherTransformer) {
            dependencies.add(Dependency.afterWrite(otherTransformer));
            return this;
        }

        public Builder beforeWrite(String otherTransformer) {
            dependencies.add(Dependency.beforeWrite(otherTransformer));
            return this;
        }

        public Builder inPhase(String... phase) {
            phases.addAll(Arrays.asList(phase));
            return this;
        }
    }

    public Transformer(
            String compilerName,
            String transformerName,
            List<String> phases,
            List<Dependency> dependencies) {
        this.compilerName = compilerName;

        transformer = new ClassNode();
        transformer.version =  Opcodes.V1_5;
        transformer.access = Opcodes.ACC_PUBLIC;
        transformer.name = transformerName;
        transformer.superName = "java/lang/Object";
        transformer.interfaces.add("org/quiltmc/asmr/processor/AsmrTransformer");

        transformer.methods.add(getPhases(phases));

        addRoundDependencies = addRoundDependencies(dependencies);
        transformer.methods.add(addRoundDependencies);

        read = read();
        transformer.methods.add(read);
    }

    private MethodNode getPhases(List<String> phases) {
        MethodNode method = new MethodNode(ASM4, ACC_PUBLIC, "getPhases", "()Ljava/util/List;", "", new String[]{});

        LabelNode start = new LabelNode(new Label());
        LabelNode end = new LabelNode(new Label());

        method.localVariables.add(new LocalVariableNode("this", "L" + transformer.name + ";", "", start, end, 0));
        method.localVariables.add(new LocalVariableNode("phases", "Ljava/util/List;", "", start, end, 1));
        method.maxLocals = 2;

        method.instructions.add(start);
        method.instructions.add(new TypeInsnNode(Opcodes.NEW, "java/util/ArrayList"));
        method.instructions.add(new InsnNode(Opcodes.DUP));
        method.instructions.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, "java/util/ArrayList", "<init>", "()V"));
        method.instructions.add(new VarInsnNode(Opcodes.ASTORE, 1));
        for (String phase : phases) {
            method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
            if (phase == null) {
                method.instructions.add(new InsnNode(Opcodes.ACONST_NULL));
            }
            else {
                method.instructions.add(new LdcInsnNode(phase));
            }
            method.instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true));
            method.instructions.add(new InsnNode(Opcodes.POP));
        }
        method.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
        method.instructions.add(new InsnNode(Opcodes.ARETURN));
        method.instructions.add(end);
        method.maxStack = 2;

        return method;
    }

    private MethodNode addRoundDependencies(List<Dependency> dependencies) {
        MethodNode method = new MethodNode(ASM4, ACC_PUBLIC, "addRoundDependencies", "(Lorg/quiltmc/asmr/processor/AsmrProcessor;)V", "", new String[]{});

        LabelNode start = new LabelNode(new Label());
        LabelNode end = new LabelNode(new Label());

        method.localVariables.add(new LocalVariableNode("this", "L" + transformer.name + ";", "", start, end, 0));
        method.localVariables.add(new LocalVariableNode("processor", "Lorg/quiltmc/asmr/processor/AsmrProcessor;", "", start, end, 1));
        method.maxLocals = 2;

        method.instructions.add(start);
        for (Dependency dependency : dependencies) {
            method.instructions.add(new VarInsnNode(ALOAD, 1));
            method.instructions.add(new VarInsnNode(ALOAD, 0));
            method.instructions.add(new LdcInsnNode(dependency.getOtherTransformer()));
            String call;
            switch (dependency.getType()) {
                case BEFORE_ROUND:
                    call = "addRoundDependent";
                    break;
                case AFTER_ROUND:
                    call = "addRoundDependency";
                    break;
                case BEFORE_WRITE:
                    call = "addWriteDependent";
                    break;
                case AFTER_WRITE:
                    call = "addWriteDependency";
                    break;
                default:
                    throw new RuntimeException();
            }
            method.instructions.add(new MethodInsnNode(INVOKEVIRTUAL, "org/quiltmc/asmr/processor/AsmrProcessor", call, "(Lorg/quiltmc/asmr/processor/AsmrTransformer;Ljava/lang/String;)V"));
        }
        method.instructions.add(new InsnNode(Opcodes.RETURN));
        method.instructions.add(end);
        method.maxStack = 0;

        return method;
    }

    private MethodNode read() {
        MethodNode method = new MethodNode(ASM4, ACC_PUBLIC, "read", "(Lorg/quiltmc/asmr/processor/AsmrProcessor;)V", "", new String[]{});

        LabelNode start = new LabelNode(new Label());
        LabelNode end = new LabelNode(new Label());

        method.localVariables.add(new LocalVariableNode("this", "L" + transformer.name + ";", "", start, end, 0));
        method.localVariables.add(new LocalVariableNode("processor", "Lorg/quiltmc/asmr/processor/AsmrProcessor;", "", start, end, 1));
        method.maxLocals = 2;

        method.instructions.add(start);
        method.instructions.add(new InsnNode(Opcodes.RETURN));
        method.instructions.add(end);
        method.maxStack = 0;

        return method;
    }

    public byte[] toByteArray() {
        ClassWriter writer = new ClassWriter(0);
        transformer.accept(writer);
        return writer.toByteArray();
    }
}
