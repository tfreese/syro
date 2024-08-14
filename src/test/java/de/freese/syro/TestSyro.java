package de.freese.syro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.stream.Stream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.freese.syro.io.ByteBufReader;
import de.freese.syro.io.ByteBufWriter;
import de.freese.syro.io.ByteBufferReader;
import de.freese.syro.io.ByteBufferWriter;
import de.freese.syro.io.DataReader;
import de.freese.syro.io.DataWriter;
import de.freese.syro.io.InputStreamReader;
import de.freese.syro.io.OutputStreamWriter;
import de.freese.syro.serializer.ReflectionSerializer;

@SuppressWarnings("ALL")
class TestSyro {
    private static final int BUFFER_SIZE = 1024 * 18;

    private static final DataHolder DATA_HOLDER_BYTE_BUF = new DataHolder() {
        private final ByteBuf buffer = UnpooledByteBufAllocator.DEFAULT.buffer(BUFFER_SIZE);

        @Override
        public DataReader createReader() {
            return new ByteBufReader(buffer);
        }

        @Override
        public DataWriter createWriter() {
            buffer.clear();

            return new ByteBufWriter(buffer);
        }
    };

    private static final DataHolder DATA_HOLDER_BYTE_BUFFER = new DataHolder() {
        private final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

        @Override
        public DataReader createReader() {
            buffer.flip();

            return new ByteBufferReader(buffer);
        }

        @Override
        public DataWriter createWriter() {
            buffer.clear();

            return new ByteBufferWriter(buffer);
        }
    };

    private static final DataHolder DATA_HOLDER_OUTPUT_INPUT_STREAM = new DataHolder() {
        private ByteArrayOutputStream outputStream;

        @Override
        public DataReader createReader() {
            return new InputStreamReader(new ByteArrayInputStream(outputStream.toByteArray()));
        }

        @Override
        public DataWriter createWriter() {
            outputStream = new ByteArrayOutputStream(BUFFER_SIZE);

            return new OutputStreamWriter(outputStream);
        }
    };

    private interface DataHolder {
        DataReader createReader();

        DataWriter createWriter();
    }

    static Stream<Arguments> createArguments() {
        return Stream.of(
                Arguments.of("ByteBuf", new Syro(), DATA_HOLDER_BYTE_BUF),
                Arguments.of("ByteBuffer", new Syro(), DATA_HOLDER_BYTE_BUFFER),
                Arguments.of("InputOutputStream", new Syro(), DATA_HOLDER_OUTPUT_INPUT_STREAM)
        );
    }

    @ParameterizedTest(name = "{index} -> {0}")
    @MethodSource("createArguments")
    void testBoolean(final String name, final Syro syro, final DataHolder dataHolder) {
        final DataWriter writer = dataHolder.createWriter();
        syro.write(writer, null, Boolean.class);
        syro.write(writer, true);
        syro.write(writer, false);

        final DataReader reader = dataHolder.createReader();
        assertNull(syro.read(reader, Boolean.class));
        assertTrue(syro.read(reader, boolean.class));
        assertFalse(syro.read(reader, boolean.class));
    }

    @ParameterizedTest(name = "{index} -> {0}")
    @MethodSource("createArguments")
    void testDouble(final String name, final Syro syro, final DataHolder dataHolder) {
        final DataWriter writer = dataHolder.createWriter();
        syro.write(writer, null, Double.class);
        syro.write(writer, 1D);
        syro.write(writer, Double.valueOf(2D));

        final DataReader reader = dataHolder.createReader();
        assertNull(syro.read(reader, Double.class));
        assertEquals(1D, syro.read(reader, double.class));
        assertEquals(Double.valueOf(2D), syro.read(reader, Double.class));
    }

    @ParameterizedTest(name = "{index} -> {0}")
    @MethodSource("createArguments")
    void testException(final String name, final Syro syro, final DataHolder dataHolder) {
        final IOException origin = new IOException("Test");
        final StackTraceElement[] stackTraceOrigin = origin.getStackTrace();

        final DataWriter writer = dataHolder.createWriter();

        final Exception exception = assertThrows(IllegalArgumentException.class, () -> syro.write(writer, origin));
        assertNotNull(exception);
        assertEquals("no serializer found for type: java.io.IOException", exception.getMessage());

        syro.write(writer, origin, Exception.class);

        final DataReader reader = dataHolder.createReader();
        final Exception other = syro.read(reader, Exception.class);
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

    @ParameterizedTest(name = "{index} -> {0}")
    @MethodSource("createArguments")
    void testFloat(final String name, final Syro syro, final DataHolder dataHolder) {
        final DataWriter writer = dataHolder.createWriter();
        syro.write(writer, null, Float.class);
        syro.write(writer, 1F);
        syro.write(writer, Float.valueOf(2F));

        final DataReader reader = dataHolder.createReader();
        assertNull(syro.read(reader, Float.class));
        assertEquals(1F, syro.read(reader, float.class));
        assertEquals(Float.valueOf(2F), syro.read(reader, Float.class));
    }

    @ParameterizedTest(name = "{index} -> {0}")
    @MethodSource("createArguments")
    void testInteger(final String name, final Syro syro, final DataHolder dataHolder) {
        final DataWriter writer = dataHolder.createWriter();
        syro.write(writer, null, Integer.class);
        syro.write(writer, 1);
        syro.write(writer, Integer.valueOf(2));

        final DataReader reader = dataHolder.createReader();
        assertNull(syro.read(reader, Integer.class));
        assertEquals(1, syro.read(reader, int.class));
        assertEquals(Integer.valueOf(2), syro.read(reader, Integer.class));
    }

    @ParameterizedTest(name = "{index} -> {0}")
    @MethodSource("createArguments")
    void testLong(final String name, final Syro syro, final DataHolder dataHolder) {
        final DataWriter writer = dataHolder.createWriter();
        syro.write(writer, null, Long.class);
        syro.write(writer, 1L);
        syro.write(writer, Long.valueOf(2L));

        final DataReader reader = dataHolder.createReader();
        assertNull(syro.read(reader, Long.class));
        assertEquals(1L, syro.read(reader, long.class));
        assertEquals(Long.valueOf(2L), syro.read(reader, Long.class));
    }

    @ParameterizedTest(name = "{index} -> {0}")
    @MethodSource("createArguments")
    void testNotExist(final String name, final Syro syro, final DataHolder dataHolder) {
        final LocalDateTime now = LocalDateTime.now();

        final DataWriter writer = dataHolder.createWriter();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> syro.write(writer, now));
        assertNotNull(exception);
        assertEquals("no serializer found for type: java.time.LocalDateTime", exception.getMessage());

        final DataReader reader = dataHolder.createReader();
        exception = assertThrows(IllegalArgumentException.class, () -> syro.read(reader, LocalDateTime.class));
        assertNotNull(exception);
        assertEquals("no serializer found for type: java.time.LocalDateTime", exception.getMessage());
    }

    @ParameterizedTest(name = "{index} -> {0}")
    @MethodSource("createArguments")
    void testReflection(final String name, final Syro syro, final DataHolder dataHolder) {
        syro.register(Point.class, new ReflectionSerializer<>());

        final Point point = new Point(1, 2);

        final DataWriter writer = dataHolder.createWriter();
        syro.write(writer, point);

        final DataReader reader = dataHolder.createReader();
        assertEquals(point, syro.read(reader, Point.class));
    }

    @ParameterizedTest(name = "{index} -> {0}")
    @MethodSource("createArguments")
    void testString(final String name, final Syro syro, final DataHolder dataHolder) {
        final String text = "abcABC123,.;:-_ÖÄÜöäü*'#+`?ß´987/()=?";

        final DataWriter writer = dataHolder.createWriter();
        syro.write(writer, null, String.class);
        syro.write(writer, "");
        syro.write(writer, text);

        final DataReader reader = dataHolder.createReader();
        assertNull(syro.read(reader, String.class));
        assertEquals("", syro.read(reader, String.class));
        assertEquals(text, syro.read(reader, String.class));
    }
}
