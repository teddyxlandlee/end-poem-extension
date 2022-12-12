package xland.mcmod.endpoemext;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.IntCollection;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.OrderedText;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import java.util.List;

public final class Locators {
    private Locators() {}

    static final Logger LOGGER = LogUtils.getLogger();

    private static final Identifier PRE_POEM = new Identifier("end_poem_extension", "poem_pre.json");
    private static final Identifier POST_POEM = new Identifier("end_poem_extension", "poem_post.json");
    private static final Identifier PRE_MOJANG_CREDITS = new Identifier("end_poem_extension", "pre_mojang_credits.json");
    private static final Identifier PRE_POSTCREDITS = new Identifier("end_poem_extension", "pre_postcredits.json");
    private static final Identifier POST_POSTCREDITS = new Identifier("end_poem_extension", "post_postcredits.json");

    private static final String ID_END = "texts/end.txt";
    private static final String ID_CREDITS = "texts/credits.json";
    private static final String ID_POST_CREDITS = "texts/postcredits.txt";

    /* @return true if to redirect, false otherwise */
    public static boolean tryRedirect(String id, List<OrderedText> texts, IntCollection centeredLines) {
        final EndTextAcceptor acceptor = EndTextAcceptor.createVanilla(texts, centeredLines);
        final ResourceManager resourceManager = MinecraftClient.getInstance().getResourceManager();
        Locator locator;

        switch (id) {
            case ID_END -> {
                locator = new IndexedPoemLikeLocator(PRE_POEM);
                locator.visit(resourceManager, acceptor);

                locator = new PoemLocator();
                locator.visit(resourceManager, acceptor);

                locator = new IndexedPoemLikeLocator(POST_POEM);
                locator.visit(resourceManager, acceptor);

                return true;
            }
            case ID_CREDITS -> {
                locator = new AlternativeCreditsLocator(PRE_MOJANG_CREDITS);
                locator.visit(resourceManager, acceptor);

                locator = new MojangCreditsLocator();
                locator.visit(resourceManager, acceptor);

                locator = new ModCreditsLocator();
                locator.visit(resourceManager, acceptor);

                return true;
            }
            case ID_POST_CREDITS -> {
                locator = new IndexedPoemLikeLocator(PRE_POSTCREDITS);
                locator.visit(resourceManager, acceptor);

                locator = new VanillaPostCreditsLocator();
                locator.visit(resourceManager, acceptor);

                locator = new IndexedPoemLikeLocator(POST_POSTCREDITS);
                locator.visit(resourceManager, acceptor);

                return true;
            }
            default -> {
                return false;
            }
        }
    }
}
