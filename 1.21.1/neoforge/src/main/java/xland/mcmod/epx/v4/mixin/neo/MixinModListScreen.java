package xland.mcmod.epx.v4.mixin.neo;

import com.llamalad7.mixinextras.sugar.Local;
import net.neoforged.neoforge.client.gui.ModListScreen;
import net.neoforged.neoforgespi.language.IModInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xland.mcmod.epx.v4.neo.PoemCreditsNeo;

import java.util.List;

@Mixin(ModListScreen.class)
public class MixinModListScreen {
    @Inject(
            method = "updateCache",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/neoforged/neoforge/client/gui/ModListScreen$InfoPanel;setInfo(Ljava/util/List;Lnet/minecraft/resources/ResourceLocation;Lnet/neoforged/neoforge/common/util/Size2i;)V"
            )
    )
    private void appendLines(
            CallbackInfo ci,
            @Local(name = "lines") List<String> lines,
            @Local(name = "selectedMod") IModInfo selectedMod
    ) {
        if ("endpoemext".equals(selectedMod.getModId())) {
            PoemCreditsNeo.appendLines(lines);
        }
    }
}
