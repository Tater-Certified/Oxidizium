package com.github.tatercertified.oxidizium_tester.test;

import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BenchmarkManager {
    private static final Map<String, BenchmarkResult> RESULTS = new HashMap<>();
    private static final Pattern PROGRESS_PATTERN = Pattern.compile("# Run progress:\\s*([0-9.]+)%\\s*complete");
    private static final Pattern BENCHMARK_NAME_PATTERN = Pattern.compile("# Benchmark:.*\\.Benchmarks\\.(.+)");

    public static void runBenchmarks() {
        // Set up async progress listener
        BlockingQueue<String> logQueue = new LinkedBlockingQueue<>();
        Thread progressThread = new Thread(() -> {
            try {
                TestingGUI.resetProgressBar();
                while (true) {
                    String line = logQueue.take();

                    Matcher progressMatcher = PROGRESS_PATTERN.matcher(line);
                    if (progressMatcher.find()) {
                        String percent = progressMatcher.group(1);
                        percent = percent.split("\\.")[0];
                        TestingGUI.setProgressBar(Integer.parseInt(percent));
                        TestingGUI.setCurrentTestNameFast("Benchmarking " + percent + " Percent");
                    }

                    Matcher nameMatcher = BENCHMARK_NAME_PATTERN.matcher(line);
                    if (nameMatcher.find()) {
                        TestingGUI.setCurrentMethodFast(nameMatcher.group(1));
                    }
                }
            } catch (InterruptedException e) {
                TestingGUI.setProgressBar(100);
                Thread.currentThread().interrupt();
            }
        }, "BenchmarkProgressListener");
        progressThread.setDaemon(true);
        progressThread.start();

        // Redirect stdout to capture JMH output
        PrintStream originalOut = System.out;
        System.setOut(new ProgressCapturingPrintStream(originalOut, logQueue));

        try {
            Collection<RunResult> benchmarkResults = run(new OptionsBuilder()
                    .include(Benchmarks.class.getName() + ".*")
                    .forks(0)
                    .build());

            for (RunResult runResult : benchmarkResults) {
                String name = runResult.getParams().getBenchmark();
                double currentAvg = runResult.getPrimaryResult().getScore();
                name = name.substring(name.lastIndexOf(".") + 1);

                // Use raw JMH scores — JMH already accounts for its own overhead.
                if (name.startsWith("native_")) {
                    name = name.replaceFirst("native_", "");

                    if (RESULTS.containsKey(name)) {
                        RESULTS.get(name).setNativeAvgExecTimeUs(currentAvg);
                    } else {
                        BenchmarkResult result = new BenchmarkResult();
                        result.setMethodName(name);
                        result.setNativeAvgExecTimeUs(currentAvg);
                        RESULTS.put(name, result);
                    }
                } else {
                    if (RESULTS.containsKey(name)) {
                        RESULTS.get(name).setJavaAvgExecTimeUs(currentAvg);
                    } else {
                        BenchmarkResult result = new BenchmarkResult();
                        result.setMethodName(name);
                        result.setJavaAvgExecTimeUs(currentAvg);
                        RESULTS.put(name, result);
                    }
                }
            }
        } finally {
            System.setOut(originalOut);
        }
    }

    /**
     * A PrintStream that forwards output to another stream while also
     * pushing complete lines to a queue for async parsing.
     */
    private static class ProgressCapturingPrintStream extends PrintStream {
        private final BlockingQueue<String> queue;
        private final StringBuilder buffer = new StringBuilder();

        ProgressCapturingPrintStream(OutputStream out, BlockingQueue<String> queue) {
            super(out, true);
            this.queue = queue;
        }

        @Override
        public void write(int b) {
            super.write(b);
            char c = (char) b;
            buffer.append(c);
            if (c == '\n') {
                String line = buffer.substring(0, buffer.length() - 1);
                buffer.setLength(0);
                try {
                    queue.put(line);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        @Override
        public void write(byte[] b, int off, int len) {
            super.write(b, off, len);
            for (int i = off; i < off + len; i++) {
                char c = (char) b[i];
                buffer.append(c);
                if (c == '\n') {
                    String line = buffer.substring(0, buffer.length() - 1);
                    buffer.setLength(0);
                    try {
                        queue.put(line);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
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
        return RESULTS.values().stream().filter(result -> {
            double ratio = result.speedImprovement();
            return ratio > 0 && ratio < 1.0;
        }).count();
    }

    /**
     * Gets the number of results that are faster than Java
     * @return Number of results faster than Java
     */
    public static long getFaster() {
        return RESULTS.values().stream().filter(result -> result.speedImprovement() > 1.0).count();
    }

    /**
     * Calculates the average speed improvement across all complete benchmarks.
     * @return Average of javaTime / nativeTime ratios. >1.0 means native is faster on average.
     */
    public static double getAverageSpeedImprovement() {
        List<BenchmarkResult> complete = RESULTS.values().stream()
                .filter(BenchmarkResult::hasBothResults)
                .toList();
        if (complete.isEmpty()) {
            return 0.0;
        }
        return complete.stream().mapToDouble(BenchmarkResult::speedImprovement).average().orElse(0.0);
    }

    /**
     * Gets the benchmark with the best speed improvement (highest ratio).
     * @return The best result, or null if no complete benchmarks exist.
     */
    public static BenchmarkResult getBestResult() {
        return RESULTS.values().stream()
                .filter(BenchmarkResult::hasBothResults)
                .max(BenchmarkResult::compareTo)
                .orElse(null);
    }

    /**
     * Gets the benchmark with the worst speed improvement (lowest ratio).
     * @return The worst result, or null if no complete benchmarks exist.
     */
    public static BenchmarkResult getWorstResult() {
        return RESULTS.values().stream()
                .filter(BenchmarkResult::hasBothResults)
                .min(BenchmarkResult::compareTo)
                .orElse(null);
    }

    /**
     * Gets the number of benchmarks that have both Java and native results.
     * @return Count of complete benchmarks.
     */
    public static long getCompleteCount() {
        return RESULTS.values().stream().filter(BenchmarkResult::hasBothResults).count();
    }
}
