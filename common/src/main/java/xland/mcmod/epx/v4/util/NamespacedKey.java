package xland.mcmod.epx.v4.util;

import com.google.common.base.Preconditions;
import org.intellij.lang.annotations.Language;

import java.util.regex.Pattern;

public record NamespacedKey(@org.intellij.lang.annotations.Pattern(REGEX_NAMESPACE) String namespace,
                            @org.intellij.lang.annotations.Pattern(REGEX_PATH) String path) {
    private static final @Language("RegExp") String REGEX_NAMESPACE = "^[a-z0-9\\u002e\\u002d_]+$";
    private static final @Language("RegExp") String REGEX_PATH = "^[a-z0-9\\u002e\\u002d_/]+$";
    private static final Pattern PATTERN_NAMESPACE = Pattern.compile(REGEX_NAMESPACE);
    private static final Pattern PATTERN_PATH = Pattern.compile(REGEX_PATH);

    public NamespacedKey {
        Preconditions.checkArgument(PATTERN_NAMESPACE.matcher(namespace).matches(), "Illegal namespace");
        Preconditions.checkArgument(PATTERN_PATH.matcher(path).matches(), "Illegal path");
    }

    @Override
    public String toString() {
        return namespace + ":" + path;
    }

    @SuppressWarnings("PatternValidation")
    public static NamespacedKey of(@org.intellij.lang.annotations.Pattern(REGEX_NAMESPACE) String namespace,
                                   @org.intellij.lang.annotations.Pattern(REGEX_PATH) String path) {
        return new NamespacedKey(namespace, path);
    }

    @SuppressWarnings("PatternValidation")
    public static NamespacedKey of(@org.intellij.lang.annotations.Pattern("^([0-9a-z_\\-]+:)?[0-9a-z_\\-/]+$") String key) {
        final int i = key.indexOf(':');
        if (i < 0) return ofMinecraft(key);
        return new NamespacedKey(key.substring(0, i), key.substring(i+1));
    }

    @SuppressWarnings("PatternValidation")
    public static NamespacedKey tryParse(String key) throws IllegalArgumentException {
        return of(key);
    }

    @SuppressWarnings("PatternValidation")
    public static NamespacedKey ofMinecraft(@org.intellij.lang.annotations.Pattern(REGEX_PATH) String path) {
        return new NamespacedKey("minecraft", path);
    }
}
