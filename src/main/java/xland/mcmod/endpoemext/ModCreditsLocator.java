package xland.mcmod.endpoemext;

import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class ModCreditsLocator implements Locator {
    @Override
    public List<Resource> locate(ResourceManager manager) {
        return manager.getAllNamespaces().stream()
                .flatMap(ns -> {
                    try {
                        return Stream.of(manager.getResource(new Identifier(ns, "texts/mod_credits.json")));
                    } catch (IOException e) {
                        return Stream.empty();
                    }
                })
                .toList();
    }

    @Override
    public CreditsElementReader openReader(EndTextAcceptor acceptor) {
        return new CreditsReader(acceptor);
    }
}
