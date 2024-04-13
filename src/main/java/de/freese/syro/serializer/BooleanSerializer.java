package de.freese.syro.serializer;

import java.util.function.Function;

import de.freese.syro.adapter.DataReader;
import de.freese.syro.adapter.DataWriter;

public class BooleanSerializer implements Serializer<Boolean> {
    @Override
    public Boolean read(final Function<Class<?>, Serializer<?>> registry, final DataReader reader, final Class<Boolean> type) {
        return reader.readBooleanOrNull();
    }

    @Override
    public void write(final Function<Class<?>, Serializer<?>> registry, final DataWriter writer, final Boolean value) {
        writer.writeBooleanOrNull(value);
    }
}
