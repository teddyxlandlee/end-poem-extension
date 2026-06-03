package xland.mcmod.epx.v4.splash;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import xland.mcmod.epx.v4.util.JsonHelper;
import org.jetbrains.annotations.NotNullByDefault;
import org.slf4j.Logger;
import xland.mcmod.epx.v4.support.ClientEnvironment;
import xland.mcmod.epx.v4.support.ClientResource;
import xland.mcmod.epx.v4.util.NamespacedKey;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

@NotNullByDefault
public record SplashModification(
        Collection<String> adds,
        Collection<String> removes
) {
    public SplashModification() {
        this(new LinkedHashSet<>(), new LinkedHashSet<>());
    }

    private void add(String s) {
        removes.remove(s);
        adds.add(s);
    }

    private void remove(String s) {
        adds.remove(s);
        removes.add(s);
    }

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final NamespacedKey SPLASH_MODIFICATION = NamespacedKey.ofMinecraft("texts/splash_modify.json");

    private static SplashModification readModifications() {
        SplashModification modification = new SplashModification();

        Iterable<? extends ClientResource> resources;
        try {
            resources = ClientEnvironment.getInstance().getResourceManager().readResources(SPLASH_MODIFICATION);
        } catch (Exception e) {
            LOGGER.error("Failed to load splash configs from {}", SPLASH_MODIFICATION, e);
            resources = Collections.emptySet();
        }
        for (ClientResource resource : resources) {
            try (var reader = resource.openReader()) {
                final JsonObject obj = JsonHelper.parseObject(reader);
                readArray(obj, "add", modification::add);
                readArray(obj, "remove", modification::remove);
            } catch (Exception e) {
                LOGGER.error("Failed to read splash config from {}", SPLASH_MODIFICATION, e);
            }
        }
        return modification;
    }

    public static Stream<String> modify(Stream<String> stream) {
        SplashModification modification = readModifications();

        stream = Stream.concat(stream, modification.adds().stream());
        stream = stream.filter(Predicate.not(modification.removes()::contains));
        return stream;
    }

    @Deprecated(since = "Minecraft 1.21.11")
    public static List<String> modify(List<String> list) {
        list = new ArrayList<>(list);
        SplashModification modification = readModifications();

        list.addAll(modification.adds());
        list.removeAll(modification.removes());
        return list;
    }

    private static void readArray(JsonObject obj, String key, Consumer<String> c) {
        final JsonArray array = JsonHelper.getAsJsonArray(obj, key, null);
        if (array == null) return;
        for (JsonElement e : array) {
            c.accept(JsonHelper.convertToString(e, key));
        }
    }
}
