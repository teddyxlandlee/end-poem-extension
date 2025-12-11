package xland.mcmod.endpoemext;

import net.minecraft.resources.Identifier;

public class VanillaPostCreditsLocator extends VanillaTextLocator {
    private static final Identifier POST_CREDITS = Identifier.withDefaultNamespace("texts/postcredits.txt");

    protected VanillaPostCreditsLocator() {
        super(POST_CREDITS);
    }

    @Override
    protected Identifier getAlternativePath(String langCode) {
        return Identifier.fromNamespaceAndPath("end_poem_extension", "texts/postcredits/" + langCode + ".txt");
    }
}
