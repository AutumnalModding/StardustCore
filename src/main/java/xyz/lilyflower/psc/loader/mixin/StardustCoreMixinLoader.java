package xyz.lilyflower.psc.loader.mixin;

import com.gtnewhorizon.gtnhmixins.ILateMixinLoader;
import com.gtnewhorizon.gtnhmixins.LateMixin;
import net.minecraft.launchwrapper.Launch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.lib.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StardustCoreMixinLoader implements IMixinConfigPlugin {

    private static final Logger LOG = LogManager.getLogger("StardustCore Mixin Loader");
    private static final Path MODS_DIRECTORY_PATH = new File(Launch.minecraftHome, "mods/").toPath();

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return StardustCoreMixinLoader.getMixins(StardustCoreMixins.Phase.NORMAL);
    }

    public static List<String> getMixins(StardustCoreMixins.Phase phase) {
        List<String> mixins = new ArrayList<>();
        for (StardustCoreMixins mixin : StardustCoreMixins.values()) {
            if (mixin.phase == phase) {
                LOG.debug("Loading mixin: {}", mixin.mixinClass);
                mixins.add(mixin.mixinClass);
            }
        }

        return mixins;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @LateMixin
    public static class FoTLateMixinLoader implements ILateMixinLoader {
        public FoTLateMixinLoader() {}

        @Override
        public String getMixinConfig() {
            return "mixins.psc.late.json";
        }

        @Override
        public List<String> getMixins(Set<String> loadedMods) {
            return StardustCoreMixinLoader.getMixins(StardustCoreMixins.Phase.LATE);
        }
    }
}


