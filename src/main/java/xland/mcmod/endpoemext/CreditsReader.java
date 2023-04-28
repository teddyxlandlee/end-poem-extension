package xland.mcmod.endpoemext;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.JsonHelper;

import java.io.Reader;

public class CreditsReader extends CreditsElementReader {
    private static final Text SEPARATOR_LINE = Text.literal("============").formatted(Formatting.WHITE);
    private static final String CENTERED_LINE_PREFIX = "           ";   // 11 spaces
    protected CreditsReader(EndTextAcceptor acceptor) {
        super(acceptor);
    }

    @Override
    protected void read(Reader reader) {
        JsonArray arr = JsonHelper.deserializeArray(reader);
        for (JsonElement e : arr) {
            JsonObject eachSection = JsonHelper.asObject(e, "section");
            String string = eachSection.get("section").getAsString();
            acceptor.addText(SEPARATOR_LINE, true);
            acceptor.addText(Text.literal(string).formatted(Formatting.YELLOW), true);
            acceptor.addText(SEPARATOR_LINE, true);
            acceptor.addEmptyLine();
            acceptor.addEmptyLine();
            JsonArray disciplines = eachSection.getAsJsonArray("disciplines");
            if (disciplines != null) {
                for (JsonElement e0 : disciplines) {
                    JsonObject eachDiscipline = e0.getAsJsonObject();
                    String discipline = JsonHelper.getString(eachDiscipline, "discipline", "");
                    if (!discipline.isBlank()) {
                        // this.addCreditsLine(Component.literal(string2).withStyle(ChatFormatting.YELLOW), true);
                        acceptor.addText(Text.literal(discipline).formatted(Formatting.YELLOW), true);
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
            JsonObject oneTitle = JsonHelper.asObject(e1, "title");
            String string2 = oneTitle.get("title").getAsString();
            JsonArray names = JsonHelper.getArray(oneTitle, "names");
            acceptor.addText(Text.literal(string2).formatted(Formatting.GRAY), false);
            for (JsonElement e2 : names) {
                String name = JsonHelper.asString(e2, "name");
                acceptor.addText(
                        Text.literal(CENTERED_LINE_PREFIX).append(name).formatted(Formatting.WHITE),
                        false);
            }
            acceptor.addEmptyLine();
            acceptor.addEmptyLine();
        }
    }
}
