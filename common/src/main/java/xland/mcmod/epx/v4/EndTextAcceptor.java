package xland.mcmod.epx.v4;

import it.unimi.dsi.fastutil.ints.IntCollection;
import java.util.List;
import java.util.function.IntConsumer;

import xland.mcmod.epx.v4.support.Text;
import xland.mcmod.epx.v4.support.TextFormatter;

@SuppressWarnings("ClassCanBeRecord")
public class EndTextAcceptor<F> {
    protected static final int MAX_WIDTH = 274;
    private final List<? super F> textVisitor;
    private final IntConsumer centeredLineVisitor;
    private final TextFormatter<F> textFormatter;

    public EndTextAcceptor(List<? super F> textVisitor, IntConsumer centeredLineVisitor, TextFormatter<F> textFormatter) {
        this.textVisitor = textVisitor;
        this.centeredLineVisitor = centeredLineVisitor;
        this.textFormatter = textFormatter;
    }
    
    public static <F> EndTextAcceptor<F> createVanilla(List<F> texts, IntCollection centeredLines, TextFormatter<F> textFormatter) {
        return new EndTextAcceptor<>(texts, centeredLines::add, textFormatter);
    }

    public void addEmptyLine() {
        this.textVisitor.add(textFormatter.formattedOf());
    }

    public void addText(String text) {
        this.textVisitor.addAll(textFormatter.splitFormattedOf(Text.of(text), MAX_WIDTH));
    }

    public void addLine(Text text, boolean centered) {
        if (centered) {
            this.centeredLineVisitor.accept(this.textVisitor.size());
        }
        this.textVisitor.add(textFormatter.formattedOf(text));
    }
}
