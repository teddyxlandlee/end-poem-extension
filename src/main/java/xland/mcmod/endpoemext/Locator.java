package xland.mcmod.endpoemext;

import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;

import java.io.BufferedReader;
import java.util.List;

public interface Locator {
    List<Resource> locate(ResourceManager manager);

    CreditsElementReader openReader(EndTextAcceptor acceptor);

    default void visit(ResourceManager manager, EndTextAcceptor acceptor) {
        for (Resource resource : locate(manager)) {
            try (BufferedReader reader = resource.getReader()) {
                openReader(acceptor).read(reader);
            } catch (Exception e) {
                Locators.LOGGER.error("Failed to visit resources", e);
            }
        }
    }
}
