package xyz.lilyflower.psc.config;

import cpw.mods.fml.common.Loader;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import net.minecraftforge.common.config.Configuration;

public  final class ConfigLoader {
    public static void add(String mod, Consumer<Configuration> runner) {
        ArrayList<Consumer<Configuration>> runners = CONFIG_RUNNERS.getOrDefault(mod, new ArrayList<>());
        runners.add(runner);
        CONFIG_RUNNERS.put(mod, runners);
    }

    public static void run(File config) {
        Configuration cfg = new Configuration(config);

        CONFIG_RUNNERS.forEach((mod, runners) -> {
            if (Loader.isModLoaded(mod)) {
                for (Consumer<Configuration> runner : runners) {
                    runner.accept(cfg);
                }
            }
        });

        if (cfg.hasChanged()) {
            cfg.save();
        }
    }

    private static final HashMap<String, ArrayList<Consumer<Configuration>>> CONFIG_RUNNERS = new HashMap<>();
}