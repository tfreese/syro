package de.freese.syro.serializer;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import de.freese.syro.SerializerRegistry;
import de.freese.syro.io.DataReader;
import de.freese.syro.io.DataWriter;

public class ExceptionSerializer implements Serializer<Exception> {
    @Override
    public Exception read(final SerializerRegistry registry, final DataReader reader) {
        final String clazzName = reader.readString();
        final String message = reader.readString();
        final int stackTraceLength = reader.readInteger();

        final StackTraceElement[] stackTrace = new StackTraceElement[stackTraceLength];

        final Serializer<StackTraceElement> stackTraceElementSerializer = registry.getSerializer(StackTraceElement.class);
        
        for (int i = 0; i < stackTrace.length; i++) {
            stackTrace[i] = stackTraceElementSerializer.read(registry, reader);
        }

        Exception exception = null;

        try {
            // A look-up that can find public constructors/methods.
            final MethodHandles.Lookup lookup = MethodHandles.publicLookup();

            // final Constructor<?> constructor = clazz.getDeclaredConstructor(String.class);
            // exception = constructor.newInstance(message);
            // final Class<? extends Exception> clazz = (Class<? extends Exception>) Class.forName(clazzName);
            final Class<?> clazz = lookup.findClass(clazzName);

            // Search for method that: have return type of void (Constructor) and accept a String parameter.
            final MethodType methodType = MethodType.methodType(void.class, String.class);

            // Find the constructor based on the MethodType defined above.
            final MethodHandle invokableClassConstructor = lookup.findConstructor(clazz, methodType);

            // Create an instance of the Invokable class by calling the exact handle, pass in the param value.
            exception = (Exception) invokableClassConstructor.invokeWithArguments(message);
        }
        catch (Throwable ex) {
            exception = new Exception(message);
        }

        exception.setStackTrace(stackTrace);

        return exception;
    }

    @Override
    public void write(final SerializerRegistry registry, final DataWriter writer, final Exception value) {
        writer.writeString(value.getClass().getName());
        writer.writeString(value.getMessage());

        final StackTraceElement[] stackTrace = value.getStackTrace();
        writer.writeInteger(stackTrace.length);

        final Serializer<StackTraceElement> stackTraceElementSerializer = registry.getSerializer(StackTraceElement.class);

        for (StackTraceElement stackTraceElement : stackTrace) {
            stackTraceElementSerializer.write(registry, writer, stackTraceElement);
        }
    }
}
