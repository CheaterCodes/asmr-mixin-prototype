package org.quiltmc.asmr.compiler;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CompilerLibTests {
    @Test
    public void basic() throws Exception {
        Transformer transformer = new Transformer.Builder()
                .name("org/quiltmc/asmr/compiler/BasicTransformer")
                .compiler("CompilerLibTest")
                .inPhase("READ_INITIAL", null)
                .afterRound("after/round/Transformer")
                .beforeRound("before/round/Transformer")
                .afterWrite("after/write/Transformer")
                .beforeWrite("before/write/Transformer")
                .build();

        Path path = Paths.get("build/generated/classes/transformers/java/test", "org/quiltmc/asmr/compiler");
        Files.createDirectories(path);
        Files.deleteIfExists(path.resolve("BasicTransformer.class"));
        Files.createFile(path.resolve("BasicTransformer.class"));
        Files.newOutputStream(path.resolve("BasicTransformer.class")).write(transformer.toByteArray());
    }
}
