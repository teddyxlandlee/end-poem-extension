package xland.mcmod.endpoemext;

import java.io.BufferedReader;
import java.util.List;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public interface Locator {
    List<Resource> locate(ResourceManager manager);

    CreditsElementReader openReader(EndTextAcceptor acceptor);

    default void visit(ResourceManager manager, EndTextAcceptor acceptor) {
        for (Resource resource : locate(manager)) {
            try (BufferedReader reader = resource.openAsReader()) {
                openReader(acceptor).read(reader);
            } catch (Exception e) {
                Locators.LOGGER.error("Failed to visit resources", e);
            }
        }
    }
}
