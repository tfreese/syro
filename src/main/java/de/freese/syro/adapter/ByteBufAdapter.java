// Created: 04.10.2020
package de.freese.syro.adapter;

import java.util.Objects;

import io.netty.buffer.ByteBuf;

/**
 * @author Thomas Freese
 */
public class ByteBufAdapter implements DataReader, DataWriter {
    private final ByteBuf byteBuf;

    public ByteBufAdapter(final ByteBuf byteBuf) {
        super();

        this.byteBuf = Objects.requireNonNull(byteBuf, "byteBuf required");
    }

    @Override
    public byte readByte() {
        return byteBuf.readByte();
    }

    @Override
    public byte[] readBytes(final int length) {
        final byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        return bytes;
    }

    @Override
    public double readDouble() {
        return byteBuf.readDouble();
    }

    @Override
    public float readFloat() {
        return byteBuf.readFloat();
    }

    @Override
    public int readInteger() {
        return byteBuf.readInt();
    }

    @Override
    public long readLong() {
        return byteBuf.readLong();
    }

    @Override
    public void writeByte(final byte value) {
        byteBuf.writeByte(value);
    }

    @Override
    public void writeBytes(final byte[] bytes) {
        byteBuf.writeBytes(bytes);
    }

    @Override
    public void writeDouble(final double value) {
        byteBuf.writeDouble(value);
    }

    @Override
    public void writeFloat(final float value) {
        byteBuf.writeFloat(value);
    }

    @Override
    public void writeInteger(final int value) {
        byteBuf.writeInt(value);
    }

    @Override
    public void writeLong(final long value) {
        byteBuf.writeLong(value);
    }
}
