package xland.mcmod.endpoemext;

import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

public abstract class VanillaTextLocator implements Locator {
    private final ResourceLocation vanillaPath;

    protected VanillaTextLocator(ResourceLocation vanillaPath) {
        this.vanillaPath = vanillaPath;
    }

    public static String getLangCode() {
        return Minecraft.getInstance().getLanguageManager().getSelected();
    }

    @Override
    public List<Resource> locate(ResourceManager manager) {
        final String langCode = getLangCode();
        return manager.getResource(getAlternativePath(langCode))
                .or(() -> manager.getResource(vanillaPath))
                .map(Collections::singletonList)
                .orElseThrow(() -> new IllegalStateException("Mojang should have bundled " + vanillaPath));
    }

    protected abstract ResourceLocation getAlternativePath(String langCode);

    @Override
    public CreditsElementReader openReader(EndTextAcceptor acceptor) {
        return new PoemLikeTextReader(acceptor);
    }
}
