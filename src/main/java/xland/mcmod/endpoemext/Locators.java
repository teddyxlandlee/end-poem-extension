package xland.mcmod.endpoemext;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.IntCollection;
import org.slf4j.Logger;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FormattedCharSequence;

public final class Locators {
    private Locators() {}

    static final Logger LOGGER = LogUtils.getLogger();

    private static final ResourceLocation PRE_POEM = new ResourceLocation("end_poem_extension", "poem_pre.json");
    private static final ResourceLocation POST_POEM = new ResourceLocation("end_poem_extension", "poem_post.json");
    private static final ResourceLocation PRE_MOJANG_CREDITS = new ResourceLocation("end_poem_extension", "pre_mojang_credits.json");
    private static final ResourceLocation PRE_POSTCREDITS = new ResourceLocation("end_poem_extension", "pre_postcredits.json");
    private static final ResourceLocation POST_POSTCREDITS = new ResourceLocation("end_poem_extension", "post_postcredits.json");

    private static final String ID_END = "texts/end.txt";
    private static final String ID_CREDITS = "texts/credits.json";
    private static final String ID_POST_CREDITS = "texts/postcredits.txt";

    /* @return true if to redirect, false otherwise */
    public static boolean tryRedirect(String id, List<FormattedCharSequence> texts, IntCollection centeredLines) {
        final EndTextAcceptor acceptor = EndTextAcceptor.createVanilla(texts, centeredLines);
        final ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
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
