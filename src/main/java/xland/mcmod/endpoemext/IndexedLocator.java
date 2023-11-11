package xland.mcmod.endpoemext;

import com.google.gson.JsonArray;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class IndexedLocator implements Locator {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public List<Resource> locate(ResourceManager manager) {
        final ResourceLocation indexPath = getIndexPath();
        //final Optional<Resource> index = manager.getResource(indexPath);
        final List<Resource> allResources = manager.getResourceStack(indexPath);
        if (allResources.isEmpty())
            return Collections.emptyList();
        final String defaultSuffix = defaultSuffix();
        List<Resource> resources = new ArrayList<>();

        for (Resource resource : allResources) {
            try (var reader = resource.openAsReader()) {
                final JsonArray arr = GsonHelper.parseArray(reader);
                for (var e : arr) {
                    if (GsonHelper.isStringValue(e)) {
                        resources.add(manager.getResourceOrThrow(new ResourceLocation(e.getAsString())));
                    } else if (e.isJsonObject()) {
                        var o = e.getAsJsonObject();
                        boolean isI18n = GsonHelper.getAsBoolean(o, "is_i18n", false);
                        String path = GsonHelper.getAsString(o, "path");
                        String suffix = GsonHelper.getAsString(o, "default_suffix", defaultSuffix);
                        if (isI18n) {
                            String p0 = transformDir(path) + VanillaTextLocator.getLangCode() + '.' + suffix;
                            resources.add(manager.getResource(new ResourceLocation(p0))
                                    .or(() -> manager.getResource(new ResourceLocation(transformDir(path) + "en_us." + suffix)))
                                    .orElseThrow(() -> new FileNotFoundException("i18n resource: " + path)));
                        } else {
                            resources.add(manager.getResourceOrThrow(new ResourceLocation(path)));
                        }
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Failed to locate partial resources from {}", indexPath, e);
            }
        }

        return resources;
    }

    protected abstract ResourceLocation getIndexPath();
    protected String defaultSuffix() { return "txt"; }

    @Override
    public abstract CreditsElementReader openReader(EndTextAcceptor acceptor);

    private static String transformDir(String path) {
        return path.endsWith("/") ? path : path.concat("/");
    }
}
