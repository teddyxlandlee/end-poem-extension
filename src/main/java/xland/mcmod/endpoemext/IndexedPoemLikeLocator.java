package xland.mcmod.endpoemext;

import net.minecraft.resources.ResourceLocation;

public class IndexedPoemLikeLocator extends IndexedLocator {
    private final ResourceLocation indexPath;

    public IndexedPoemLikeLocator(ResourceLocation indexPath) {
        this.indexPath = indexPath;
    }

    @Override
    protected ResourceLocation getIndexPath() {
        return indexPath;
    }

    @Override
    public CreditsElementReader openReader(EndTextAcceptor acceptor) {
        return new PoemLikeTextReader(acceptor);
    }
}
