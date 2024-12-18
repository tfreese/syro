package de.freese.syro.serializer;

import de.freese.syro.io.DataReader;
import de.freese.syro.io.DataWriter;

public final class DoubleSerializer implements Serializer<Double> {
    private static final class DoubleSerializerHolder {
        private static final DoubleSerializer INSTANCE = new DoubleSerializer();

        private DoubleSerializerHolder() {
            super();
        }
    }

    public static DoubleSerializer getInstance() {
        return DoubleSerializerHolder.INSTANCE;
    }

    private DoubleSerializer() {
        super();
    }

    @Override
    public Double read(final DataReader reader) {
        return reader.readDoubleOrNull();
    }

    @Override
    public void write(final DataWriter writer, final Double value) {
        writer.writeDoubleOrNull(value);
    }
}
