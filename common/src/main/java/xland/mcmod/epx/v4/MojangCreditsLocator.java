package xland.mcmod.epx.v4;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import xland.mcmod.epx.v4.support.ClientResource;
import xland.mcmod.epx.v4.support.ClientResourceManager;
import xland.mcmod.epx.v4.util.NamespacedKey;

public class MojangCreditsLocator implements Locator {
    private static final NamespacedKey MOJANG_CREDITS = NamespacedKey.ofMinecraft("texts/credits.json");

    @Override
    public List<ClientResource> locate(ClientResourceManager manager) {
        try {
            return Collections.singletonList(manager.readFirstResource(MOJANG_CREDITS));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load Mojang credits", e);
        }
    }

    @Override
    public CreditsElementReader openReader(EndTextAcceptor<?> acceptor) {
        return new CreditsReader(acceptor);
    }
}
