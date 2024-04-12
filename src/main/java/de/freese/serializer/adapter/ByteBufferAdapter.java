// Created: 22.09.2020
package de.freese.serializer.adapter;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * @author Thomas Freese
 */
public class ByteBufferAdapter implements DataReader, DataWriter {
    private final ByteBuffer byteBuffer;

    public ByteBufferAdapter(final ByteBuffer byteBuffer) {
        super();

        this.byteBuffer = Objects.requireNonNull(byteBuffer, "byteBuffer required");
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
