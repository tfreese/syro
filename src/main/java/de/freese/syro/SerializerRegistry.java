package de.freese.syro;

import de.freese.syro.serializer.Serializer;

public interface SerializerRegistry {
    <S> Serializer<S> getSerializer(Class<S> type);

    <S> void register(Class<S> type, Serializer<S> serializer);
}
