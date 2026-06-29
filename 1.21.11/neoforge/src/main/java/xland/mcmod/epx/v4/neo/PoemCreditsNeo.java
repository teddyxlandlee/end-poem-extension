package xland.mcmod.epx.v4.neo;

import net.minecraft.client.resources.language.I18n;
import org.jetbrains.annotations.NotNullByDefault;
import xland.mcmod.epx.v4.impl.PoemCreditsCommon;

import java.util.List;

@NotNullByDefault
public class PoemCreditsNeo extends PoemCreditsCommon {
    public static void appendLines(List<? super String> lines) {
        patchForgeLike(lines, () -> poemCredits, () -> postCreditsAuthors, I18n::get);
    }
}
