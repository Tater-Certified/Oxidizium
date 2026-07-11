package com.github.tatercertified.oxidizium_tester.test;

import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BenchmarkManager {
    private static final Map<String, BenchmarkResult> RESULTS = new HashMap<>();

    public static void runBenchmarks() {
        Collection<RunResult> baselineResults = run(new OptionsBuilder()
                .include(BaseLineBenchmark.class.getSimpleName())
                .forks(1)
                .build());

        double baselineAvg = getPrimaryResult(baselineResults);

        Collection<RunResult> benchmarkResults = run(new OptionsBuilder()
                .include(Benchmarks.class.getSimpleName())
                .forks(1)
                .build());

        for (RunResult runResult : benchmarkResults) {
            String name = runResult.getParams().getBenchmark();
            double currentAvg = runResult.getPrimaryResult().getScore();

            if (name.startsWith("native_")) {
                name = name.replaceFirst("native_", "");

                if (RESULTS.containsKey(name)) {
                    RESULTS.get(name).setNativeAvgExecTimeUs(baselineAvg - currentAvg);
                } else {
                    BenchmarkResult result = new BenchmarkResult();
                    result.setMethodName(name);
                    result.setNativeAvgExecTimeUs(baselineAvg - currentAvg);
                    RESULTS.put(name, result);
                }
            } else {
                if (RESULTS.containsKey(name)) {
                    RESULTS.get(name).setJavaAvgExecTimeUs(baselineAvg - currentAvg);
                } else {
                    BenchmarkResult result = new BenchmarkResult();
                    result.setMethodName(name);
                    result.setJavaAvgExecTimeUs(baselineAvg - currentAvg);
                    RESULTS.put(name, result);
                }
            }
        }
    }

    private static Collection<RunResult> run(Options options) {
        try {
            return new Runner(options).run();
        } catch (RunnerException e) {
            throw new RuntimeException("Benchmark run failed", e);
        }
    }

    private static double getPrimaryResult(Collection<RunResult> results) {
        return results.iterator().next().getPrimaryResult().getScore();
    }

    /**
     * Gets a sorted list of the results
     * @return Gets a sorted list of the results
     */
    public static List<BenchmarkResult> getResults() {
        return RESULTS.values().stream().sorted().toList();
    }

    /**
     * Gets the number of results that are slower than Java
     * @return Number of results slower than Java
     */
    public static long getSlower() {
        return RESULTS.values().stream().filter(result -> result.speedImprovement() < 0).count();
    }
}
