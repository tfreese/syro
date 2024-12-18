// Created: 04.10.2020
package de.freese.syro.io;

import java.util.Objects;

import io.netty.buffer.ByteBuf;

/**
 * @author Thomas Freese
 */
public class ByteBufReader implements DataReader {
    private final ByteBuf byteBuf;

    public ByteBufReader(final ByteBuf byteBuf) {
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
}
