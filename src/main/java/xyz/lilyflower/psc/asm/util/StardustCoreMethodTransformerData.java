package xyz.lilyflower.psc.asm.util;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class StardustCoreMethodTransformerData {
    public final ClassNode node;
    public final MethodNode method;

    StardustCoreMethodTransformerData(ClassNode node, MethodNode method) {
        this.node = node;
        this.method = method;
    }
}
