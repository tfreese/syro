// Created: 24.09.2020
package de.freese.syro.serializer;

import de.freese.syro.SerializerRegistry;
import de.freese.syro.io.DataReader;
import de.freese.syro.io.DataWriter;

/**
 * @author Thomas Freese
 */
public interface Serializer<T> {
    T read(SerializerRegistry registry, DataReader reader);

    void write(SerializerRegistry registry, DataWriter writer, T value);
}
