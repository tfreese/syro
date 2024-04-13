// Created: 24.09.2020
package de.freese.syro.serializer;

import java.util.function.Function;

import de.freese.syro.adapter.DataReader;
import de.freese.syro.adapter.DataWriter;

/**
 * @author Thomas Freese
 */
public interface Serializer<T> {
    T read(Function<Class<?>, Serializer<?>> registry, DataReader reader, Class<T> type);

    void write(Function<Class<?>, Serializer<?>> registry, DataWriter writer, T value);
}
