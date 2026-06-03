package xland.mcmod.epx.v4.fabric;

import com.google.common.collect.Iterables;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.Nullable;
import xland.mcmod.enchlevellangpatch.api.EnchantmentLevelLangPatch;
import xland.mcmod.epx.v4.impl.ClientEnvironmentImpl;
import xland.mcmod.epx.v4.util.PoemCredits;

import java.util.Collections;
import java.util.Map;

@NotNullByDefault
public class LangPatchCopyright extends PoemCredits implements SimpleSynchronousResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return ClientEnvironmentImpl.toVanillaId(KEY);
    }

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
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new LangPatchCopyright());
        EnchantmentLevelLangPatch.registerPatch(TRANSLATION_PREDICATE, patchHook(() -> poemCredits, I18n::get)::apply);
    }
}
