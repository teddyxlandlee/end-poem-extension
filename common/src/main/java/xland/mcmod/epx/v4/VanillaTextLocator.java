package xland.mcmod.epx.v4;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.jetbrains.annotations.ApiStatus;
import xland.mcmod.epx.v4.support.ClientEnvironment;
import xland.mcmod.epx.v4.support.ClientResource;
import xland.mcmod.epx.v4.support.ClientResourceManager;
import xland.mcmod.epx.v4.util.NamespacedKey;

public abstract class VanillaTextLocator implements Locator {
    private final NamespacedKey vanillaPath;
    private final boolean isVanillaResourceSkippable;

    /**
     * @param vanillaPath Path in vanilla resource pack
     * @param isVanillaResourceSkippable if set to true, resources under the vanilla path can be
     *                                   skipped under {@link ClientResource#isShouldSkip()}
     *                                   convention. Note that resources under {@linkplain #getAlternativePath
     *                                   alternative path} will <b>not</b> be skippable.
     */
    @ApiStatus.AvailableSince("4.1.0")
    protected VanillaTextLocator(NamespacedKey vanillaPath, boolean isVanillaResourceSkippable) {
        this.vanillaPath = vanillaPath;
        this.isVanillaResourceSkippable = isVanillaResourceSkippable;
    }

    protected VanillaTextLocator(NamespacedKey vanillaPath) {
        this(vanillaPath, false);
    }

    @Override
    public List<ClientResource> locate(ClientResourceManager manager) {
        final String langCode = ClientEnvironment.getInstance().getCurrentLanguage();
        NamespacedKey path = getAlternativePath(langCode);
        try {
            Optional<ClientResource> ret = manager.readFirstResourceOptionally(path);

            if (isVanillaResourceSkippable) {
                ret = Optional.of(Locators.firstUnskippedOrLast(manager.readResourceStack(vanillaPath), vanillaPath));
            } else {
                if (ret.isEmpty()) ret = manager.readFirstResourceOptionally(vanillaPath);
                if (ret.isEmpty()) throw new FileNotFoundException("Vanilla resource " + vanillaPath + " is absent");
            }

            return Collections.singletonList(ret.get());
        } catch (Exception e) {
            Locators.LOGGER.error("Failed to locate {}, probed by {}", path, this, e);
            return Collections.emptyList();
        }
    }

    protected abstract NamespacedKey getAlternativePath(String langCode);

    @Override
    public CreditsElementReader openReader(EndTextAcceptor<?> acceptor) {
        return new PoemLikeTextReader(acceptor);
    }
}
