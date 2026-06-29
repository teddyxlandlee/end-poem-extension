package xland.mcmod.epx.v4.neo;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import xland.mcmod.epx.v4.impl.ClientEnvironmentImpl;
import xland.mcmod.epx.v4.util.PoemCredits;

@EventBusSubscriber
public class EndPoemExtensionEventsNeo {
    @SubscribeEvent
    public static void onResourceReload(AddClientReloadListenersEvent event) {
        event.addListener(ClientEnvironmentImpl.toVanillaId(PoemCredits.KEY), new PoemCreditsNeo());
    }
}
