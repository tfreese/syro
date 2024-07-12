package de.freese.syro;

import java.util.HashMap;
import java.util.Map;

import de.freese.syro.io.DataReader;
import de.freese.syro.io.DataWriter;
import de.freese.syro.serializer.BooleanSerializer;
import de.freese.syro.serializer.DoubleSerializer;
import de.freese.syro.serializer.ExceptionSerializer;
import de.freese.syro.serializer.FloatSerializer;
import de.freese.syro.serializer.IntegerSerializer;
import de.freese.syro.serializer.LongSerializer;
import de.freese.syro.serializer.Serializer;
import de.freese.syro.serializer.StackTraceElementSerializer;
import de.freese.syro.serializer.StringSerializer;

@SuppressWarnings("ALL")
public final class Syro implements SerializerRegistry {

    // public static <I, O> Syro<I, O> of(final Function<I, DataReader> dataReaderWrapper, final Function<O, DataWriter> dataWriterWrapper) {
    //     return new Syro<>(Objects.requireNonNull(dataReaderWrapper, "dataReaderWrapper required"),
    //             Objects.requireNonNull(dataWriterWrapper, "dataWriterWrapper required"));
    // }
    //
    // public static Syro<ByteBuf, ByteBuf> ofByteBuf() {
    //     return new Syro<>(ByteBufReader::new, ByteBufWriter::new);
    // }
    //
    // public static Syro<ByteBuffer, ByteBuffer> ofByteBuffer() {
    //     return new Syro<>(ByteBufferReader::new, ByteBufferWriter::new);
    // }
    //
    // public static Syro<InputStream, OutputStream> ofIoStream() {
    //     return new Syro<>(InputStreamReader::new, OutputStreamWriter::new);
    // }
    //
    // public static <I> Syro<I, Void> ofReader(final Function<I, DataReader> dataReaderWrapper) {
    //     return new Syro<>(Objects.requireNonNull(dataReaderWrapper, "dataReaderWrapper required"), null);
    // }
    //
    // public static <O> Syro<Void, O> ofWriter(final Function<O, DataWriter> dataWriterWrapper) {
    //     return new Syro<>(null, Objects.requireNonNull(dataWriterWrapper, "dataWriterWrapper required"));
    // }

    private final Map<Class<?>, Serializer<?>> serializers = new HashMap<>();

    public Syro() {
        super();

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
        final Serializer<?> serializer = this.serializers.get(type);

        // if (serializer == null) {
        //     Class<?> superClass = type.getSuperclass();
        //
        //     while (superClass != null) {
        //         serializer = this.serializers.get(superClass);
        //
        //         if (serializer != null) {
        //             break;
        //         }
        //
        //         superClass = superClass.getSuperclass();
        //     }
        // }
        //
        // if (serializer == null) {
        //     for (Class<?> ifc : type.getInterfaces()) {
        //         serializer = this.serializers.get(ifc);
        //
        //         if (serializer != null) {
        //             break;
        //         }
        //     }
        // }

        if (serializer == null) {
            throw new IllegalArgumentException("no serializer found for type: " + type.getName());
        }

        return (Serializer<S>) serializer;
    }

    public <T> T read(final DataReader reader, final Class<T> type) {
        final Serializer<T> serializer = getSerializer(type);

        return serializer.read(this, reader);
    }

    @Override
    public <T> void register(final Class<T> type, final Serializer<T> serializer) {
        this.serializers.put(type, serializer);
    }

    public <T> void write(final DataWriter writer, final T value) {
        final Class<T> type = (Class<T>) value.getClass();

        write(writer, value, type);
    }

    public <T> void write(final DataWriter writer, final T value, final Class<T> type) {
        final Serializer<T> serializer = getSerializer(type);

        serializer.write(this, writer, value);
    }
}
