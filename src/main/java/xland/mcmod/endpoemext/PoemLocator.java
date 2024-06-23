package xland.mcmod.endpoemext;

import net.minecraft.resources.ResourceLocation;

public class PoemLocator extends VanillaTextLocator {
    private static final ResourceLocation VANILLA_POEM = ResourceLocation.withDefaultNamespace("texts/end.txt");

    public PoemLocator() {
        super(VANILLA_POEM);
    }

    @Override
    protected ResourceLocation getAlternativePath(String langCode) {
        return ResourceLocation.fromNamespaceAndPath("end_poem_extension", "texts/end_poem/" + langCode + ".txt");
    }
}
