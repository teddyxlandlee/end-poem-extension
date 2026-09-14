package xland.mcmod.epx.v4.neo;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
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
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.AccessFlag;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import static java.lang.constant.ConstantDescs.*;

final class DisplayInfoFactory {
    @SuppressWarnings("unused") // referenced via MethodHandle
    public static Component credits() {
        final MutableComponent poemCredits = Component.translatable("epx.moddisplayinfo.poem_translation_credits")
                .withStyle(s -> s.withClickEvent(new ClickEvent.OpenUrl(
                        URI.create("https://epx-packs.mods.hixland.com/v2/translation_copyright.html")
                )));
        final MutableComponent subComponent = Component.empty().setStyle(Style.EMPTY);
        poemCredits.append(subComponent);

        List<Component> lines = PoemCreditsImpl.getLinesAsComponent();
        lines.forEach(subComponent::append);

        return poemCredits;
    }

    @SuppressWarnings("unused") // referenced via MethodHandle
    public static Component displayName() {
        return Component.translatable("modmenu.nameTranslation.end-poem-extension");
    }

    @SuppressWarnings("unused") // referenced via MethodHandle
    public static Component description() {
        return Component.translatable("modmenu.descriptionTranslation.end-poem-extension");
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

        final List<String> methodNames = List.of("credits", "displayName", "description");
        final List<?> classData;
        {
            var cd = new Object[methodNames.size()];
            for (int i = methodNames.size() - 1; i >= 0; i--) {
                cd[i] = lookup.findStatic(DisplayInfoFactory.class, methodNames.get(i), MethodType.methodType(Component.class));
            }
            classData = List.of(cd);
        }

        final ClassDesc thisDesc = ClassDesc.of(DisplayInfoFactory.class.getPackageName(), "DisplayInfoImpl");
        final ClassDesc superDesc = ClassDesc.ofDescriptor(superclass.descriptorString());
        final ClassDesc CD_ModContainer = ClassDesc.ofDescriptor(ModContainer.class.descriptorString());
        final ClassDesc CD_Component = ClassDesc.ofDescriptor(Component.class.descriptorString());

        final byte[] bytecode = ClassFile.of().build(thisDesc, cb -> {
                    cb
                            .withFlags(AccessFlag.SUPER, AccessFlag.SYNTHETIC, AccessFlag.FINAL)
                            .withSuperclass(superDesc)
                            .withVersion(ClassFile.JAVA_25_VERSION, 0)
                            .withMethodBody(INIT_NAME, MethodTypeDesc.of(CD_void, CD_ModContainer), 0, cob -> cob
                                    .lineNumber(100)
                                    .aload(0).aload(1)
                                    .invokespecial(superDesc, INIT_NAME, MethodTypeDesc.of(CD_void, CD_ModContainer))
                                    .return_()
                            );

                    for (int i = methodNames.size() - 1; i >= 0; i--) {
                        int baseLineNumber = i * 10, methodIndex = i;
                        cb.withMethodBody(methodNames.get(i), MethodTypeDesc.of(CD_Component), ClassFile.ACC_PUBLIC, cob -> cob
                                .lineNumber(baseLineNumber + 1)
                                .ldc(DynamicConstantDesc.ofNamed(
                                        ofConstantBootstrap(CD_MethodHandles, "classDataAt", CD_Object, CD_int),
                                        DEFAULT_NAME, CD_MethodHandle, methodIndex
                                ))
                                .lineNumber(baseLineNumber + 2)
                                .invokevirtual(CD_MethodHandle, "invokeExact", MethodTypeDesc.of(CD_Component))
                                .areturn()
                        );
                    }

                    cb.with(SourceFileAttribute.of("<generated>"));
                    cb.withMethodBody("toString", MethodTypeDesc.of(CD_String), ClassFile.ACC_PUBLIC, cob -> cob
                            .lineNumber(110)
                            .aload(0)
                            .invokespecial(superDesc, "toString", MethodTypeDesc.of(CD_String))
                            .lineNumber(111)
                            .ldc("/custom")
                            .invokevirtual(CD_String, "concat", MethodTypeDesc.of(CD_String, CD_String))
                            .areturn()
                    );
                }
        );

        final MethodHandles.Lookup subclassLookup = lookup.defineHiddenClassWithClassData(
                bytecode, classData, true, MethodHandles.Lookup.ClassOption.STRONG
        );
        return (IExtensionPoint) subclassLookup.findConstructor(
                subclassLookup.lookupClass(), MethodType.methodType(void.class, ModContainer.class)
        ).invoke(modContainer);
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayInfoFactory.class);

    private DisplayInfoFactory() {}
}
