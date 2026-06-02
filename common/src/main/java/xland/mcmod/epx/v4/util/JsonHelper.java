package xland.mcmod.epx.v4.util;

import com.google.gson.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.Reader;

public final class JsonHelper {
    private JsonHelper() {}
    private static final Gson GSON = new Gson();

    public static JsonObject parseObject(Reader reader) throws JsonParseException {
        return GSON.fromJson(reader, JsonObject.class);
    }

    public static JsonArray parseArray(Reader reader) throws JsonParseException {
        return GSON.fromJson(reader, JsonArray.class);
    }

    public static JsonArray getAsJsonArray(JsonObject obj, String key) throws IllegalArgumentException {
        return convertToJsonArray(obj.get(key), key);
    }

    @Contract("_, _, !null -> !null")
    public static @Nullable JsonArray getAsJsonArray(JsonObject obj, String key, @Nullable JsonArray fallback) throws IllegalArgumentException {
        @Nullable JsonElement e = obj.get(key);
        if (e == null || !e.isJsonArray()) return fallback;
        return e.getAsJsonArray();
    }

    public static String getAsString(JsonObject obj, String key) {
        return convertToString(obj.get(key), key);
    }

    @Contract("_, _, !null -> !null")
    public static @Nullable String getAsString(JsonObject obj, String key, @Nullable String fallback) {
        @Nullable JsonElement e = obj.get(key);
        if (e == null || !e.isJsonPrimitive()) return fallback;
        return e.getAsString();
    }

    public static boolean getAsBoolean(JsonObject obj, String key, boolean fallback) {
        @Nullable JsonElement e = obj.get(key);
        if (e == null || !e.isJsonPrimitive()) return fallback;
        return e.getAsBoolean();
    }

    public static JsonObject convertToJsonObject(@Nullable JsonElement e, @Nullable String name) throws IllegalArgumentException {
        if (e == null || !e.isJsonObject()) throw new IllegalArgumentException(name + " is not an object");
        return e.getAsJsonObject();
    }

    public static JsonArray convertToJsonArray(@Nullable JsonElement e, @Nullable String name) throws IllegalArgumentException {
        if (e == null || !e.isJsonArray()) throw new IllegalArgumentException(name + " is not an array");
        return e.getAsJsonArray();
    }

    public static String convertToString(@Nullable JsonElement e, @Nullable String name) throws IllegalArgumentException {
        if (e == null || !e.isJsonPrimitive()) throw new IllegalArgumentException(name + " is not primitive");
        return e.getAsString();
    }
}
