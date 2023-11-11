package xland.mcmod.endpoemext;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public class MojangCreditsLocator implements Locator {
    private static final ResourceLocation MOJANG_CREDITS = new ResourceLocation("texts/credits.json");

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
