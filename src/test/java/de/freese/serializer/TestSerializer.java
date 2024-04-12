package de.freese.serializer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

class TestSerializer {
    private static final int BUFFER_SIZE = 1024;

    @Test
    void testBoolean() {
        final ClassSerializer<ByteBuffer, ByteBuffer> classSerializer = DefaultClassSerializer.ofByteBuffer();
        final ByteBuffer buffer = ByteBuffer.allocateDirect(BUFFER_SIZE);

        classSerializer.write(buffer, true);
        buffer.flip();
        assertTrue(classSerializer.read(buffer, Boolean.class));

        buffer.clear();
        classSerializer.write(buffer, Boolean.FALSE);
        buffer.flip();
        assertFalse(classSerializer.read(buffer, Boolean.class));

        buffer.clear();
        classSerializer.writeNull(buffer, Boolean.class);
        buffer.flip();
        assertNull(classSerializer.read(buffer, Boolean.class));
    }
}
