package xland.mcmod.endpoemext;

import net.minecraft.resources.Identifier;

public class PoemLocator extends VanillaTextLocator {
    private static final Identifier VANILLA_POEM = Identifier.withDefaultNamespace("texts/end.txt");

    public PoemLocator() {
        super(VANILLA_POEM);
    }

    @Override
    protected Identifier getAlternativePath(String langCode) {
        return Identifier.fromNamespaceAndPath("end_poem_extension", "texts/end_poem/" + langCode + ".txt");
    }
}
