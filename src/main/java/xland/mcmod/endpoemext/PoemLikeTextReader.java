package xland.mcmod.endpoemext;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.RandomSource;

public class PoemLikeTextReader extends CreditsElementReader {
    private static final String OBFUSCATION_PLACEHOLDER = "" + ChatFormatting.WHITE + ChatFormatting.OBFUSCATED + ChatFormatting.GREEN + ChatFormatting.AQUA;
    private final String username = Minecraft.getInstance().getUser().getName();

    protected PoemLikeTextReader(EndTextAcceptor acceptor) {
        super(acceptor);
    }

    @Override
    protected void read(Reader reader) throws IOException {
        BufferedReader bufferedReader = IOUtils.buffer(reader);
        RandomSource random = RandomSource.create(8124371L);
        int i;
        String s;
        while ((s = bufferedReader.readLine()) != null) {
            s = s.replaceAll("PLAYERNAME", username);
            while ((i = s.indexOf(OBFUSCATION_PLACEHOLDER)) >= 0) {
                String prefix = s.substring(0, i);
                String suffix = s.substring(i + OBFUSCATION_PLACEHOLDER.length());
                s = prefix + ChatFormatting.WHITE + ChatFormatting.OBFUSCATED + "XXXXXXXX".substring(0, random.nextInt(4) + 3) + suffix;
            }
            acceptor.addText(s);
            acceptor.addEmptyLine();
        }
        for (i = 0; i < 8; ++i) {
            acceptor.addEmptyLine();
        }
    }
}
