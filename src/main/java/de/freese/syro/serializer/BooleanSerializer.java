package de.freese.syro.serializer;

import de.freese.syro.SerializerRegistry;
import de.freese.syro.io.DataReader;
import de.freese.syro.io.DataWriter;

public class BooleanSerializer implements Serializer<Boolean> {
    @Override
    public Boolean read(final SerializerRegistry registry, final DataReader reader) {
        return reader.readBooleanOrNull();
    }

    @Override
    public void write(final SerializerRegistry registry, final DataWriter writer, final Boolean value) {
        writer.writeBooleanOrNull(value);
    }
}
