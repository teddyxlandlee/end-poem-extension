package xland.mcmod.epx.v4.support;

import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.io.Reader;

public interface ClientResource {
    Reader openReader() throws IOException;

    String SKIP_KEY = "end-poem-extension:skip";

    /**
     * <p>A skippable resource is those whose {@code .mcmeta} file contains
     * an item: <pre>{@code
     * "end-poem-extension:skip": {}
     * }</pre></p>
     * <p>They are skippable by {@code credits.json} and <b>vanilla-path</b>{@code end.txt}
     * locators. The locators will instead try to read the resource in the lower-priority
     * resource pack.</p>
     *
     * <p>This provides resource pack creators with an option for "vanilla fallbacks".</p>
     *
     * @see #SKIP_KEY
     */
    @ApiStatus.AvailableSince("4.1.0")
    default boolean isShouldSkip() {
        return false;
    }
}
