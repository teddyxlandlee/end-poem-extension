package xland.mcmod.epx.v4.fabric;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.jetbrains.annotations.NotNullByDefault;
import xland.mcmod.enchlevellangpatch.api.EnchantmentLevelLangPatch;
import xland.mcmod.epx.v4.impl.ClientEnvironmentImpl;
import xland.mcmod.epx.v4.impl.PoemCreditsCommon;

@NotNullByDefault
public class LangPatchCopyright extends PoemCreditsCommon implements SimpleSynchronousResourceReloadListener {
    @Override
    public ResourceLocation getFabricId() {
        return ClientEnvironmentImpl.toVanillaId(KEY);
    }

    public static void init() {
        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new LangPatchCopyright());
        EnchantmentLevelLangPatch.registerPatch(TRANSLATION_PREDICATE, patchHook(() -> poemCredits, () -> postCreditsAuthors, I18n::get)::apply);
    }
}
