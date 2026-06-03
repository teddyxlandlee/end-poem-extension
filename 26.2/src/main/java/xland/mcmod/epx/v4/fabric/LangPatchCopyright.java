package xland.mcmod.epx.v4.fabric;

import com.google.common.collect.Iterables;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.Nullable;
import xland.mcmod.enchlevellangpatch.api.EnchantmentLevelLangPatch;
import xland.mcmod.epx.v4.impl.ClientEnvironmentImpl;
import xland.mcmod.epx.v4.util.PoemCredits;

import java.util.Collections;
import java.util.Map;

@NotNullByDefault
public class LangPatchCopyright extends PoemCredits implements ResourceManagerReloadListener {
    private static volatile Map<String, @Nullable String> poemCredits = Collections.emptyMap();

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        var map = resourceManager.listResources(RESOURCE_DIR, id -> isMetadataResource(ClientEnvironmentImpl.fromVanillaId(id)));
        poemCredits = loadToMap(Iterables.transform(map.entrySet(), e -> Map.entry(
                ClientEnvironmentImpl.fromVanillaId(e.getKey()),
                e.getValue()::openAsReader
        )));
    }

    public static void init() {
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(ClientEnvironmentImpl.toVanillaId(KEY), new LangPatchCopyright());
        EnchantmentLevelLangPatch.registerPatch(TRANSLATION_PREDICATE, patchHook(() -> poemCredits, I18n::get)::apply);
    }
}
