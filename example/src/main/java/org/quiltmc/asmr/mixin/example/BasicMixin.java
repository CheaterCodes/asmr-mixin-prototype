package org.quiltmc.asmr.mixin.example;

import org.quiltmc.asmr.mixin.Mixin;

@Mixin(target = "org/quiltmc/asmr/mixin/example/BasicClass")
public abstract class BasicMixin implements Runnable {
    private int field;

    @Override
    public void run() {
    }

    private Runnable complexMethod(int len, boolean really, BasicClass.Inner self) {
        this.field = 0;
        this.run();
        return this;
    }
}
