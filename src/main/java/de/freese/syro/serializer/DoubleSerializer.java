package de.freese.syro.serializer;

import de.freese.syro.SerializerRegistry;
import de.freese.syro.io.DataReader;
import de.freese.syro.io.DataWriter;

public class DoubleSerializer implements Serializer<Double> {
    @Override
    public Double read(final SerializerRegistry registry, final DataReader reader) {
        return reader.readDoubleOrNull();
    }

    @Override
    public void write(final SerializerRegistry registry, final DataWriter writer, final Double value) {
        writer.writeDoubleOrNull(value);
    }
}
