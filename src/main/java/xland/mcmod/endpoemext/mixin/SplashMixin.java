package xland.mcmod.endpoemext.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xland.mcmod.endpoemext.splash.SplashModification;

import java.util.List;
import net.minecraft.client.resources.SplashManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

@Mixin(SplashManager.class)
public class SplashMixin {
    @Inject(at = @At("RETURN"), method = "prepare(Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)Ljava/util/List;",
            cancellable = true)
    private void modifyList(ResourceManager resourceManager, ProfilerFiller profiler, CallbackInfoReturnable<List<String>> cir) {
        cir.setReturnValue(SplashModification.modify(cir.getReturnValue()));
    }
}
