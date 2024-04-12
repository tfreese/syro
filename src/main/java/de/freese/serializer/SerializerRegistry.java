package de.freese.serializer;

import de.freese.serializer.serializer.Serializer;

public interface SerializerRegistry {
    Serializer<?> getSerializer(Class<?> type);

    <T> void register(Class<T> type, Serializer<T> serializer);
}
