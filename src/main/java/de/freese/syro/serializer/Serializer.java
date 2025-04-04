// Created: 24.09.2020
package de.freese.syro.serializer;

import de.freese.syro.io.DataReader;
import de.freese.syro.io.DataWriter;

/**
 * @author Thomas Freese
 */
public interface Serializer<T> {
    T read(DataReader reader);

    void write(DataWriter writer, T value);
}
