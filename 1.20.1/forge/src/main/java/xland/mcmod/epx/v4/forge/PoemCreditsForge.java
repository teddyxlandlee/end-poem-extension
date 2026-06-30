package xland.mcmod.epx.v4.forge;

import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.forgespi.language.IModInfo;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.Nullable;
import xland.mcmod.epx.v4.impl.PoemCreditsCommon;

import java.util.List;

@NotNullByDefault
public class PoemCreditsForge extends PoemCreditsCommon {
    @SuppressWarnings("unused") // entrypoint
    public static void appendLinesForge(List<@Nullable String> lines, IModInfo selectedMod) {
        if (!"endpoemext".equals(selectedMod.getModId())) return;
        patchForgeLike(lines, () -> poemCredits, () -> postCreditsAuthors, I18n::get);
    }
}
