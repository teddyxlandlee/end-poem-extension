package xland.mcmod.endpoemext;

import net.minecraft.util.Identifier;

public class IndexedPoemLikeLocator extends IndexedLocator {
    private final Identifier indexPath;

    public IndexedPoemLikeLocator(Identifier indexPath) {
        this.indexPath = indexPath;
    }

    @Override
    protected Identifier getIndexPath() {
        return indexPath;
    }

    @Override
    public CreditsElementReader openReader(EndTextAcceptor acceptor) {
        return new PoemLikeTextReader(acceptor);
    }
}
