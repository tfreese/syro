package de.freese.syro.serializer;

import org.jspecify.annotations.Nullable;

import de.freese.syro.io.DataReader;
import de.freese.syro.io.DataWriter;

public final class StringSerializer implements Serializer<String> {
    private static final class StringSerializerHolder {
        private static final StringSerializer INSTANCE = new StringSerializer();

        private StringSerializerHolder() {
            super();
        }
    }

    public static StringSerializer getInstance() {
        return StringSerializerHolder.INSTANCE;
    }

    private StringSerializer() {
        super();
    }

    @Override
    @Nullable
    public String read(final DataReader reader) {
        return reader.readString();
    }

    @Override
    public void write(final DataWriter writer, @Nullable final String value) {
        writer.writeString(value);
    }
}
