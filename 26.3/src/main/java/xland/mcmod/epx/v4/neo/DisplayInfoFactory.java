package xland.mcmod.epx.v4.neo;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.neoforged.fml.IExtensionPoint;
import net.neoforged.fml.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xland.mcmod.epx.v4.impl.PoemCreditsImpl;

import java.lang.classfile.ClassFile;
import java.lang.classfile.attribute.SourceFileAttribute;
import java.lang.constant.ClassDesc;
import java.lang.constant.DynamicConstantDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.AccessFlag;
import java.util.List;
import java.util.Optional;

import static java.lang.constant.ConstantDescs.*;

final class DisplayInfoFactory {
    // wrap `Component DefaultModDisplayInfo.description()`
    public static Component appendLines(Component old) {
        List<Component> lines = PoemCreditsImpl.getLinesAsComponent();
        MutableComponent ret = Component.empty().append(old).append("\n");
        lines.forEach(c -> ret.append("\n").append(c));
        return ret;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    static void tryApplyDisplayInfo(final ModContainer container) {
        modDisplayInfo().ifPresent((c) -> {
            IExtensionPoint displayInfo;
            try {
                displayInfo = constructDisplayInfo(container);
            } catch (Throwable t) {
                LOGGER.warn("Cannot construct custom display info for {}, fallback to default", container.getModId(), t);
                return;
            }

            container.registerExtensionPoint((Class) c, displayInfo);
            LOGGER.info("Registered custom display info for {}", container.getModId());
        });
    }

    private static Optional<Class<? extends IExtensionPoint>> modDisplayInfo() {
        try {
            Class<?> c = MethodHandles.lookup().findClass("net.neoforged.neoforge.client.gui.modlist.ModDisplayInfo");
            return Optional.of(c.asSubclass(IExtensionPoint.class));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    private static IExtensionPoint constructDisplayInfo(ModContainer modContainer) throws Throwable {
        // public DefaultModDisplayInfo(ModContainer)
        final var lookup = MethodHandles.lookup();
        final Class<? extends IExtensionPoint> superclass = lookup.findClass("net.neoforged.neoforge.client.gui.modlist.DefaultModDisplayInfo")
                .asSubclass(IExtensionPoint.class);

        final MethodHandle MH_appendLines = lookup.findStatic(DisplayInfoFactory.class, "appendLines", MethodType.methodType(Component.class, Component.class));

        final ClassDesc thisDesc = ClassDesc.of(DisplayInfoFactory.class.getPackageName(), "DisplayInfoImpl");
        final ClassDesc superDesc = ClassDesc.ofDescriptor(superclass.descriptorString());
        final ClassDesc CD_ModContainer = ClassDesc.ofDescriptor(ModContainer.class.descriptorString());
        final ClassDesc CD_Component = ClassDesc.ofDescriptor(Component.class.descriptorString());

        final byte[] bytecode = ClassFile.of().build(thisDesc, cb -> cb
                .withFlags(AccessFlag.SUPER, AccessFlag.SYNTHETIC, AccessFlag.FINAL)
                .withSuperclass(superDesc)
                .withVersion(ClassFile.JAVA_25_VERSION, 0)
                .withMethodBody(INIT_NAME, MethodTypeDesc.of(CD_void, CD_ModContainer), 0, cob -> cob
                        .lineNumber(10)
                        .aload(0).aload(1)
                        .invokespecial(superDesc, INIT_NAME, MethodTypeDesc.of(CD_void, CD_ModContainer))
                        .return_()
                )
                // Component description();
                .withMethodBody("component", MethodTypeDesc.of(CD_Component), ClassFile.ACC_PUBLIC, cob -> cob
                        .lineNumber(20)
                        .aload(0)
                        .invokespecial(superDesc, "component", MethodTypeDesc.of(CD_Component))
                        .lineNumber(21)
                        .ldc(DynamicConstantDesc.ofNamed(
                                ofConstantBootstrap(CD_MethodHandles, "classData", CD_Object),
                                DEFAULT_NAME, CD_MethodHandle
                        ))
                        .swap()
                        .lineNumber(22)
                        .invokevirtual(CD_MethodHandle, "invokeExact", MethodTypeDesc.of(CD_Component, CD_Component))
                        .areturn()
                )
                .with(SourceFileAttribute.of("<generated>"))
                .withMethodBody("toString", MethodTypeDesc.of(CD_String), ClassFile.ACC_PUBLIC, cob -> cob
                        .lineNumber(110)
                        .aload(0)
                        .invokespecial(superDesc, "toString", MethodTypeDesc.of(CD_String))
                        .lineNumber(111)
                        .ldc("/custom")
                        .invokevirtual(CD_String, "concat", MethodTypeDesc.of(CD_String, CD_String))
                        .areturn()
                )
        );

        final MethodHandles.Lookup subclassLookup = lookup.defineHiddenClassWithClassData(
                bytecode, MH_appendLines, true, MethodHandles.Lookup.ClassOption.STRONG
        );
        return (IExtensionPoint) subclassLookup.findConstructor(
                subclassLookup.lookupClass(), MethodType.methodType(void.class, ModContainer.class)
        ).invoke(modContainer);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayInfoFactory.class);
}
