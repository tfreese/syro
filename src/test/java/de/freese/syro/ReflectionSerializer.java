package de.freese.syro;

import de.freese.syro.io.DataReader;
import de.freese.syro.io.DataWriter;
import de.freese.syro.serializer.Serializer;

public final class ReflectionSerializer<T> implements Serializer<T> {
    @Override
    public T read(final DataReader reader) {
        return null;
        
        // // A look-up that can find public constructors/methods.
        // final MethodHandles.Lookup lookup = MethodHandles.publicLookup();
        //
        // try {
        //     final String clazzName = reader.readString();
        //     // final Class<?> clazz =  Class.forName(clazzName);
        //     final Class<?> clazz = lookup.findClass(clazzName);
        //     final Constructor<?> constructor = clazz.getDeclaredConstructor();
        //
        //     final Object value = constructor.newInstance();
        //
        //     final int fieldLength = reader.readInteger();
        //
        //     for (int i = 0; i < fieldLength; i++) {
        //         final byte nullFlag = reader.readByte();
        //
        //         if (nullFlag == -1) {
        //             continue;
        //         }
        //
        //         final String fieldName = reader.readString();
        //
        //         final Field field = clazz.getDeclaredField(fieldName);
        //         final Serializer<Object> fieldSerializer = (Serializer<Object>) registry.getSerializer(field.getType());
        //         final Object fieldValue = fieldSerializer.read(reader);
        //
        //         field.set(value, fieldValue);
        //
        //         // final VarHandle varHandle = lookup.findVarHandle(clazz, fieldName, field.getType());
        //         // varHandle.set(field.getType().cast(fieldValue)); // java.lang.ClassCastException: Cannot cast java.lang.Integer to int
        //     }
        //
        //     return (T) value;
        // }
        // catch (RuntimeException ex) {
        //     throw ex;
        // }
        // catch (Exception ex) {
        //     throw new IllegalStateException(ex);
        // }
    }

    @Override
    public void write(final DataWriter writer, final T value) {
        // try {
        //     final Class<?> type = value.getClass();
        //     writer.writeString(type.getName());
        //
        //     final List<Field> fields = Stream.of(type.getDeclaredFields()).filter(field -> !Modifier.isStatic(field.getModifiers())).toList();
        //     writer.writeInteger(fields.size());
        //
        //     for (Field field : fields) {
        //         final Object fieldValue = field.get(value);
        //
        //         if (fieldValue == null) {
        //             // Null-Flag
        //             writer.writeByte((byte) -1);
        //             continue;
        //         }
        //
        //         writer.writeByte((byte) 1);
        //
        //         writer.writeString(field.getName());
        //
        //         final Serializer<Object> serializer = (Serializer<Object>) registry.getSerializer(fieldValue.getClass());
        //         serializer.write(writer, fieldValue);
        //     }
        // }
        // catch (RuntimeException ex) {
        //     throw ex;
        // }
        // catch (Exception ex) {
        //     throw new IllegalStateException(ex);
        // }
    }
}
