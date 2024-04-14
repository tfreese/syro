package de.freese.syro.serializer;

import de.freese.syro.SerializerRegistry;
import de.freese.syro.adapter.DataReader;
import de.freese.syro.adapter.DataWriter;

public class BooleanSerializer implements Serializer<Boolean> {
    @Override
    public Boolean read(final SerializerRegistry registry, final DataReader reader, final Class<Boolean> type) {
        return reader.readBooleanOrNull();
    }

    @Override
    public void write(final SerializerRegistry registry, final DataWriter writer, final Boolean value) {
        writer.writeBooleanOrNull(value);
    }
}
