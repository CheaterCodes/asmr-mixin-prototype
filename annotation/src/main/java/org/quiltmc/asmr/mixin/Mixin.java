package org.quiltmc.asmr.mixin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface Mixin {
    Class<?>[] value() default { };

    String[] target() default { };
}
