package xland.mcmod.epx.v4.fabric;

import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.server.packs.PackType;
import org.jetbrains.annotations.NotNullByDefault;
import xland.mcmod.enchlevellangpatch.api.EnchantmentLevelLangPatch;
import xland.mcmod.epx.v4.impl.ClientEnvironmentImpl;
import xland.mcmod.epx.v4.impl.PoemCreditsCommon;

@NotNullByDefault
public class LangPatchCopyright extends PoemCreditsCommon {
    public static void init() {
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(ClientEnvironmentImpl.toVanillaId(KEY), new LangPatchCopyright());
        EnchantmentLevelLangPatch.registerPatch(TRANSLATION_PREDICATE, patchHook(() -> poemCredits, () -> postCreditsAuthors, I18n::get)::apply);
    }
}
