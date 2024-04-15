package de.freese.syro.serializer;

import de.freese.syro.SerializerRegistry;
import de.freese.syro.io.DataReader;
import de.freese.syro.io.DataWriter;

public class StringSerializer implements Serializer<String> {
    @Override
    public String read(final SerializerRegistry registry, final DataReader reader, final Class<String> type) {
        return reader.readString();
    }

    @Override
    public void write(final SerializerRegistry registry, final DataWriter writer, final String value) {
        writer.writeString(value);
    }
}
