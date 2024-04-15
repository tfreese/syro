// Created: 22.09.2020
package de.freese.syro.io;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author Thomas Freese
 */
public class ByteBufferReader implements DataReader {
    private final ByteBuffer byteBuffer;

    public ByteBufferReader(final ByteBuffer byteBuffer) {
        super();

        this.byteBuffer = Objects.requireNonNull(byteBuffer, "byteBuffer required");
    }

    @Override
    public Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    @Override
    public byte readByte() {
        return byteBuffer.get();
    }

    @Override
    public byte[] readBytes(final int length) {
        final byte[] bytes = new byte[length];
        byteBuffer.get(bytes);

        return bytes;
    }

    @Override
    public double readDouble() {
        return byteBuffer.getDouble();
    }

    @Override
    public float readFloat() {
        return byteBuffer.getFloat();
    }

    @Override
    public int readInteger() {
        return byteBuffer.getInt();
    }

    @Override
    public long readLong() {
        return byteBuffer.getLong();
    }
}
