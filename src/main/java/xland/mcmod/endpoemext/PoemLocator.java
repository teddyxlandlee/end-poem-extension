package xland.mcmod.endpoemext;

import net.minecraft.util.Identifier;

public class PoemLocator extends VanillaTextLocator {
    private static final Identifier VANILLA_POEM = new Identifier("texts/end.txt");

    public PoemLocator() {
        super(VANILLA_POEM);
    }

    @Override
    protected Identifier getAlternativePath(String langCode) {
        return new Identifier("end_poem_extension", "texts/end_poem/" + langCode + ".txt");
    }
}
