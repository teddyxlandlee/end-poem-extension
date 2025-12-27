package xland.mcmod.endpoemext.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import xland.mcmod.endpoemext.splash.SplashModification;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(targets = "net.neoforged.neoforge.client.resources.NeoForgeSplashHooks", remap = false)
@Pseudo
public abstract class SplashMixinNeo {
    @WrapOperation(
            method = "loadSplashes(Lnet/minecraft/server/packs/resources/ResourceManager;)Ljava/util/List;",
            at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;")
    )
    private static Stream<String> modifyStringList(Stream<String> instance, Predicate<? super String> predicate, Operation<Stream<String>> original) {
        // The original method filters out "This message will never appear on the splash screen, isn't that weird?"
        // We'll keep it and do our own modifications
        return SplashModification.modify(instance);
    }
}
