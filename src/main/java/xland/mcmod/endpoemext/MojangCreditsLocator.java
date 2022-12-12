package xland.mcmod.endpoemext;

import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

public class MojangCreditsLocator implements Locator {
    private static final Identifier MOJANG_CREDITS = new Identifier("texts/credits.json");

    @Override
    public List<Resource> locate(ResourceManager manager) {
        try {
            return Collections.singletonList(manager.getResourceOrThrow(MOJANG_CREDITS));
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Failed to load Mojang credits", e);
        }
    }

    @Override
    public CreditsElementReader openReader(EndTextAcceptor acceptor) {
        return new CreditsReader(acceptor);
    }
}
