package xland.mcmod.epx.v4.mixin.neo;

import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xland.mcmod.epx.v4.neo.PoemCreditsNeo;

import java.util.List;

@Mixin(targets = "net.neoforged.neoforge.client.gui.ModListScreen$InfoPanel")
public class MixinModListScreenInfoPanel {
    @Inject(at = @At("HEAD"), method = "resizeContent")
    private void appendLines(List<String> lines, CallbackInfoReturnable<List<FormattedCharSequence>> cir) {
        PoemCreditsNeo.appendLines(lines);
    }
}
