package xland.mcmod.epx.v4.support;

import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Experimental
public interface TextFormatter<F> {
    F formattedOf();
    F formattedOf(Text text);
    List<F> splitFormattedOf(Text text, int maxWidth);
}
