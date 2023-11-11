package xland.mcmod.endpoemext;

import it.unimi.dsi.fastutil.ints.IntCollection;
import java.util.List;
import java.util.function.IntConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class EndTextAcceptor {
    protected static final int MAX_WIDTH = 274;
    private final List<? super FormattedCharSequence> textVisitor;
    private final IntConsumer centeredLineVisitor;

    public EndTextAcceptor(List<? super FormattedCharSequence> textVisitor, IntConsumer centeredLineVisitor) {
        this.textVisitor = textVisitor;
        this.centeredLineVisitor = centeredLineVisitor;
    }
    
    public static EndTextAcceptor createVanilla(List<FormattedCharSequence> texts, IntCollection centeredLines) {
        return new EndTextAcceptor(texts, centeredLines::add);
    }

    public void addEmptyLine() {
        this.textVisitor.add(FormattedCharSequence.EMPTY);
    }

    public void addText(String text) {
        this.textVisitor.addAll(Minecraft.getInstance().font.split(Component.literal(text), MAX_WIDTH));
    }

    public void addText(Component text, boolean centered) {
        if (centered) {
            this.centeredLineVisitor.accept(this.textVisitor.size());
        }
        this.textVisitor.add(text.getVisualOrderText());
    }
}
