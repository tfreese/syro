package de.freese.syro.serializer;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import de.freese.syro.SerializerRegistry;
import de.freese.syro.io.DataReader;
import de.freese.syro.io.DataWriter;

public class ExceptionSerializer implements Serializer<Exception> {
    @Override
    public Exception read(final SerializerRegistry registry, final DataReader reader, final Class<Exception> type) {
        final String clazzName = reader.readString();
        final String message = reader.readString();
        final int stackTraceLength = reader.readInteger();

        final Serializer<StackTraceElement> stackTraceElementSerializer = registry.getSerializer(StackTraceElement.class);
        final StackTraceElement[] stackTrace = new StackTraceElement[stackTraceLength];

        for (int i = 0; i < stackTrace.length; i++) {
            stackTrace[i] = stackTraceElementSerializer.read(registry, reader, StackTraceElement.class);
        }

        Exception exception = null;

        try {
            //            // final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            //            // final Class<? extends Exception> clazz = (Class<? extends Exception>) classLoader.loadClass(clazzName);
            //            // final Class<? extends Exception> clazz = (Class<? extends Exception>) Class.forName(clazzName, true, classLoader);
            final Class<? extends Exception> clazz = (Class<? extends Exception>) Class.forName(clazzName);
            //            final Constructor<? extends Exception> constructor = clazz.getDeclaredConstructor(String.class);
            //
            //            exception = constructor.newInstance(message);

            // A look-up that can find public methods.
            final MethodHandles.Lookup publicMethodHandlesLookup = MethodHandles.publicLookup();

            // Search for method that: have return type of void (Constructor) and accept a String parameter.
            final MethodType methodType = MethodType.methodType(void.class, String.class);

            // Find the constructor based on the MethodType defined above.
            final MethodHandle invokableClassConstructor = publicMethodHandlesLookup.findConstructor(clazz, methodType);

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

        final Serializer<StackTraceElement> stackTraceElementSerializer = registry.getSerializer(StackTraceElement.class);
        final StackTraceElement[] stackTrace = value.getStackTrace();
        writer.writeInteger(stackTrace.length);

        for (StackTraceElement stackTraceElement : stackTrace) {
            stackTraceElementSerializer.write(registry, writer, stackTraceElement);
        }
    }
}
