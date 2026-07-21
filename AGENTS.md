# AGENTS.md - Syro Serializer Development Guide

**Syro** is a lightweight, Kryo-inspired binary serialization library. This guide helps AI agents understand architecture, patterns, and workflows specific to this project.

## Architecture Overview

### Core Design Pattern: Pluggable I/O Abstraction + Generic Serializers

The codebase separates **serialization logic** from **I/O backend**, enabling multiple transport layers with identical serializer implementations:

```
Serializer<T>  ──→  read(DataReader)/write(DataWriter, T)
                     ↓
        ┌────────────┼────────────┐
        ↓            ↓            ↓
  ByteBufferReader  ByteBufReader  InputStreamReader
  (NIO optimized)  (Netty perf)    (Streams)
```

**Critical understanding**: Serializers never directly touch I/O. They work through `DataReader`/`DataWriter` interfaces with default implementations for primitives (conversion logic lives in DataWriter, not backends).

### Singleton Serializers Pattern

All primitive/common type serializers use **Bill Pugh singleton**:

```java
// IntegerSerializer.java example structure
public class IntegerSerializer implements Serializer<Integer> {
    private static class Holder {
        static final IntegerSerializer INSTANCE = new IntegerSerializer();
    }
    public static IntegerSerializer getInstance() {
        return Holder.INSTANCE;
    }
    private IntegerSerializer() {} // private constructor
}
```

**Why**: Eliminates allocation overhead during repeated serialization calls. No synchronization needed.

### Null Handling Convention

All primitive types support nullable variants through `DataWriter.writeXxxOrNull()` / `DataReader.readXxxOrNull()`:
- Writes `byte 0` for null
- Writes `byte 1` + value for non-null

See `DataWriter.java:17-24` for the pattern. All new serializers must handle nullability symmetrically.

## Project-Specific Patterns & Conventions

### 1. Creating Custom Serializers

Implement `Serializer<T>` interface (only 2 methods). Example from TestSyro:

```java
class Point implements Serializer<Point> {
    @Override
    public Point read(DataReader reader) {
        int x = reader.readInteger();
        int y = reader.readInteger();
        return new Point(x, y);
    }
    @Override
    public void write(DataWriter writer, @Nullable Point value) {
        if (value == null) {
            writer.writeInteger(-1); // null marker
            return;
        }
        writer.writeInteger(value.x);
        writer.writeInteger(value.y);
    }
}
```

**Pattern**: Handle nulls explicitly (no automatic null-checking). Use DataReader/Writer for all I/O.

### 2. NullAway Enforcement

Project enforces null-safety via **ErrorProne + NullAway** (build.gradle:337-349):
- All public methods must be annotated: `@Nullable` return or `@Nullable` parameter
- Packages scanned: `de.freese.syro.*` (see gradle.properties:343)
- NullAway:JSpecifyMode=true → checks generics nullness (Java 22+)

**When implementing**: Every parameter and return in public API must have explicit `@Nullable` or implicit non-null.

```java
// Correct
public @Nullable String readString() { ... }
public void writeString(@Nullable String value) { ... }

// Will fail NullAway
public String readString() { ... } // Missing @Nullable (or needs non-null guarantee)
```

### 3. I/O Backend Abstraction

Three backends in `de.freese.syro.io`:
- **ByteBufferWriter/Reader**: NIO `ByteBuffer` + native calls (`putInt`, `getInt`)
- **ByteBufWriter/Reader**: Netty `ByteBuf` wrapper (for Netty-based systems)
- **OutputStreamWriter/InputStreamReader**: Traditional `OutputStream`/`InputStream`

**When choosing**:
- ByteBuffer: High-performance, off-heap capable, predictable allocation
- ByteBuf: Netty integration, pooling support
- Streams: Legacy systems, unknown size data

All backends implement identical DataWriter/DataReader interface. Tests parametrize across all three (TestSyro:41-60).

## Test Architecture

### Parametrized Testing Pattern

TestSyro uses **JUnit 5 parametrized tests** running same tests against all 3 I/O backends:

```java
@ParameterizedTest
@CsvSource({...}) // Actually uses annotation for each backend
void testSerializationRoundTrip(@AggregateFrom(...) DataReader reader, DataWriter writer) {
    // Runs with: ByteBuffer, ByteBuf, OutputStream variants
}
```

**When adding tests**: Use the parametrized pattern to catch backend-specific issues. New serializers must be tested in TestSyro (see lines 41-281).

## Critical Build Workflows

### Standard Commands

```bash
# Full clean build with all quality gates
gradle clean build

# Build without tests (faster iteration)
gradle build -x test

# Run tests + quality checks separately
gradle test
gradle checkstyleMain checkstyleTest
gradle sonarlintMain sonarlintTest

# Publish to local Maven repo (build/mvn-repo/snapshots)
gradle publishSyroMvnPublicationToLocalMvnRepository

# Generate JARs + sources + javadoc
gradle assemble

# Skip output buffering (useful for debugging)
gradle --no-parallel test
```

### Quality Gates in Pipeline

1. **Checkstyle** (config/checkstyle/checkstyle.xml): Line length 180, method naming conventions
2. **SonarLint** (build.gradle:712-794): Java code quality rules, disabled S106 (System.out), S110 (deep inheritance), S112 (generic exceptions)
3. **ErrorProne + NullAway**: Enabled during `compileJava` / `compileTestJava`
4. **Reproducible builds** (build.gradle:164-176): JAR file times normalized for consistency

### JVM Configuration

Java 25+ required (gradle.properties:119). Gradle daemon gets 4GB heap (gradle.properties:33):
```properties
org.gradle.jvmargs = -Xmx4096m --enable-native-access=ALL-UNNAMED --sun-misc-unsafe-memory-access=allow
```

**Note**: `--enable-native-access` required for Unsafe operations in ByteBuffer optimizations. `--sun-misc-unsafe-memory-access=allow` for Java 25 compatibility.

## File Organization Reference

**Core interfaces** (contracts):
- `src/main/java/de/freese/syro/serializer/Serializer.java` — Generic serializer interface
- `src/main/java/de/freese/syro/io/DataReader.java` — Read abstraction + primitive defaults
- `src/main/java/de/freese/syro/io/DataWriter.java` — Write abstraction + primitive defaults

**Concrete serializers** (by type category):
- Primitives: `IntegerSerializer`, `LongSerializer`, `DoubleSerializer`, etc. (all singletons)
- Complex: `ExceptionSerializer`, `StackTraceElementSerializer` (handle nested structures)

**I/O backends**:
- `ByteBufferWriter.java`, `ByteBufWriter.java`, `OutputStreamWriter.java`
- Corresponding Readers (same pattern)

**Tests**:
- `src/test/java/de/freese/syro/TestSyro.java` — Parametrized test suite (all backends)

## Common Development Scenarios

### Adding a New Primitive Serializer Type

1. Create `class NewTypeSerializer implements Serializer<NewType>` (singleton pattern)
2. Implement `read()` / `write()` using DataReader/Writer methods
3. Add annotations: `@Nullable` on parameters/returns as needed
4. Add parametrized test case in TestSyro
5. Run: `gradle build` to verify NullAway + Checkstyle

### Fixing a Null Safety Warning

The warning points to a method violating NullAway contracts. Options:
- **Add `@Nullable` annotation** if parameter/return can be null
- **Add null-check** if guarantee is needed (e.g., `Objects.requireNonNull()`)
- **Suppress warning** only if false positive (rare; document why)

Example fix:
```java
// Before (fails NullAway)
public String getValue() { return field; }

// After
public @Nullable String getValue() { return field; }
```

### Optimizing I/O for Performance

- Use ByteBufferWriter if controlling off-heap allocation
- Use ByteBufWriter with Netty pooling if already in Netty context
- Avoid OutputStreamWriter for high-throughput scenarios (allocates byte arrays per call)
- Test changes against all 3 backends via parametrized tests

### Debugging Serialization Issues

Run tests without parallel execution:
```bash
gradle --no-parallel test
```

Enable debug logging in build.gradle (search `testLogging` section, set `showStandardStreams = true`).

## External Context

- **Kryo project**: Original inspiration (https://github.com/EsotericSoftware/kryo)
- **Netty**: Optional I/O backend (io.netty:netty-buffer dependency)
- **JSpecify**: Null-safety annotations standard (@Nullable from org.jspecify)
- **ErrorProne**: Google's static analysis (enabled via Gradle plugin)

## Dependencies Summary

| Dependency | Role | Scope |
|-----------|------|-------|
| `io.netty:netty-buffer` | Optional I/O backend | `api` |
| `org.jspecify:jspecify` | Null-safety annotations | `api` |
| `org.junit.jupiter:junit-jupiter` | Test framework | `testImplementation` |

No external serialization libraries (Gson, Jackson, etc.) — intentionally lightweight.

