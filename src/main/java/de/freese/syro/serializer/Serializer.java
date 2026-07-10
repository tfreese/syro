// Created: 24.09.2020
package de.freese.syro.serializer;

import org.jspecify.annotations.Nullable;

import de.freese.syro.io.DataReader;
import de.freese.syro.io.DataWriter;

/**
 * @author Thomas Freese
 */
public interface Serializer<T> {
    @Nullable
    T read(DataReader reader);

    void write(DataWriter writer, @Nullable T value);
}
