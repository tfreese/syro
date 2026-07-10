package de.freese.syro.serializer;

import org.jspecify.annotations.Nullable;

import de.freese.syro.io.DataReader;
import de.freese.syro.io.DataWriter;

public final class LongSerializer implements Serializer<Long> {
    private static final class LongSerializerHolder {
        private static final LongSerializer INSTANCE = new LongSerializer();

        private LongSerializerHolder() {
            super();
        }
    }

    public static LongSerializer getInstance() {
        return LongSerializerHolder.INSTANCE;
    }

    private LongSerializer() {
        super();
    }

    @Override
    @Nullable
    public Long read(final DataReader reader) {
        return reader.readLongOrNull();
    }

    @Override
    public void write(final DataWriter writer, @Nullable final Long value) {
        writer.writeLongOrNull(value);
    }
}
