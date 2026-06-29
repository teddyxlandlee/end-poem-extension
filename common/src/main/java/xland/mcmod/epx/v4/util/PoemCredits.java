package xland.mcmod.epx.v4.util;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import xland.mcmod.epx.v4.support.ClientEnvironment;
import xland.mcmod.epx.v4.support.ClientResource;

import java.io.BufferedReader;
import java.util.*;
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
            final String langKey = trimPath(entry.getKey().path(), ".metadata");
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

    public static boolean isPostCreditsAuthor(NamespacedKey key) {
        return "end_poem_extension".equals(key.namespace()) && key.path().endsWith(".postcredits-author");
    }

    public static Map<String, @Nullable String> loadPostCreditsAuthors(Iterable<? extends Map.Entry<? extends NamespacedKey, ? extends ClientResource>> resources) {
        Map<String, @Nullable String> newMap = new HashMap<>();
        for (var entry: resources) {
            final String langKey = trimPath(entry.getKey().path(), ".postcredits-author");
            @Nullable String author;
            try (var reader = entry.getValue().openReader(); BufferedReader br = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader)) {
                // Read first line, drop the rest
                author = br.readLine();
            } catch (Exception e) {
                LOGGER.warn("Failed to load post-credits author in language {}", langKey, e);
                continue;
            }
            if (author == null || (author = author.trim()).isEmpty()) {
                continue;
            }

            newMap.put(langKey, author);
        }

        return Collections.unmodifiableMap(newMap);
    }

    private static String trimPath(String path, String suffix) {
        if (path.startsWith("texts/end_poem/")) path = path.substring("texts/end_poem/".length());
        if (path.endsWith(suffix)) path = path.substring(0, path.length() - suffix.length());
        return path;
    }

    public static final Predicate<String> TRANSLATION_PREDICATE = Predicate.isEqual("modmenu.descriptionTranslation.end-poem-extension");
    private static final String LOADED_WITH_KEY = "epx.poem.copyright.loaded_with";
    private static final String POST_CREDITS_AUTHOR_KEY = "epx.poem.copyright.post_credits.author";

    private static List<String> loadSuffix(
            Supplier<Map<String, @Nullable String>> poemCreditsMap,
            Supplier<Map<String, @Nullable String>> postCreditsAuthorMap,
            BiFunction<? super String, ? super Object, ? extends String> i18nLoopback
    ) {
        final var ret = new ArrayList<String>(2);
        final String currentLanguage = ClientEnvironment.getInstance().getCurrentLanguage();
        @Nullable String langCredit = poemCreditsMap.get().get(currentLanguage);
        @Nullable String postCreditsAuthor = postCreditsAuthorMap.get().get(currentLanguage);
        if (langCredit != null) {
            ret.add(i18nLoopback.apply(LOADED_WITH_KEY, langCredit));
        }
        if (postCreditsAuthor != null) {
            ret.add(i18nLoopback.apply(POST_CREDITS_AUTHOR_KEY, postCreditsAuthor));
        }
        return ret;
    }

    public static BiFunction<Map<String, String>, String, @Nullable String> patchHook(
            Supplier<Map<String, @Nullable String>> poemCreditsMap,
            Supplier<Map<String, @Nullable String>> postCreditsAuthorMap,
            BiFunction<? super String, ? super Object, ? extends String> i18nLoopback
    ) {
        return (translationStorage, key) -> {
            final @Nullable String originalTranslation = translationStorage.get(key);
            if (originalTranslation == null) return null;
            final List<String> suffix = loadSuffix(poemCreditsMap, postCreditsAuthorMap, i18nLoopback);
            if (suffix.isEmpty()) return null;
            StringBuilder sb = new StringBuilder(originalTranslation);
            suffix.forEach(sb::append);
            return sb.toString();
        };
    }
    //ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(IDENTIFIER, new LangPatchCopyright());
    //EnchantmentLevelLangPatch.registerPatch(TRANSLATION_PREDICATE, patchHook(() -> map, I18n::get)::apply;

    public static void patchForgeLike(List<? super String> lines,
                                      Supplier<Map<String, @Nullable String>> poemCreditsMap,
                                      Supplier<Map<String, @Nullable String>> postCreditsAuthorMap,
                                      BiFunction<? super String, ? super Object, ? extends String> i18nLoopback) {
        lines.addAll(loadSuffix(poemCreditsMap, postCreditsAuthorMap, i18nLoopback));
    }
}
