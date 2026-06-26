package xland.mcmod.epx.v4;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import xland.mcmod.epx.v4.support.ClientResource;
import xland.mcmod.epx.v4.support.ClientResourceManager;
import xland.mcmod.epx.v4.util.NamespacedKey;

public class ModCreditsLocator implements Locator {
    @Override
    public List<ClientResource> locate(ClientResourceManager manager) {
        return manager.getResourceNamespaces().stream()
                .flatMap(ns -> {
                    Locators.LOGGER.debug("Locating mod_credits.json in namespace {}", ns);
                    try {
                        return manager.readFirstResourceOptionally(NamespacedKey.of(ns, "texts/mod_credits.json")).stream();
                    } catch (IOException e) {
                        Locators.LOGGER.debug("Skipped mod_credits.json in namespace {} due to I/O error", ns, e);
                        return Stream.empty();
                    }
                })
                .toList();
    }

    @Override
    public CreditsElementReader openReader(EndTextAcceptor<?> acceptor) {
        return new CreditsReader(acceptor);
    }
}
