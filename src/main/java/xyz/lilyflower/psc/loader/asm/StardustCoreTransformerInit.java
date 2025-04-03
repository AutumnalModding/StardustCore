package xyz.lilyflower.psc.loader.asm;

import net.minecraft.launchwrapper.IClassTransformer;
import xyz.lilyflower.psc.asm.util.StardustCoreTransformerLoader;

public class StardustCoreTransformerInit implements IClassTransformer {
    public StardustCoreTransformerInit() {}

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        return StardustCoreTransformerLoader.run(name, bytes);
    }
}