package xland.mcmod.epx.v4;

import com.google.gson.JsonArray;
import com.mojang.logging.LogUtils;
import xland.mcmod.epx.v4.util.JsonHelper;
import org.slf4j.Logger;
import xland.mcmod.epx.v4.support.ClientEnvironment;
import xland.mcmod.epx.v4.support.ClientResource;
import xland.mcmod.epx.v4.support.ClientResourceManager;
import xland.mcmod.epx.v4.util.NamespacedKey;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public abstract class IndexedLocator implements Locator {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public List<ClientResource> locate(ClientResourceManager manager) {
        final NamespacedKey indexPath = this.getIndexPath();
        //final Optional<Resource> index = manager.getResource(indexPath);
        final Collection<? extends ClientResource> allResources;
        try {
            allResources = manager.readResources(indexPath);
        } catch (Exception e) {
            LOGGER.error("Failed to load {}", indexPath, e);
            return Collections.emptyList();
        }

        if (allResources.isEmpty())
            return Collections.emptyList();
        final String defaultSuffix = getDefaultSuffix();
        var resources = new ArrayList<ClientResource>();

        for (var resource : allResources) {
            try (var reader = resource.openReader()) {
                final JsonArray arr = JsonHelper.parseArray(reader);
                for (var e : arr) {
                    if (e.isJsonPrimitive()) {  // we don't distinguish string/number/boolean now
                        resources.add(manager.readFirstResource(NamespacedKey.tryParse(e.getAsString())));
                    } else if (e.isJsonObject()) {
                        var o = e.getAsJsonObject();
                        boolean isI18n = JsonHelper.getAsBoolean(o, "is_i18n", false);
                        String path = JsonHelper.getAsString(o, "path");
                        String suffix = JsonHelper.getAsString(o, "default_suffix", defaultSuffix);
                        if (isI18n) {
                            String p0 = normalizeDirPath(path) + ClientEnvironment.getInstance().getCurrentLanguage() + '.' + suffix;
                            resources.add(manager.readFirstResourceOptionally(NamespacedKey.tryParse(p0))
                                    .or(() -> {
                                        try {
                                            return manager.readFirstResourceOptionally(
                                                    NamespacedKey.tryParse(normalizeDirPath(path) + "en_us." + suffix)
                                            );
                                        } catch (IOException ex) {
                                            return Optional.empty();
                                        }
                                    })
                                    .orElseThrow(() -> new FileNotFoundException("i18n resource: " + path)));
                        } else {
                            resources.add(manager.readFirstResource(NamespacedKey.tryParse(path)));
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Failed to locate partial resources from {}", indexPath, e);
            }
        }

        return resources;
    }

    protected abstract NamespacedKey getIndexPath();
    protected String getDefaultSuffix() {
        return "txt";
    }

    @Override
    public abstract CreditsElementReader openReader(EndTextAcceptor<?> acceptor);

    private static String normalizeDirPath(String path) {
        return path.endsWith("/") ? path : path.concat("/");
    }
}
