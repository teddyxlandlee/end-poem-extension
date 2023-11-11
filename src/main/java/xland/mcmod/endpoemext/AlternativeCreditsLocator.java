package xland.mcmod.endpoemext;

import net.minecraft.resources.ResourceLocation;

public class AlternativeCreditsLocator extends IndexedLocator {
    private final ResourceLocation indexPath;

    public AlternativeCreditsLocator(ResourceLocation indexPath) {
        this.indexPath = indexPath;
    }

    @Override
    protected ResourceLocation getIndexPath() {
        return indexPath;
    }

    @Override
    public CreditsElementReader openReader(EndTextAcceptor acceptor) {
        return new CreditsReader(acceptor);
    }

    @Override
    protected String defaultSuffix() {
        return "json";
    }
}
