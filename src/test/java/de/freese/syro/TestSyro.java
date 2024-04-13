package de.freese.syro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.Test;

class TestSyro {
    private static final int BUFFER_SIZE = 1024;

    @Test
    void testByteBuf() {
        final Syro<ByteBuf, ByteBuf> syro = Syro.ofByteBuf();
        final ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(BUFFER_SIZE);

        final Consumer<Object> writeConsumer = value -> {
            buffer.clear();
            syro.write(buffer, value);
        };
        final Consumer<Class<?>> writeNullConsumer = type -> {
            buffer.clear();
            syro.writeNull(buffer, type);
        };
        final Function<Class<?>, Object> readerFunction = type -> syro.read(buffer, type);

        test(writeConsumer, writeNullConsumer, readerFunction);
    }

    @Test
    void testByteBuffer() {
        final Syro<ByteBuffer, ByteBuffer> syro = Syro.ofByteBuffer();
        final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

        final Consumer<Object> writeConsumer = value -> {
            buffer.clear();
            syro.write(buffer, value);
        };
        final Consumer<Class<?>> writeNullConsumer = type -> {
            buffer.clear();
            syro.writeNull(buffer, type);
        };
        final Function<Class<?>, Object> readerFunction = type -> {
            buffer.flip();
            return syro.read(buffer, type);
        };

        test(writeConsumer, writeNullConsumer, readerFunction);
    }

    @Test
    void testIoStream() {
        final Syro<InputStream, OutputStream> syro = Syro.ofIoStream();
        final AtomicReference<byte[]> reference = new AtomicReference<>(null);

        final Consumer<Object> writeConsumer = value -> {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            syro.write(outputStream, value);
            reference.set(outputStream.toByteArray());
        };
        final Consumer<Class<?>> writeNullConsumer = type -> {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            syro.writeNull(outputStream, type);
            reference.set(outputStream.toByteArray());
        };
        final Function<Class<?>, Object> readerFunction = type -> syro.read(new ByteArrayInputStream(reference.get()), type);

        test(writeConsumer, writeNullConsumer, readerFunction);
    }

    private void test(final Consumer<Object> writeConsumer, final Consumer<Class<?>> writeNullConsumer, final Function<Class<?>, Object> readerFunction) {
        testString(writeConsumer, writeNullConsumer, readerFunction);
        testBoolean(writeConsumer, writeNullConsumer, readerFunction);
    }

    private void testBoolean(final Consumer<Object> writeConsumer, final Consumer<Class<?>> writeNullConsumer, final Function<Class<?>, Object> readerFunction) {
        writeConsumer.accept(true);
        assertEquals(true, readerFunction.apply(Boolean.class));

        writeConsumer.accept(Boolean.FALSE);
        assertEquals(Boolean.FALSE, readerFunction.apply(Boolean.class));

        writeNullConsumer.accept(Boolean.class);
        assertNull(readerFunction.apply(Boolean.class));
    }

    private void testString(final Consumer<Object> writeConsumer, final Consumer<Class<?>> writeNullConsumer, final Function<Class<?>, Object> readerFunction) {
        final String text = "abcABC123,.;:-_ÖÄÜöäü*'#+`?ß´987/()=?";
        writeConsumer.accept(text);
        assertEquals(text, readerFunction.apply(String.class));

        writeConsumer.accept("");
        assertEquals("", readerFunction.apply(String.class));

        writeNullConsumer.accept(String.class);
        assertNull(readerFunction.apply(String.class));
    }
}
