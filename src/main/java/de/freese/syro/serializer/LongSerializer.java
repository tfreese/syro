package de.freese.syro.serializer;

import de.freese.syro.SerializerRegistry;
import de.freese.syro.adapter.DataReader;
import de.freese.syro.adapter.DataWriter;

public class LongSerializer implements Serializer<Long> {
    @Override
    public Long read(final SerializerRegistry registry, final DataReader reader, final Class<Long> type) {
        return reader.readLongOrNull();
    }

    @Override
    public void write(final SerializerRegistry registry, final DataWriter writer, final Long value) {
        writer.writeLongOrNull(value);
    }
}
