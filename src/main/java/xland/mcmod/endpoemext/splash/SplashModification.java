package xland.mcmod.endpoemext.splash;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Consumer;

public record SplashModification(
        Collection<String> add,
        Collection<String> remove
) {
    public SplashModification() {
        this(new LinkedHashSet<>(), new LinkedHashSet<>());
    }

    private void add(String s) {
        remove.remove(s);
        add.add(s);
    }

    private void remove(String s) {
        add.remove(s);
        remove.add(s);
    }

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Identifier SPLASH_MODIFICATION = new Identifier("texts/splash_modify.json");

    public static List<String> modify(List<String> list) {
        list = new ArrayList<>(list);
        var resourceManager = MinecraftClient.getInstance().getResourceManager();
        SplashModification modification = new SplashModification();

        for (Resource resource : resourceManager.getAllResources(SPLASH_MODIFICATION)) {
            try (var reader = resource.getReader()) {
                final JsonObject obj = JsonHelper.deserialize(reader);
                readArray(obj, "add", modification::add);
                readArray(obj, "remove", modification::remove);
            } catch (Exception e) {
                LOGGER.error("Failed to read splash config from {}", SPLASH_MODIFICATION, e);
            }
        }

        list.addAll(modification.add());
        list.removeAll(modification.remove());
        return list;
    }

    private static void readArray(JsonObject obj, String key, Consumer<String> c) {
        final JsonArray array = JsonHelper.getArray(obj, key, null);
        if (array == null) return;
        for (JsonElement e : array) {
            c.accept(JsonHelper.asString(e, key));
        }
    }
}
