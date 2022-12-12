package xland.mcmod.endpoemext.mixin;

import net.minecraft.client.resource.SplashTextResourceSupplier;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.profiler.Profiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xland.mcmod.endpoemext.splash.SplashModification;

import java.util.List;

@Mixin(SplashTextResourceSupplier.class)
public class SplashMixin {
    @Inject(at = @At("RETURN"), method = "prepare(Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)Ljava/util/List;",
            cancellable = true)
    private void modifyList(ResourceManager resourceManager, Profiler profiler, CallbackInfoReturnable<List<String>> cir) {
        cir.setReturnValue(SplashModification.modify(cir.getReturnValue()));
    }
}
