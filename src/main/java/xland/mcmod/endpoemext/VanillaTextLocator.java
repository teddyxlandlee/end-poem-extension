package xland.mcmod.endpoemext;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public abstract class VanillaTextLocator implements Locator {
    private final Identifier vanillaPath;

    protected VanillaTextLocator(Identifier vanillaPath) {
        this.vanillaPath = vanillaPath;
    }

    public static String getLangCode() {
        return MinecraftClient.getInstance().getLanguageManager().getLanguage().getCode();
    }

    @Override
    public List<Resource> locate(ResourceManager manager) {
        final String langCode = getLangCode();
        Resource r0;
        try {
            r0 = manager.getResource(getAlternativePath(langCode));
        } catch (IOException e) {
            try {
                r0 = manager.getResource(vanillaPath);
            } catch (IOException ex) {
                throw new IllegalStateException("Mojang should have bundled " + vanillaPath);
            }
        }
        return Collections.singletonList(r0);
    }

    protected abstract Identifier getAlternativePath(String langCode);

    @Override
    public CreditsElementReader openReader(EndTextAcceptor acceptor) {
        return new PoemLikeTextReader(acceptor);
    }
}
