package xyz.lilyflower.psc;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.lilyflower.psc.config.ConfigLoader;

@Mod(modid = "psc", version = StardustCore.VERSION, dependencies = "")
public class StardustCore {
    public static final String VERSION = "1.0";

    public static final Logger LOGGER = LogManager.getLogger("Stardust Core");

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ConfigLoader.run(event.getSuggestedConfigurationFile());
//        System.exit(0);
    }
}