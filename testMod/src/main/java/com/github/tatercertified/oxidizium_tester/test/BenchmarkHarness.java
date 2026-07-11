package com.github.tatercertified.oxidizium_tester.test;

import com.github.tatercertified.oxidizium.Config;
import com.github.tatercertified.oxidizium.utils.annotation.Max;
import com.github.tatercertified.oxidizium.utils.annotation.Min;
import com.github.tatercertified.oxidizium.utils.annotation.NonZero;
import com.github.tatercertified.oxidizium.utils.annotation.PositiveOnly;
import com.github.tatercertified.oxidizium_tester.OxidiziumTester;
import org.openjdk.jmh.infra.Blackhole;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Microbenchmark harness that compares native (Rust) method performance against pure Java implementations.
 * Uses JMH's Blackhole to prevent the JVM from optimizing away benchmarked code,
 * while retaining a custom harness for dynamic runtime method discovery.
 */
public final class BenchmarkHarness {

    private static final int WARMUP_ITERATIONS = 100;

    private BenchmarkHarness() {
        // utility class
    }

    /**
     * Benchmarks a single method pair (native vs Java).
     * @param nativeMethod The cloned native mixin method
     * @param javaMethod The original Java method from the vanilla class
     * @param iterations Number of benchmark iterations
     * @return BenchmarkResult with timing data
     */
    public static BenchmarkResult benchmarkMethod(Method nativeMethod, Method javaMethod, String methodName, int iterations) {
        Class<?>[] parameterTypes = nativeMethod.getParameterTypes();
        Object[] args = prepareArguments(nativeMethod, parameterTypes);

        Blackhole bh = new Blackhole();

        // Warmup phase - JIT compilation
        for (int i = 0; i < WARMUP_ITERATIONS; i++) {
            Object nativeResult = invoke(nativeMethod, args);
            Object javaResult = invoke(javaMethod, args);
            bh.consume(nativeResult);
            bh.consume(javaResult);
        }

        // Benchmark native implementation
        long nativeStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            Object result = invoke(nativeMethod, args);
            bh.consume(result);
        }
        long nativeEnd = System.nanoTime();
        long nativeTotal = nativeEnd - nativeStart;

        // Benchmark Java implementation
        long javaStart = System.nanoTime();
        for (int i = 0; i < iterations; i++) {
            Object result = invoke(javaMethod, args);
            bh.consume(result);
        }
        long javaEnd = System.nanoTime();
        long javaTotal = javaEnd - javaStart;

        double nativeAvg = (double) nativeTotal / iterations;
        double javaAvg = (double) javaTotal / iterations;

        double speedupRatio;
        boolean nativeSlower;
        if (javaTotal > 0) {
            speedupRatio = (double) javaTotal / nativeTotal;
            nativeSlower = nativeTotal > javaTotal;
        } else {
            speedupRatio = Double.POSITIVE_INFINITY;
            nativeSlower = false;
        }

        return new BenchmarkResult(methodName, nativeTotal, javaTotal, nativeAvg, javaAvg, speedupRatio, nativeSlower);
    }

    /**
     * Benchmarks all methods from a native mixin class against the vanilla class.
     * @param nativeClass The cloned mixin class with native calls
     * @param vanillaClass The original vanilla class (e.g. Mth)
     * @param iterations Number of iterations per method
     * @param returnFilter Optional filter for return types; null to include all
     * @return List of benchmark results
     */
    public static List<BenchmarkResult> benchmarkClass(Class<?> nativeClass, Class<?> vanillaClass, int iterations, Class<?>... returnFilter) {
        List<BenchmarkResult> results = new ArrayList<>();

        for (Method nativeMethod : nativeClass.getDeclaredMethods()) {
            try {
                String methodName = formatMethodSignature(nativeMethod);
                Method javaMethod = findVanillaMethod(vanillaClass, nativeMethod);
                if (javaMethod == null) {
                    continue;
                }

                if (returnFilter != null && returnFilter.length > 0) {
                    boolean matches = false;
                    for (Class<?> filter : returnFilter) {
                        if (filter == nativeMethod.getReturnType()) {
                            matches = true;
                            break;
                        }
                    }
                    if (!matches) {
                        continue;
                    }
                }

                BenchmarkResult result = benchmarkMethod(nativeMethod, javaMethod, methodName, iterations);
                results.add(result);

                if (Config.getInstance().debug()) {
                    OxidiziumTester.TEST_LOGGER.info(
                            "Benchmark [{}]: Native={:.2f}ns Java={:.2f}ns ({})",
                            methodName, result.nativeAvgNanos(), result.javaAvgNanos(), result.speedupText()
                    );
                }
            } catch (Exception e) {
                if (Config.getInstance().debug()) {
                    OxidiziumTester.TEST_LOGGER.warn("Failed to benchmark method: {}", e.getMessage());
                }
            }
        }

        return results;
    }

    private static Object[] prepareArguments(Method method, Class<?>[] parameterTypes) {
        Object[] args = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] == String.class) {
                args[i] = String.valueOf(getRandomValue(int.class));
            } else {
                args[i] = getRandomValue(parameterTypes[i]);
            }
        }

        // Apply annotation-based constraints
        adjustArguments(method, args);

        return args;
    }

    private static void adjustArguments(Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();
        Integer minIndex = null;
        Integer maxIndex = null;

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].isAnnotationPresent(NonZero.class) && args[i] != null) {
                int val = ((Number) args[i]).intValue();
                args[i] = val == 0 ? val + 1 : val;
            }

            if (parameters[i].isAnnotationPresent(PositiveOnly.class) && args[i] != null) {
                args[i] = Math.abs(((Number) args[i]).intValue());
            }

            if (parameters[i].isAnnotationPresent(Min.class)) {
                minIndex = i;
            } else if (parameters[i].isAnnotationPresent(Max.class)) {
                maxIndex = i;
            }
        }

        // Ensure min <= max
        if (minIndex != null && maxIndex != null && args[minIndex] != null && args[maxIndex] != null) {
            if (((Number) args[minIndex]).doubleValue() > ((Number) args[maxIndex]).doubleValue()) {
                Object temp = args[minIndex];
                args[minIndex] = args[maxIndex];
                args[maxIndex] = temp;
            }
        }
    }

    private static Object getRandomValue(Class<?> type) {
        RandomValueGenerator gen = RandomValueGenerator.get(type);
        return gen != null ? gen.generate() : 0;
    }

    /**
     * Invokes a static method and returns its result (null for void methods).
     * Swallows exceptions so the benchmark loop isn't interrupted.
     */
    private static Object invoke(Method method, Object[] args) {
        try {
            return method.invoke(null, args);
        } catch (InvocationTargetException | IllegalAccessException ignored) {
            // Benchmark should use consistent args; errors logged elsewhere
            return null;
        }
    }

    private static Method findVanillaMethod(Class<?> vanillaClass, Method mixinMethod) {
        try {
            String vanillaName = mapMethodName(mixinMethod);
            return vanillaClass.getMethod(vanillaName, mixinMethod.getParameterTypes());
        } catch (NoSuchMethodException e) {
            // Try with original name as fallback
            try {
                return vanillaClass.getMethod(mixinMethod.getName(), mixinMethod.getParameterTypes());
            } catch (NoSuchMethodException ex2) {
                return null;
            }
        }
    }

    private static String mapMethodName(Method method) {
        // For methods with numeric suffixes that are mapping artifacts, try common patterns
        String name = method.getName();
        if (name.startsWith("method_")) {
            return name; // Already mapped or intermediary
        }
        return name;
    }

    private static String formatMethodSignature(Method method) {
        StringBuilder sb = new StringBuilder();
        sb.append(method.getName()).append("(");
        Class<?>[] params = method.getParameterTypes();
        for (int i = 0; i < params.length; i++) {
            sb.append(params[i].getSimpleName());
            if (i < params.length - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * Enum-style singleton for generating random test values per type.
     */
    private enum RandomValueGenerator {
        FLOAT(float.class) {
            @Override
            public Object generate() {
                return ThreadLocalRandom.current().nextFloat((float) (-Math.PI * 4.0F), (float) (Math.PI * 4.0F));
            }
        },
        INT(int.class) {
            @Override
            public Object generate() {
                return ThreadLocalRandom.current().nextInt(-1000, 1000);
            }
        },
        DOUBLE(double.class) {
            @Override
            public Object generate() {
                return ThreadLocalRandom.current().nextDouble(-Math.PI * 4.0, Math.PI * 4.0);
            }
        },
        LONG(long.class) {
            @Override
            public Object generate() {
                return ThreadLocalRandom.current().nextLong(-1000, 1000);
            }
        },
        BYTE(byte.class) {
            @Override
            public Object generate() {
                return (byte) ThreadLocalRandom.current().nextInt(-128, 127);
            }
        },
        BOOLEAN(boolean.class) {
            @Override
            public Object generate() {
                return ThreadLocalRandom.current().nextBoolean();
            }
        };

        private final Class<?> type;

        RandomValueGenerator(Class<?> type) {
            this.type = type;
        }

        public abstract Object generate();

        public static RandomValueGenerator get(Class<?> type) {
            for (RandomValueGenerator gen : values()) {
                if (gen.type == type) {
                    return gen;
                }
            }
            return null;
        }
    }
}
