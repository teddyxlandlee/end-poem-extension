package xland.mcmod.endpoemext;

import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.List;

public class ModCreditsLocator implements Locator {
    @Override
    public List<Resource> locate(ResourceManager manager) {
        return manager.getAllNamespaces().stream()
                .flatMap(ns -> manager.getResource(new Identifier(ns, "texts/mod_credits.json")).stream())
                .toList();
    }

    @Override
    public CreditsElementReader openReader(EndTextAcceptor acceptor) {
        return new CreditsReader(acceptor);
    }
}
