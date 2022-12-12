package xland.mcmod.endpoemext.mixin;

import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.client.gui.screen.CreditsScreen;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xland.mcmod.endpoemext.Locators;

import java.util.List;

@Mixin(CreditsScreen.class)
public class CreditsScreenMixin {
    @Shadow private List<OrderedText> credits;

    @Shadow private IntSet centeredLines;

    @Inject(at = @At("HEAD"), method = "load", cancellable = true)
    private void injectLoadText(String id, @Coerce Object reader, CallbackInfo ci) {
        if (Locators.tryRedirect(id, credits, centeredLines))
            ci.cancel();
    }
}
