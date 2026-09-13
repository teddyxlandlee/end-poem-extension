package xland.mcmod.epx.v4.neo;

import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.Event;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;
import xland.mcmod.epx.v4.impl.ClientEnvironmentImpl;
import xland.mcmod.epx.v4.impl.PoemCreditsImpl;
import xland.mcmod.epx.v4.util.PoemCredits;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleProxies;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

@Mod("endpoemext")
public class EndPoemExtensionNeo {
    public EndPoemExtensionNeo(IEventBus bus, Dist dist) {
        if (dist != Dist.CLIENT) {
            throw new IllegalStateException("End Poem Extension is a client-only mod, but your environment seems to be a server");
        }

        var lookup = MethodHandles.lookup();

        Class<? extends Event> eventClass;
        MethodHandle handleAddListener;
        try {
            eventClass = lookup.findClass("net.neoforged.neoforge.client.event.AddClientReloadListenersEvent")
                    .asSubclass(Event.class);
            handleAddListener = lookup.findVirtual(eventClass, "addListener", MethodType.methodType(
                    void.class, Identifier.class, PreparableReloadListener.class
            ));
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Incompatible AddClientReloadListenersEvent class", e);
        }

        bus.addListener(eventClass, event -> {
            AddListener addListener = MethodHandleProxies.asInterfaceInstance(
                    AddListener.class, handleAddListener.bindTo(event)
            );
            addListener.addListener(ClientEnvironmentImpl.toVanillaId(PoemCredits.KEY), new PoemCreditsImpl());
        });
    }

    @FunctionalInterface
    @ApiStatus.Internal
    @ApiStatus.NonExtendable
    public interface AddListener {
        void addListener(Identifier id, PreparableReloadListener listener);
    }
}
