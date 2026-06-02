package xland.mcmod.epx.v4;

import xland.mcmod.epx.v4.util.NamespacedKey;

public class VanillaPostCreditsLocator extends VanillaTextLocator {
    private static final NamespacedKey POST_CREDITS = NamespacedKey.ofMinecraft("texts/postcredits.txt");

    protected VanillaPostCreditsLocator() {
        super(POST_CREDITS);
    }

    @Override
    protected NamespacedKey getAlternativePath(String langCode) {
        //noinspection PatternValidation
        return NamespacedKey.of("end_poem_extension", "texts/postcredits/" + langCode + ".txt");
    }
}
