package xyz.lilyflower.psc.unused;

import cpw.mods.fml.common.asm.transformers.TerminalTransformer;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import xyz.lilyflower.psc.asm.util.StardustCoreTransformer;
import xyz.lilyflower.psc.asm.util.StardustCoreMethodTransformerData;

@SuppressWarnings("unused")
public class TerminalTransformerExitVisitorTransformer implements StardustCoreTransformer {
    private static final String EV_CALLBACK_OWNER = org.objectweb.asm.Type.getInternalName(TerminalTransformer.ExitVisitor.class);

    public static final MethodVisitor ALLOW_EXIT_CALLS = new MethodVisitor(Opcodes.ASM5) {
        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            final boolean warn = !(owner.equals("net/minecraft/client/Minecraft") ||
                    owner.equals("net/minecraft/server/dedicated/DedicatedServer") ||
                    owner.equals("cpw/mods/fml/common/FMLCommonHandler") ||
                    owner.startsWith("com/jcraft/jogg/") ||
                    owner.startsWith("scala/sys/") ||
                    owner.startsWith("xyz/lilyflower/")
            );

            if (opcode == Opcodes.INVOKESTATIC && owner.equals("java/lang/System") && name.equals("exit") && desc.equals("(I)V")) {
                if (warn) {
                    FMLRelaunchLog.warning("=============================================================");
                    FMLRelaunchLog.warning("MOD HAS DIRECT REFERENCE System.exit() THIS IS NOT ALLOWED REROUTING TO FML!");
                    FMLRelaunchLog.warning("Offendor: %s.%s%s", owner, name, desc);
                    FMLRelaunchLog.warning("Use FMLCommonHandler.exitJava instead");
                    FMLRelaunchLog.warning("=============================================================");
                }

                owner = EV_CALLBACK_OWNER;
                name = "systemExitCalled";
            } else if (opcode == Opcodes.INVOKEVIRTUAL && owner.equals("java/lang/Runtime") && name.equals("exit") && desc.equals("(I)V")) {
                if (warn) {
                    FMLRelaunchLog.warning("=============================================================");
                    FMLRelaunchLog.warning("MOD HAS DIRECT REFERENCE Runtime.exit() THIS IS NOT ALLOWED REROUTING TO FML!");
                    FMLRelaunchLog.warning("Offendor: %s.%s%s", owner, name, desc);
                    FMLRelaunchLog.warning("Use FMLCommonHandler.exitJava instead");
                    FMLRelaunchLog.warning("=============================================================");
                }

                opcode = Opcodes.INVOKESTATIC;
                owner = EV_CALLBACK_OWNER;
                name = "runtimeExitCalled";
                desc = "(Ljava/lang/Runtime;I)V";
            } else if (opcode == Opcodes.INVOKEVIRTUAL && owner.equals("java/lang/Runtime") && name.equals("halt") && desc.equals("(I)V")) {
                if (warn) {
                    FMLRelaunchLog.warning("=============================================================");
                    FMLRelaunchLog.warning("MOD HAS DIRECT REFERENCE Runtime.halt() THIS IS NOT ALLOWED REROUTING TO FML!");
                    FMLRelaunchLog.warning("Offendor: %s.%s%s", owner, name, desc);
                    FMLRelaunchLog.warning("Use FMLCommonHandler.exitJava instead");
                    FMLRelaunchLog.warning("=============================================================");
                }

                opcode = Opcodes.INVOKESTATIC;
                owner = EV_CALLBACK_OWNER;
                name = "runtimeHaltCalled";
                desc = "(Ljava/lang/Runtime;I)V";
            }

            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    };

    void patch_visitMethod(StardustCoreMethodTransformerData data) {
        System.out.println("a");

        InsnList insns = new InsnList();

        insns.add(new FieldInsnNode(Opcodes.GETSTATIC, "xyz/lilyflower/psc/asm/TerminalTransformerExitVisitorTransformer", "ALLOW_EXIT_CALLS", "Lorg/objectweb/asm/MethodVisitor;"));
        insns.add(new InsnNode(Opcodes.ARETURN));

        data.method.instructions.insert(insns);
    }
}
