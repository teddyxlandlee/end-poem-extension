package xland.mcmod.endpoemext;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public abstract class VanillaTextLocator implements Locator {
    private final Identifier vanillaPath;

    protected VanillaTextLocator(Identifier vanillaPath) {
        this.vanillaPath = vanillaPath;
    }

    public static String getLangCode() {
        return MinecraftClient.getInstance().getLanguageManager().getLanguage();
    }

    @Override
    public List<Resource> locate(ResourceManager manager) {
        final String langCode = getLangCode();
        return manager.getResource(getAlternativePath(langCode))
                .or(() -> manager.getResource(vanillaPath))
                .map(Collections::singletonList)
                .orElseThrow(() -> new IllegalStateException("Mojang should have bundled " + vanillaPath));
    }

    protected abstract Identifier getAlternativePath(String langCode);

    @Override
    public CreditsElementReader openReader(EndTextAcceptor acceptor) {
        return new PoemLikeTextReader(acceptor);
    }
}
