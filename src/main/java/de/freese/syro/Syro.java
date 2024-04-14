package de.freese.syro;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import io.netty.buffer.ByteBuf;

import de.freese.syro.adapter.ByteBufAdapter;
import de.freese.syro.adapter.ByteBufferAdapter;
import de.freese.syro.adapter.DataReader;
import de.freese.syro.adapter.DataWriter;
import de.freese.syro.adapter.IoStreamAdapter;
import de.freese.syro.serializer.BooleanSerializer;
import de.freese.syro.serializer.DoubleSerializer;
import de.freese.syro.serializer.ExceptionSerializer;
import de.freese.syro.serializer.FloatSerializer;
import de.freese.syro.serializer.IntegerSerializer;
import de.freese.syro.serializer.LongSerializer;
import de.freese.syro.serializer.ObjectSerializer;
import de.freese.syro.serializer.Serializer;
import de.freese.syro.serializer.StackTraceElementSerializer;
import de.freese.syro.serializer.StringSerializer;

@SuppressWarnings("ALL")
public final class Syro<R, W> implements SerializerRegistry {

    public static <I, O> Syro<I, O> of(final Function<I, DataReader> dataReaderWrapper, final Function<O, DataWriter> dataWriterWrapper) {
        return new Syro<>(Objects.requireNonNull(dataReaderWrapper, "dataReaderWrapper required"),
                Objects.requireNonNull(dataWriterWrapper, "dataWriterWrapper required"));
    }

    public static Syro<ByteBuf, ByteBuf> ofByteBuf() {
        return new Syro<>(ByteBufAdapter::new, ByteBufAdapter::new);
    }

    public static Syro<ByteBuffer, ByteBuffer> ofByteBuffer() {
        return new Syro<>(ByteBufferAdapter::new, ByteBufferAdapter::new);
    }

    public static Syro<InputStream, OutputStream> ofIoStream() {
        return new Syro<>(IoStreamAdapter::new, IoStreamAdapter::new);
    }

    public static <I> Syro<I, Void> ofReader(final Function<I, DataReader> dataReaderWrapper) {
        return new Syro<>(Objects.requireNonNull(dataReaderWrapper, "dataReaderWrapper required"), null);
    }

    public static <O> Syro<Void, O> ofWriter(final Function<O, DataWriter> dataWriterWrapper) {
        return new Syro<>(null, Objects.requireNonNull(dataWriterWrapper, "dataWriterWrapper required"));
    }

    private final Function<R, DataReader> dataReaderWrapper;
    private final Function<W, DataWriter> dataWriterWrapper;
    private final Map<Class<?>, Serializer<?>> serializers = new HashMap<>();

    private Syro(final Function<R, DataReader> dataReaderWrapper, final Function<W, DataWriter> dataWriterWrapper) {
        super();

        this.dataReaderWrapper = dataReaderWrapper;
        this.dataWriterWrapper = dataWriterWrapper;

        register(String.class, new StringSerializer());
        register(Boolean.class, new BooleanSerializer());
        register(boolean.class, new BooleanSerializer());
        register(Integer.class, new IntegerSerializer());
        register(int.class, new IntegerSerializer());
        register(Long.class, new LongSerializer());
        register(long.class, new LongSerializer());
        register(Float.class, new FloatSerializer());
        register(float.class, new FloatSerializer());
        register(Double.class, new DoubleSerializer());
        register(double.class, new DoubleSerializer());

        register(StackTraceElement.class, new StackTraceElementSerializer());
        register(Exception.class, new ExceptionSerializer());
    }

    @Override
    public <S> Serializer<S> getSerializer(final Class<S> type) {
        Serializer<?> serializer = this.serializers.get(type);

        if (serializer == null) {
            Class<?> superClass = type.getSuperclass();

            while (superClass != null) {
                serializer = this.serializers.get(type.getSuperclass());

                if (serializer != null) {
                    break;
                }

                superClass = superClass.getSuperclass();
            }
        }

        if (serializer == null) {
            for (Class<?> ifc : type.getInterfaces()) {
                serializer = this.serializers.get(ifc);

                if (serializer != null) {
                    break;
                }
            }
        }

        if (serializer == null) {
            // throw new UnsupportedOperationException("no serializer found for type: " + type);
            serializer = ObjectSerializer.getInstance();
        }

        return (Serializer<S>) serializer;
    }

    public <T> T read(final R source, final Class<T> type) {
        final Serializer<T> serializer = getSerializer(type);
        final DataReader reader = wrapToReader(source);

        return serializer.read(this, reader, type);
    }

    @Override
    public <T> void register(final Class<T> type, final Serializer<T> serializer) {
        this.serializers.put(type, serializer);
    }

    public <T> void write(final W sink, final T value) {
        final Class<T> type = (Class<T>) value.getClass();
        final Serializer<T> serializer = getSerializer(type);
        final DataWriter writer = wrapToWriter(sink);

        serializer.write(this, writer, value);
    }

    public void writeNull(final W sink, final Class<?> type) {
        final Serializer<?> serializer = getSerializer(type);
        final DataWriter writer = wrapToWriter(sink);

        serializer.write(this, writer, null);
    }

    private DataReader wrapToReader(final R source) {
        return dataReaderWrapper.apply(source);
    }

    private DataWriter wrapToWriter(final W sink) {
        return dataWriterWrapper.apply(sink);
    }
}
