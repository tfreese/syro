package de.freese.serializer;

import java.util.HashMap;
import java.util.Map;

import de.freese.serializer.adapter.DataReader;
import de.freese.serializer.adapter.DataWriter;
import de.freese.serializer.serializer.BooleanSerializer;
import de.freese.serializer.serializer.Serializer;

public abstract class AbstractClassSerializer<R, W> implements ClassSerializer<R, W> {
    private final Map<Class<?>, Serializer<?>> serializers = new HashMap<>();

    protected AbstractClassSerializer() {
        super();
    }

    @Override
    public Serializer<?> getSerializer(final Class<?> type) {
        Serializer<?> serializer = this.serializers.get(type);

        if (serializer == null) {
            for (Class<?> ifc : type.getInterfaces()) {
                serializer = this.serializers.get(ifc);

                if (serializer != null) {
                    break;
                }
            }
        }

        return serializer;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T read(final R source, final Class<T> type) {
        final Serializer<T> serializer = (Serializer<T>) getSerializer(type);

        return serializer.read(this::getSerializer, wrapToReader(source), type);
    }

    @Override
    public <T> void register(final Class<T> type, final Serializer<T> serializer) {
        this.serializers.put(type, serializer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void write(final W sink, final T value) {
        final Serializer<T> serializer = (Serializer<T>) getSerializer(value.getClass());

        serializer.write(this::getSerializer, wrapToWriter(sink), value);
    }

    @Override
    public void writeNull(final W sink, final Class<?> type) {
        final Serializer<?> serializer = getSerializer(type);

        serializer.write(this::getSerializer, wrapToWriter(sink), null);
    }

    protected void registerDefaultSerializer() {
        register(Boolean.class, new BooleanSerializer());
    }

    protected abstract DataReader wrapToReader(R source);

    protected abstract DataWriter wrapToWriter(W sink);
}
