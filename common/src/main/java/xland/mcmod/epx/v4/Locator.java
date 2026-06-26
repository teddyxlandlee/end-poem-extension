package xland.mcmod.epx.v4;

import java.io.Reader;
import java.util.List;

import xland.mcmod.epx.v4.support.ClientResource;
import xland.mcmod.epx.v4.support.ClientResourceManager;

public interface Locator {
    List<ClientResource> locate(ClientResourceManager manager);

    CreditsElementReader openReader(EndTextAcceptor<?> acceptor);

    default void visit(ClientResourceManager manager, EndTextAcceptor<?> acceptor) {
        for (ClientResource resource : this.locate(manager)) {
            try (Reader reader = resource.openReader()) {
                this.openReader(acceptor).read(reader);
            } catch (Exception e) {
                Locators.LOGGER.error("Failed to visit resources located by {}", this, e);
            }
        }
    }
}
