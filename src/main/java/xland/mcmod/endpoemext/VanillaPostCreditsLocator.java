package xland.mcmod.endpoemext;

import net.minecraft.resources.ResourceLocation;

public class VanillaPostCreditsLocator extends VanillaTextLocator {
    private static final ResourceLocation POST_CREDITS = new ResourceLocation("texts/postcredits.txt");

    protected VanillaPostCreditsLocator() {
        super(POST_CREDITS);
    }

    @Override
    protected ResourceLocation getAlternativePath(String langCode) {
        return new ResourceLocation("end_poem_extension", "texts/postcredits/" + langCode + ".txt");
    }
}
