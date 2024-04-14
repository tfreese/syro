package de.freese.syro.serializer;

import de.freese.syro.SerializerRegistry;
import de.freese.syro.adapter.DataReader;
import de.freese.syro.adapter.DataWriter;

public class IntegerSerializer implements Serializer<Integer> {
    @Override
    public Integer read(final SerializerRegistry registry, final DataReader reader, final Class<Integer> type) {
        return reader.readIntegerOrNull();
    }

    @Override
    public void write(final SerializerRegistry registry, final DataWriter writer, final Integer value) {
        writer.writeIntegerOrNull(value);
    }
}
