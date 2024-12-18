package de.freese.syro.serializer;

import de.freese.syro.io.DataReader;
import de.freese.syro.io.DataWriter;

public final class FloatSerializer implements Serializer<Float> {
    private static final class FloatSerializerHolder {
        private static final FloatSerializer INSTANCE = new FloatSerializer();

        private FloatSerializerHolder() {
            super();
        }
    }

    public static FloatSerializer getInstance() {
        return FloatSerializerHolder.INSTANCE;
    }

    private FloatSerializer() {
        super();
    }

    @Override
    public Float read(final DataReader reader) {
        return reader.readFloatOrNull();
    }

    @Override
    public void write(final DataWriter writer, final Float value) {
        writer.writeFloatOrNull(value);
    }
}
