package org.quiltmc.asmr.compiler;

public class Dependency {
    private final String otherTransformer;
    private final Type type;

    public enum Type {
        BEFORE_ROUND,
        AFTER_ROUND,
        BEFORE_WRITE,
        AFTER_WRITE
    }

    public Dependency(Type type, String otherTransformer) {
        this.type = type;
        this.otherTransformer = otherTransformer;
    }

    public static Dependency beforeRound(String otherTransformer) {
        return new Dependency(Type.BEFORE_ROUND, otherTransformer);
    }

    public static Dependency afterRound(String otherTransformer) {
        return new Dependency(Type.AFTER_ROUND, otherTransformer);
    }

    public static Dependency beforeWrite(String otherTransformer) {
        return new Dependency(Type.BEFORE_WRITE, otherTransformer);
    }

    public static Dependency afterWrite(String otherTransformer) {
        return new Dependency(Type.AFTER_WRITE, otherTransformer);
    }

    public Type getType() {
        return type;
    }

    public String getOtherTransformer() {
        return otherTransformer;
    }
}
