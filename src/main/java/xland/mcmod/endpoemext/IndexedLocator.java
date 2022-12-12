package xland.mcmod.endpoemext;

import com.google.gson.JsonArray;
import com.mojang.logging.LogUtils;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class IndexedLocator implements Locator {
    private static final Logger LOGGER = LogUtils.getLogger();

    @Override
    public List<Resource> locate(ResourceManager manager) {
        final Identifier indexPath = getIndexPath();
        //final Optional<Resource> index = manager.getResource(indexPath);
        final List<Resource> allResources;
        try {
            allResources = manager.getAllResources(indexPath);
        } catch (IOException e) {
            LOGGER.error("Failed to fetch index from {}", indexPath, e);
            return Collections.emptyList();
        }
        if (allResources.isEmpty())
            return Collections.emptyList();
        final String defaultSuffix = defaultSuffix();
        List<Resource> resources = new ArrayList<>();

        for (Resource resource : allResources) {
            try (var reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                final JsonArray arr = JsonHelper.deserializeArray(reader);
                for (var e : arr) {
                    if (JsonHelper.isString(e)) {
                        resources.add(manager.getResource(new Identifier(e.getAsString())));
                    } else if (e.isJsonObject()) {
                        var o = e.getAsJsonObject();
                        boolean isI18n = JsonHelper.getBoolean(o, "is_i18n", false);
                        String path = JsonHelper.getString(o, "path");
                        String suffix = JsonHelper.getString(o, "default_suffix", defaultSuffix);
                        if (isI18n) {
                            String p0 = path + VanillaTextLocator.getLangCode() + '.' + suffix;
                            Resource r0;
                            try {
                                r0 = manager.getResource(new Identifier(p0));
                            } catch (IOException ex) {
                                r0 = manager.getResource(new Identifier(path + "en_us." + suffix));
                            }
                            resources.add(r0);
                        } else {
                            resources.add(manager.getResource(new Identifier(path)));
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
}
