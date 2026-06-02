package xland.mcmod.epx.v4;

import xland.mcmod.epx.v4.util.NamespacedKey;

public class PoemLocator extends VanillaTextLocator {
    private static final NamespacedKey VANILLA_POEM = NamespacedKey.ofMinecraft("texts/end.txt");

    public PoemLocator() {
        super(VANILLA_POEM);
    }

    @Override
    protected NamespacedKey getAlternativePath(String langCode) {
        //noinspection PatternValidation
        return NamespacedKey.of("end_poem_extension", "texts/end_poem/" + langCode + ".txt");
    }
}
