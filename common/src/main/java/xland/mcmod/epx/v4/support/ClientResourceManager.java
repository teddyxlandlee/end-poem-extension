package xland.mcmod.epx.v4.support;

import xland.mcmod.epx.v4.util.NamespacedKey;

import java.io.IOException;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface ClientResourceManager {
    Collection<? extends ClientResource> readResources(NamespacedKey key) throws IOException;
    default ClientResource readFirstResource(NamespacedKey key) throws IOException, NoSuchElementException {
        return readFirstResourceOptionally(key).orElseThrow(() -> new NoSuchElementException(key.toString()));
    }
    Optional<ClientResource> readFirstResourceOptionally(NamespacedKey key) throws IOException;

    Collection<? extends String> getResourceNamespaces();
}
