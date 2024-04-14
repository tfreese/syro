package de.freese.syro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.Test;

import de.freese.syro.serializer.ObjectSerializer;

@SuppressWarnings("ALL")
class TestSyro {
    private static final int BUFFER_SIZE = 12 * 1024;

    @Test
    void testByteBuf() {
        final Syro<ByteBuf, ByteBuf> syro = Syro.ofByteBuf();
        syro.register(Object.class, ObjectSerializer.getInstance());

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
        syro.register(Object.class, ObjectSerializer.getInstance());

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
        syro.register(Object.class, ObjectSerializer.getInstance());

        final AtomicReference<byte[]> reference = new AtomicReference<>(null);

        final Consumer<Object> writeConsumer = value -> {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(BUFFER_SIZE);
            syro.write(outputStream, value);
            reference.set(outputStream.toByteArray());
        };
        final Consumer<Class<?>> writeNullConsumer = type -> {
            final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(BUFFER_SIZE);
            syro.writeNull(outputStream, type);
            reference.set(outputStream.toByteArray());
        };
        final Function<Class<?>, Object> readerFunction = type -> syro.read(new ByteArrayInputStream(reference.get()), type);

        test(writeConsumer, writeNullConsumer, readerFunction);
    }

    private void test(final Consumer<Object> writeConsumer, final Consumer<Class<?>> writeNullConsumer, final Function<Class<?>, Object> readerFunction) {
        testString(writeConsumer, writeNullConsumer, readerFunction);
        testBoolean(writeConsumer, writeNullConsumer, readerFunction);
        testInteger(writeConsumer, writeNullConsumer, readerFunction);
        testLong(writeConsumer, writeNullConsumer, readerFunction);
        testFloat(writeConsumer, writeNullConsumer, readerFunction);
        testDouble(writeConsumer, writeNullConsumer, readerFunction);

        testException(writeConsumer, writeNullConsumer, readerFunction);
        testObject(writeConsumer, writeNullConsumer, readerFunction);
    }

    private void testBoolean(final Consumer<Object> writeConsumer, final Consumer<Class<?>> writeNullConsumer, final Function<Class<?>, Object> readerFunction) {
        writeConsumer.accept(true);
        assertEquals(true, readerFunction.apply(Boolean.class));

        writeConsumer.accept(Boolean.FALSE);
        assertEquals(Boolean.FALSE, readerFunction.apply(Boolean.class));

        writeNullConsumer.accept(Boolean.class);
        assertNull(readerFunction.apply(Boolean.class));
    }

    private void testDouble(final Consumer<Object> writeConsumer, final Consumer<Class<?>> writeNullConsumer, final Function<Class<?>, Object> readerFunction) {
        writeConsumer.accept(1D);
        assertEquals(1D, readerFunction.apply(Double.class));

        writeConsumer.accept(Double.valueOf(2D));
        assertEquals(Double.valueOf(2D), readerFunction.apply(Double.class));

        writeNullConsumer.accept(Double.class);
        assertNull(readerFunction.apply(Double.class));
    }

    private void testException(final Consumer<Object> writeConsumer, final Consumer<Class<?>> writeNullConsumer, final Function<Class<?>, Object> readerFunction) {
        final Exception origin = new IOException("Test");
        final StackTraceElement[] stackTraceOrigin = origin.getStackTrace();

        writeConsumer.accept(origin);

        final Exception other = (Exception) readerFunction.apply(Exception.class);
        final StackTraceElement[] stackTraceOther = other.getStackTrace();

        assertEquals(origin.getClass(), other.getClass());
        assertEquals(origin.getMessage(), other.getMessage());
        assertEquals(stackTraceOrigin.length, stackTraceOther.length);

        for (int i = 0; i < stackTraceOrigin.length; i++) {
            final StackTraceElement stackTraceElementOrigin = stackTraceOrigin[i];
            final StackTraceElement stackTraceElementOther = stackTraceOther[i];
            assertEquals(stackTraceElementOrigin, stackTraceElementOther);
        }
    }

    private void testFloat(final Consumer<Object> writeConsumer, final Consumer<Class<?>> writeNullConsumer, final Function<Class<?>, Object> readerFunction) {
        writeConsumer.accept(1F);
        assertEquals(1F, readerFunction.apply(Float.class));

        writeConsumer.accept(Float.valueOf(2F));
        assertEquals(Float.valueOf(2F), readerFunction.apply(Float.class));

        writeNullConsumer.accept(Float.class);
        assertNull(readerFunction.apply(Float.class));
    }

    private void testInteger(final Consumer<Object> writeConsumer, final Consumer<Class<?>> writeNullConsumer, final Function<Class<?>, Object> readerFunction) {
        writeConsumer.accept(1);
        assertEquals(1, readerFunction.apply(Integer.class));

        writeConsumer.accept(Integer.valueOf(2));
        assertEquals(Integer.valueOf(2), readerFunction.apply(Integer.class));

        writeNullConsumer.accept(Integer.class);
        assertNull(readerFunction.apply(Integer.class));
    }

    private void testLong(final Consumer<Object> writeConsumer, final Consumer<Class<?>> writeNullConsumer, final Function<Class<?>, Object> readerFunction) {
        writeConsumer.accept(1L);
        assertEquals(1L, readerFunction.apply(Long.class));

        writeConsumer.accept(Long.valueOf(2L));
        assertEquals(Long.valueOf(2L), readerFunction.apply(Long.class));

        writeNullConsumer.accept(Long.class);
        assertNull(readerFunction.apply(Long.class));
    }

    private void testObject(final Consumer<Object> writeConsumer, final Consumer<Class<?>> writeNullConsumer, final Function<Class<?>, Object> readerFunction) {
        final Point point = new Point(1, 2);
        writeConsumer.accept(point);
        assertEquals(point, readerFunction.apply(Point.class));
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
