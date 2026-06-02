package xland.mcmod.epx.v4;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.IntCollection;
import org.slf4j.Logger;

import java.util.List;

import xland.mcmod.epx.v4.support.ClientEnvironment;
import xland.mcmod.epx.v4.util.NamespacedKey;

public final class Locators {
    private Locators() {}

    static final Logger LOGGER = LogUtils.getLogger();

    private static final NamespacedKey PRE_POEM = NamespacedKey.of("end_poem_extension", "poem_pre.json");
    private static final NamespacedKey POST_POEM = NamespacedKey.of("end_poem_extension", "poem_post.json");
    private static final NamespacedKey PRE_MOJANG_CREDITS = NamespacedKey.of("end_poem_extension", "pre_mojang_credits.json");
    private static final NamespacedKey PRE_POSTCREDITS = NamespacedKey.of("end_poem_extension", "pre_postcredits.json");
    private static final NamespacedKey POST_POSTCREDITS = NamespacedKey.of("end_poem_extension", "post_postcredits.json");

    private static final String ID_END = "texts/end.txt";
    private static final String ID_CREDITS = "texts/credits.json";
    private static final String ID_POST_CREDITS = "texts/postcredits.txt";

    /* @return true if to redirect, false otherwise */
    public static <F> boolean tryRedirect(String id, List<F> texts, IntCollection centeredLines) {
        ClientEnvironment env = ClientEnvironment.getInstance();
        final EndTextAcceptor<F> acceptor = EndTextAcceptor.createVanilla(texts, centeredLines, env.getTextFormatter());
        final var resourceManager = env.getResourceManager();
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
