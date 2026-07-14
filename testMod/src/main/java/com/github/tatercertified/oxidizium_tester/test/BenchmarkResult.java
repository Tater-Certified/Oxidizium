package com.github.tatercertified.oxidizium_tester.test;

import org.jspecify.annotations.NonNull;

public class BenchmarkResult implements Comparable<BenchmarkResult> {
    private String methodName;
    private double javaAvgExecTimeUs;
    private double nativeAvgExecTimeUs;

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public void setJavaAvgExecTimeUs(double us) {
        this.javaAvgExecTimeUs = us;
    }

    public void setNativeAvgExecTimeUs(double us) {
        this.nativeAvgExecTimeUs = us;
    }

    /**
     * Ratio of Java time to native time.
     * @return javaTime / nativeTime. >1.0 means native is faster, <1.0 means native is slower.
     *         Returns 0.0 if either value is missing (not yet benchmarked).
     */
    public double speedImprovement() {
        if (this.nativeAvgExecTimeUs <= 0 || this.javaAvgExecTimeUs <= 0) {
            return 0.0;
        }

        return this.javaAvgExecTimeUs / this.nativeAvgExecTimeUs;
    }

    @Override
    public int compareTo(@NonNull BenchmarkResult result) {
        return Double.compare(this.speedImprovement(), result.speedImprovement());
    }
}
