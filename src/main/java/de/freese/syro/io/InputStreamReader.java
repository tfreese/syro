// Created: 30.09.22
package de.freese.syro.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author Thomas Freese
 */
public class InputStreamReader implements DataReader {
    private final InputStream inputStream;

    public InputStreamReader(final InputStream inputStream) {
        super();

        this.inputStream = Objects.requireNonNull(inputStream, "inputStream required");
    }

    @Override
    public Charset getCharset() {
        return StandardCharsets.UTF_8;
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
}
