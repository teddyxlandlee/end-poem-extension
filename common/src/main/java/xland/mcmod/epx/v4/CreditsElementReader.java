package xland.mcmod.epx.v4;

import java.io.IOException;
import java.io.Reader;

public abstract class CreditsElementReader {
    protected final EndTextAcceptor<?> acceptor;

    protected CreditsElementReader(EndTextAcceptor<?> acceptor) {
        this.acceptor = acceptor;
    }

    protected abstract void read(Reader reader) throws IOException;
}