package xland.mcmod.epx.v4.forge;

import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNullByDefault;
import xland.mcmod.epx.v4.impl.PoemCreditsCommon;

import java.util.List;

@NotNullByDefault
public class PoemCreditsForge extends PoemCreditsCommon {
    @SuppressWarnings("unused") // entrypoint
    public static void appendLines(List<? super String> lines) {
        patchForgeLike(lines, () -> poemCredits, () -> postCreditsAuthors, I18n::get);
    }
}
