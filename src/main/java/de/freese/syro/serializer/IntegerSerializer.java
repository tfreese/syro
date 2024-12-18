package de.freese.syro.serializer;

import de.freese.syro.io.DataReader;
import de.freese.syro.io.DataWriter;

public final class IntegerSerializer implements Serializer<Integer> {
    private static final class IntegerSerializerHolder {
        private static final IntegerSerializer INSTANCE = new IntegerSerializer();

        private IntegerSerializerHolder() {
            super();
        }
    }

    public static IntegerSerializer getInstance() {
        return IntegerSerializerHolder.INSTANCE;
    }

    private IntegerSerializer() {
        super();
    }

    @Override
    public Integer read(final DataReader reader) {
        return reader.readIntegerOrNull();
    }

    @Override
    public void write(final DataWriter writer, final Integer value) {
        writer.writeIntegerOrNull(value);
    }
}
