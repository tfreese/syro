// Created: 30.09.22
package de.freese.syro.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author Thomas Freese
 */
public class OutputStreamWriter implements DataWriter {
    private final OutputStream outputStream;

    public OutputStreamWriter(final OutputStream outputStream) {
        super();

        this.outputStream = Objects.requireNonNull(outputStream, "outputStream required");
    }

    @Override
    public Charset getCharset() {
        return StandardCharsets.UTF_8;
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
