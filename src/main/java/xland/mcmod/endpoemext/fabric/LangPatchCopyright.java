package xland.mcmod.endpoemext.fabric;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.GsonHelper;
import org.apache.commons.lang3.Strings;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import xland.mcmod.enchlevellangpatch.api.EnchantmentLevelLangPatch;
import xland.mcmod.endpoemext.VanillaTextLocator;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.function.Predicate;

@NotNullByDefault
public final class LangPatchCopyright implements ResourceManagerReloadListener {
    private static final Identifier IDENTIFIER = Identifier.fromNamespaceAndPath("end-poem-extension", "langpatch_copyright");
    public static final Logger LOGGER = LogUtils.getLogger();
    private static volatile Map<String, @Nullable String> poemCredits = Collections.emptyMap();
    private static final Gson GSON = new Gson();

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        Map<Identifier, Resource> map = resourceManager.listResources("texts/end_poem",
                id -> "end_poem_extension".equals(id.getNamespace()) && id.getPath().endsWith(".metadata"));
        Map<String, @Nullable String> newMap = Maps.newHashMap();
        for (Map.Entry<Identifier, Resource> entry : map.entrySet()) {
            final String langKey = trimPath(entry.getKey().getPath());
            try (BufferedReader reader = entry.getValue().openAsReader()) {
                final JsonObject obj = GSON.fromJson(reader, JsonObject.class);
                final @Nullable String demoUri = GsonHelper.getAsString(obj, "demo", null);
                newMap.put(langKey, demoUri);
            } catch (IOException | JsonParseException e) {
                LOGGER.warn("Failed to load poem translation metadata in language `{}`", langKey, e);
            }
        }
        poemCredits = newMap;
    }

    private static String trimPath(String path) {
        path = Strings.CS.removeStart(path, "texts/end_poem/");
        path = Strings.CS.removeEnd(path, ".metadata");
        return path;
    }

    public static void load() {
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(IDENTIFIER, new LangPatchCopyright());
        EnchantmentLevelLangPatch.registerPatch(
                Predicate.isEqual("modmenu.descriptionTranslation.end-poem-extension"),
                (translationStorage, key) -> {
                    final String originTranslation = translationStorage.get(key);
                    final Map<String, @Nullable String> credits = poemCredits;
                    @Nullable String o = credits.get(VanillaTextLocator.getLangCode());
                    if (o == null) {
                        return originTranslation;
                    }

                    // Use I18n to allow API re-invoked
                    String s = I18n.get("epx.poem.copyright.loaded_with", o);
                    return originTranslation + s;
                }
        );
    }
}
