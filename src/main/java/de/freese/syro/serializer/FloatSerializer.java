package de.freese.syro.serializer;

import de.freese.syro.SerializerRegistry;
import de.freese.syro.adapter.DataReader;
import de.freese.syro.adapter.DataWriter;

public class FloatSerializer implements Serializer<Float> {
    @Override
    public Float read(final SerializerRegistry registry, final DataReader reader, final Class<Float> type) {
        return reader.readFloatOrNull();
    }

    @Override
    public void write(final SerializerRegistry registry, final DataWriter writer, final Float value) {
        writer.writeFloatOrNull(value);
    }
}
