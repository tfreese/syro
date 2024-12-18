package de.freese.syro.serializer;

import de.freese.syro.io.DataReader;
import de.freese.syro.io.DataWriter;

public final class BooleanSerializer implements Serializer<Boolean> {
    private static final class BooleanSerializerHolder {
        private static final BooleanSerializer INSTANCE = new BooleanSerializer();

        private BooleanSerializerHolder() {
            super();
        }
    }

    public static BooleanSerializer getInstance() {
        return BooleanSerializerHolder.INSTANCE;
    }

    private BooleanSerializer() {
        super();
    }

    @Override
    public Boolean read(final DataReader reader) {
        return reader.readBooleanOrNull();
    }

    @Override
    public void write(final DataWriter writer, final Boolean value) {
        writer.writeBooleanOrNull(value);
    }
}
