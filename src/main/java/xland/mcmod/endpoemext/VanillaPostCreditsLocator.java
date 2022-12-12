package xland.mcmod.endpoemext;

import net.minecraft.util.Identifier;

public class VanillaPostCreditsLocator extends VanillaTextLocator {
    private static final Identifier POST_CREDITS = new Identifier("texts/postcredits.txt");

    protected VanillaPostCreditsLocator() {
        super(POST_CREDITS);
    }

    @Override
    protected Identifier getAlternativePath(String langCode) {
        return new Identifier("end_poem_extension", "texts/postcredits/" + langCode + ".json");
    }
}
