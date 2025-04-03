package xyz.lilyflower.psc.asm.util;

import cpw.mods.fml.common.FMLCommonHandler;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.apache.commons.lang3.ClassUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.reflections.Reflections;
import xyz.lilyflower.psc.loader.asm.StardustCoreASMLoader;

@SuppressWarnings("deprecation")
public class StardustCoreTransformerLoader {
    private static final HashMap<String, Class<? extends StardustCoreTransformer>> TRANSFORMERS = new HashMap<>();

    public static byte[] run(String name, byte[] bytes) {
        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        ClassWriter writer = new ClassWriter(3);
        reader.accept(node, 0);

        try {
            String clazz = ClassUtils.getShortClassName(name).replaceAll("\\.", "");
            if (TRANSFORMERS.containsKey(clazz)) {
                StardustCoreASMLoader.LOGGER.debug("Transforming class '{}'...", name);
                Class<? extends StardustCoreTransformer> transformer = TRANSFORMERS.get(clazz);
                StardustCoreTransformer instance = transformer.newInstance();


                ArrayList<String> methods = new ArrayList<>();
                for (Method method : transformer.getDeclaredMethods()) {
                    if (method.getName().startsWith("patch_")) {
                        methods.add(method.getName());
                    }
                }

                for (MethodNode method : node.methods) {
                    if (methods.contains("patch_" + method.name)) {
                        StardustCoreASMLoader.LOGGER.debug("Transforming method '{}'...", method.name);
                        Method patcher = transformer.getDeclaredMethod("patch_" + method.name, StardustCoreMethodTransformerData.class);
                        patcher.setAccessible(true);
                        patcher.invoke(instance, new StardustCoreMethodTransformerData(node, method));
                    }
                }

                node.accept(writer);
                bytes = writer.toByteArray();
            }
        } catch (NoSuchMethodException | NullPointerException ignored) {
            ignored.printStackTrace();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            StardustCoreASMLoader.LOGGER.fatal("// LAUNCH FAILED //");
            exception.printStackTrace();
            FMLCommonHandler.instance().exitJava(1, true);
        }

        File clazz = new File("classes/" + ClassUtils.getShortClassName(name).replaceAll("\\.", "\\$"));
        try (FileOutputStream output = new FileOutputStream(clazz)) {
            output.write(bytes);
        } catch (IOException ignored) {}

        return bytes;
    }

    static {
        StardustCoreASMLoader.LOGGER.debug("Scanning class transformers...");
        Reflections reflections = new Reflections("xyz.lilyflower.psc.asm.transformers");
        Set<Class<? extends StardustCoreTransformer>> classes = reflections.getSubTypesOf(StardustCoreTransformer.class);

        for (Class<? extends StardustCoreTransformer> clazz : classes) {
            String name = clazz.getSimpleName();
            String transformer = name.substring(0, name.length() - 11); // remove "Transformer" in the name
            TRANSFORMERS.put(transformer, clazz);
        }

        TRANSFORMERS.forEach((transformer, clazz) -> {
            StardustCoreASMLoader.LOGGER.debug("Added class transformer for {}", transformer);
        });
    }
}
