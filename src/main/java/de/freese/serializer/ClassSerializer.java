package de.freese.serializer;

public interface ClassSerializer<R, W> extends SerializerRegistry {
    <T> T read(R source, Class<T> type);

    <T> void write(W sink, T value);

    void writeNull(W sink, Class<?> type);
}
