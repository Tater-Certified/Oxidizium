package com.github.tatercertified.oxidizium_tester.test;

import com.github.tatercertified.oxidizium.utils.annotation.*;
import com.github.tatercertified.oxidizium_tester.OxidiziumTester;
import imgui.type.ImInt;

import java.util.concurrent.atomic.AtomicReference;

public class NativeTest {
    private static final AtomicReference<ImInt> RUNS_PER_TEST = new AtomicReference<>(new ImInt(50));
    public static final int BENCHMARK_ITERATIONS_TIME_S = 1;

    public static void invokeTests() {
        // Run parity test
        TestingGUI.reset();
        try {
            ParityManager.runParityTests();
        } catch (Exception e) {
            OxidiziumTester.TEST_LOGGER.error("Failed to run Parity Check!\n   {}", e.getMessage());
            e.printStackTrace();
        }

        // Run benchmarks after correctness tests
        OxidiziumTester.TEST_LOGGER.info("Starting benchmark suite ({} second(s) per method)", BENCHMARK_ITERATIONS_TIME_S);
        try {
            runBenchmarks();
        } catch (Exception e) {
            OxidiziumTester.TEST_LOGGER.error("Failed to run Benchmarks!\n   {}", e.getMessage());
            e.printStackTrace();
        }
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
}
