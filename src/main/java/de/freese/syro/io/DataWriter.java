// Created: 22.09.2020
package de.freese.syro.io;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Thomas Freese
 */
public interface DataWriter {
    default void writeBoolean(final boolean value) {
        writeByte((byte) (value ? 1 : 0));
    }

    default void writeBooleanOrNull(final Boolean value) {
        if (value == null) {
            writeByte((byte) 0);
        }
        else {
            writeByte((byte) 1);
            writeBoolean(value);
        }
    }

    void writeByte(byte value);

    void writeBytes(byte[] bytes);

    default void writeDouble(final double value) {
        final long longValue = Double.doubleToRawLongBits(value);

        writeLong(longValue);
    }

    default void writeDoubleOrNull(final Double value) {
        if (value == null) {
            writeByte((byte) 0);
        }
        else {
            writeByte((byte) 1);
            writeDouble(value);
        }
    }

    default void writeFloat(final float value) {
        final int intValue = Float.floatToRawIntBits(value);

        writeInteger(intValue);
    }

    default void writeFloatOrNull(final Float value) {
        if (value == null) {
            writeByte((byte) 0);
        }
        else {
            writeByte((byte) 1);
            writeFloat(value);
        }
    }

    default void writeInteger(final int value) {
        final byte[] bytes = new byte[4];

        bytes[0] = (byte) (0xFF & (value >> 24));
        bytes[1] = (byte) (0xFF & (value >> 16));
        bytes[2] = (byte) (0xFF & (value >> 8));
        bytes[3] = (byte) (0xFF & value);

        writeBytes(bytes);
    }

    default void writeIntegerOrNull(final Integer value) {
        if (value == null) {
            writeByte((byte) 0);
        }
        else {
            writeByte((byte) 1);
            writeInteger(value);
        }
    }

    default void writeLong(final long value) {
        final byte[] bytes = new byte[8];

        bytes[0] = (byte) (0xFF & (value >> 56));
        bytes[1] = (byte) (0xFF & (value >> 48));
        bytes[2] = (byte) (0xFF & (value >> 40));
        bytes[3] = (byte) (0xFF & (value >> 32));
        bytes[4] = (byte) (0xFF & (value >> 24));
        bytes[5] = (byte) (0xFF & (value >> 16));
        bytes[6] = (byte) (0xFF & (value >> 8));
        bytes[7] = (byte) (0xFF & value);

        writeBytes(bytes);
    }

    default void writeLongOrNull(final Long value) {
        if (value == null) {
            writeByte((byte) 0);
        }
        else {
            writeByte((byte) 1);
            writeLong(value);
        }
    }

    default void writeString(final String value) {
        writeString(value, StandardCharsets.UTF_8);
    }

    default void writeString(final String value, final Charset charset) {
        if (value == null) {
            writeInteger(-1);
            return;
        }
        else if (value.isBlank()) {
            writeInteger(0);
            return;
        }

        final byte[] bytes = value.getBytes(charset);

        writeInteger(bytes.length);
        writeBytes(bytes);
    }
}
