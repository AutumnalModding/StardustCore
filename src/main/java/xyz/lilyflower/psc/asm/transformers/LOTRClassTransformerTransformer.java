package xyz.lilyflower.psc.asm.transformers;

import xyz.lilyflower.psc.asm.util.StardustCoreTransformer;
import xyz.lilyflower.psc.asm.util.StardustCoreMethodTransformerData;
import xyz.lilyflower.psc.asm.util.StardustCoreTransformerUtils;

@SuppressWarnings("unused")
public class LOTRClassTransformerTransformer implements StardustCoreTransformer {
    void patch_patchBlockFire(StardustCoreMethodTransformerData data) {
        StardustCoreTransformerUtils.NoopClassTransformerMethod(data);
    }

    void patch_patchSpawnerAnimals(StardustCoreMethodTransformerData data) {
        StardustCoreTransformerUtils.NoopClassTransformerMethod(data);
    }
}