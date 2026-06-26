package xland.mcmod.epx.v4.support;

import org.jetbrains.annotations.ApiStatus;
import xland.mcmod.epx.v4.util.NamespacedKey;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface ClientResourceManager {
    // actually used as a SequencedCollection, but it isn't present in Java 17
    List<? extends ClientResource> readResourceStack(NamespacedKey key) throws IOException;
    default ClientResource readFirstResource(NamespacedKey key) throws IOException, NoSuchElementException {
        return readFirstResourceOptionally(key).orElseThrow(() -> new NoSuchElementException(key.toString()));
    }
    Optional<ClientResource> readFirstResourceOptionally(NamespacedKey key) throws IOException;

    Collection<? extends String> getResourceNamespaces();

    @Deprecated(forRemoval = true, since = "4.1.0")
    @ApiStatus.ScheduledForRemoval(inVersion = "4.3")
    default Collection<? extends ClientResource> readResources(NamespacedKey key) throws IOException {
        return this.readResourceStack(key);
    }
}
