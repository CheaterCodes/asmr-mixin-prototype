package org.quiltmc.asmr.mixin;

@Mixin(BasicClass.class)
public abstract class BasicMixin implements Runnable {
    @Override
    public void run() {
        // TODO Auto-generated method stub
    }

    private Runnable complexMethod(int len, boolean really, BasicClass.Inner self) {
        return this;
    }
}
