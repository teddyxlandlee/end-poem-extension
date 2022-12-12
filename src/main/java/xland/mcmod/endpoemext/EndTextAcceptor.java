package xland.mcmod.endpoemext;

import it.unimi.dsi.fastutil.ints.IntCollection;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.IntConsumer;

public class EndTextAcceptor {
    protected static final int MAX_WIDTH = 274;
    private final List<? super OrderedText> textVisitor;
    private final IntConsumer centeredLineVisitor;

    public EndTextAcceptor(List<? super OrderedText> textVisitor, IntConsumer centeredLineVisitor) {
        this.textVisitor = textVisitor;
        this.centeredLineVisitor = centeredLineVisitor;
    }
    
    public static EndTextAcceptor createVanilla(List<OrderedText> texts, IntCollection centeredLines) {
        return new EndTextAcceptor(texts, centeredLines::add);
    }

    public void addEmptyLine() {
        this.textVisitor.add(OrderedText.EMPTY);
    }

    public void addText(String text) {
        this.textVisitor.addAll(MinecraftClient.getInstance().textRenderer.wrapLines(Text.literal(text), MAX_WIDTH));
    }

    public void addText(Text text, boolean centered) {
        if (centered) {
            this.centeredLineVisitor.accept(this.textVisitor.size());
        }
        this.textVisitor.add(text.asOrderedText());
    }
}
