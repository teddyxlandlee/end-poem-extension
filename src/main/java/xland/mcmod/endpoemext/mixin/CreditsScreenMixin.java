package xland.mcmod.endpoemext.mixin;

import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xland.mcmod.endpoemext.Locators;

import java.util.List;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.util.FormattedCharSequence;

@Mixin(WinScreen.class)
abstract class CreditsScreenMixin {
    @Accessor("lines")
    abstract List<FormattedCharSequence> lines();

    @Accessor("centeredLines")
    abstract IntSet centeredLines();

    @Inject(at = @At("HEAD"), method = "wrapCreditsIO", cancellable = true)
    private void injectLoadText(ResourceLocation id, @Coerce Object reader, CallbackInfo ci) {
        if (Locators.tryRedirect(id.getPath(), lines(), centeredLines()))
            ci.cancel();
    }
}
