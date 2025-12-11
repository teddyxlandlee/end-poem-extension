package xland.mcmod.endpoemext;

import java.util.List;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public class ModCreditsLocator implements Locator {
    @Override
    public List<Resource> locate(ResourceManager manager) {
        return manager.getNamespaces().stream()
                .flatMap(ns -> manager.getResource(Identifier.fromNamespaceAndPath(ns, "texts/mod_credits.json")).stream())
                .toList();
    }

    @Override
    public CreditsElementReader openReader(EndTextAcceptor acceptor) {
        return new CreditsReader(acceptor);
    }
}
