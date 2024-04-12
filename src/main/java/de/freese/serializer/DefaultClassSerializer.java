package de.freese.serializer;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.function.Function;

import io.netty.buffer.ByteBuf;

import de.freese.serializer.adapter.ByteBufAdapter;
import de.freese.serializer.adapter.ByteBufferAdapter;
import de.freese.serializer.adapter.DataReader;
import de.freese.serializer.adapter.DataWriter;
import de.freese.serializer.adapter.IoStreamAdapter;

public final class DefaultClassSerializer<R, W> extends AbstractClassSerializer<R, W> {

    public static ClassSerializer<ByteBuf, ByteBuf> ofByteBuf() {
        return new DefaultClassSerializer<>(ByteBufAdapter::new, ByteBufAdapter::new);
    }

    public static ClassSerializer<ByteBuffer, ByteBuffer> ofByteBuffer() {
        return new DefaultClassSerializer<>(ByteBufferAdapter::new, ByteBufferAdapter::new);
    }

    public static ClassSerializer<InputStream, OutputStream> ofIoStream() {
        return new DefaultClassSerializer<>(IoStreamAdapter::new, IoStreamAdapter::new);
    }

    private final Function<R, DataReader> dataReaderWrapper;
    private final Function<W, DataWriter> dataWriterWrapper;

    private DefaultClassSerializer(final Function<R, DataReader> dataReaderWrapper, final Function<W, DataWriter> dataWriterWrapper) {
        super();

        this.dataReaderWrapper = dataReaderWrapper;
        this.dataWriterWrapper = dataWriterWrapper;

        registerDefaultSerializer();
    }

    @Override
    protected DataReader wrapToReader(final R source) {
        return dataReaderWrapper.apply(source);
    }

    @Override
    protected DataWriter wrapToWriter(final W sink) {
        return dataWriterWrapper.apply(sink);
    }
}
