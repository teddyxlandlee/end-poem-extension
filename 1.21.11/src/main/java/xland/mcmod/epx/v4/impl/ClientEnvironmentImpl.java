package xland.mcmod.epx.v4.impl;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNullByDefault;
import org.jetbrains.annotations.Nullable;
import xland.mcmod.epx.v4.support.*;
import xland.mcmod.epx.v4.util.NamespacedKey;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@NotNullByDefault
public class ClientEnvironmentImpl implements ClientEnvironment {
    private final TextFormatter<FormattedCharSequence> textFormatter = new Formatter();

    @Override
    @Contract("->new")
    public ClientResourceManager getResourceManager() {
        return new ClientResourceLoaderImpl();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <F> TextFormatter<F> getTextFormatter() {
        return (TextFormatter<F>) textFormatter;
    }

    @Override
    public String getUsername() {
        return Minecraft.getInstance().getUser().getName();
    }

    @Override
    public String getCurrentLanguage() {
        @Nullable String languageCode = Minecraft.getInstance().options.languageCode;
        // It's a mutable field, so we shouldn't assume it's non-null
        //noinspection ConstantConditions
        return languageCode != null ? languageCode : "en_us";
    }

    private static final class Formatter implements TextFormatter<FormattedCharSequence> {
        @Override
        public FormattedCharSequence formattedOf() {
            return FormattedCharSequence.EMPTY;
        }

        @Override
        public FormattedCharSequence formattedOf(Text text) {
            return newComponentFromText(text).getVisualOrderText();
        }

        @Override
        public List<FormattedCharSequence> splitFormattedOf(Text text, int maxWidth) {
            return Minecraft.getInstance().font.split(newComponentFromText(text), maxWidth);
        }
    }

    static MutableComponent newComponentFromText(Text text) {
        var component = Component.literal(text.literal());
        @Nullable TextFormatting formatting = text.formatting();
        if (formatting != null) component.withStyle(vanillaChatFormatting(formatting));
        return component;
    }

    static ChatFormatting vanillaChatFormatting(TextFormatting textFormatting) {
        return switch (textFormatting) {
            case BLACK -> ChatFormatting.BLACK;
            case DARK_BLUE -> ChatFormatting.DARK_BLUE;
            case DARK_GREEN -> ChatFormatting.DARK_GREEN;
            case DARK_AQUA -> ChatFormatting.DARK_AQUA;
            case DARK_RED -> ChatFormatting.DARK_RED;
            case DARK_PURPLE -> ChatFormatting.DARK_PURPLE;
            case GOLD -> ChatFormatting.GOLD;
            case GRAY -> ChatFormatting.GRAY;
            case DARK_GRAY -> ChatFormatting.DARK_GRAY;
            case BLUE -> ChatFormatting.BLUE;
            case GREEN -> ChatFormatting.GREEN;
            case AQUA -> ChatFormatting.AQUA;
            case RED -> ChatFormatting.RED;
            case LIGHT_PURPLE -> ChatFormatting.LIGHT_PURPLE;
            case YELLOW -> ChatFormatting.YELLOW;
            case WHITE -> ChatFormatting.WHITE;
            case OBFUSCATED -> ChatFormatting.OBFUSCATED;
            case BOLD -> ChatFormatting.BOLD;
            case STRIKETHROUGH -> ChatFormatting.STRIKETHROUGH;
            case UNDERLINE -> ChatFormatting.UNDERLINE;
            case ITALIC -> ChatFormatting.ITALIC;
            case RESET -> ChatFormatting.RESET;
        };
    }

    private static final class ClientResourceLoaderImpl implements ClientResourceManager {
        private final ResourceManager vanillaManager = Minecraft.getInstance().getResourceManager();

        private static final Function<Resource, ClientResource> RESOURCE_TRANSFORMER = r -> r::openAsReader;

        @Override
        public Collection<? extends ClientResource> readResources(NamespacedKey key) {
            List<Resource> resourceStack = vanillaManager.getResourceStack(toVanillaId(key));
            return Lists.transform(resourceStack, RESOURCE_TRANSFORMER);
        }

        @Override
        public Optional<ClientResource> readFirstResourceOptionally(NamespacedKey key) {
            return vanillaManager.getResource(toVanillaId(key)).map(RESOURCE_TRANSFORMER);
        }

        @Override
        public Collection<? extends String> getResourceNamespaces() {
            return vanillaManager.getNamespaces();
        }
    }

    static Identifier toVanillaId(NamespacedKey key) {
        return Identifier.fromNamespaceAndPath(key.namespace(), key.path());
    }
}
