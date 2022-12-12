package xland.mcmod.endpoemext;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.random.Random;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class TextReader extends CreditsElementReader {
    private static final String OBFUSCATION_PLACEHOLDER = "" + Formatting.WHITE + Formatting.OBFUSCATED + Formatting.GREEN + Formatting.AQUA;
    private final String username = MinecraftClient.getInstance().getSession().getUsername();

    protected TextReader(EndTextAcceptor acceptor) {
        super(acceptor);
    }

    @Override
    protected void read(Reader reader) throws IOException {
        BufferedReader bufferedReader = IOUtils.buffer(reader);
        Random random = Random.create(8124371L);
        int i;
        String s;
        while ((s = bufferedReader.readLine()) != null) {
            s = s.replaceAll("PLAYERNAME", username);
            while ((i = s.indexOf(OBFUSCATION_PLACEHOLDER)) >= 0) {
                String prefix = s.substring(0, i);
                String suffix = s.substring(i + OBFUSCATION_PLACEHOLDER.length());
                s = prefix + Formatting.WHITE + Formatting.OBFUSCATED + "XXXXXXXX".substring(0, random.nextInt(4) + 3) + suffix;
            }
            acceptor.addText(s);
            acceptor.addEmptyLine();
        }
        for (i = 0; i < 8; ++i) {
            acceptor.addEmptyLine();
        }
    }
}
