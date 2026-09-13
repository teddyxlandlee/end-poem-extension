package xland.mcmod.epx.v4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Random;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

import xland.mcmod.epx.v4.support.ClientEnvironment;
import xland.mcmod.epx.v4.support.TextFormatting;

public class PoemLikeTextReader extends CreditsElementReader {
    private static final String OBFUSCATION_PLACEHOLDER = "" + TextFormatting.WHITE + TextFormatting.OBFUSCATED + TextFormatting.GREEN + TextFormatting.AQUA;
    private static final String OBF_TEXT_PREFIX = "" + TextFormatting.WHITE + TextFormatting.OBFUSCATED;
    private final String username = ClientEnvironment.getInstance().getUsername();

    protected PoemLikeTextReader(EndTextAcceptor<?> acceptor) {
        super(acceptor);
    }

    private static final long RANDOM_SEED = 0x7bf7d3L;

    @Override
    protected void read(Reader reader) throws IOException {
        BufferedReader bufferedReader = reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
        RandomGenerator random;
        try {
            random = RandomGeneratorFactory.of("Xoroshiro128PlusPlus").create(RANDOM_SEED);
        } catch (Throwable e) {
            random = new Random(RANDOM_SEED);
            Locators.LOGGER.warn(
                    "Xoroshiro128PlusPlus RandomGeneratorFactory is unsupported on this JVM. Fallback to legacy ({}). Random behavior may change.",
                    random, e
            );
        }

        String s;
        while ((s = bufferedReader.readLine()) != null) {
            s = s.replace("PLAYERNAME", username);
            int obfStart;

            if ((obfStart = s.indexOf(OBFUSCATION_PLACEHOLDER)) >= 0) {
                StringBuilder sb = new StringBuilder(s.length() + 48);
                int prefixStart = 0;
                do {
                    sb.append(s, prefixStart, obfStart);
                    sb.append(OBF_TEXT_PREFIX).append("X".repeat(random.nextInt(3, 7)));

                    prefixStart = obfStart + OBFUSCATION_PLACEHOLDER.length();
                } while ((obfStart = s.indexOf(OBFUSCATION_PLACEHOLDER, prefixStart)) >= 0);
                sb.append(s, prefixStart, s.length());
                s = sb.toString();
            }

            acceptor.addText(s);
            acceptor.addEmptyLine();
        }
        for (int i = 0; i < 8; ++i) {
            acceptor.addEmptyLine();
        }
    }
}
