package xland.mcmod.epx.v4.support;

import java.util.List;

public interface TextFormatter<F> {
    F formattedOf();
    F formattedOf(Text text);
    List<F> splitFormattedOf(Text text, int maxWidth);
}
