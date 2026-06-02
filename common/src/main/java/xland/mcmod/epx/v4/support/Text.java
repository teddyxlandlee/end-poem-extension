package xland.mcmod.epx.v4.support;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record Text(String literal, @Nullable TextFormatting formatting) {
    public static Text of(String literal) {
        return new Text(literal, null);
    }

    public static Text of(String literal, TextFormatting formatting) {
        return new Text(literal, Objects.requireNonNull(formatting));
    }
}
