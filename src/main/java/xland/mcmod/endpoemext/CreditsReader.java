package xland.mcmod.endpoemext;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.Reader;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.GsonHelper;

public class CreditsReader extends CreditsElementReader {
    private static final Component SEPARATOR_LINE = Component.literal("============").withStyle(ChatFormatting.WHITE);
    private static final String CENTERED_LINE_PREFIX = "           ";   // 11 spaces
    protected CreditsReader(EndTextAcceptor acceptor) {
        super(acceptor);
    }

    @Override
    protected void read(Reader reader) {
        JsonArray arr = GsonHelper.parseArray(reader);
        for (JsonElement e : arr) {
            JsonObject eachSection = GsonHelper.convertToJsonObject(e, "section");
            String string = eachSection.get("section").getAsString();
            acceptor.addText(SEPARATOR_LINE, true);
            acceptor.addText(Component.literal(string).withStyle(ChatFormatting.YELLOW), true);
            acceptor.addText(SEPARATOR_LINE, true);
            acceptor.addEmptyLine();
            acceptor.addEmptyLine();
            JsonArray disciplines = eachSection.getAsJsonArray("disciplines");
            if (disciplines != null) {
                for (JsonElement e0 : disciplines) {
                    JsonObject eachDiscipline = e0.getAsJsonObject();
                    String discipline = GsonHelper.getAsString(eachDiscipline, "discipline", "");
                    if (!discipline.isBlank()) {
                        // this.addCreditsLine(Component.literal(string2).withStyle(ChatFormatting.YELLOW), true);
                        acceptor.addText(Component.literal(discipline).withStyle(ChatFormatting.YELLOW), true);
                        acceptor.addEmptyLine();
                        acceptor.addEmptyLine();
                    }
                    JsonArray titles = eachDiscipline.getAsJsonArray("titles");
                    readTitles(titles);
                }
            } else {
                JsonArray titles = eachSection.getAsJsonArray("titles");
                readTitles(titles);
            }
        }
    }

    private void readTitles(JsonArray titles) {
        for (JsonElement e1 : titles) {
            JsonObject oneTitle = GsonHelper.convertToJsonObject(e1, "title");
            String string2 = oneTitle.get("title").getAsString();
            JsonArray names = GsonHelper.getAsJsonArray(oneTitle, "names");
            acceptor.addText(Component.literal(string2).withStyle(ChatFormatting.GRAY), false);
            for (JsonElement e2 : names) {
                String name = GsonHelper.convertToString(e2, "name");
                acceptor.addText(
                        Component.literal(CENTERED_LINE_PREFIX).append(name).withStyle(ChatFormatting.WHITE),
                        false);
            }
            acceptor.addEmptyLine();
            acceptor.addEmptyLine();
        }
    }
}
