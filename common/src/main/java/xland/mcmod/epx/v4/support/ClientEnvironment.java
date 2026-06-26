package xland.mcmod.epx.v4.support;

import org.jetbrains.annotations.ApiStatus;

import java.util.ServiceLoader;

@ApiStatus.Internal
public interface ClientEnvironment {
    static ClientEnvironment getInstance() {
        class Holder {
            // Fabric/Forge/NeoForge (for MC 1.20.1+) all support service loading
            static final ClientEnvironment INSTANCE = ServiceLoader.load(ClientEnvironment.class)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("ClientEnvironment not implemented"));
        }
        return Holder.INSTANCE;
    }

    ClientResourceManager getResourceManager();

    <F> TextFormatter<F> getTextFormatter();

    String getUsername();
    String getCurrentLanguage();
}
