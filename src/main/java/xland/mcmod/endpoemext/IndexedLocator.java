package xland.mcmod.endpoemext;

import com.google.gson.JsonArray;
import com.mojang.logging.LogUtils;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class IndexedLocator implements Locator {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public List<Resource> locate(ResourceManager manager) {
        final Identifier indexPath = getIndexPath();
        //final Optional<Resource> index = manager.getResource(indexPath);
        final List<Resource> allResources = manager.getAllResources(indexPath);
        if (allResources.isEmpty())
            return Collections.emptyList();
        final String defaultSuffix = defaultSuffix();
        List<Resource> resources = new ArrayList<>();

        for (Resource resource : allResources) {
            try (var reader = resource.getReader()) {
                final JsonArray arr = JsonHelper.deserializeArray(reader);
                for (var e : arr) {
                    if (JsonHelper.isString(e)) {
                        resources.add(manager.getResourceOrThrow(new Identifier(e.getAsString())));
                    } else if (e.isJsonObject()) {
                        var o = e.getAsJsonObject();
                        boolean isI18n = JsonHelper.getBoolean(o, "is_i18n", false);
                        String path = JsonHelper.getString(o, "path");
                        String suffix = JsonHelper.getString(o, "default_suffix", defaultSuffix);
                        if (isI18n) {
                            String p0 = transformDir(path) + VanillaTextLocator.getLangCode() + '.' + suffix;
                            resources.add(manager.getResource(new Identifier(p0))
                                    .or(() -> manager.getResource(new Identifier(transformDir(path) + "en_us." + suffix)))
                                    .orElseThrow(() -> new FileNotFoundException("i18n resource: " + path)));
                        } else {
                            resources.add(manager.getResourceOrThrow(new Identifier(path)));
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Failed to locate partial resources from {}", indexPath, e);
            }
        }

        return resources;
    }

    protected abstract Identifier getIndexPath();
    protected String defaultSuffix() { return "txt"; }

    @Override
    public abstract CreditsElementReader openReader(EndTextAcceptor acceptor);

    private static String transformDir(String path) {
        return path.endsWith("/") ? path : path.concat("/");
    }
}
