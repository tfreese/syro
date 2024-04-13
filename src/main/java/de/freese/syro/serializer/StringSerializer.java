package de.freese.syro.serializer;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import de.freese.syro.adapter.DataReader;
import de.freese.syro.adapter.DataWriter;

public class StringSerializer implements Serializer<String> {
    @Override
    public String read(final Function<Class<?>, Serializer<?>> registry, final DataReader reader, final Class<String> type) {
        return reader.readString(StandardCharsets.UTF_8);
    }

    @Override
    public void write(final Function<Class<?>, Serializer<?>> registry, final DataWriter writer, final String value) {
        writer.writeString(value, StandardCharsets.UTF_8);
    }
}
