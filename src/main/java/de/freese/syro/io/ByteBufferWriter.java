// Created: 22.09.2020
package de.freese.syro.io;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author Thomas Freese
 */
public class ByteBufferWriter implements DataWriter {
    private final ByteBuffer byteBuffer;

    public ByteBufferWriter(final ByteBuffer byteBuffer) {
        super();

        this.byteBuffer = Objects.requireNonNull(byteBuffer, "byteBuffer required");
    }

    @Override
    public Charset getCharset() {
        return StandardCharsets.UTF_8;
    }

    @Override
    public void writeByte(final byte value) {
        byteBuffer.put(value);
    }

    @Override
    public void writeBytes(final byte[] bytes) {
        byteBuffer.put(bytes);
    }

    @Override
    public void writeDouble(final double value) {
        byteBuffer.putDouble(value);
    }

    @Override
    public void writeFloat(final float value) {
        byteBuffer.putFloat(value);
    }

    @Override
    public void writeInteger(final int value) {
        byteBuffer.putInt(value);
    }

    @Override
    public void writeLong(final long value) {
        byteBuffer.putLong(value);
    }
}
