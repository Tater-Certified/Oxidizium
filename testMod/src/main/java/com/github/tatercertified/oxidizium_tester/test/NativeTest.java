package com.github.tatercertified.oxidizium_tester.test;

import com.github.tatercertified.oxidizium.Config;
import com.github.tatercertified.oxidizium.utils.annotation.*;
import com.github.tatercertified.oxidizium_tester.OxidiziumTester;
import com.github.tatercertified.oxidizium_tester.utils.MixinCloner;
import imgui.type.ImInt;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;

public class NativeTest {
    private static final float FLOAT_TOLERANCE = 0.0001F;
    private static final double DOUBLE_TOLERANCE = 0.0000001;
    private static final Deque<Runnable> TASKS = new ArrayDeque<>();
    private static int totalRuns = 0;
    private static final AtomicReference<ImInt> RUNS_PER_TEST = new AtomicReference<>(new ImInt(50));
    public static final int BENCHMARK_ITERATIONS_TIME_S = 1;

    public static void prepareTests() {
        testFramework("com/github/tatercertified/oxidizium/mixin/MthMixin", "Native Math", Mth.class, RUNS_PER_TEST.get().intValue(), float.class, double.class, int.class, long.class, byte.class, boolean.class);
        testFramework("com/github/tatercertified/oxidizium/mixin/compat/LithiumMthMixin", "Native Math Lithium Compat", Mth.class, RUNS_PER_TEST.get().intValue(), float.class, double.class, int.class, long.class, byte.class);
        TestingGUI.setTotalTests(totalRuns);
    }

    public static void invokeTests() {
        totalRuns = 0;
        TestingGUI.reset();
        NativeTest.prepareTests();
        while (!TASKS.isEmpty()) {
            TASKS.poll().run();
        }

        // Run benchmarks after correctness tests
        OxidiziumTester.TEST_LOGGER.info("Starting benchmark suite ({} second(s) per method)", BENCHMARK_ITERATIONS_TIME_S);
        runBenchmarks();
        TestingGUI.setBenchmarkComplete(true);
    }

    public static ImInt getRunsPerTest() {
        return RUNS_PER_TEST.get();
    }


    public static int getBenchmarkTime() {
        return BENCHMARK_ITERATIONS_TIME_S;
    }

    /**
     * Runs all benchmarks.
     */
    public static void runBenchmarks() {
        TestingGUI.setCurrentTestName("Benchmarking...");
        OxidiziumTester.TEST_LOGGER.info("Benchmarking...");
        TestingGUI.setCurrentClass("Benchmarks");
        BenchmarkManager.runBenchmarks();
        OxidiziumTester.TEST_LOGGER.info("Benchmark complete");
        TestingGUI.setCurrentTestName("Benchmarking Complete");
    }

    /**
     * Tests parity between a native Mixin and the original Java class
     * @param mixinPath Path to the Mixin; Ex: <b>"com/github/tatercertified/oxidizium/mixin/compat/LithiumMathHelperMixin"</b>
     * @param testName Name of the test
     * @param vanillaClass Vanilla class instance
     * @param runs Number of times to run the test
     * @param returnFilter A filter for return types of tested methods. Null for no filter
     */
    private static void testFramework(String mixinPath, String testName, Class<?> vanillaClass, int runs, @Nullable Class<?>... returnFilter) {
        String mixinName = mixinPath.substring(mixinPath.lastIndexOf('/') + 1);
        String clone = "com/github/tatercertified/oxidizium/Cloned" + mixinName;
        Class<?> mixin;
        try {
            mixin = MixinCloner.cloneStaticMethods(mixinPath, clone);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        totalRuns += (getTotalTestedMethods(mixin, vanillaClass, returnFilter) * runs);

        Runnable test = () -> {
            OxidiziumTester.TEST_LOGGER.info("Starting {} Test", testName);
            TestingGUI.setCurrentTestName(testName);
            TestingGUI.setCurrentClass(vanillaClass.getSimpleName());
            for (int i = 0; i < runs; i++) {
                TestingGUI.setCurrentRun(i);
                for (Method method : mixin.getDeclaredMethods()) {
                    try {
                        Method vanillaMethod = vanillaClass.getMethod(method.getName(), method.getParameterTypes());
                        if (returnFilter == null || Arrays.stream(returnFilter).anyMatch(clazz -> clazz == method.getReturnType())) {
                            TestingGUI.setCurrentMethod(formatMethod(method.getName(), vanillaMethod));
                            try {
                                invokeAndTest(method, vanillaMethod, method.getName());
                            } catch (Exception e) {
                                String error;
                                if (e instanceof InvocationTargetException exception) {
                                    error = exception.getCause().toString();
                                } else {
                                    error = e.toString();
                                }
                                TestingGUI.addError(formatMethod(method.getName(), vanillaMethod), false, error);
                            }
                        }
                    } catch (NoSuchMethodException _) {
                    }
                }
            }
            OxidiziumTester.TEST_LOGGER.info("{} Test Has Concluded", testName);
        };
        TASKS.add(test);
    }

    private static int getTotalTestedMethods(Class<?> clazz, Class<?> vanillaClass, @Nullable Class<?>... returnFilter) {
        int count = 0;
        for (Method method : clazz.getDeclaredMethods()) {
            if (Arrays.stream(returnFilter).anyMatch(clazz1 -> clazz1 == method.getReturnType())) {
                try {
                    vanillaClass.getMethod(method.getName(), method.getParameterTypes());
                } catch (NoSuchMethodException e) {
                    continue;
                }
                count++;
            }
        }
        return count;
    }

    private static String formatMethod(String remappedMethodName, Method method) {
        StringBuilder builder = new StringBuilder();
        builder.append(remappedMethodName).append("(");

        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            builder.append(parameterTypes[i].getSimpleName());
            if (i < parameterTypes.length - 1) {
                builder.append(", ");
            }
        }

        builder.append(")");
        return builder.toString();
    }

    private static void invokeAndTest(Method nativeMethod, Method javaMethod, String remappedMethodName) throws Exception {
        Class<?>[] parameterTypes = nativeMethod.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];

        for (int i = 0; i < parameterTypes.length; i++) {
            if (parameterTypes[i] == String.class) {
                args[i] = String.valueOf(getCorrectValue(int.class));
            } else {
                args[i] = getCorrectValue(parameterTypes[i]);
            }
        }

        // Adjust values for parameters named 'min' and 'max'
        adjustMinMaxArguments(nativeMethod, args);

        Object nativeResult = nativeMethod.invoke(null, args);
        Object javaResult = javaMethod.invoke(null, args);

        OxidiziumTester.TEST_LOGGER.info("Native: {}, Java: {}", nativeResult, javaResult);

        boolean resultsEqual = areResultsEquivalent(nativeResult, javaResult, nativeMethod.getReturnType(), javaMethod, remappedMethodName);

        assertTrue(resultsEqual,
                String.format("\u001B[31m %s is invalid: %s != %s \u001B[0m", nativeMethod.getName(), nativeResult, javaResult));
    }

    private static boolean areResultsEquivalent(Object nativeResult, Object javaResult, Class<?> returnType, Method vanillaMethod, String remappedMethodName) {
        boolean acceptable;
        boolean exact = true;
        if (returnType.equals(float.class)) {
            float error = Math.abs((float) nativeResult - (float) javaResult);
            acceptable = error <= FLOAT_TOLERANCE;
            exact = nativeResult.equals(javaResult);
        } else if (returnType.equals(double.class)) {
            double error = Math.abs((double) nativeResult - (double) javaResult);
            acceptable = error <= DOUBLE_TOLERANCE;
            exact = nativeResult.equals(javaResult);
        } else {
            acceptable = nativeResult.equals(javaResult);
        }

        if (acceptable && !exact) {
            TestingGUI.addError(formatMethod(remappedMethodName, vanillaMethod), true, "[Native] " + nativeResult + " != [Java] " + javaResult);
        } else if (!acceptable) {
            TestingGUI.addError(formatMethod(remappedMethodName, vanillaMethod), false, "[Native] " + nativeResult + " != [Java] " + javaResult);
        }

        return acceptable;
    }

    private static void adjustMinMaxArguments(Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();
        Integer minIndex = null;
        Integer maxIndex = null;

        for (int i = 0; i < parameters.length; i++) {
            Class<?> type = parameters[i].getType();
            Number val = (Number) args[i];

            if (parameters[i].isAnnotationPresent(NonZero.class) && val.doubleValue() == 0.0) {
                args[i] = castToType(val.doubleValue() + 1.0, type);
            }

            if (parameters[i].isAnnotationPresent(PositiveOnly.class)) {
                args[i] = castToType(Math.abs(val.doubleValue()), type);
            }

            if (parameters[i].isAnnotationPresent(Min.class)) {
                minIndex = i;
            } else if (parameters[i].isAnnotationPresent(Max.class)) {
                maxIndex = i;
            }
        }

        if (minIndex != null && maxIndex != null) {
            if (((Number) args[minIndex]).doubleValue() > ((Number) args[maxIndex]).doubleValue()) {

                Object temp = args[minIndex];
                args[minIndex] = args[maxIndex];
                args[maxIndex] = temp;
            }
        } else if (minIndex != null) {
            // Only min identified, assume last parameter as max
            maxIndex = parameters.length - 1;
            if (((Number) args[minIndex]).doubleValue() > ((Number) args[maxIndex]).doubleValue()) {
                if (Config.getInstance().debug()) {
                    OxidiziumTester.TEST_LOGGER.info("Assuming last parameter as 'max' and swapping with 'min'");
                }

                Object temp = args[minIndex];
                args[minIndex] = args[maxIndex];
                args[maxIndex] = temp;
            }
        }
    }

    private static Object castToType(double value, Class<?> type) {
        if (type == byte.class || type == Byte.class) return (byte) value;
        if (type == short.class || type == Short.class) return (short) value;
        if (type == int.class || type == Integer.class) return (int) value;
        if (type == long.class || type == Long.class) return (long) value;
        if (type == float.class || type == Float.class) return (float) value;
        return value;
    }

    private static Number getCorrectValue(Class<?> type) {
        return switch (type.getName()) {
            case "float" -> ThreadLocalRandom.current().nextFloat((float) (-Math.PI * 4.0F), (float) (Math.PI * 4.0F));
            case "int" -> ThreadLocalRandom.current().nextInt(-1000, 1000);
            case "double" -> ThreadLocalRandom.current().nextDouble(-Math.PI * 4.0, Math.PI * 4.0);
            case "long" -> ThreadLocalRandom.current().nextLong(-1000, 1000);
            case "byte" -> (byte) ThreadLocalRandom.current().nextInt(-128, 127);
            case "org.apache.commons.lang3.math.Fraction" -> Fraction.getFraction(ThreadLocalRandom.current().nextInt(-100, 100), ThreadLocalRandom.current().nextInt(1, 100));
            default -> throw new IllegalArgumentException("Unsupported type: " + type.getName());
        };
    }

    private static void assertTrue(boolean bool, String ifFailed) {
        if (!bool && Config.getInstance().debug()) {
            OxidiziumTester.TEST_LOGGER.error(ifFailed);
        }
    }
}
