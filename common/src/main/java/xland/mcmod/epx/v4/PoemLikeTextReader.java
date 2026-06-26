package xland.mcmod.epx.v4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

import xland.mcmod.epx.v4.support.ClientEnvironment;
import xland.mcmod.epx.v4.support.TextFormatting;

public class PoemLikeTextReader extends CreditsElementReader {
    private static final String OBFUSCATION_PLACEHOLDER = "" + TextFormatting.WHITE + TextFormatting.OBFUSCATED + TextFormatting.GREEN + TextFormatting.AQUA;
    private final String username = ClientEnvironment.getInstance().getUsername();

    protected PoemLikeTextReader(EndTextAcceptor<?> acceptor) {
        super(acceptor);
    }

    @Override
    protected void read(Reader reader) throws IOException {
        BufferedReader bufferedReader = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
        RandomGenerator random = RandomGeneratorFactory.of("Xoroshiro128PlusPlus").create(0x7bf7d3L);
        int i;
        String s;
        while ((s = bufferedReader.readLine()) != null) {
            s = s.replaceAll("PLAYERNAME", username);
            while ((i = s.indexOf(OBFUSCATION_PLACEHOLDER)) >= 0) {
                String prefix = s.substring(0, i);
                String suffix = s.substring(i + OBFUSCATION_PLACEHOLDER.length());
                s = prefix + TextFormatting.WHITE + TextFormatting.OBFUSCATED + "X".repeat(random.nextInt(3, 7)) + suffix;
            }
            acceptor.addText(s);
            acceptor.addEmptyLine();
        }
        for (i = 0; i < 8; ++i) {
            acceptor.addEmptyLine();
        }
    }
}
