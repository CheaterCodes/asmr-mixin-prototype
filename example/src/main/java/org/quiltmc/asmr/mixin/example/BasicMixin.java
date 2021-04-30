package org.quiltmc.asmr.mixin.example;

import org.quiltmc.asmr.mixin.Mixin;

@Mixin(target = "org/quiltmc/asmr/mixin/example/BasicClass")
public abstract class BasicMixin implements Runnable {
    private int field;

    @Override
    public void run() {
        // TODO Auto-generated method stub
    }

    private Runnable complexMethod(int len, boolean really, BasicClass.Inner self) {
        return this;
    }
}
