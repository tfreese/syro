// Created: 22.09.2020
package de.freese.syro.io;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Thomas Freese
 */
public interface DataReader {
    default boolean readBoolean() {
        return readByte() == 1;
    }

    @SuppressWarnings("java:S2447")
    default Boolean readBooleanOrNull() {
        if (readByte() == 0) {
            return null;
        }

        return readBoolean();
    }

    byte readByte();

    byte[] readBytes(int length);

    default double readDouble() {
        final long longValue = readLong();

        return Double.longBitsToDouble(longValue);
    }

    default Double readDoubleOrNull() {
        if (readByte() == 0) {
            return null;
        }

        return readDouble();
    }

    default float readFloat() {
        final int intValue = readInteger();

        return Float.intBitsToFloat(intValue);
    }

    default Float readFloatOrNull() {
        if (readByte() == 0) {
            return null;
        }

        return readFloat();
    }

    default int readInteger() {
        final byte[] bytes = readBytes(4);

        return ((bytes[0] & 0xFF) << 24)
                + ((bytes[1] & 0xFF) << 16)
                + ((bytes[2] & 0xFF) << 8)
                + (bytes[3] & 0xFF);
    }

    default Integer readIntegerOrNull() {
        if (readByte() == 0) {
            return null;
        }

        return readInteger();
    }

    default long readLong() {
        final byte[] bytes = readBytes(8);

        return ((long) (bytes[0] & 0xFF) << 56)
                + ((long) (bytes[1] & 0xFF) << 48)
                + ((long) (bytes[2] & 0xFF) << 40)
                + ((long) (bytes[3] & 0xFF) << 32)
                + ((long) (bytes[4] & 0xFF) << 24)
                + ((long) (bytes[5] & 0xFF) << 16)
                + ((long) (bytes[6] & 0xFF) << 8)
                + ((long) bytes[7] & 0xFF);
    }

    default Long readLongOrNull() {
        if (readByte() == 0) {
            return null;
        }

        return readLong();
    }

    default String readString() {
        return readString(StandardCharsets.UTF_8);
    }

    default String readString(final Charset charset) {
        final int length = readInteger();

        if (length == -1) {
            return null;
        }
        else if (length == 0) {
            return "";
        }

        final byte[] bytes = readBytes(length);

        return new String(bytes, charset);
    }
}
