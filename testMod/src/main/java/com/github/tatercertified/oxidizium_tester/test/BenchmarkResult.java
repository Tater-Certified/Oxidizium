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

    public void setJavaAvgExecTimeUs(double Us) {
        this.javaAvgExecTimeUs = Us;
    }

    public void setNativeAvgExecTimeUs(double Us) {
        this.nativeAvgExecTimeUs = Us;
    }

    /**
     * Gives the speed improvement in "times" faster. Ex: 0.75x faster.
     * Negative values means the native version is slower. Ex: -0.5 = 0.5x slower
     * @return "times" faster/slower
     */
    public double speedImprovement() {
        if (this.nativeAvgExecTimeUs <= 0 || this.javaAvgExecTimeUs <= 0) {
            return 0.0;
        }

        if (this.nativeAvgExecTimeUs < this.javaAvgExecTimeUs) {
            // Native is faster
            return this.javaAvgExecTimeUs / this.nativeAvgExecTimeUs;
        } else if (this.nativeAvgExecTimeUs > this.javaAvgExecTimeUs) {
            // Java is faster (make negative)
            return -(this.nativeAvgExecTimeUs / this.javaAvgExecTimeUs);
        }

        return 1.0;
    }

    @Override
    public int compareTo(@NonNull BenchmarkResult result) {
        return Double.compare(this.speedImprovement(), result.speedImprovement());
    }
}
