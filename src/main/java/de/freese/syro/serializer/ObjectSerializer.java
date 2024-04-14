package de.freese.syro.serializer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Stream;

import de.freese.syro.SerializerRegistry;
import de.freese.syro.adapter.DataReader;
import de.freese.syro.adapter.DataWriter;

public final class ObjectSerializer implements Serializer<Object> {
    private static final class ObjectSerializerHolder {
        private static final ObjectSerializer INSTANCE = new ObjectSerializer();

        private ObjectSerializerHolder() {
            super();
        }
    }

    public static ObjectSerializer getInstance() {
        return ObjectSerializerHolder.INSTANCE;
    }

    private ObjectSerializer() {
        super();
    }

    @Override
    public Object read(final SerializerRegistry registry, final DataReader reader, final Class<Object> type) {
        try {
            final String clazzName = reader.readString();
            final Class<? extends Exception> clazz = (Class<? extends Exception>) Class.forName(clazzName);
            final Constructor<? extends Exception> constructor = clazz.getDeclaredConstructor();

            final Object value = constructor.newInstance();

            final int fieldLength = reader.readInteger();

            for (int i = 0; i < fieldLength; i++) {
                final byte nullFlag = reader.readByte();

                if (nullFlag == -1) {
                    continue;
                }

                final String fieldName = reader.readString();

                final Field field = clazz.getDeclaredField(fieldName);
                final Serializer<Object> fieldSerializer = (Serializer<Object>) registry.getSerializer(field.getType());

                field.set(value, fieldSerializer.read(registry, reader, (Class<Object>) field.getType()));
            }

            return value;
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public void write(final SerializerRegistry registry, final DataWriter writer, final Object value) {
        try {
            final Class<?> type = value.getClass();
            writer.writeString(type.getName());

            final List<Field> fields = Stream.of(type.getDeclaredFields()).filter(field -> !Modifier.isStatic(field.getModifiers())).toList();
            writer.writeInteger(fields.size());

            for (Field field : fields) {
                final Object fieldValue = field.get(value);

                if (fieldValue == null) {
                    // Null-Flag
                    writer.writeByte((byte) -1);
                    continue;
                }

                writer.writeByte((byte) 1);

                writer.writeString(field.getName());

                final Serializer<Object> serializer = (Serializer<Object>) registry.getSerializer(fieldValue.getClass());

                serializer.write(registry, writer, fieldValue);
            }
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
}
