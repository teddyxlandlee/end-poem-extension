package xland.mcmod.epx.v4.forge;

import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("endpoemext")
public class EndPoemExtensionForge {
    public EndPoemExtensionForge() {
        @SuppressWarnings("removal")    // constructor-embedded context is unsupported yet in Forge 46.x, MC 1.20
        var ctx = FMLJavaModLoadingContext.get();
        ctx.getModEventBus().addListener(EndPoemExtensionForge::onResourceReload);
    }

    private static void onResourceReload(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new PoemCreditsForge());
    }
}
