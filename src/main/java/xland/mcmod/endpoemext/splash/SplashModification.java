package xland.mcmod.endpoemext.splash;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNullByDefault;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
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
    private static final Identifier SPLASH_MODIFICATION = Identifier.withDefaultNamespace("texts/splash_modify.json");

    private static SplashModification readModifications() {
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
        final JsonArray array = GsonHelper.getAsJsonArray(obj, key, null);
        if (array == null) return;
        for (JsonElement e : array) {
            c.accept(GsonHelper.convertToString(e, key));
        }
    }
}
