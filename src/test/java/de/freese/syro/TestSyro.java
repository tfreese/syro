package de.freese.syro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
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
import de.freese.syro.serializer.BooleanSerializer;
import de.freese.syro.serializer.DoubleSerializer;
import de.freese.syro.serializer.ExceptionSerializer;
import de.freese.syro.serializer.FloatSerializer;
import de.freese.syro.serializer.IntegerSerializer;
import de.freese.syro.serializer.LongSerializer;
import de.freese.syro.serializer.Serializer;
import de.freese.syro.serializer.StringSerializer;

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

    // @TempDir(cleanup = CleanupMode.ALWAYS)
    // private static Path pathTest;

    static Stream<Arguments> createArguments() {
        return Stream.of(
                Arguments.of("ByteBuf", DATA_HOLDER_BYTE_BUF),
                Arguments.of("ByteBuffer", DATA_HOLDER_BYTE_BUFFER),
                Arguments.of("InputOutputStream", DATA_HOLDER_OUTPUT_INPUT_STREAM)
        );
    }

    @ParameterizedTest(name = "{index} -> {0}")
    @MethodSource("createArguments")
    void testBoolean(final String name, final DataHolder dataHolder) {
        final Serializer<Boolean> serializer = BooleanSerializer.getInstance();

        final DataWriter writer = dataHolder.createWriter();
        serializer.write(writer, null);
        serializer.write(writer, true);
        serializer.write(writer, false);

        final DataReader reader = dataHolder.createReader();
        assertNull(serializer.read(reader));
        assertTrue(serializer.read(reader));
        assertFalse(serializer.read(reader));
    }

    @ParameterizedTest(name = "{index} -> {0}")
    @MethodSource("createArguments")
    void testDouble(final String name, final DataHolder dataHolder) {
        final Serializer<Double> serializer = DoubleSerializer.getInstance();

        final DataWriter writer = dataHolder.createWriter();
        serializer.write(writer, null);
        serializer.write(writer, 1D);
        serializer.write(writer, Double.valueOf(2D));

        final DataReader reader = dataHolder.createReader();
        assertNull(serializer.read(reader));
        assertEquals(1D, serializer.read(reader));
        assertEquals(Double.valueOf(2D), serializer.read(reader));
    }

    @ParameterizedTest(name = "{index} -> {0}")
    @MethodSource("createArguments")
    void testException(final String name, final DataHolder dataHolder) {
        final Serializer<Exception> serializer = ExceptionSerializer.getInstance();

        final IOException exception = new IOException("Test");
        final StackTraceElement[] stackTraceOrigin = exception.getStackTrace();

        final DataWriter writer = dataHolder.createWriter();
        serializer.write(writer, exception);

        final DataReader reader = dataHolder.createReader();
        final Exception other = serializer.read(reader);
        final StackTraceElement[] stackTraceOther = other.getStackTrace();

        assertEquals(exception.getClass(), other.getClass());
        assertEquals(exception.getMessage(), other.getMessage());
        assertEquals(stackTraceOrigin.length, stackTraceOther.length);

        for (int i = 0; i < stackTraceOrigin.length; i++) {
            final StackTraceElement stackTraceElementOrigin = stackTraceOrigin[i];
            final StackTraceElement stackTraceElementOther = stackTraceOther[i];
            assertEquals(stackTraceElementOrigin, stackTraceElementOther);
        }
    }

    @ParameterizedTest(name = "{index} -> {0}")
    @MethodSource("createArguments")
    void testFloat(final String name, final DataHolder dataHolder) {
        final Serializer<Float> serializer = FloatSerializer.getInstance();

        final DataWriter writer = dataHolder.createWriter();
        serializer.write(writer, null);
        serializer.write(writer, 1F);
        serializer.write(writer, Float.valueOf(2F));

        final DataReader reader = dataHolder.createReader();
        assertNull(serializer.read(reader));
        assertEquals(1F, serializer.read(reader));
        assertEquals(Float.valueOf(2F), serializer.read(reader));
    }

    @ParameterizedTest(name = "{index} -> {0}")
    @MethodSource("createArguments")
    void testInteger(final String name, final DataHolder dataHolder) {
        final Serializer<Integer> serializer = IntegerSerializer.getInstance();

        final DataWriter writer = dataHolder.createWriter();
        serializer.write(writer, null);
        serializer.write(writer, 1);
        serializer.write(writer, Integer.valueOf(2));

        final DataReader reader = dataHolder.createReader();
        assertNull(serializer.read(reader));
        assertEquals(1, serializer.read(reader));
        assertEquals(Integer.valueOf(2), serializer.read(reader));
    }

    @ParameterizedTest(name = "{index} -> {0}")
    @MethodSource("createArguments")
    void testLong(final String name, final DataHolder dataHolder) {
        final Serializer<Long> serializer = LongSerializer.getInstance();

        final DataWriter writer = dataHolder.createWriter();
        serializer.write(writer, null);
        serializer.write(writer, 1L);
        serializer.write(writer, Long.valueOf(2L));

        final DataReader reader = dataHolder.createReader();
        assertNull(serializer.read(reader));
        assertEquals(1L, serializer.read(reader));
        assertEquals(Long.valueOf(2L), serializer.read(reader));
    }

    @ParameterizedTest(name = "{index} -> {0}")
    @MethodSource("createArguments")
    void testPoint(final String name, final DataHolder dataHolder) {
        final Serializer<Point> serializer = new Serializer<Point>() {
            @Override
            public Point read(final DataReader reader) {
                final int x = reader.readInteger();
                final int y = reader.readInteger();

                return new Point(x, y);
            }

            @Override
            public void write(final DataWriter writer, final Point value) {
                writer.writeInteger(value.x);
                writer.writeInteger(value.y);
            }
        };

        final Point point = new Point(1, 2);

        final DataWriter writer = dataHolder.createWriter();
        serializer.write(writer, point);

        final DataReader reader = dataHolder.createReader();
        assertEquals(point, serializer.read(reader));
    }

    @ParameterizedTest(name = "{index} -> {0}")
    @MethodSource("createArguments")
    void testString(final String name, final DataHolder dataHolder) {
        final Serializer<String> serializer = StringSerializer.getInstance();

        final String text = "abcABC123,.;:-_ÖÄÜöäü*'#+`?ß´987/()=?";

        final DataWriter writer = dataHolder.createWriter();
        serializer.write(writer, null);
        serializer.write(writer, "");
        serializer.write(writer, text);

        final DataReader reader = dataHolder.createReader();
        assertNull(serializer.read(reader));
        assertEquals("", serializer.read(reader));
        assertEquals(text, serializer.read(reader));
    }
}
