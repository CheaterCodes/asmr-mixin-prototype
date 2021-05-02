package __packageName__;

import org.quiltmc.asmr.processor.AsmrProcessor;
import org.quiltmc.asmr.processor.AsmrTransformer;
import org.quiltmc.asmr.processor.capture.AsmrCopyNodeCaputre;
import org.quiltmc.asmr.processor.capture.AsmrNodeCapture;
import org.quiltmc.asmr.processor.capture.AsmrSliceCapture;
import org.quiltmc.asmr.processor.tree.AsmrNode;
import org.quiltmc.asmr.processor.tree.AsmrValueListNode;
import org.quiltmc.asmr.processor.tree.AsmrValueNode;
import org.quiltmc.asmr.processor.tree.member.AsmrMethodListNode;
import org.quiltmc.asmr.processor.tree.member.AsmrMethodNode;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class __transformerName__ implements AsmrTransformer {
    public List<String> getPhases() {
        List<String> l = new ArrayList<>();
        l.add("READ_INITIAL");
        l.add("READ_INITIAL");
        l.add("READ_INITIAL");
        l.add("READ_INITIAL");
        l.add("READ_INITIAL");
        l.add(null);
        return l;
    }

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
            AsmrSliceCapture<AsmrMethodNode> targetCapture = processor.refCapture(targetClass.methods(), 0, 0, true, false);
            processor.withClass(source, sourceClass -> {
                sourceClass.methods().forEach(sourceMethod -> {
                    if ((sourceMethod.name().value() + sourceMethod.desc().value()).equals(method)) {
                        AsmrNodeCapture<AsmrMethodNode> sourceCapture = processor.copyCapture(sourceMethod);
                        processor.addWrite(this, targetCapture, () -> {
                            AsmrMethodListNode methods = new AsmrMethodListNode();
                            AsmrMethodNode m = methods.add();
                            processor.substitute(m, sourceCapture);
                            replaceRecursive(processor, m, source, target);
                            return methods;
                        });
                    }
                });
            });
        });
    }

    private void replaceRecursive(AsmrProcessor processor, AsmrNode<?> node, String replace, String with) {
        if (node instanceof AsmrValueNode) {
            Object value = ((AsmrValueNode<?>)node).value();
            if (value instanceof String && ((String)value).contains(replace)) {
                String newValue = ((AsmrValueNode<String>)node).value().replace(replace, with);
                AsmrValueNode<String> sourceNode = new AsmrValueNode<String>().init(newValue);
                ((AsmrValueNode<String>)node).copyFrom(sourceNode);
            }
        }

        node.children().forEach(c -> replaceRecursive(processor, c, replace, with));
    }
}
