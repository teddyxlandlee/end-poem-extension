package xland.mcmod.epx.v4;

import xland.mcmod.epx.v4.util.NamespacedKey;

public class AlternativeCreditsLocator extends IndexedLocator {
    private final NamespacedKey indexPath;

    public AlternativeCreditsLocator(NamespacedKey indexPath) {
        this.indexPath = indexPath;
    }

    @Override
    protected NamespacedKey getIndexPath() {
        return indexPath;
    }

    @Override
    public CreditsElementReader openReader(EndTextAcceptor<?> acceptor) {
        return new CreditsReader(acceptor);
    }

    @Override
    protected String getDefaultSuffix() {
        return "json";
    }
}
