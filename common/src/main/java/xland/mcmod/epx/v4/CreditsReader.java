package xland.mcmod.epx.v4;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.Reader;
import xland.mcmod.epx.v4.util.JsonHelper;
import xland.mcmod.epx.v4.support.Text;
import xland.mcmod.epx.v4.support.TextFormatting;

public class CreditsReader extends CreditsElementReader {
    private final Text separatorLine = Text.of("============", TextFormatting.WHITE);
    private static final String CENTERED_LINE_PREFIX = "\040".repeat(11);   // 11 spaces

    protected CreditsReader(EndTextAcceptor<?> acceptor) {
        super(acceptor);
    }

    @Override
    protected void read(Reader reader) {
        JsonArray arr = JsonHelper.parseArray(reader);
        for (JsonElement e : arr) {
            JsonObject eachSection = JsonHelper.convertToJsonObject(e, "section");
            String string = eachSection.get("section").getAsString();
            acceptor.addLine(separatorLine, true);
            acceptor.addLine(Text.of(string, TextFormatting.YELLOW), true);
            acceptor.addLine(separatorLine, true);
            acceptor.addEmptyLine();
            acceptor.addEmptyLine();
            JsonArray disciplines = eachSection.getAsJsonArray("disciplines");
            if (disciplines != null) {
                for (JsonElement e0 : disciplines) {
                    JsonObject eachDiscipline = e0.getAsJsonObject();
                    String discipline = JsonHelper.getAsString(eachDiscipline, "discipline", "");
                    if (!discipline.isBlank()) {
                        acceptor.addLine(Text.of(discipline, TextFormatting.YELLOW), true);
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
            JsonObject oneTitle = JsonHelper.convertToJsonObject(e1, "title");
            String title = oneTitle.get("title").getAsString();
            JsonArray names = JsonHelper.getAsJsonArray(oneTitle, "names");
            acceptor.addLine(Text.of(title, TextFormatting.GRAY), false);
            for (JsonElement e2 : names) {
                String name = JsonHelper.convertToString(e2, "name");
                acceptor.addLine(
                        Text.of(CENTERED_LINE_PREFIX + name, TextFormatting.WHITE),
                        false);
            }
            acceptor.addEmptyLine();
            acceptor.addEmptyLine();
        }
    }
}
