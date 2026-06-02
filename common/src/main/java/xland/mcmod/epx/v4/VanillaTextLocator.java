package xland.mcmod.epx.v4;

import java.util.Collections;
import java.util.List;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import xland.mcmod.epx.v4.support.ClientEnvironment;
import xland.mcmod.epx.v4.support.ClientResource;
import xland.mcmod.epx.v4.support.ClientResourceManager;
import xland.mcmod.epx.v4.util.NamespacedKey;

public abstract class VanillaTextLocator implements Locator {
    private final NamespacedKey vanillaPath;
    private static final Logger LOGGER = LogUtils.getLogger();

    protected VanillaTextLocator(NamespacedKey vanillaPath) {
        this.vanillaPath = vanillaPath;
    }

    @Override
    public List<ClientResource> locate(ClientResourceManager manager) {
        final String langCode = ClientEnvironment.getInstance().getCurrentLanguage();
        NamespacedKey path = getAlternativePath(langCode);
        try {
            var ret = manager.readFirstResourceOptionally(path);
            if (ret.isEmpty()) ret = manager.readFirstResourceOptionally(vanillaPath);
            if (ret.isEmpty()) throw new IllegalStateException(vanillaPath + " is absent in vanilla resource pack");
            return Collections.singletonList(ret.get());
        } catch (Exception e) {
            LOGGER.error("Failed to locate {}", path, e);
            return Collections.emptyList();
        }
    }

    protected abstract NamespacedKey getAlternativePath(String langCode);

    @Override
    public CreditsElementReader openReader(EndTextAcceptor<?> acceptor) {
        return new PoemLikeTextReader(acceptor);
    }
}
