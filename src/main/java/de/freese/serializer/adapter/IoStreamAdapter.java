// Created: 30.09.22
package de.freese.serializer.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.util.Objects;

/**
 * @author Thomas Freese
 */
public class IoStreamAdapter implements DataReader, DataWriter {
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public IoStreamAdapter(final InputStream inputStream) {
        this(inputStream, OutputStream.nullOutputStream());
    }

    public IoStreamAdapter(final OutputStream outputStream) {
        this(InputStream.nullInputStream(), outputStream);
    }

    public IoStreamAdapter(final InputStream inputStream, final OutputStream outputStream) {
        super();

        this.inputStream = Objects.requireNonNull(inputStream, "inputStream required");
        this.outputStream = Objects.requireNonNull(outputStream, "outputStream required");
    }

    @Override
    public byte readByte() {
        try {
            return (byte) inputStream.read();
        }
        catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public byte[] readBytes(final int length) {
        try {
            return inputStream.readNBytes(length);
        }
        catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public void writeByte(final byte value) {
        try {
            outputStream.write(value);
        }
        catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

    @Override
    public void writeBytes(final byte[] bytes) {
        try {
            outputStream.write(bytes);
        }
        catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
