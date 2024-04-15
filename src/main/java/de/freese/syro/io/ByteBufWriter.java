// Created: 04.10.2020
package de.freese.syro.io;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import io.netty.buffer.ByteBuf;

/**
 * @author Thomas Freese
 */
public class ByteBufWriter implements DataWriter {
    private final ByteBuf byteBuf;

    public ByteBufWriter(final ByteBuf byteBuf) {
        super();

        this.byteBuf = Objects.requireNonNull(byteBuf, "byteBuf required");
    }

    @Override
    public Charset getCharset() {
        return StandardCharsets.UTF_8;
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
