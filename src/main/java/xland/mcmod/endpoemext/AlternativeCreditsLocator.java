package xland.mcmod.endpoemext;

import net.minecraft.util.Identifier;

public class AlternativeCreditsLocator extends IndexedLocator {
    private final Identifier indexPath;

    public AlternativeCreditsLocator(Identifier indexPath) {
        this.indexPath = indexPath;
    }

    @Override
    protected Identifier getIndexPath() {
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
