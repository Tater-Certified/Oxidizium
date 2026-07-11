package com.github.tatercertified.oxidizium_tester.test;

/**
 * Holds benchmark results for a single method comparison between native (Rust) and pure Java implementations.
 */
public record BenchmarkResult(
        String methodName,
        long nativeTotalNanos,
        long javaTotalNanos,
        double nativeAvgNanos,
        double javaAvgNanos,
        double speedupRatio,
        boolean nativeSlower
) {
    /**
     * Returns a human-readable string showing the relative performance.
     * E.g. "1.23x faster" or "0.82x slower"
     */
    public String speedupText() {
        if (nativeSlower) {
            return String.format("%.2fx slower", speedupRatio);
        }
        return String.format("%.2fx faster", speedupRatio);
    }
}
