package xland.mcmod.epx.v4.mixin;

import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xland.mcmod.epx.v4.Locators;

import java.util.List;

@Mixin(WinScreen.class)
public abstract class MixinWinScreen {
    @Accessor("lines")
    abstract List<FormattedCharSequence> epx$lines();

    @Accessor("centeredLines")
    abstract IntSet epx$centeredLines();

    @Inject(at = @At("HEAD"), cancellable = true, method = "wrapCreditsIO")
    private void redirect(Identifier identifier, @Coerce Object creditsReader, CallbackInfo ci) {
        if (Locators.tryRedirect(/*id=*/identifier.getPath(), epx$lines(), this.epx$centeredLines())) {
            ci.cancel();
        }
    }
}
