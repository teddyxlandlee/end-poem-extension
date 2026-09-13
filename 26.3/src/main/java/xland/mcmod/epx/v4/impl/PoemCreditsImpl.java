package xland.mcmod.epx.v4.impl;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.Nullable;
import xland.mcmod.enchlevellangpatch.api.EnchantmentLevelLangPatch;
import xland.mcmod.epx.v4.support.ClientResource;
import xland.mcmod.epx.v4.util.NamespacedKey;
import xland.mcmod.epx.v4.util.PoemCredits;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@NotNullByDefault
public class PoemCreditsImpl extends PoemCredits implements ResourceManagerReloadListener {
    private static final Function<Map.Entry<Identifier, Resource>, Map.Entry<NamespacedKey, ClientResource>> TRANSFORM = e -> Map.entry(
            ClientEnvironmentImpl.fromVanillaId(e.getKey()),
            e.getValue()::openAsReader
    );

    protected static volatile Map<String, @Nullable String> poemCredits = Collections.emptyMap();
    protected static volatile Map<String, @Nullable String> postCreditsAuthors = Collections.emptyMap();

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        var map = resourceManager.listResources(RESOURCE_DIR, id -> isMetadataResource(ClientEnvironmentImpl.fromVanillaId(id)));
        poemCredits = loadToMap(Iterables.transform(map.entrySet(), TRANSFORM));
        LOGGER.debug("Loaded poem credits: {}", poemCredits);

        map = resourceManager.listResources(RESOURCE_DIR, id -> isPostCreditsAuthor(ClientEnvironmentImpl.fromVanillaId(id)));
        postCreditsAuthors = loadPostCreditsAuthors(Iterables.transform(map.entrySet(), TRANSFORM));
        LOGGER.debug("Loaded post-credits author: {}", postCreditsAuthors);
    }

    public static void initFabric() {
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(ClientEnvironmentImpl.toVanillaId(KEY), new PoemCreditsImpl());
        EnchantmentLevelLangPatch.registerPatch(TRANSLATION_PREDICATE, patchHook(() -> poemCredits, () -> postCreditsAuthors, I18n::get)::apply);
    }

    public static void appendLines(List<@Nullable String> lines) {
        patchForgeLike(lines, () -> poemCredits, () -> postCreditsAuthors, I18n::get);
    }
}
