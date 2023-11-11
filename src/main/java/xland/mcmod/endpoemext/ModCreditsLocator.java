package xland.mcmod.endpoemext;

import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public class ModCreditsLocator implements Locator {
    @Override
    public List<Resource> locate(ResourceManager manager) {
        return manager.getNamespaces().stream()
                .flatMap(ns -> manager.getResource(new ResourceLocation(ns, "texts/mod_credits.json")).stream())
                .toList();
    }

    @Override
    public CreditsElementReader openReader(EndTextAcceptor acceptor) {
        return new CreditsReader(acceptor);
    }
}
