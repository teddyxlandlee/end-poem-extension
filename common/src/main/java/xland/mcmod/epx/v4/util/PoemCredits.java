package xland.mcmod.epx.v4.util;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import xland.mcmod.epx.v4.support.ClientEnvironment;
import xland.mcmod.epx.v4.support.ClientResource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class PoemCredits {
    protected static final Logger LOGGER = LogUtils.getLogger();
    public static final NamespacedKey KEY = NamespacedKey.of("end-poem-extension", "langpatch_copyright");
    public static final String RESOURCE_DIR = "texts/end_poem";

    public static boolean isMetadataResource(NamespacedKey key) {
        return "end_poem_extension".equals(key.namespace()) && key.path().endsWith(".metadata");
    }

    public static Map<String, @Nullable String> loadToMap(Iterable<? extends Map.Entry<? extends NamespacedKey, ? extends ClientResource>> metadataResources) {
//        Map<Identifier, Resource> map = resourceManager.listResources(RESOURCE_DIR, id -> isMetadataResource(ClientEnvironmentImpl.fromVanillaId(id)));
        Map<String, @Nullable String> newMap = new HashMap<>();
        for (var entry: metadataResources) {
            final String langKey = trimPath(entry.getKey().path());
            try (var reader = entry.getValue().openReader()) {
                final JsonObject obj = JsonHelper.parseObject(reader);
                final @Nullable String demoUri = JsonHelper.getAsString(obj, "demo", null);
                newMap.put(langKey, demoUri);
            } catch (Exception e) {
                LOGGER.warn("Failed to load poem translation metadata in language `{}`", langKey, e);
            }
        }
        return Collections.unmodifiableMap(newMap);
    }

    private static String trimPath(String path) {
        if (path.startsWith("texts/end_poem/")) path = path.substring("texts/end_poem/".length());
        if (path.endsWith(".metadata")) path = path.substring(0, path.length() - ".metadata".length());
        return path;
    }

    public static final Predicate<String> TRANSLATION_PREDICATE = Predicate.isEqual("modmenu.descriptionTranslation.end-poem-extension");

    public static BiFunction<Map<String, String>, String, @Nullable String> patchHook(
            Supplier<Map<String, @Nullable String>> poemCreditsMap,
            BiFunction<? super String, ? super Object, ? extends String> i18nLoopback
    ) {
        return (translationStorage, key) -> {
            final @Nullable String originalTranslation = translationStorage.get(key);
            if (originalTranslation == null) return null;
            @Nullable String langCredit = poemCreditsMap.get().get(ClientEnvironment.getInstance().getCurrentLanguage());
            if (langCredit == null) return null;

            String s = i18nLoopback.apply("epx.poem.copyright.loaded_with", langCredit);
            return originalTranslation + s;
        };
    }
    //ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(IDENTIFIER, new LangPatchCopyright());
    //EnchantmentLevelLangPatch.registerPatch(TRANSLATION_PREDICATE, patchHook(() -> map, I18n::get)::apply;
}
