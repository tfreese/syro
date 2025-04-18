// Created: 24.09.2020
package de.freese.syro.serializer;

import de.freese.syro.io.DataReader;
import de.freese.syro.io.DataWriter;

/**
 * @author Thomas Freese
 */
public final class StackTraceElementSerializer implements Serializer<StackTraceElement> {
    private static final class StackTraceElementSerializerHolder {
        private static final StackTraceElementSerializer INSTANCE = new StackTraceElementSerializer();

        private StackTraceElementSerializerHolder() {
            super();
        }
    }

    public static StackTraceElementSerializer getInstance() {
        return StackTraceElementSerializerHolder.INSTANCE;
    }

    private StackTraceElementSerializer() {
        super();
    }

    @Override
    public StackTraceElement read(final DataReader reader) {
        final String classLoaderName = reader.readString();
        final String moduleName = reader.readString();
        final String moduleVersion = reader.readString();
        final String className = reader.readString();
        final String methodName = reader.readString();
        final String fileName = reader.readString();
        final int lineNumber = reader.readInteger();

        return new StackTraceElement(classLoaderName, moduleName, moduleVersion, className, methodName, fileName, lineNumber);
    }

    @Override
    public void write(final DataWriter writer, final StackTraceElement value) {
        writer.writeString(value.getClassLoaderName());
        writer.writeString(value.getModuleName());
        writer.writeString(value.getModuleVersion());
        writer.writeString(value.getClassName());
        writer.writeString(value.getMethodName());
        writer.writeString(value.getFileName());
        writer.writeInteger(value.getLineNumber());
    }
}
