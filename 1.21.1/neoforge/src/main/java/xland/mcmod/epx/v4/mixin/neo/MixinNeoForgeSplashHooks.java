package xland.mcmod.epx.v4.mixin.neo;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.neoforged.neoforge.client.resources.NeoForgeSplashHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xland.mcmod.epx.v4.splash.SplashModification;

import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(NeoForgeSplashHooks.class)
public abstract class MixinNeoForgeSplashHooks {
    @WrapOperation(
            method = "loadSplashes",
            at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;")
    )
    private static Stream<String> modifyStringList(Stream<String> instance, Predicate<? super String> predicate, Operation<Stream<String>> original) {
        // The original method filters out "This message will never appear on the splash screen, isn't that weird?"
        // We'll keep it and do our own modifications
        return SplashModification.modify(instance);
    }
}
