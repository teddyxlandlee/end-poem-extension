package xland.mcmod.endpoemext.fabric;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import xland.mcmod.enchlevellangpatch.api.EnchantmentLevelLangPatch;
import xland.mcmod.endpoemext.VanillaTextLocator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.function.Predicate;

public final class LangPatchCopyright implements SimpleSynchronousResourceReloadListener {
    private static final ResourceLocation IDENTIFIER = ResourceLocation.fromNamespaceAndPath("end-poem-extension", "langpatch_copyright");
    public static final Logger LOGGER = LogUtils.getLogger();
    private static volatile Map<String, String> poemCredits = Collections.emptyMap();
    private static final Gson GSON = new Gson();

    @Override
    public ResourceLocation getFabricId() {
        return IDENTIFIER;
    }

    @Override
    public void onResourceManagerReload(@Nonnull ResourceManager resourceManager) {
        Map<ResourceLocation, Resource> map = resourceManager.listResources("texts/end_poem",
                id -> "end_poem_extension".equals(id.getNamespace()) && id.getPath().endsWith(".metadata"));
        Map<String, String> newMap = Maps.newHashMap();
        for (Map.Entry<ResourceLocation, Resource> entry : map.entrySet()) {
            final String langKey = StringUtils.removeEnd(
                    StringUtils.removeStart(entry.getKey().getPath(), "texts/end_poem/"),
                    ".metadata"
            );
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

    public static void load() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new LangPatchCopyright());
        EnchantmentLevelLangPatch.registerPatch(
                Predicate.isEqual("modmenu.descriptionTranslation.end-poem-extension"),
                (translationStorage, key) -> {
                    final String originTranslation = translationStorage.get(key);
                    final Map<String, String> credits = poemCredits;
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
