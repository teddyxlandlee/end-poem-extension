package xland.mcmod.endpoemext.splash;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.GsonHelper;
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
    private static final ResourceLocation SPLASH_MODIFICATION = new ResourceLocation("texts/splash_modify.json");

    public static List<String> modify(List<String> list) {
        list = new ArrayList<>(list);
        var resourceManager = Minecraft.getInstance().getResourceManager();
        SplashModification modification = new SplashModification();

        for (Resource resource : resourceManager.getResourceStack(SPLASH_MODIFICATION)) {
            try (var reader = resource.openAsReader()) {
                final JsonObject obj = GsonHelper.parse(reader);
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
        final JsonArray array = GsonHelper.getAsJsonArray(obj, key, null);
        if (array == null) return;
        for (JsonElement e : array) {
            c.accept(GsonHelper.convertToString(e, key));
        }
    }
}
