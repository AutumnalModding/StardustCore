package xyz.lilyflower.psc.loader.asm;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.lilyflower.psc.asm.util.FakeTransformerExclusions;

@SuppressWarnings("unchecked")
@IFMLLoadingPlugin.MCVersion("1.7.10")
@IFMLLoadingPlugin.SortingIndex(-1)
@IFMLLoadingPlugin.TransformerExclusions({"xyz.lilyflower.psc.asm", "xyz.lilyflower.psc.loader.asm", "org.reflections", "org.slf4j", "javassist"})
public class StardustCoreASMLoader implements IFMLLoadingPlugin {
    public static final Logger LOGGER = LogManager.getLogger("Stardust Core Class Transformer System");

    @Override
    public String[] getASMTransformerClass() {
        return new String[]{StardustCoreTransformerInit.class.getName()};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass() {
        return null;
    }

    // this is incredibly cursed lmfao
    static {
        LOGGER.info("// PREPARING TO FIRE //");

        ClassLoader cl = StardustCoreASMLoader.class.getClassLoader();
        if (cl instanceof LaunchClassLoader loader) {
            try {
                Field field = loader.getClass().getDeclaredField("transformerExceptions");
                field.setAccessible(true);
                Object obj = field.get(loader);

                if (obj instanceof Set) {
                    Set<String> set = (Set<String>) obj;
                    FakeTransformerExclusions exclusions = new FakeTransformerExclusions();
                    exclusions.addAll(set);
                    field.set(loader, exclusions);
                }
            } catch (NoSuchFieldException | IllegalAccessException exception) {
                LOGGER.fatal("// LAUNCH FAILURE //");
                exception.printStackTrace();
                FMLCommonHandler.instance().exitJava(1, true);
            }
        }
    }
}