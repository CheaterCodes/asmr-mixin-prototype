package __packageName__;

import org.quiltmc.asmr.processor.AsmrProcessor;
import org.quiltmc.asmr.processor.AsmrTransformer;
import org.quiltmc.asmr.processor.capture.AsmrNodeCapture;
import org.quiltmc.asmr.processor.capture.AsmrSliceCapture;
import org.quiltmc.asmr.processor.tree.AsmrValueListNode;
import org.quiltmc.asmr.processor.tree.AsmrValueNode;
import org.quiltmc.asmr.processor.tree.member.AsmrMethodListNode;
import org.quiltmc.asmr.processor.tree.member.AsmrMethodNode;

@SuppressWarnings("unused")
public class __transformerName__ implements AsmrTransformer {
    public void apply(AsmrProcessor processor) {
    }

    @Override
    public void read(AsmrProcessor processor) {
        //Transformations:__body__
    }

    private void implementInterface(AsmrProcessor processor, String target, String iface) {
        processor.withClass(target, node -> {
            AsmrSliceCapture<AsmrValueNode<String>> capture = processor.refCapture(node.interfaces(), 0, 0, true, false);
            processor.addWrite(this, capture, () -> {
                AsmrValueListNode<String> list = new AsmrValueListNode<String>();
                list.add().init(iface);
                return list;
            });
        });
    }

    private void copyMethod(AsmrProcessor processor, String target, String source, String method) {
        processor.withClass(target, targetClass -> {
            AsmrSliceCapture<AsmrMethodNode> targeCapture = processor.refCapture(targetClass.methods(), 0, 0, true, false);
            processor.withClass(source, sourceClass -> {
                sourceClass.methods().forEach(sourceMethod -> {
                    if ((sourceMethod.name().value() + sourceMethod.desc().value()).equals(method)) {
                        AsmrNodeCapture<AsmrMethodNode> sourceCapture = processor.copyCapture(sourceMethod);
                        processor.addWrite(this, targeCapture, () -> {
                            AsmrMethodListNode methods = new AsmrMethodListNode();
                            processor.substitute(methods.add(), sourceCapture);
                            return methods;
                        });
                    }
                });
            });
        });
    }
}
