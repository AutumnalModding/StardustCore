package xyz.lilyflower.psc.asm.util;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

public class StardustCoreTransformerUtils {
    public static class LogLevel {
        public static final String DEBUG = "debug";
        public static final String INFO = "info";
        public static final String WARNING = "warn";
        public static final String ERROR = "error";
        public static final String FATAL = "fatal"; // Wake the fucking sysadmin up
    }

    public static void NoopClassTransformerMethod(StardustCoreMethodTransformerData data) {
        InsnList instructions = new InsnList();

        instructions.add(new VarInsnNode(Opcodes.ALOAD, 2));
        instructions.add(new InsnNode(Opcodes.ARETURN));

        data.method.instructions.insert(instructions);
    }

    public static void LogMessage(StardustCoreMethodTransformerData data, String level, String message) {
        InsnList instructions = new InsnList();

        instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, "xyz/lilyflower/psc/loader/asm/StardustCoreASMLoader", "LOGGER", "Lorg/apache/logging/log4j/Logger;"));
        instructions.add(new LdcInsnNode(message));
        instructions.add(new MethodInsnNode(Opcodes.INVOKEINTERFACE, "org/apache/logging/log4j/Logger", level, "(Ljava/lang/String;)V", true));

        data.method.instructions.insert(instructions);
    }

    public static void KillJVM(StardustCoreMethodTransformerData data) {
        InsnList instructions = new InsnList();

        instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "cpw/mods/fml/common/FMLCommonHandler", "instance", "()Lcpw/mods/fml/common/FMLCommonHandler;", false));
        instructions.add(new InsnNode(Opcodes.ICONST_0));
        instructions.add(new InsnNode(Opcodes.ICONST_1));
        instructions.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "cpw/mods/fml/common/FMLCommonHandler", "exitJava", "(IZ)V", false));

        data.method.instructions.insert(instructions);
    }
}
