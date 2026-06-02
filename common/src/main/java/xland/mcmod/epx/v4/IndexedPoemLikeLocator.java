package xland.mcmod.epx.v4;

import xland.mcmod.epx.v4.util.NamespacedKey;

public class IndexedPoemLikeLocator extends IndexedLocator {
    private final NamespacedKey indexPath;

    public IndexedPoemLikeLocator(NamespacedKey indexPath) {
        this.indexPath = indexPath;
    }

    @Override
    protected NamespacedKey getIndexPath() {
        return indexPath;
    }

    @Override
    public CreditsElementReader openReader(EndTextAcceptor<?> acceptor) {
        return new PoemLikeTextReader(acceptor);
    }
}
