package xland.mcmod.epx.v4.support;

import java.io.IOException;
import java.io.Reader;

@FunctionalInterface
public interface ClientResource {
    Reader openReader() throws IOException;
}
